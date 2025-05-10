package assets;

import javax.swing.*;
import java.io.File;


public class Main {
    public static TheWindow s;
    public static Settings set = new Settings(new File("Settings.txt"));

    static String lang = set.Load();
    public static boolean isJDK(){
        try{
            Class.forName("com.sun.tools.javac.Main");
            return true;
        }catch (ClassNotFoundException c){
            return false;
        }
    }
    public static void main(String[] args){
        l langstr = new l();
        if(isJDK()){
            s = new TheWindow();
        }else{
            try {
                JOptionPane.showMessageDialog(null,langstr.ls[l.getlangnum(lang,true)][0]);
            } finally {
                System.exit(1);
            }
        }
    }
}
