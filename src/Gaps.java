/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author arashzahoory
 */
public class Gaps {

    private final int gapSize;
    private final int gapLocation;

    public Gaps(int gapSize, int gapLocation) {
        this.gapSize = gapSize;
        this.gapLocation = gapLocation;
    }
    
    public Gaps(){
        gapSize = 100;
        gapLocation = 0;
    }

    public int getGapSize() {
        return gapSize;
    }

    public int getGapLocation() {
        return gapLocation;
    }
}
