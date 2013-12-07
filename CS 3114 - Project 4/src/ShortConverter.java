import java.nio.ShortBuffer;
import java.nio.ByteBuffer;


// -------------------------------------------------------------------------
/**
 *  utility class that allows for byte[] to be easily viewed as short values
 *
 *  @author Dan
 *  @version Nov 4, 2013
 */
public class ShortConverter
{
    // ----------------------------------------------------------
    /**
     * get key value and return as short
     * @param record to be converted
     * @return key value as a short
     */
    public short getKey(byte[] record)
    {
        ShortBuffer buff = ByteBuffer.wrap(record).asShortBuffer();
        return buff.get(0);
    }

    // ----------------------------------------------------------
    /**
     * get record value and retrun as a short
     * @param record record to be converted
     * @return value as a short
     */
    public short getValue(byte[] record)
    {
        ShortBuffer buff = ByteBuffer.wrap(record).asShortBuffer();
        return buff.get(1);

    }
}
