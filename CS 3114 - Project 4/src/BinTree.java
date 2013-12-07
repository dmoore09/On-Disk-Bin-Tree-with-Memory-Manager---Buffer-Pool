


/**
 * // -------------------------------------------------------------------------
/**
 *  A BinTree is a data structure used for storing point data
 *  in two or more dimensions.
 *
 *  @author Harjas Ahuja, Daniel Moore
 *  @version Oct 13, 2013
 */
public class BinTree
{


    private int size;
    private TreeNode root;
    private MemManager memMan;
    private NodeConverter convert;

    // ----------------------------------------------------------
    /**
     * initialize the tree
     * @param mem manager that stores memory for the tree on disk
     */
    public BinTree(MemManager mem)
    {
        convert = new NodeConverter(mem);
        memMan = mem;
        root = Flyweight.getInstance();
    }


    // ----------------------------------------------------------
    /**
     * insert a watcher into the bin tree
     * @param watcher being inserted
     * @param coord key for watcher
     */
    public void insert(WatcherUser watcher, Coordinate coord)
    {
        Rectangle box = new Rectangle(360, 180, 0, 0);
        int rootHandle = insertHelp(root, watcher, coord, 0, box);
        byte[] rootb = memMan.get(rootHandle);
        root = convert.deserialize(rootb);

        if (!root.isLeaf()){
            ((InternalNode)root).setHandle(rootHandle);
        }
        size++;
    }

    // ----------------------------------------------------------
    /**
     * Helper method for insert
     * @param rNode root of current subtree
     * @param watcher being inserted
     * @param coord key for watcher
     * @param level of rNode
     * @param box current bounding box
     * @return node being inserted
     */
    private int insertHelp(TreeNode rNode, WatcherUser watcher, Coordinate coord, int level, Rectangle box)
    {
        boolean split;
        int curLvl = level;
        //determine the split at this level
        //TODO check here when testing
        if (level % 2 == 0)
        {
            split = true;
        }
        else
        {
            split = false;
        }
        //empty leaf node
        if (rNode == Flyweight.getInstance())
        {
            //create a new leaf node in memory and return the handle
            byte[] watcherByte = convert.WatcherToByte(watcher);
            int watchHandle = memMan.insert(watcherByte, watcherByte.length);

            LeafNode leaf = new LeafNode(memMan, watchHandle);
            byte[] leafB = convert.searialize(leaf);
            int leafHandle = memMan.insert(leafB, leafB.length);
            return leafHandle;
        }
        //internal node split x-axis
        else if (!rNode.isLeaf())
        {
            Rectangle[] tangles = box.split(split);
            //compare bounding box
            if(coord.getX() >= tangles[0].minX() && coord.getX() <= tangles[0].maxX() &&
                coord.getY() >= tangles[0].minY() && coord.getY() <= tangles[0].maxY())
            {
                ((InternalNode)rNode).setRight(insertHelp(((InternalNode)rNode).right(),
                    watcher, coord, curLvl + 1, tangles[0]));
            }
            //belongs on the left
            else if (coord.getX() >= tangles[1].minX() && coord.getX() <= tangles[1].maxX() &&
                coord.getY() >= tangles[1].minY() && coord.getY() <= tangles[1].maxY())
            {
                ((InternalNode)rNode).setLeft(insertHelp(((InternalNode)rNode).left(),
                    watcher, coord, curLvl + 1, tangles[1]));
            }
        }

        //non empty leaf node
        else if (rNode.isLeaf())
        {
            //create a new internal node in memory
            TreeNode newRootNode = new InternalNode(-1, -1, memMan);
            //write to disk
            byte[] newRootBytes = convert.InternalToByte((InternalNode)newRootNode);
            int newRootHandle = memMan.insert(newRootBytes, 9);
            ((InternalNode)newRootNode).setHandle(newRootHandle);


            insertHelp(newRootNode, (((LeafNode)rNode).value()),
                ((LeafNode)rNode).value().getCoord(), level, box);

            insertHelp(newRootNode, watcher, coord, level, box);

            //return pointer to new subtree
            return newRootHandle;
        }

        return ((InternalNode)rNode).myHandle;
    }


    // ----------------------------------------------------------
    /**
     * remove a watcher at a specified coordinate
     * @param coord key of watcher
     */
    public void remove(Coordinate coord)
    {
        Rectangle box = new Rectangle(360, 180, 0, 0);
        //if watcher is in list remove it


        int rootHandle = removeHelp(root, coord, 0, box);
        size--;

        if (size == 0)
        {
            root = Flyweight.getInstance();
        }
        else
        {
            byte[] rootBytes = memMan.get(rootHandle);
            root = convert.deserialize(rootBytes);
        }


    }

