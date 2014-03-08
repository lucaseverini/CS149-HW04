/**
 *
 * @author arashzahoory
 */
public class MegaByte {
    
    private String type;  // . (empty), A, B, C, ..., a, b, c, ...
    
    //create MegaByte object with type given
    public MegaByte(String type) {
        this.type = type;
    }
    
    //create MegaByte object that is empty
    public MegaByte(){
        type = "[__]";
    }
    
    //return the MegaByte type
    public String getMegaByte(){
        return type;
    }
    
    //set the MegaByte type
    public void setMegaByte(String type){
        this.type = type;
    } 
    
}
