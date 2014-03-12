/**
 *
 * @author arashzahoory
 */
public class MegaByte {
    
    private String type;  // . (empty), A, B, C, ..., a, b, c, ...
    private final boolean full;
    
    //create MegaByte object with type given
    public MegaByte(String type) {
        this.type = type;
        full = true;
    }
    
    //create MegaByte object that is empty
    public MegaByte(){
        type = "[__]";
        full = false;
    }
    
    //return the MegaByte type
    public String getMegaByte(){
        return type;
    }
    
    //set the MegaByte type
    public void setMegaByte(String type){
        this.type = type;
    } 
    
    public boolean isFull(){
        return full;
    }
    
}
