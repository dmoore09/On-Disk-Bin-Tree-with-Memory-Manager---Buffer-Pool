import java.text.DecimalFormat;


// -------------------------------------------------------------------------
/**
 *  Takes in a string and finds out what command it is and does the required
 *  operation
 *
 *  @author Dan
 *  @version Dec 2, 2013
 */
public class CommandHandler
{
    private BinTree tree;

    // ----------------------------------------------------------
    /**
     * Create a new CommandHandler object.
     * @param bin tree that will be used to process requests
     */
    public CommandHandler(BinTree bin) {
        tree = bin;
    }


    // ----------------------------------------------------------
    /**
     * decode the command and perform the operation
     * @param command
     */
    public void decodeCommand(String command) {
         //split the string
         String[] splitCmd = command.split(" ");

         //add command check
         if (splitCmd[0].equals("add")) {
             add(splitCmd);
         }
         //delete command check
         else if (splitCmd[0].equals("delete")) {
             delete(splitCmd);
         }
         //search command check
         else if (splitCmd[0].equals("search")) {
             search(splitCmd);
         }
         //debug
         else {
             debug(splitCmd);
         }
    }

    /**
     * process an add command, adds a wathcer to the bintree
     * @param cmd add command
     */
    private void add(String[] cmd){
        //round x and y to first decimal place
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        double x = Double.valueOf(cmd[1]);
        double y = Double.valueOf(cmd[2]);
        x = Double.valueOf(twoDForm.format(x));
        y = Double.valueOf(twoDForm.format(y));

        //create a coordinate for the insert
        Coordinate coord = new Coordinate(x + 180.0, y + 90.0);

        //create a watcherUser
        WatcherUser watcher = new WatcherUser(cmd[3], y, x);

        //make sure it is not a duplicate
        if (tree.find(coord) == Flyweight.getInstance()) {
            tree.insert(watcher, coord);
            System.out.println(cmd[3] + " " + x + " " + y + " is added to the"
                + " bintree");
        }
        else {
            System.out.println(cmd[3] + " " + x + " " + y +
                " duplicates a watcher already in the bintree");
        }


    }

    /**
     * process an delete command, deletes a wathcer to the bintree
     * @param cmd delete command
     */
    private void delete(String[] cmd){

        //round x and y to first decimal place
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        double x = Double.valueOf(cmd[1]);
        double y = Double.valueOf(cmd[2]);
        x = Double.valueOf(twoDForm.format(x));
        y = Double.valueOf(twoDForm.format(y));

        //create a coordinate for the insert
        Coordinate coord = new Coordinate(x + 180.0, y + 90.0);


        if (tree.find(coord) == Flyweight.getInstance())
        {
            System.out.println("There is no record at "  + x + " " + y + " in the "
                + "bintree");
        }
        else
        {
            tree.remove(coord);
            //TODO get name
            System.out.println("name " + x + " " + y + " is removed from the "
                + "bintree");
        }


    }

    /**
     * process an search command, searches for a watcher in the bintree
     * @param cmd search command
     */
    private void search(String[] cmd){

        //round x and y to first decimal place
        DecimalFormat twoDForm = new DecimalFormat("#.#");
        double x = Double.valueOf(cmd[1]);
        double y = Double.valueOf(cmd[2]);
        x = Double.valueOf(twoDForm.format(x));
        y = Double.valueOf(twoDForm.format(y));

        double radius = Double.valueOf(cmd[3]);

        //create a coordinate for the search
        Coordinate coord = new Coordinate(x + 180.0, y + 90.0);

        int nodesVisited = tree.regionSearch(coord, radius);

        System.out.println("Search " + x + " " + y +
            " returned the following watchers:");
        System.out.println("Watcher search caused " + nodesVisited +
            " bintree nodes to be visited.");
    }

    /**
     * process an debug command, searches for a watcher in the bintree
     * @param cmd debug
     */
    private void debug(String[] cmd){
        tree.preOrder();
    }
}
