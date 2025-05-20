package assets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class SettingsUI extends JFrame {
    l ll = new l();
    Settings set = Main.set;
    JButton DoneLan = new JButton("Done");
    JLabel lan = new JLabel("Languages:");
    JComboBox<String> jcb = new JComboBox<>();
    JPanel sp0 = new JPanel();
    JPanel linksp = new JPanel();
    JButton abtn = new JButton(ll.ls[l.getlangnum(Main.lang,false)][8]);
    JPanel p = new JPanel(new BorderLayout());
    JButton Basic = new JButton(ll.ls[l.getlangnum(Main.lang,false)][6]);
    void vf(){this.setVisible(false);}
    public SettingsUI(){
        initGui();
        initAction();
        this.setTitle("Java Quantum Editor: Settings");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                vf();
            }
        });
        this.setBounds(800,500,900,750);
        this.setVisible(true);
    }
    void initGui(){
        JLabel Title = new JLabel(ll.ls[l.getlangnum(Main.lang,false)][8]);
        Title.setFont(new Font(Font.SERIF,Font.BOLD,17));
        if(Objects.equals(Main.lang, "zh")){
            jcb.addItem("繁體中文");
            jcb.addItem("English");
        }else {
            jcb.addItem("English");
            jcb.addItem("繁體中文");
        }
        lan.setFont(new Font(Font.SERIF,Font.ITALIC,20));
        p.add(Basic,BorderLayout.WEST);
        p.add(abtn,BorderLayout.CENTER);
        sp0.add(lan);
        sp0.add(jcb);
        sp0.add(DoneLan);
        sp0.setVisible(false);
        linksp.add(Title);
        linksp.add(new JLinkLabel(ll.ls[l.getlangnum(Main.lang,false)][9],"https://reurl.cc/W0MKR5",new Font(Font.DIALOG_INPUT, Font.ITALIC,30)));
        linksp.setVisible(false);
        this.add(p, BorderLayout.NORTH);
    }
    void initAction(){
        DoneLan.addActionListener((ActionEvent e)-> {
            if(jcb.getSelectedItem() == "繁體中文"){
                Main.lang = "zh";
            }else {
                Main.lang = "en";
            }
            set.Save();
            Main.s.Ref();
            JOptionPane.showMessageDialog(null,ll.ls[l.getlangnum(Main.lang,false)][7]);
            vf();
        });
        Basic.addActionListener((event)->{
            this.add(sp0,BorderLayout.CENTER);
            sp0.setVisible(true);
            remove(linksp);
            linksp.setVisible(false);
        } );
        abtn.addActionListener((e)->{
            this.add(linksp,BorderLayout.CENTER);
            remove(sp0);
            linksp.setVisible(true);
            sp0.setVisible(false);
        });
    }
}
