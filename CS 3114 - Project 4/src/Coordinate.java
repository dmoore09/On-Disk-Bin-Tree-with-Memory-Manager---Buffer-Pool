
// -------------------------------------------------------------------------
/**
 *  wrapper class for coordinates
 *
 *  @author Dan
 *  @version Oct 17, 2013
 */
public class Coordinate
{
    private double xCoord;
    private double yCoord;

    // ----------------------------------------------------------
    /**
     * Create a new Coordinate object.
     * @param x coordinate of object
     * @param y coordinate of object
     */
    public Coordinate(double x, double y)
    {
        xCoord = x;
        yCoord = y;
    }

    /**
     * get x coordinate
     * @return xCoord
     */
    public double getX()
    {
        return xCoord;
    }

    /**
     * get y coordinate
     * @return yCoord
     */
    public double getY()
    {
        return yCoord;
    }




}
