package assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import assets.enums.EnumSaveMode;
import assets.libs.gui.AutoScrollTextArea;

import static assets.libs.tools.FileTools.fread;


public class TheWindow extends JFrame {

    private static final int INDENT_SPACES = 4;
    final JMenuBar jmb = new JMenuBar();
    final JMenu[] jMenu = new JMenu[]{
            new JMenu("File"),
            new JMenu("Edit"),
            new JMenu("Run")
    };
    final JMenuItem[][] items = new JMenuItem[][]{
            new JMenuItem[]{
                    new JMenuItem("Open"),
                    new JMenuItem("Save")
            },
            new JMenuItem[]{
                    new JMenuItem("Settings...")
            },
            new JMenuItem[]{
                    new JMenuItem("Run"),
                    new JMenuItem("Compile"),
                    new JMenuItem("Stop")
            }
    };
    JPanel mainp = new JPanel();
    JPanel codep = new JPanel();
    JPanel con = new JPanel();
    JLabel co = new JLabel("CONSOLE");
    JLabel codelab = new JLabel("CODE");
    AutoScrollTextArea console = new AutoScrollTextArea("", 50, 50);

    public final String ver = "Release v1.2";
    JScrollPane CP = new JScrollPane(console);

    JButton chat;
    boolean isProgramRunning = false;

    JTextArea codes = new JTextArea("""
            public class Main{
                public static void main(String[] args)    {
                    int n = 0;
                    while(true){
                        n++;
                        System.out.println(String.valueOf(n));
                        if(n >= 100)break;
                        try{
                            Thread.sleep(100L);
                        }catch(InterruptedException w){
                            throw new RuntimeException(w);
                        }
                    }
                }
            }""", 30, 50);
    JScrollPane codeBar = new JScrollPane(codes);
    File f = null;

