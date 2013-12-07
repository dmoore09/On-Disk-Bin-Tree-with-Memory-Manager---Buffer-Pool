import java.nio.ShortBuffer;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;


// -------------------------------------------------------------------------
/**
 *   Memory manager. Decides where to write data in the buffer pool.
 *
 *  @author Dan
 *  @version Dec 2, 2013
 */
public class MemManager implements MemManADT
{
    /**
     * buffer pool will be implemented later
     */
    //BufferPool pool;
    /**
     * for incremental development makes the pool in main memory
     */
    byte[] mainPool;
    /**
     * current position in the pool
     */
    int pos;
    /**
     * freelist to be used later
     */
     //TODO wtf do i use for a free list



    // ----------------------------------------------------------
    /**
     * Create a new MemManager object.
     * @param file to be r/w to
     * @param blockSize size of a buffer
     * @param numBuffers number of buffers in the pool
     */
    public MemManager(RandomAccessFile file, int blockSize, int numBuffers)
    {
        //bufferpool that represents the mem manager's array
        //pool = new BufferPool(file, blockSize, numBuffers);
        mainPool = new byte[10000];
        pos = 0;
    }

    /**
     * recieves a message from the bintree and stores the byte representation
     * of one of the following:
     *
     *      1. internal node (9 bytes)
     *      2. empty node
     *      3. leaf node (5 bytes + watcher bytes)
     *
     * the first thing mem man does is add a two byte field onto the beggining
     * of the message to represent the size of the message
     */
    @Override
    public int insert(byte[] info, int length)
    {
        //back up current position to be used as a handle
        int posHandle = pos;

        //store length of the message first
        ByteBuffer buff = ByteBuffer.allocate(2);
        buff.putShort((short)length);
        buff.rewind();
        mainPool[pos] = buff.get();
        pos++;
        mainPool[pos] = buff.get();
        pos++;


        //Increment testing 1 just add byte array into main pool
        for (int i = 0; i < length; i++) {
            mainPool[pos] = info[i];
            pos++;
        }

        //TODO Auto-generated method stub
        return posHandle;
    }

    /**
     * gets a new byte from the pool
     * @param handle handle for byte
     */
    @Override
    public byte[] get(int handle)
    {
        //go to the handle in memory and find length
        byte[] length = new byte[2];
        length[0] = mainPool[handle];
        length[1] = mainPool[handle + 1];

        ByteBuffer buff = ByteBuffer.wrap(length);
        short len = buff.getShort();

        //create a new array large enough for the data
        byte[] data = new byte[len];

        //copy data
        int handlePos = handle + 2;
        for (int i = 0; i < len; i++) {
            data[i] = mainPool[handlePos];
            handlePos++;
        }

        return data;
    }

    @Override
    public void release(int h)
    {
        // TODO for now does nothing

    }

    /**
     * find space inside the free list to fit the message
     * @param info byte that needs to be stored
     * @param length of the message to be stored in bytes
     * @return new handle for byte position
     */
    public int findSpace(byte[] info, int length) {

        return 0;
    }

    // ----------------------------------------------------------
    /**
     * updates a current entry in the, childbytes will always be 4 bytes
     * @param childBytes of child
     * @param handle of entry
     * @param leftOrRight true if left, false if right
     */
    public void insertAt(byte[] childBytes, int handle, boolean leftOrRight)
    {
        int bytePos;
        //determine byte pos within entry
        if (leftOrRight) {
            //if it is the left node then we need the second entry
            //+ 2 to skip size indicator, + 5 to jump to left child
           bytePos = handle + 7;
        }
        else {
            //if it is the right node then we need the first entry
            //+ 2 to skip size indicator, + 1 to jump to right child
            bytePos = handle + 3;
        }


        for (int i = 0; i < 4; i++) {
            mainPool[bytePos] = childBytes[i];
            bytePos = bytePos + 1;
        }

    }

    //TODO body of insert
    //allocate an extra 2 bytes in a new array to store the length of the
    //message
//    byte[] newMessage = new byte[info.length + 2];
//    ByteBuffer buff = ByteBuffer.allocate(2);
//    buff.putInt(length);
//    newMessage[0] = buff.get();
//    newMessage[1] = buff.get();
//
//    //find a place to store the new message, store it, return the handle
//    int newHandle = findSpace(info, length);

}
