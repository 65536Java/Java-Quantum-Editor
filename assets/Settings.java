package assets;

import java.io.*;
import java.util.Locale;

public class Settings {
    File sf = null;
    public Settings(File optionsFile){
        sf = optionsFile;
        if(!sf.exists()){
            try {
                sf.createNewFile();
                FileWriter fw = null;
                try {
                    fw = new FileWriter(sf);
                } catch (IOException e) {

                }
                BufferedWriter bw = new BufferedWriter(fw);
                try {
                    bw.write("zh");
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                }
            } catch (IOException e) {
                Save();
            }
        }
    }
    static Locale def = Locale.getDefault();
    public String Load(){
        String fs = TheWindow.fread(sf);
        if(fs.isEmpty())Main.lang = def.getLanguage(); else Main.lang = fs;
        return Main.lang;
    }
    public void Save(){
        FileWriter fw = null;
        try {
            fw = new FileWriter(sf);
        } catch (IOException e) {

        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            bw.write((Main.lang=="zh") ? "" :"zh");
            bw.flush();
            bw.close();
        } catch (IOException e) {
        }
    }
}
