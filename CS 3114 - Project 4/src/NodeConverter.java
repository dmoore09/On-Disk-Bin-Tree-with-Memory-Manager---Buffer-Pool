import java.nio.ByteBuffer;


// -------------------------------------------------------------------------
/**
 *  Helper class for the bintree, has two functions:
 *
 *  1. Converts any type of tree node to its byte representation so it can be
 *  stored on disk
 *
 *  2. Converts any appropriate byte array back to a node so it can be used by
 *  bin tree methods
 *
 *  @author Dan
 *  @version Dec 3, 2013
 */
public class NodeConverter
{

    private MemManager mem;

    // ----------------------------------------------------------
    /**
     * Create a new NodeConverter object.
     * @param mem manager used to initialize nodes
     */
    public NodeConverter(MemManager mem) {
        this.mem = mem;
    }

    // ----------------------------------------------------------
    /**
     * searialize a treenode into a byte array and return it
     * @param node to be inserted into pool
     * @return new handle
     */
    public byte[] searialize(TreeNode node) {

        byte[] newNode = null;

        //check to see if it is internal or leaf
        if (node.isLeaf()) {
            newNode = LeafToByte((LeafNode)node);
        }
        else {
            newNode = InternalToByte((InternalNode)node);
        }

        return newNode;
    }

    /**
     * deserialize a treenode from the mem manager and return it
     * @param node byte array to be converted
     * @return a new node
     */
    public TreeNode deserialize(byte[] node) {

        TreeNode newNode = null;

        if (node[0] == leaf) {
            newNode = ByteToLeaf(node);
        }
        else {
            newNode = ByteToInternal(node);
        }

        return newNode;

    }

    /**
     * handle value of an empty leaf node. used as a pointer
     */
    private final byte[] flyweightB = new byte[] {-0x1, -0x1, -0x1, -0x1};

    //one byte values used to distinguish between internal and leaf nodes
    private static final byte internal = 0x0;
    private static final byte leaf = 0x1;


    /**
     * Convert an internal node to its proper byte representation
     *
     *      Internal nodes will store 9 bytes: a one-byte field used to
     *      distinguish internal from leaf nodes, followed by two 4-byte
     *      fields to store handles for the two children.
     *
     * @param node to be converted
     * @return byte representation of node
     */
    public byte[] InternalToByte(InternalNode node) {
        byte[] internalN = new byte[9];
        //set to indicate internal node
        internalN[0] = internal;

        //set child fields
        int right = node.getRightHandle();
        int left = node.getLeftHandle();

        ByteBuffer buff = ByteBuffer.allocate(8);
        buff.putInt(right);
        buff.putInt(left);
        buff.rewind();

        for (int i = 1; i < 9; i++) {
            internalN[i] = buff.get();
        }

        return internalN;
    }

    /**
     * Convert a byte array to an internal node
     *
     *      Internal nodes will store 9 bytes: a one-byte field used to
     *      distinguish internal from leaf nodes, followed by two 4-byte
     *      fields to store handles for the two children.
     *
     * @param bNode to be converted
     * @return object representation of node
     */
    public InternalNode ByteToInternal(byte[] bNode) {
        //wrap in a byte buffer
        ByteBuffer buff = ByteBuffer.wrap(bNode);
        //increment position past internal/leaf indicator
        buff.position(1);
        //get handles
        int right = buff.getInt();
        int left = buff.getInt();


        InternalNode internal1 = new InternalNode(right, left, mem);

        return internal1;
    }

    /**
     * Convert a leaf node to its proper byte representation
     *
     *      Internal nodes will store 9 bytes: a one-byte field used to
     *      distinguish internal from leaf nodes, followed by two 4-byte
     *      fields to store handles for the two children.
     *
     * @param node to be converted
     * @return byte representation of node
     */
    public byte[] LeafToByte(LeafNode node) {
        byte[] leafN = new byte[5];
        //set to indicate leaf node
        leafN[0] = leaf;

        //set watcher field
         int handle = node.getwHandle();

         ByteBuffer buff = ByteBuffer.allocate(4);
         buff.putInt(handle);
         buff.rewind();

         leafN[1] = buff.get();
         leafN[2] = buff.get();
         leafN[3] = buff.get();
         leafN[4] = buff.get();
         return leafN;
    }

    /**
     * Convert a byte array to a leaf node
     *
     *      Leaf nodes that contain a watcher record will require 5 bytes,
     *      storing the following fields: a one-byte field used to distinguish
     *      internal from leaf nodes, and a 4-byte field that stores the handle
     *      to the watcher record.
     *
     * @param bNode to be converted
     * @return object representation of node
     */
    public LeafNode ByteToLeaf (byte[] bNode) {

        //wrap in a byte buffer
        ByteBuffer buff = ByteBuffer.wrap(bNode);
        //increment position past internal/leaf indicator
        buff.position(1);
        //get handles
        int watcher = buff.getInt();

        LeafNode leafN = new LeafNode(mem, watcher);

        return leafN;
    }

    /**
     * Convert a watcher to its proper byte representation
     *
     *      First, an 8-byte double value for the Watcher's X coordinate, then
     *      an 8-byte double value for the Watcher's Y-coordinate, then a series
     *      of characters that store the Watcher's name. The Watcher name
     *      should just store the letters in the name.
     *
     * @param watcher to be converted
     * @return byte representation of a wathcer
     */
    public byte[] WatcherToByte (WatcherUser watcher) {
        //get name
        String name = watcher.name();
        ByteBuffer nameB = ByteBuffer.allocate(name.length() * 2);

        for (int i = 0; i < name.length(); i++) {
            nameB.putChar(name.charAt(i));
        }
        nameB.rewind();

        //create a byte array that holds 16 bytes + name length bytes
        byte[] watcherB = new byte[16 + (name.length() * 2)];

        double x = watcher.longitude;
        double y = watcher.latitude;

        ByteBuffer xBuff = ByteBuffer.allocate(8);
        ByteBuffer yBuff = ByteBuffer.allocate(8);
        xBuff.putDouble(x);
        yBuff.putDouble(y);
        xBuff.rewind();
        yBuff.rewind();

        //copy the doubles into the byte array
        for (int i = 0; i < 16; i++) {
            if (i < 8) {
                watcherB[i] = xBuff.get();
            }
            else {
                watcherB[i] = yBuff.get();
            }
        }

        //copy the name into the byte array
        for (int i = 16; i < 16 + (name.length() * 2); i++) {
            watcherB[i] = nameB.get();
        }
        return watcherB;
    }

    /**
     * Convert a byte array to a watcher
     *
     *      First, an 8-byte double value for the Watcher's X coordinate, then
     *      an 8-byte double value for the Watcher's Y-coordinate, then a series
     *      of characters that store the Watcher's name. The Watcher name
     *      should just store the letters in the name.
     * @param watcher byte array
     * @return object representation of watcher
     */
    public WatcherUser ByteToWatcher (byte[] watcher) {

        //length of the name
        int length = (watcher.length - 16)/2;
        ByteBuffer buff = ByteBuffer.wrap(watcher);
        double x = buff.getDouble();
        double y = buff.getDouble();

        String name = "";
        //get name
        for (int i = 0; i < length; i++) {
            name = name + buff.getChar();
        }

        //create new watcher
        WatcherUser user = new WatcherUser(name, y, x);

        return user;
    }

}
