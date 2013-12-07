
// -------------------------------------------------------------------------
/**
 *  Rectangle class to represent bounding boxes
 *
 *  @author Dan
 *  @version Oct 15, 2013
 */
public class Rectangle
{
    private double maxX;
    private double maxY;
    private double minX;
    private double minY;

    /**
     * @param maxX
     * @param maxY
     * @param minX
     * @param minY
     */
    public Rectangle(double maxX, double maxY, double minX, double minY)
    {
        this.maxX = maxX;
        this.maxY = maxY;
        this.minX = minX;
        this.minY = minY;
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param xOrY split x if true, split y if false
     * @return 2 rectangles
     */
    public Rectangle[] split(boolean xOrY)
    {
        Rectangle[] tangles = new Rectangle[2];
        if (xOrY)
        {

            //create 2 rectangles split by the x-axis
            tangles[0] = new Rectangle(maxX, maxY, (maxX + minX)/2, minY);
            tangles[1] = new Rectangle((maxX + minX)/2, maxY, minX, minY);
            return tangles;
        }
        else
        {
            //create 2 rectangles split by the y-axis
            tangles[0] = new Rectangle(maxX, maxY, minX, (maxY + minY)/2);
            tangles[1] = new Rectangle(maxX, (maxY + minY)/2, minX, minY);
            return tangles;
        }
    }

    public double maxX()
    {
        return maxX;
    }

    public double minX()
    {
        return minX;
    }

    public double maxY()
    {
        return maxY;
    }

    public double minY()
    {
        return minY;
    }
}
