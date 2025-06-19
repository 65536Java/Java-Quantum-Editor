package assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoadScreen extends JFrame {
    public TheWindow showFr(boolean s){
        setUndecorated(true);
        setSize(780, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 視窗置中
        Image imageIcon = null;
        try {
            imageIcon = ImageIO.read(getClass().getResourceAsStream("logo.png"));
        } catch (IOException e) {

        }
        JLabel imageLabel = new JLabel(new ImageIcon(imageIcon));
        add(imageLabel);
        setVisible(true);
        TheWindow t = new TheWindow(s);
        setVisible(false);
        return t;
    }
}
