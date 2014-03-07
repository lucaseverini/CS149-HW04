
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author arashzahoory
 */
public class printToFile {


    /**
     * This prints the content of String variable 'content' text file name is:
     * newFile.txt
     *
     * @param text
     */
    public void printToFile(String text) {
        try {
            File file = new File("test", "newFile.txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            //parameter 'content' is content to be written to file
            //should probably store in a string, then put here.
            bw.write(text);
            bw.close();
            System.out.println("\nFirst Come First Serve has been printed to file. \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
