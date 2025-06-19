package assets;

import assets.libs.Settings;
import assets.libs.tools.SettingsTool;
import assets.libs.gui.JLinkLabel;
import assets.libs.tools.LanguageTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class SettingsUI extends JFrame {
    final LanguageTools LT = new LanguageTools();
    LayoutManager layout = new GridBagLayout();
    SettingsTool set = Main.set;
    JButton done = new JButton("Done");
    JLabel lan = new JLabel(LT.translateWithArray("Languages:", Main.language));
    JComboBox<String> selectLanguage = new JComboBox<>();
    //JPanel settings1 = new JPanel(new BorderLayout());
    JPanel settings1Panel0 = new JPanel();
    JPanel linksPanel1 = new JPanel();
    JButton abtn = new JButton(LT.translateWithArray("Links", Main.language));
    JPanel settingsPanel = new JPanel(new BorderLayout());
    JButton basic = new JButton(LT.translateWithArray("Basic", Main.language));
    JButton update = new JButton(LT.translateWithArray("Check for updates", Main.language));
    void vf() {
        this.setVisible(false);
    }
    public SettingsUI(){
        initGui();
        initActions();
        this.setTitle("Java Quantum Editor: Settings");
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                vf();
            }
        });
        this.setSize(900,750);
        this.setVisible(true);
    }
    void initGui(){
        JLabel Title = new JLabel(LT.translateWithArray("Links", Main.language));
        Title.setFont(new Font(Font.SERIF,Font.BOLD,17));
        if(Main.language.contains("繁體中文")){
            selectLanguage.addItem("繁體中文");
            selectLanguage.addItem("English");
        }else {
            selectLanguage.addItem("English");
            selectLanguage.addItem("繁體中文");
        }
        lan.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,20));
        settingsPanel.setLayout(layout);
        settingsPanel.setBackground(Color.GRAY);
        settingsPanel.add(basic);
        settingsPanel.add(abtn);
        settings1Panel0.setSize(10,Main.s.getHeight());
        settings1Panel0.setLayout(new GridBagLayout());
        settings1Panel0.setBorder(null);
        settings1Panel0.add(lan);
        settings1Panel0.add(selectLanguage);
        settings1Panel0.setVisible(false);
        //settings1.add(settings1Panel0, BorderLayout.NORTH);
        linksPanel1.setLayout(layout);
        linksPanel1.add(Title);
        linksPanel1.add(new JLinkLabel(LT.translateWithArray("Author's Youtube channel", Main.language),"https://reurl.cc/W0MKR5",new Font(Font.DIALOG_INPUT, Font.ITALIC,30)));
        linksPanel1.setVisible(false);
        this.add(settingsPanel, BorderLayout.NORTH);
        this.add(done,BorderLayout.SOUTH);
    }
    private void initActions(){
        done.addActionListener((ActionEvent event)-> {
            Main.language = (String)selectLanguage.getSelectedItem();
            set.save(new Settings(Main.language));
            Main.s.refreshUI();
            JOptionPane.showMessageDialog(null, LT.translateWithArray("Changes was applied.", Main.language));
            vf();
        });
        basic.addActionListener((event)->{
            this.add(settings1Panel0,BorderLayout.CENTER);
            remove(linksPanel1);
            settings1Panel0.setVisible(true);
            linksPanel1.setVisible(false);
        } );
        abtn.addActionListener((event)->{
            this.add(linksPanel1,BorderLayout.CENTER);
            remove(settings1Panel0);
            linksPanel1.setVisible(true);
            settings1Panel0.setVisible(false);
        });
        update.addActionListener((event)->{
            JOptionPane.showMessageDialog(null,"Coming soon!");
        });
    }
}
