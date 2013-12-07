
// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Harjas Ahuja, Daniel Moore
 *  @version Oct 7, 2013
 */
public class LeafNode
    implements TreeNode
{
    private WatcherUser handle;
    private int wHandle;
    private MemManager mem;
    private NodeConverter convert;

    // ----------------------------------------------------------


    /**
     * Create a new LeafNode object.
     * @param mem manager
     * @param watcherHandle handle of the watcher payload
     */
    public LeafNode(MemManager mem, int watcherHandle)
    {
        convert = new NodeConverter(mem);
        this.mem = mem;
        wHandle = watcherHandle;
    }

    /**
     * Indicates it is a leaf node
     * @return always true
     */
    @Override
    public boolean isLeaf()
    {
        return true;
    }

    /**
     * get stored data
     * @return watcher value
     */
    public WatcherUser value()
    {
        //retrieve the wather from memory
        byte[] watcher = mem.get(wHandle);

        //convert the watcher and return
        return convert.ByteToWatcher(watcher);
    }

    /**
     * visit the node and print out its values
     */
    public void visit()
    {
        System.out.println(value().toString());
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @return get handle integer for node converter to use
     */
    public int getwHandle()
    {
        return wHandle;
    }
}
