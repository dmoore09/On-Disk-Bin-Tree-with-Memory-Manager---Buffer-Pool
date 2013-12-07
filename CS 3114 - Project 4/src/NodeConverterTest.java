import java.nio.ByteBuffer;
import junit.framework.TestCase;


// -------------------------------------------------------------------------
/**
 *  test NodeConverter Methods
 *
 *  @author Dan
 *  @version Dec 4, 2013
 */
public class NodeConverterTest
    extends TestCase
{

    private InternalNode in;
    private LeafNode leaf;
    private NodeConverter con;
    private WatcherUser user;

    /**
     * set up before each test
     */
    public void setUp() {
        con = new NodeConverter(null);
        in = new InternalNode(1, 2, null);
        leaf = new LeafNode(null, 10);
        user = new WatcherUser("Bobby", 1, 2);
    }

    /**
     * test desearilaize and searialize a internal node
     */
    public void testDeAndSeIn() {
        byte[] inByte = con.searialize(in);

        ByteBuffer buff = ByteBuffer.wrap(inByte);
        buff.position(1);
        int right = buff.getInt();
        int left = buff.getInt();

        assertEquals(1, right);
        assertEquals(2, left);


        assertEquals(inByte[0], 0x0);

        InternalNode node = (InternalNode)con.deserialize(inByte);

        assertEquals(1, node.getRightHandle());
        assertEquals(2, node.getLeftHandle());
    }

    /**
     * test desearilaize and searialize a leaf node
     */
    public void testDeAndSeLeaf() {
        byte[] leafByte = con.searialize(leaf);

        ByteBuffer buff = ByteBuffer.wrap(leafByte);
        buff.position(1);
        int watcher = buff.getInt();

        assertEquals(10, watcher);

        assertEquals(leafByte[0], 0x1);

        LeafNode node = (LeafNode)con.deserialize(leafByte);

        assertEquals(10, node.getwHandle());
     }

    /**
     * convert a watcher
     */
    public void testDeAndSeWa() {
        byte[] watcherByte = con.WatcherToByte(user);

        ByteBuffer buff = ByteBuffer.wrap(watcherByte);
        double x = buff.getDouble();
        double y = buff.getDouble();
        assertEquals(2.0, x);
        assertEquals(1.0, y);

        String name = "";
        for (int i = 0; i < (watcherByte.length - 16)/2; i++) {
            name = name + buff.getChar();
        }

        assertEquals("Bobby", name);

        WatcherUser newWatch = con.ByteToWatcher(watcherByte);

        assertEquals(2.0, newWatch.longitude);
        assertEquals(1.0, newWatch.latitude);

        assertEquals("Bobby", newWatch.name());
    }


}
