import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;
import junit.framework.TestCase;


// -------------------------------------------------------------------------
/**
 *  Test buffer pool methods
 *
 *  @author Dan
 *  @version Nov 5, 2013
 */
public class BufferPoolTest
    extends junit.framework.TestCase
{
    private BufferPool pool;
    private RandomAccessFile file;
    private ShortConverter converter;

    /**
     * set up a random access file for testing
     */
    public void setUp() throws IOException {
        file = new RandomAccessFile("test", "rw");
        file.writeShort(10);
        file.writeShort(5);
        pool = new BufferPool(file, 4096, 1);
        converter = new ShortConverter();
    }

    public void testAll() throws IOException {
        byte[] read = new byte[4];
        pool.getbytes(read, 4, 0);
        assertEquals(10, converter.getKey(read));
        byte[] write = new byte[4];
        pool.insert(write, 0, 2);
        pool.getbytes(read, 4, 0);
        assertEquals(5, converter.getValue(read));
    }
}
