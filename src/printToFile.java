/*
 * @author Arash Zahoory, Luca Severini, Romeo Stevens
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * This class manages the printing of text to a file
 *
 */
public class printToFile {
    /**
     * This prints the incoming String parameter: text to file:
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
            bw.write(text);
            bw.close();
            System.out.println("\nFirst Come First Serve has been printed to file. \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
