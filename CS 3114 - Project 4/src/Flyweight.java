
// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Dan
 *  @version Oct 17, 2013
 */
public class Flyweight
    implements TreeNode
{

    private static final Flyweight flyweightIn = new Flyweight();

    private Flyweight() {}

    // ----------------------------------------------------------
    /**
     * get the instance
     * @return flyWeight
     */
    public static Flyweight getInstance() {
        return flyweightIn;
    }

    @Override
    public boolean isLeaf()
    {
        // TODO Auto-generated method stub
        return false;
    }


}