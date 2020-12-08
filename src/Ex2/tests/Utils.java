package Ex2.tests;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    public static String stringFromFile(String path){
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(path)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return content;
    }
}
