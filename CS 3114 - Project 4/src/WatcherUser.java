
// -------------------------------------------------------------------------
/**
 *  Class that holds watcher data
 *
 *  @author Dan
 *  @version Oct 14, 2013
 */
public class WatcherUser
{
    /**
     * Hold the user name of the watcher user.
     */
    private String name;

    /**
     * Holds the latitude coordinate of the watcher user.
     */
    public double    latitude;

    /**
     * Holds the longitude coordinate of the watcher user.
     */
    public double    longitude;

    /**
     * holds x, y coordinates of the watcher user
     */
    private Coordinate coord;

    /**
     * Creates a new WatcherUser object.
     *
     * @param userName
     *            The user name of the watcher user.
     * @param lat
     *            The latitude coordinate of the watcher user.
     * @param lon
     *            The longitude coordinate of the watcher user.
     */
    public WatcherUser(String userName, double lat, double lon)
    {
        name = userName;
        coord = new Coordinate(lon + 180, lat + 90);
        latitude = lat;
        longitude = lon;
    }

    /**
     * print out name lon lat
     */
    @Override
    public String toString()
    {
        return name + " " + longitude + " " + latitude;

    }


    // ----------------------------------------------------------
    /**
     * get name from
     * @return name
     */
    public String name()
    {
        return name;
    }

    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param coord
     * @param split
     * @return 0 if equal, 1 if larger, -1 if smaller
     */
    public int compareTo(double coord, boolean split)
    {
            if (split)
            {
                if (coord > longitude + 180)
                {
                    return -1;
                }
                else if (coord < longitude + 180)
                {
                    return 1;
                }
                return 0;
            }
            else
            {
                if (coord > latitude + 90)
                {
                    return -1;
                }
                else if (coord < latitude + 90)
                {
                    return 1;
                }
                return 0;
            }
    }

    // ----------------------------------------------------------
    /**
     * get the coordinates of the watcher user
     * @return coord
     */
    public Coordinate getCoord()
    {
        return coord;
    }

}
