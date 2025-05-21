package assets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JLinkLabel extends JLabel {
    private String url;
    l ll = new l();
    public JLinkLabel(String text, String url,Font f) {
        this.setFont(f);
        this.setLink(text,url);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int ret = JOptionPane.showConfirmDialog(null, String.format(ll.ls[l.getlangnum(Main.lang, false)][10], url),"Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(ret == 1)return;
                try {
                    Desktop.getDesktop().browse(new URI(getURL()));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    public void setLink(String text, String url) {
        setText(String.format("<html><a href='%s'>%s</a></html>", url, text));
        this.url = url;
    }
    public String getURL() {
        return url;
    }
}