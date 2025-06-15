package assets.libs.gui;

import javax.swing.*;

public class AutoScrollTextArea extends JTextArea {
    public AutoScrollTextArea(String s, int i, int i1) {
        super(s,i,i1);
    }

    @Override
    public void append(String str) {
        super.append(str);
        end();
    }

    public void end(){
        this.setCaretPosition(this.getDocument().getLength());
    }
}
