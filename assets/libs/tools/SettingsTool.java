package assets.libs.tools;

import java.io.*;
import assets.libs.Settings;

public class SettingsTool {
    File sf;
    public SettingsTool(File optionsFile) {
        sf = optionsFile;
        if(!sf.exists()){
            save(new Settings("English"));
        }
    }

    public String[] loadAndReturn(){
        String language = FileTools.freadLine(sf, 1);
        String font = FileTools.freadLine(sf, 2);
        return new String[]{language,font};
    }
    public Settings save(Settings settings){
        try (FileWriter fw = new FileWriter(sf);) {
            try (BufferedWriter bw = new BufferedWriter(fw, 65535);){
                bw.write(settings.getLanguage()+"\n");
                bw.flush();
            }
        }catch (IOException ioException){}
        return settings;
    }
}
