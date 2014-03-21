/*
 * @author Arash Zahoory, Luca Severini, Romeo Stevens
 */

public class MegaByte {

    private String type;  // . (empty), A, B, C, ..., a, b, c, ...
    private final boolean full;

    /**
     * Constructor for class MegaByte that includes arguments from caller
     *
     * @param type the name of the memory unit being created
     */
    public MegaByte(String type) {
        this.type = type;
        full = true;
    }

    /**
     * Constructor for class MegaByte that implies an empty position
     *
     * 
     */
    public MegaByte() {
        type = "[__]";
        full = false;
    }

    /**
     * This returns the name of the memory bit being created
     *
     * @return 'oneSimulation' The string representing the simulation
     */
    public String getMegaByte() {
        return type;
    }

    
    /**
     * This sets the name of the megabyte object
     *
     * @param type
     */
    public void setMegaByte(String type) {
        this.type = type;
    }

    /**
     * This returns whether or not a megabyte unit is full or empty
     *
     * @return full
     */
    public boolean isFull() {
        return full;
    }

}
