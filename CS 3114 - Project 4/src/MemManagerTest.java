import junit.framework.TestCase;


// -------------------------------------------------------------------------
/**
 *  Test Memmanager methods
 *
 *  @author Dan
 *  @version Dec 5, 2013
 */
public class MemManagerTest
    extends TestCase
{


    /**
     * insert byte then retrieve it
     */
    public void testInsertAndGet() {
        MemManager mem = new MemManager(null, 0, 0);

        byte[] testByte1 = {0x1, 0x1, 0x1, 0x1};
        byte[] testByte2 = {0x2, 0x2, 0x2, 0x2};

        //test insert
        int oneHandle = mem.insert(testByte1, 4);
        int twoHandle = mem.insert(testByte2, 4);

        assertEquals(0, oneHandle);
        assertEquals(6, twoHandle);

        //test get
        byte[] get1 = mem.get(oneHandle);
        byte[] get2 = mem.get(twoHandle);

        assertEquals(4, get1.length);
        assertEquals(4, get1.length);

        assertEquals(1, get1[0]);
        assertEquals(1, get1[1]);
        assertEquals(1, get1[2]);
        assertEquals(1, get1[3]);

        assertEquals(2, get2[0]);
        assertEquals(2, get2[1]);
        assertEquals(2, get2[2]);
        assertEquals(2, get2[3]);
    }


}
