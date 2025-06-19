
package assets;

import assets.libs.tools.SettingsTool;
import assets.libs.tools.LanguageTools;

import java.io.File;
import javax.swing.*;


public class Main {
    public static TheWindow s;
    public static SettingsTool set;
    public static String language;
    public static final LanguageTools LT = new LanguageTools();
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
        LanguageTools ls = new LanguageTools();
        if (Main.isJDK()) s = new LoadScreen().showFr(true);
        else {
            s = new LoadScreen().showFr(false);
            try {
                JOptionPane.showMessageDialog(null, ls.translateWithArray("Stop", Main.language));
            }
            finally {
                System.exit(1);
            }
        }
    }

    static {
        new File("Java Quantum Editor").mkdir();
        set = new SettingsTool(new File("Java Quantum Editor\\Settings.txt"));
        language = set.loadAndReturn()[0];
    }
}
