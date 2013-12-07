import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


// -------------------------------------------------------------------------
/**
 *  Class that stores all of the data from the file being read into 4096
 *  byte arrays. Writes data to disk once heapsort is finished on all the arrays
 *
 *  @author Dan
 *  @version Oct 22, 2013
 */
public class BufferPool implements BufferPoolADT
{
    /**
     * Array list that holds all buffers and keeps them in order
     */
    AList<Buffer> LRUbufferList;

    /**
     * file to be read from and, written to
     */
    RandomAccessFile file;

    private int blockSize;

    /**
     * number of times the cache is hit
     */
    int hits = 0;

    /**
     * number of times cache is missed
     */
    int misses = 0;

    /**
     * number of times blocks have been read from the disc
     */
    int discReads = 0;

    /**
     * number of times blocks have been read from the disc
     */
    int discWrites = 0;

    // ----------------------------------------------------------
    /**
     * Create a new BufferPool object.
     * @param file randomAccessFile to be loaded into the buffer pool
     * @param blockSize size of buffer blocks in bytes
     * @param numBuffers number of buffer blocks
     */
    public BufferPool(RandomAccessFile file, int blockSize, int numBuffers)
    {
        //create a new
        LRUbufferList = new AList<Buffer>(numBuffers);
        this.file = file;
        this.blockSize = blockSize;

        //read data into buffer pool
        try
        {
            populatePool(numBuffers);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------
    /**
     * reorder the buffer list so that it maintains a LRU pattern
     */
    public void LRUupdate()
    {
        //make sure index is valid
        Buffer buffer = LRUbufferList.remove();
        LRUbufferList.moveToStart();
        LRUbufferList.insert(buffer);
    }

    // ----------------------------------------------------------
    /**
     * reads data from the recently opened file and creates buffers/adds
     * them to the pool
     * @param numBuffers number of buffers to be created
     * @throws IOException
     */
    public void populatePool(int numBuffers) throws IOException
    {
        long counter = 0;
        for (int i = 0; i < numBuffers; i++)
        {
            file.seek(counter);
            //allocate new byte array
            byte[] b = new byte[4096];

            //add bytes to buffer from file
            file.read(b);
            Buffer newBuff = new Buffer(b, i);

            //update file pointer

            //add buffer to list
            LRUbufferList.append(newBuff);
            discReads = discReads + 1;
            counter = counter + 4096;

        }
        //put pointer back at beggining of the file
        file.seek(0);
    }

    /** Copy "sz" bytes from "space" to position "pos" in the
    buffered storage, must set dirty bit if changed
     * @throws IOException */
    @Override
    public void insert(byte[] space, int sz, int pos) throws IOException
    {
        //find the buffer at position
        Buffer desiredBuffer = findBuffer(pos);
        //find position of bytes inside buffer array
        int bytePosition = (pos * 4) % 4096;
        byte[] temp = desiredBuffer.getSpace();
        for (int i = 0; i < sz; i++)
        {
            //make copy for index i
            temp[bytePosition] = space[i];
            bytePosition++;
         }
         desiredBuffer.setSpace(temp);
         //set dirty bit
         desiredBuffer.setDirtyBit(true);
    }

    /** Copy "sz" bytes from position "pos" of the buffered
    storage to "space".
     * @throws IOException */
    @Override
    public void getbytes(byte[] space, int sz, int pos) throws IOException
    {
        //get the buffer containing the desirded block
        Buffer desiredBuffer = findBuffer(pos);
        //find position of bytes inside buffer array
        int bytePosition = (pos * 4) % 4096;
        byte[] buffSpace = desiredBuffer.getSpace();
        for (int i = 0; i < sz; i++)
        {
            //make copy for index i
            space[i] = buffSpace[bytePosition];
            bytePosition++;
         }
    }



    // ----------------------------------------------------------
    /**
     * Finds buffer containing record at pos j in the file. Buffer pool will
     * still be pointing at cur position of buffer
     * @param pos of record in the file
     * @return buffer with pos as posID
     * @throws IOException
     */
    public Buffer findBuffer(int pos) throws IOException
    {
        Buffer desiredBuffer = null;
        //find posID of buffer
        int buffPos = pos/1024;
        LRUbufferList.moveToStart();
        // TODO get a buffer from pos
        for (int i = 0; i < LRUbufferList.length(); i++)
        {
            //buffer with correct posID is found and assigned
            if (buffPos == LRUbufferList.getValue().getPosID())
            {
                desiredBuffer = LRUbufferList.getValue();
                hits = hits + 1;
                break;
            }
            LRUbufferList.next();
        }
        //buffer containing block not found must pull from disk
        if (desiredBuffer == null)
        {
            //TODO
            byte[] b = new byte[4096];

            //move to correct block and read in data values
            file.seek(4096 * buffPos);
            file.read(b, 0, 4096);
            file.seek(0);
            //create a new buffer from the block position
            desiredBuffer = new Buffer(b, buffPos);
            //remove LRU buffer and append on new one
            LRUbufferList.moveToEnd();
            removeFromPool();
            //LRUbufferList.remove();
            LRUbufferList.append(desiredBuffer);
            //update buffer list
            misses = misses + 1;
            discReads = discReads + 1;

        }
        LRUupdate();
        return desiredBuffer;
    }

    /**
     * remove a buffer from LRU list and write to the disc if the dirty bit is
     * set
     * @throws IOException
     */
    private void removeFromPool() throws IOException
    {
        //remove LRU buffer
        //LRUbufferList.moveToEnd();
        LRUbufferList.moveToPos( LRUbufferList.length() - 1);
        Buffer removed = LRUbufferList.remove();

        //write buffer to the list if dirty bit
        if (removed.getDirtyBit())
        {
            //write to disc
            //move to correct block
            file.seek(removed.getPosID() * 4096);
            byte[] b = removed.getSpace();
            //Overwrite file
            file.write(b);
            discWrites = discWrites + 1;
            file.seek(0);
        }

    }

    /**
     * flush the buffer pool once it is done, write all change buffers to the
     * disk
     * @throws IOException
     */
    public void flushPool() throws IOException
    {
        LRUbufferList.moveToStart();
        while (LRUbufferList.length() > 0)
        {
            Buffer removed = LRUbufferList.remove();
            //write buffer to the list if dirty bit
            if (removed.getDirtyBit())
            {
                //write to disc
                //move to correct block
                file.seek(removed.getPosID() * 4096);
                byte[] b = removed.getSpace();
                //Overwrite file
                file.write(b);
                discWrites = discWrites + 1;
                file.seek(0);
            }
        }
    }

    /**
     * return the size of the file in buffer pool
     * @return size of the file
     */
    public long fileSize()
    {
        long fileSize = 0;
        try
        {
            fileSize = file.length()/4;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileSize;

    }

    // ----------------------------------------------------------
    /**
     * print cache hits
     */
    public void printHits()
    {
        System.out.println("Number of cache hits: " + hits);
    }

    // ----------------------------------------------------------
    /**
     * print cache misses
     */
    public void printMisses()
    {
        System.out.println("Number of cache misses: " + misses);
    }

    // ----------------------------------------------------------
    /**
     * print diskReads
     */
    public void printDiskReads()
    {
        System.out.println("Number of disk reads: " + discReads);
    }

    // ----------------------------------------------------------
    /**
     * print disk writes
     */
    public void printDiskWrites()
    {
        System.out.println("Number of disk writes: " + discWrites);
    }
}
