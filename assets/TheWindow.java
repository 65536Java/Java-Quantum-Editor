package assets;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import assets.enums.EnumSaveMode;

import static assets.libs.tools.FileTools.fread;


public class TheWindow extends JFrame {
    private static final int INDENT_SPACES = 4;
    /*JMenuBar jmb = new JMenuBar();
    JMenu[] jMenu = new JMenu[]{
    };
    JMenuItem[][] items = new JMenuItem[][]{

    };*/
    JPanel mainp = new JPanel();
    JPanel codep = new JPanel();
    JPanel japan = new JPanel();
    JPanel con = new JPanel();
    JLabel co = new JLabel("CONSOLE");
    JLabel codelab = new JLabel("CODE");
    JTextArea console = new JTextArea("", 50, 50);

    public final String ver = "Release v1.1";
    JScrollPane CP = new JScrollPane(console);
    JButton settings;
    JButton load;
    JButton run;
    JButton compile;
    JButton save;
    JButton stop;
    JButton chat;
    boolean stopping = false;

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

    private File save(EnumSaveMode mode, String s) {
        File filetmp;
        if (mode == EnumSaveMode.SAVE || this.f == null) {
            JFileChooser jf = new JFileChooser("C:\\");
            int result = jf.showDialog(this, "Save");
            filetmp = jf.getSelectedFile();
            if(result == JFileChooser.CANCEL_OPTION)return null;
            try (FileWriter fw = new FileWriter(filetmp);
                 BufferedWriter bf = new BufferedWriter(fw)){
                bf.write(s);
                bf.flush();
                this.f = filetmp;
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
                    compile.setEnabled(false);
                    while ((line = reader.readLine()) != null) {
                        console.append(line + "\n");

                    }
                    int exitCode = process.waitFor();
                    console.append("\n\n\n[Java Compiler] Exited with code: " + exitCode);
                    compile.setEnabled(true);
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
                    run.setEnabled(codes.getText().contains("public static void main(String[] "));
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
                run.setEnabled(codes.getText().contains("public static void main(String[] "));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                run.setEnabled(codes.getText().contains("public static void main(String[] "));
            }
        });
    }
    public TheWindow(boolean shown) {
        if (!shown) {
            return;
        }
        initAutoIndent();
        this.setBackground(Color.darkGray);
        codeBar.setAutoscrolls(true);
        stop = new JButton();
        save = new JButton();
        run = new JButton();
        load = new JButton();
        compile = new JButton();
        settings = new JButton();
        chat = new JButton();
        try {
            console.setFont(Font.createFont(Font.TRUETYPE_FONT,TheWindow.class.getResourceAsStream("resources/font/JetBrainsMono-Bold.ttf")).deriveFont(15.5f));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        compile.setEnabled(Main.isJDK());
        con.setLayout(new BorderLayout());
        co.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
        con.add(co, BorderLayout.NORTH);
        run.setEnabled(Main.isJDK());
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
        load.addActionListener((ActionEvent e) -> {
            JFileChooser jf = new JFileChooser();
            do {
                jf.showDialog(null, "Open .java file");
                f = jf.getSelectedFile();
            } while (!f.getName().endsWith(".java"));

            try {
                codes.setText(fread(f));
            } catch (Exception ex) {
            }
            run.setEnabled(codes.getText().contains("public static void main(String[] "));
        });
        this.setLayout(new BorderLayout());
        codeBar.setLocation(this.getWidth() / 2, this.getHeight() / 2);
        compile.addActionListener((ActionEvent e) -> {
            compile();
        });
        codeBar.setSize(500, Integer.MAX_VALUE - 10);
        compile.setLocation(this.getWidth() / 2, this.getHeight());
        codes.setEditable(true);
        save.addActionListener((ActionEvent e) -> {
            save(EnumSaveMode.SAVE,codes.getText());
            run.setEnabled(codes.getText().contains("public static void main(String[] "));
        });
        run.addActionListener((ActionEvent e) -> {
            console.setText("");
            File saved = this.save(EnumSaveMode.GETFILE,codes.getText());
            try (FileWriter start = new FileWriter(new File(String.format("%s/Start.bat", saved.getParent())))){
                start.write(String.format("java \"%s\"\npause",saved.getName()));
                console.append(String.format("[System message] generated \"Start.bat\" in \"%s\"\n",saved.getParent()));
                stopping = false;
                //if(codes.getText().contains("System.in") || codes.getText().contains("read") || codes.getText().contains("Frame("))
                ProcessBuilder processBuilder = new ProcessBuilder("java", saved.getPath());
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                Thread n = new Thread(() -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    try {
                        stop.setEnabled(true);
                        run.setEnabled(false);
                        while ((line = reader.readLine()) != null) {
                            console.append(line + "\n");
                            if (stopping) {
                                process.destroyForcibly();
                                console.append("[System message] Process was destoried.");
                                break;
                            }
                        }
                        stop.setEnabled(false);
                        int exitCode = process.waitFor();
                        console.append("\n\n\n[Java Virtual Machine] Exited with code: " + exitCode);
                        run.setEnabled(true);
                    } catch (IOException | InterruptedException ioException) {
                        throw new RuntimeException(ioException);
                    }
                });
                n.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        settings.addActionListener((actionEvent) -> {
            new SettingsUI();
        });
        stop.addActionListener((e) -> {
            stopping = true;
        });
        chat.addActionListener((e) -> new AIChat());
        stop.setEnabled(false);
        console.setEditable(false);
        codelab.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
        codes.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        codep.setLayout(new BorderLayout());
        codep.add(codeBar, BorderLayout.CENTER);
        codep.add(codelab, BorderLayout.NORTH);
        con.add(CP, BorderLayout.CENTER);
        japan.add(load);
        japan.add(save);
        japan.add(run);
        japan.add(stop);
        japan.add(compile);
        japan.add(settings);
        japan.setBackground(Color.GRAY);
        mainp.setLayout(new BorderLayout());
        CP.setAutoscrolls(true);
        mainp.add(codep, BorderLayout.NORTH);
        mainp.add(con, BorderLayout.CENTER);
        this.add(mainp, BorderLayout.CENTER);
        this.add(japan, BorderLayout.NORTH);
        this.add(chat,BorderLayout.SOUTH);
        this.setVisible(true);
    }

    public void refreshUI() {
        stop.setText(Main.LT.translateWithArray("Stop", Main.language));
        compile.setText(Main.LT.translateWithArray("Compile", Main.language));
        run.setText(Main.LT.translateWithArray("Run", Main.language));
        save.setText(Main.LT.translateWithArray("Save", Main.language));
        load.setText(Main.LT.translateWithArray("Load", Main.language));
        settings.setText(Main.LT.translateWithArray("Settings...", Main.language));
        chat.setText(Main.LT.translateWithArray("Chat with Gemini 2.0 flash",Main.language));
    }
}