    public TheWindow(boolean shown) {
        setJMenuBar(jmb);
        items[0][0].addActionListener((e)->{
            JFileChooser jf = new JFileChooser();
            do {
                jf.showDialog(null, "Open .java file");
                f = jf.getSelectedFile();
            } while (!f.getName().endsWith(".java"));

            try {
                codes.setText(fread(f));
            } catch (Exception ex) {
            }
            items[2][0].setEnabled(codes.getText().contains("public static void main(String[] "));
        });
        items[0][1].addActionListener((e)->{
            save(EnumSaveMode.SAVE,codes.getText());
            items[2][0].setEnabled(codes.getText().contains("public static void main(String[] "));
        });
        items[1][0].addActionListener((e)->new SettingsUI());
        items[2][0].addActionListener((ActionEvent e) -> {
            console.setText("");
            File saved = this.save(EnumSaveMode.GETFILE,codes.getText());
            try (FileWriter start = new FileWriter(String.format("%s/Start.bat", saved.getParent()))){
                start.write(String.format("@ECHO OFF\njava \"%s\"\npause",saved.getName()));
                console.append(String.format("[System message] generated \"Start.bat\" in \"%s\"\n",saved.getParent()));
                isProgramRunning = true;
                ProcessBuilder processBuilder = new ProcessBuilder("java", saved.getPath());
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                Thread n = new Thread(() -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    try {
                        items[2][2].setEnabled(true);
                        items[2][0].setEnabled(false);
                        while ((line = reader.readLine()) != null) {
                            console.append(line + "\n");
                            if (!isProgramRunning) {
                                process.destroyForcibly();
                                console.append("[System message] Process was destroyed.");
                                break;
                            }
                        }
                        items[2][2].setEnabled(false);
                        int exitCode = process.waitFor();
                        console.append("\n\n\n[Java Virtual Machine] Exited with code: " + exitCode);
                        items[2][0].setEnabled(true);
                    } catch (IOException | InterruptedException ioException) {
                        throw new RuntimeException(ioException);
                    }
                });
                n.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        items[2][1].addActionListener((e) -> compile());
        items[2][2].addActionListener((e) -> isProgramRunning = false);
        for (JMenuItem j1 : items[0]) {
            jMenu[0].add(j1);
        }
        for (JMenuItem j1 : items[1]) {
            jMenu[1].add(j1);
        }
        for (JMenuItem j1 : items[2]) {
            jMenu[2].add(j1);
        }
        for (int i = 0; i < jMenu.length; i++) {
            jmb.add(jMenu[i]);
        }
        if (!shown) {
            return;
        }
        initAutoIndent();
        this.setBackground(Color.darkGray);
        codeBar.setAutoscrolls(true);
        chat = new JButton();
        console.setFont(new Font(Font.DIALOG,Font.ITALIC,15));
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        con.setLayout(new BorderLayout());
        co.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
        con.add(co, BorderLayout.NORTH);
        codes.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        codeBar.setViewportView(codes);
        refreshUI();
        this.setTitle("Java Quantum Editor " + ver);
        codes.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        this.setSize(1000, 800);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        this.setLayout(new BorderLayout());
        try {
            this.setIconImage(ImageIO.read(getClass().getResourceAsStream("channels4_profile.png")));
        } catch (IOException e) {
            e.fillInStackTrace();
        }
        codeBar.setLocation(this.getWidth() / 2, this.getHeight() / 2);
        codeBar.setSize(500, Integer.MAX_VALUE - 10);
        codes.setEditable(true);
        chat.addActionListener((e) -> new AIChat());
        console.setEditable(false);
        codelab.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
        codes.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        codep.setLayout(new BorderLayout());
        codep.add(codeBar, BorderLayout.CENTER);
        codep.add(codelab, BorderLayout.NORTH);
        con.add(CP, BorderLayout.CENTER);
        mainp.setLayout(new BorderLayout());
        CP.setAutoscrolls(true);
        mainp.add(codep, BorderLayout.NORTH);
        mainp.add(con, BorderLayout.CENTER);
        this.add(mainp, BorderLayout.CENTER);
        this.add(chat,BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private File save(EnumSaveMode mode, String s) {
        File fileTmp;
        if (mode == EnumSaveMode.SAVE || this.f == null) {
            JFileChooser jf = new JFileChooser("C:\\");
            int result = jf.showDialog(this, "Save");
            fileTmp = jf.getSelectedFile();
            if(result == JFileChooser.CANCEL_OPTION)return null;
            try (FileWriter fw = new FileWriter(fileTmp);
                 BufferedWriter bf = new BufferedWriter(fw)){
                bf.write(s);
                bf.flush();
                this.f = fileTmp;
            } catch (IOException _a) {
                _a.printStackTrace();
            }
        }else if(this.f != null){
            try (FileWriter fw = new FileWriter(f);
                 BufferedWriter bf = new BufferedWriter(fw)){
                bf.write(s);
                bf.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return this.f;
    }


    private void compile() {
        console.setText("");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("javac", this.save(EnumSaveMode.GETFILE, codes.getText()).getPath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            Thread n = new Thread(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                try {
                    items[2][1].setEnabled(false);
                    while ((line = reader.readLine()) != null) {
                        console.append(line + "\n");

                    }
                    int exitCode = process.waitFor();
                    console.append("\n\n\n[Java Compiler] Exited with code: " + exitCode);
                    items[2][1].setEnabled(true);
                    if (exitCode == 0) {
                        JOptionPane.showMessageDialog(null, "Compilation successful!", "Java Compiler", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Compilation failed!\nPlease see console!", "Java Compiler", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException | InterruptedException ioException) {
                    throw new RuntimeException(ioException);
                }
            });
            n.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    private void initAutoIndent(){
        System.out.println("Initializing auto indent...");
        codes.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    items[2][0].setEnabled(codes.getText().contains("public static void main(String[] "));
                    if (e.getLength() == 1 && "\n".equals(e.getDocument().getText(e.getOffset(), 1))) {
                        SwingUtilities.invokeLater(() -> {
                            try {
                                int caretPosition = codes.getCaretPosition(); // 當前游標位置
                                int lineNumber = codes.getLineOfOffset(caretPosition - 1);
                                int startOfPrevLine = codes.getLineStartOffset(lineNumber);
                                int endOfPrevLine = codes.getLineEndOffset(lineNumber);
                                int currentIndent = 0;

                                String prevLine = codes.getText(startOfPrevLine, endOfPrevLine - startOfPrevLine);
                                String trimmedPrevLine = prevLine.trim();


                                for (int i = 0; i < prevLine.length(); i++) {
                                    char c = prevLine.charAt(i);
                                    if (c == ' ') {
                                        currentIndent++;
                                    } else if (c == '\t') {
                                        currentIndent += INDENT_SPACES;
                                    } else {
                                        break;
                                    }
                                }
                                int newIndent = currentIndent;
                                if (trimmedPrevLine.endsWith("{") || trimmedPrevLine.endsWith("(")) {
                                    newIndent += INDENT_SPACES;
                                } else if (trimmedPrevLine.endsWith("}") || trimmedPrevLine.endsWith(");") || trimmedPrevLine.endsWith(";") || trimmedPrevLine.equals("break;") || trimmedPrevLine.equals("return;") || trimmedPrevLine.equals("continue;")) {
                                    if (trimmedPrevLine.equals("}")) {
                                        newIndent -= INDENT_SPACES;
                                        if (newIndent < 0) newIndent = 0;
                                    }
                                }

                                StringBuilder indentString = new StringBuilder();
                                for (int i = 0; i < newIndent; i++) {
                                    indentString.append(' ');
                                }
                                codes.insert(indentString.toString(), caretPosition);
                            } catch (BadLocationException ex) {

                            }
                        });
                    }
                } catch (BadLocationException ex) {

                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                items[2][0].setEnabled(codes.getText().contains("public static void main(String[] "));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                items[2][0].setEnabled(codes.getText().contains("public static void main(String[] "));
            }
        });
    }

    public void refreshUI() {
        jMenu[0].setText(Main.LT.translateWithArray("File",Main.language));
        jMenu[1].setText(Main.LT.translateWithArray("Edit",Main.language));
        jMenu[2].setText(Main.LT.translateWithArray("Run",Main.language));
        items[0][1].setText(Main.LT.translateWithArray("Save", Main.language));
        items[0][0].setText(Main.LT.translateWithArray("Load", Main.language));
        items[1][0].setText(Main.LT.translateWithArray("Settings...", Main.language));
        items[2][0].setText(Main.LT.translateWithArray("Run", Main.language));
        items[2][1].setText(Main.LT.translateWithArray("Compile", Main.language));
        items[2][2].setText(Main.LT.translateWithArray("Stop", Main.language));
        chat.setText(Main.LT.translateWithArray("Chat with Gemini 2.0 flash",Main.language));
    }
}