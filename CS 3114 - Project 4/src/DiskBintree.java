import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

//On my honor:
//
//- I have not used source code obtained from another student,
//or any other unauthorized source, either modified or
//unmodified.
//
//- All source code and documentation used in my program is
//either my original work, or was derived by me from the
//source code published in the textbook for this course.
//
//- I have not discussed coding details about this project with
//anyone other than my partner (in the case of a joint
//submission), instructor, ACM/UPE tutors or the TAs assigned
//to this course. I understand that I may discuss the concepts
//of this program with other students, and that another student
//may help me debug my program so long as neither of us writes
//anything during the discussion or modifies any computer file
//during the discussion. I have violated neither the spirit nor
//letter of this restriction.
// -------------------------------------------------------------------------
/**
 *  Main class for project 4 holds the main method.
 *
 *  @author Dan
 *  @version Dec 2, 2013
 */
public class DiskBintree
{
    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param args holds command file name, numb-buffers, buffersize
     */
    public static void main(String[] args){
        //numb-buffers, buffersize
        int numBuffers = Integer.parseInt(args[1]);
        int bufferSize = Integer.parseInt(args[2]);

        //open commands file to read from
        Scanner commands = null;
        //open a new randomAccessfile to store bin tree data onto
        RandomAccessFile data = null;
        try
        {
            commands = new Scanner(new File(args[0]));
            data = new RandomAccessFile("p4bin.dat", "rw");
        }
        catch (FileNotFoundException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //create a memory manager based on specifications
        MemManager manager = new MemManager(data, bufferSize, numBuffers);

        //create a new bintree and pass a pointer to the new memory manager
        BinTree tree = new BinTree(manager);

        //create a command handler and pass a pointer to the new bintree
        CommandHandler cmdHandler = new CommandHandler(tree);

        //loop through the commands file until the end
        while (commands.hasNextLine()){
             //get current line and decode it and perfom operation
             String command = commands.nextLine();
             cmdHandler.decodeCommand(command);
        }

        //tree.preOrder();
    }
}
