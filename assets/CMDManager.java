package assets;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CMDManager {
    public void Run(String command) {
        ProcessBuilder p = new ProcessBuilder(command);
        try {
            p.start();
        } catch (IOException e) {

        }
    }
    public void open(File file) {
        ProcessBuilder pb = new ProcessBuilder("cmd /c start cmd /c java "+file.getPath());
        try {
            var pr = pb.start();
            try (var is = new BufferedReader(new InputStreamReader(pr.getInputStream()))){
                var l = "";
                while((l = is.readLine()) != null){
                    System.out.println(l);
                }
            }
            pb = new ProcessBuilder("pause");
            pb.start();
        } catch (IOException e) {
        }
    }
}
