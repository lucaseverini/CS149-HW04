/*
 * @author Arash Zahoory, Luca Severini, Romeo Stevens
 */

/**
 * Gaps is a class that manages the information regarding gaps in memory
 *
 */
public class Gaps {

    private final int gapSize;
    private final int gapLocation;

     /**
     * Constructor for class Gaps that includes arguments from caller
     *
     * @param gapSize the size of the gap that this object is being created for
     * @param gapLocation the location of the gap for which this object is being created for
     */
    public Gaps(int gapSize, int gapLocation) {
        this.gapSize = gapSize;
        this.gapLocation = gapLocation;
    }
    
    /**
     * Constructor for class Gaps that does not include arguments from caller (is an empty gap)
     *
     */
    public Gaps(){
        gapSize = 100;
        gapLocation = 0;
    }

    /**
     * This returns the size of the gap
     *
     * @return gapSize
     */
    public int getGapSize() {
        return gapSize;
    }
    
    /**
     * This returns the location of the gap
     *
     * @return gapLocation
     */
    public int getGapLocation() {
        return gapLocation;
    }
}
