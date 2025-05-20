
package assets;

import java.io.File;
import javax.swing.JOptionPane;

public class Main {
    public static TheWindow s;
    public static Settings set;
    static String lang;
    public static boolean isJDK() {
        try {
            Class.forName("com.sun.tools.javac.Main");
            return true;
        }
        catch (ClassNotFoundException c) {
            return false;
        }
    }

    public static void main(String[] args) {
        l langstr = new l();
        if (Main.isJDK()) {
            s = new TheWindow(true);
        } else {
            s = new TheWindow(false);
            try {
                JOptionPane.showMessageDialog(null, langstr.ls[l.getlangnum(lang, true)][0]);
            }
            finally {
                System.exit(1);
            }
        }
    }

    static {
        set = new Settings(new File("Settings.txt"));
        lang = set.Load();
    }
}
