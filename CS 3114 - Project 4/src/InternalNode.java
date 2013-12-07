import java.nio.ByteBuffer;


// -------------------------------------------------------------------------
/**
 *   node has 2 pointers for left and right, and stores data value
 *
 *  @author Harjas Ahuja, Daniel Moore
 *  @version Oct 7, 2013
 */
public class InternalNode
    implements TreeNode
{

    //handles for next nodes
    public int right;
    public int left;
    //node knows about its own handle so it can update itself on disk if
    //a left or right pointer changes
    public int myHandle;
    //Memory Manager reference
    MemManager mem;
    NodeConverter convert;

    // ----------------------------------------------------------
    /**
     * Create a new InternalNode object.
     * @param right right child
     * @param left left child
     * @param mem manager
     */
    InternalNode(int right, int left, MemManager mem)
    {
        this.right = right;
        this.left = left;
        this.mem = mem;
        convert = new NodeConverter(mem);
    }

    /**
     * Always false
     * @return false not a leaf node
     */
    @Override
    public boolean isLeaf()
    {
        return false;
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param handle
     */
    public void setHandle(int handle) {
        myHandle = handle;
    }


    /**
     * Set the right pointer of the node
     * @param newRight node
     */
    public void setRight(int newRight)
    {
//        //update data on disk if it has changed
        if (right != newRight) {

            right = newRight;

            //create a new array to hold data
            byte[] rightBytes = new byte[4];

            //convert handle to bytes
            ByteBuffer buff = ByteBuffer.allocate(4);
            buff.putInt(newRight);
            buff.rewind();

            rightBytes[0] = buff.get();
            rightBytes[1] = buff.get();
            rightBytes[2] = buff.get();
            rightBytes[3] = buff.get();

            mem.insertAt(rightBytes, myHandle, false);
        }

    }

    /**
     * Set the left pointer of the node
     * @param newLeft node
     */
    public void setLeft(int newLeft)
    {
      //update data on disk if it has changed
        if (left != newLeft) {
            left = newLeft;

          //create a new array to hold data
            byte[] leftBytes = new byte[4];

            //convert handle to bytes
            ByteBuffer buff = ByteBuffer.allocate(4);
            buff.putInt(newLeft);
            buff.rewind();

            leftBytes[0] = buff.get();
            leftBytes[1] = buff.get();
            leftBytes[2] = buff.get();
            leftBytes[3] = buff.get();

            mem.insertAt(leftBytes, myHandle, true);

        }
    }

    /**
     * get right child
     * @return right child
     */
    public TreeNode right()
    {
        if (right == -1) {
            return Flyweight.getInstance();
        }
        //retrieve node from memory
        byte[] rightN = mem.get(right);

        //convert to an object and return
        return convert.deserialize(rightN);
    }

    /**
     * get left child
     * @return left child
     */
    public TreeNode left()
    {
        if (left == -1) {
            return Flyweight.getInstance();
        }
        //retrieve node from memory
        byte[] leftN = mem.get(left);

        //convert to an object and return
        return convert.deserialize(leftN);
    }

    public String toString()
    {
        return "I";
    }

    // ----------------------------------------------------------
    /**
     * get right handle
     * @return right
     */
    public int getRightHandle() {
        return right;
    }

    // ----------------------------------------------------------
    /**
     * get left handle
     * @return left
     */
    public int getLeftHandle() {
        return left;
    }
}