    // ----------------------------------------------------------
    /**
     * helper method for remove because the find method is always called we
     * know that the object is in the tree so we must find and remove it
     * @param rNode root of current subtree
     * @param coord of object being removed
     * @param level of rNode
     * @param xBox upper corner of bounding box
     * @param yBox upper corner of bounding box
     * @return watcher removed
     */
    private int removeHelp(TreeNode rNode, Coordinate coord, int level, Rectangle box)
    {
        boolean split;
        //split value that will be compared to current coordinate
        //location of bounding box upper corner
        int curLvl = level;
        //determine the split at this level
        split = false;
        if (level % 2 == 0)
        {
            split = true;
        }

        if (rNode == Flyweight.getInstance())
        {
            return -1;
        }
        //Node is found return Flyweight.getInstance()
        if (rNode.isLeaf() && rNode.isLeaf() && ((LeafNode)rNode).value().compareTo(coord.getX(), true) == 0 &&
            ((LeafNode)rNode).value().compareTo(coord.getY(), false) == 0)
        {
            return -1;
        }

        //internal node splitting x
        else if (!rNode.isLeaf())
        {

            Rectangle[] tangles = box.split(split);
            //is on the right
            if (coord.getX() >= tangles[0].minX() && coord.getX() <= tangles[0].maxX() &&
                coord.getY() >= tangles[0].minY() && coord.getY() <= tangles[0].maxY())
            {
                ((InternalNode)rNode).setRight(removeHelp(((InternalNode)rNode).right(), coord, curLvl + 1, tangles[0]));
            }
            //is on the left
            else if (coord.getX() >= tangles[1].minX() && coord.getX() <= tangles[1].maxX() &&
                coord.getY() >= tangles[1].minY() && coord.getY() <= tangles[1].maxY())
            {
                ((InternalNode)rNode).setLeft(removeHelp(((InternalNode)rNode).left(), coord, curLvl + 1, tangles[1]));
            }
            if (((InternalNode)rNode).right() == Flyweight.getInstance() && ((InternalNode)rNode).left() == Flyweight.getInstance())
            {
                return -1;
            }
            else if(((InternalNode)rNode).left() != Flyweight.getInstance() && ((InternalNode)rNode).left().isLeaf() && ((InternalNode)rNode).right() == Flyweight.getInstance())
            {
                return ((InternalNode)rNode).left;
            }
            else if(((InternalNode)rNode).right() != Flyweight.getInstance() && ((InternalNode)rNode).right().isLeaf() && ((InternalNode)rNode).left() == Flyweight.getInstance())
            {
                return ((InternalNode)rNode).right;
            }
            else
            {
                //TODO how to replace this? return rNode;
                return ((InternalNode)rNode).myHandle;
            }

        }
        return ((InternalNode)rNode).myHandle;
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param coord search key
     * @return watcher at certain coordinate
     */
    public TreeNode find(Coordinate coord)
    {
        Rectangle box = new Rectangle(360, 180, 0, 0);
        return findHelp(root, coord, 0, box);
    }

    /**
     * Find a certain value inside the tree
     * @param rNode root of current subtree
     * @param coord key of object being searched for
     * @param level of rNode
     * @param xBox bounding box upper corner
     * @param yBox bounding box upper corner
     * @return Flyweight.getInstance() if not in tree watcher if found
     */
    private TreeNode findHelp(TreeNode rNode, Coordinate coord, int level, Rectangle box)
    {
        boolean split;

        int curLvl = level;
        //determine the split at this level
        if (level % 2 == 0)
        {
            split = true;
        }
        else
        {
            split = false;
        }

        if(rNode == Flyweight.getInstance())
        {
            return Flyweight.getInstance();
        }
        //Has been found
        else if (rNode.isLeaf() && ((LeafNode)rNode).value().compareTo(coord.getX(), true) == 0 &&
            ((LeafNode)rNode).value().compareTo(coord.getY(), false) == 0)
        {
            return rNode;
        }
        //not in tree

        //internal node split x
        else if (!rNode.isLeaf())
        {
            Rectangle[] tangles = box.split(split);
            //is on the right
            if (coord.getX() >= tangles[0].minX() && coord.getX() <= tangles[0].maxX() &&
                coord.getY() >= tangles[0].minY() && coord.getY() <= tangles[0].maxY())
            {
                return findHelp(((InternalNode)rNode).right(), coord, curLvl + 1, tangles[0]);
            }
            //is on the left
            else if(coord.getX() >= tangles[1].minX() && coord.getX() <= tangles[1].maxX() &&
                coord.getY() >= tangles[1].minY() && coord.getY() <= tangles[1].maxY())
            {
                return findHelp(((InternalNode)rNode).left(), coord, curLvl + 1, tangles[1]);
            }
        }
        return Flyweight.getInstance();
    }


    // ----------------------------------------------------------
    /**
     *  Amount of objects in the bin tree
     * @return size
     */
    public int size()
    {
        return size;
    }


    // ----------------------------------------------------------
    /**
     * preOrder traversal of the tree. prints out nodes corresponding to their
     * type
     */
    public void preOrder()
    {
        preOrderHelp(root);
    }

    // ----------------------------------------------------------
    /**
     * preOrder traversal of the tree. prints out nodes corresponding to their
     * type
     * @param rNode root of current subtree
     */
    private void preOrderHelp(TreeNode rNode)
    {

        if (rNode == Flyweight.getInstance())
        {
            System.out.println("E");
        }
        else if (rNode.isLeaf())
        {
            WatcherUser watcher = ((LeafNode)rNode).value();
            System.out.println(watcher.toString());
        }
        else
        {
            System.out.println(((InternalNode)rNode).toString());
            preOrderHelp(((InternalNode)rNode).left());
            preOrderHelp(((InternalNode)rNode).right());
        }
    }

    // ----------------------------------------------------------
    /**
     * search a region of the BinTree for a watcher. print out all watchers
     * that are close to coordinate
     * @param coord of earthquake we are observing
     * @param mag 2 * mag^3
     * @return number of nodes visited
     */
    public int regionSearch(Coordinate coord, double mag)
    {

        //search box
        double maxX = coord.getX() + mag;
        double minX = coord.getX() - mag;
        double maxY = coord.getY() + mag;
        double minY = coord.getY() - mag;

        Rectangle box1 = new Rectangle(360, 180, 0, 0);
        Rectangle box2 = new Rectangle(maxX, maxY, minX, minY);
        return regionSearchHelp(root, coord, 0, box1, box2, 0, mag);

    }

    // --------------------------------------------------c--------
    /**
     * Helper method for region search
     * @param rNode root of current subtree
     * @param coord of earthquake being examined
     * @param level of rNode
     * @param bbox
     * @param sbox
     * @return number of nodes visited
     */
    private int regionSearchHelp(TreeNode rNode, Coordinate coord, int level, Rectangle bbox, Rectangle sbox, int visited, double mag)
    {
        int newVisited = visited + 1;
        //check which coordinate we are splitting
        int curLvl = level;
        boolean split = false;
        if (level % 2 == 0)
        {
            split = true;
        }

        if (rNode == Flyweight.getInstance())
        {
            System.out.println("Visisted empty Leaf");
            return newVisited;
        }
        //internal node
        if (!rNode.isLeaf())
        {
            Rectangle[] tangles = bbox.split(split);

            //overlaps the left
            if (tangles[1].minY() <= sbox.maxY() && tangles[1].maxY() >= sbox.minY() && tangles[1].minX() <= sbox.maxX() && tangles[1].maxX() >= sbox.minX())
            {
                newVisited = regionSearchHelp(((InternalNode)rNode).left(), coord, curLvl + 1, tangles[1], sbox, newVisited, mag);
            }
            //overlaps the right
            if (tangles[0].minY() <= sbox.maxY() && tangles[0].maxY() >= sbox.minY() && tangles[0].minX() <= sbox.maxX() && tangles[0].maxX() >= sbox.minX())
            {
                newVisited = regionSearchHelp(((InternalNode)rNode).right(), coord, curLvl + 1, tangles[0], sbox, newVisited, mag);
            }

        }
        //leaf node
        else if (rNode.isLeaf())
        {
            double watcherX = ((LeafNode)rNode).value().getCoord().getX();
            double watcherY = ((LeafNode)rNode).value().getCoord().getY();
            double cX = coord.getX();
            double cY = coord.getY();
            double distance = Math.sqrt(Math.pow(watcherX - cX, 2) + Math.pow(watcherY - cY, 2));
            //check if leaf box intersects
            if (distance <= mag)
            {
                ((LeafNode)rNode).visit();
                return newVisited;
            }
        }
        //Flyweight.getInstance()
        return newVisited;
    }

}
