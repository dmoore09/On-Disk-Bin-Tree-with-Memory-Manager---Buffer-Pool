
// -------------------------------------------------------------------------
/**
 *  Pointer to a buffer, contains posID, data, and dirtyBit value. Will
 *  be stored in LRUarray in the buffer pool. Simplifies storing additional
 *  data needed to analyze buffer blocks
 *
 *  @author Dan
 *  @version Oct 24, 2013
 */
public class Buffer
{
    //actual data
    private byte[] space;
    //boolean to specify if it changed
    private boolean dirtyBit;
    //position ID
    private int posID;

    // ----------------------------------------------------------
    /**
     * Create a new Buffer object.
     * @param data to be stored
     * @param pos block #
     */
    public Buffer(byte[] data, int pos)
    {
        space = new byte[4096];
        dirtyBit = false;
        posID = pos;
        for (int i = 0; i < 4096; i++)
        {
            space[i] = data[i];
        }
    }

    // ----------------------------------------------------------
    /**
     * return data stored
     * @return data stored
     */
    public byte[] getSpace()
    {
        return space;
    }

 // ----------------------------------------------------------
    /**
     * update data in buffer, assumed this is done when data is changed
     * @param data to be stored in space array
     */
    public void setSpace(byte[] data)
    {
        for (int i = 0; i < 4096; i++)
        {
            space[i] = data[i];
        }
    }

    // ----------------------------------------------------------
    /**
     * return data stored
     * @return data stored
     */
    public boolean getDirtyBit()
    {
        return dirtyBit;
    }

    // ----------------------------------------------------------
    /**
     * return position
     * @return ID
     */
    public int getPosID()
    {
        return posID;
    }

 // ----------------------------------------------------------
    /**
     * set posID
     * @param pos in array
     */
    public void setPosID(int pos)
    {
        posID = pos;
    }

    // ----------------------------------------------------------
    /**
     * Sets the dirty bit to b
     * @param b boolean value to set dirty bit to
     */
    public void setDirtyBit(boolean b)
    {
        dirtyBit = b;
    }
}

