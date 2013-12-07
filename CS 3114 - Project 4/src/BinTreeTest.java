
import junit.framework.TestCase;


// -------------------------------------------------------------------------
/**
 *  Test BinTree methods
 *
 *  @author Dan
 *  @version Oct 14, 2013
 */
public class BinTreeTest
    extends TestCase
{

    private BinTree tree;
    WatcherUser a;
    WatcherUser b;
    WatcherUser c;
    WatcherUser d;
    WatcherUser e;

    /**
     * run before each test case
     */
    public void setUp()
    {
        tree = new BinTree(null);
        a = new WatcherUser("Riley", 10, 45);
        b = new WatcherUser("Taylor", 30, 70);
        c = new WatcherUser("Nevaeh", 320, 130);
        d = new WatcherUser("Dominic", 55, 170);
        e = new WatcherUser("Tristan", 240, 110);
    }

    // ----------------------------------------------------------
    /**
     * test the insert method and delete
     */
    public void testInsertDelete()
    {
        Coordinate RCoord = new Coordinate(74.3, 65.7);
        Coordinate TCoord = new Coordinate(21.2 + 180, -38.6 + 90);
        Coordinate NCoord = new Coordinate(-11.0 + 180, 63.1 + 90);
        Coordinate DCoord = new Coordinate(-79.2 + 180, 37.3 + 90);
        Coordinate TrisCoord = new Coordinate(-117.1 + 180, 5.0 + 90);


        tree.insert(a, RCoord);
        tree.insert(b, TCoord);
        tree.insert(c, NCoord);
        tree.insert(d, DCoord);
        tree.insert(e, TrisCoord);
        tree.preOrder();

        assertEquals(5, tree.size());

        tree.remove(RCoord);
        tree.remove(TCoord);
        tree.remove(TrisCoord);
        tree.remove(NCoord);
        tree.remove(DCoord);

        tree.preOrder();

        assertEquals(0, tree.size());
    }

    /**
     * test the delete method
     */
    public void testFindRegionSearch()
    {
        Coordinate aCoord = new Coordinate(10, 45);
        Coordinate bCoord = new Coordinate(350, 45);
        Coordinate cCoord = new Coordinate(24, 150);

        tree.insert(a, aCoord);
        tree.insert(b, bCoord);
        tree.insert(c, cCoord);

        assertNotNull(tree.find(cCoord));

        assertNotNull(tree.regionSearch(cCoord, 27 * 2));
    }

    public void testInsertDel2()
    {
        Coordinate aCoord = new Coordinate(10, 45);
        Coordinate bCoord = new Coordinate(350, 45);
        tree.insert(a, aCoord);
        tree.insert(b, bCoord);
        tree.remove(bCoord);
        tree.preOrder();

        assertEquals(1, tree.size());


    }




}