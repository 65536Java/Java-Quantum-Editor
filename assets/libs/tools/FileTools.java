package assets.libs.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileTools {
    public static String freadLine(File ff, int l) {
        try (FileReader fr = new FileReader(ff);
             BufferedReader bf = new BufferedReader(fr);){
            String s = null;
            int counter = 0;
            String line;
            while ((line = bf.readLine()) != null) {
                counter++;
                if(counter == l)s = line + "\n";
            }
            return s;
        } catch (Exception exception) {
            return null;
        }
    }
    public static String fread(File ff) {
        try (FileReader fr = new FileReader(ff);
             BufferedReader bf = new BufferedReader(fr);){
            StringBuffer sb = new StringBuffer();

            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception exception) {
            return null;
        }
    }

}
