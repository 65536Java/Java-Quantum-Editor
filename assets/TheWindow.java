package assets;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.tools.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;




public class TheWindow extends JFrame {
    private static final int INDENT_SPACES = 4; // 定義一個縮排單位為 4 個空格
    JPanel mainp = new JPanel();
    JPanel codep = new JPanel();
    JPanel japan = new JPanel();
    JPanel con = new JPanel();
    JLabel co = new JLabel("CONSOLE");
    JLabel codelab = new JLabel("CODE");
    l ls = new l();
    JTextArea Console = new JTextArea("", 30, 50);
    JavaCompiler jcr = ToolProvider.getSystemJavaCompiler();
    //public final String author = "iert_MCPL";
    public final String ver = "Release v1.0";
    JScrollPane CP = new JScrollPane(Console);
    JButton settings;
    JButton load;
    JButton run;
    JButton compile;
    JButton save;
    JButton stop;
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

    private File saveAs(String mode) {
        if (mode == "save" || this.f == null) {
            JFileChooser jf = new JFileChooser();
            int result = jf.showDialog(this, "Save");
            this.f = jf.getSelectedFile();
            if(result == JFileChooser.CANCEL_OPTION)return null;
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (Throwable _) {

                }
            }
            FileWriter fw = null;
            try {
                fw = new FileWriter(f);
            } catch (Exception _) {

            }
            char[] a = new char[]{};
            try {
                fw = new FileWriter(this.f);
            } catch (Exception _) {

            }
            try {
                BufferedWriter bf = new BufferedWriter(fw);
                bf.write(codes.getText());
                bf.flush();
                bf.close();
            } catch (IOException _a) {
                _a.printStackTrace();
            }
        }else if(this.f != null){
            FileWriter fw = null;
            try {
                fw = new FileWriter(f);
            } catch (Exception _) {

            }
            BufferedWriter bf = new BufferedWriter(fw);
            try {
                bf.write(codes.getText());
                bf.flush();
                bf.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this.f;
        }
        return this.f;
    }

    public static String fread(File ff) {
        try {
            FileReader fr;
            fr = new FileReader(ff);
            StringBuffer sb = new StringBuffer();
            BufferedReader bf = new BufferedReader(fr);
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception exception) {
            return null;
        }
    }

    private void compile() {
        Console.setText("");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("javac", this.saveAs("get").getPath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            Thread n = new Thread(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                try {
                    compile.setEnabled(false);
                    while ((line = reader.readLine()) != null) {
                        Console.append(line + "\n");

                    }
                    int exitCode = process.waitFor();
                    Console.append("\n\n\n[Java Compiler] Exited with code: " + exitCode);
                    compile.setEnabled(true);
                } catch (IOException | InterruptedException ioException) {
                    throw new RuntimeException(ioException);
                }
            });
            n.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int res = jcr.run(null, null, null, this.f.getPath());
        if (res == 0) {
            JOptionPane.showMessageDialog(null, "Compilation successful!", "Java Compiler", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Compilation failed!\nPlease see console!", "Java Compiler", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void autoIndentInition(){
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
        autoIndentInition();
        this.setBackground(Color.darkGray);
        codeBar.setAutoscrolls(true);
        stop = new JButton(ls.ls[l.getlangnum(Main.lang, false)][11]);
        save = new JButton(ls.ls[l.getlangnum(Main.lang, false)][3]);
        run = new JButton(ls.ls[l.getlangnum(Main.lang, false)][2]);
        load = new JButton(ls.ls[l.getlangnum(Main.lang, false)][4]);
        compile = new JButton(ls.ls[l.getlangnum(Main.lang, false)][1]);
        settings = new JButton(ls.ls[l.getlangnum(Main.lang, false)][5]);
        Console.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        compile.setEnabled(Main.isJDK());
        con.setLayout(new BorderLayout());
        co.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));
        con.add(co, BorderLayout.NORTH);
        run.setEnabled(Main.isJDK());
        codes.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        codeBar.setViewportView(codes);
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
            FileReader fr = null;
            do {
                jf.showDialog(null, "Open .java file");
                f = jf.getSelectedFile();
            } while (!f.getName().endsWith(".java"));

            try {
                codes.setText(fread(f));
            } catch (Exception ex) {
            }
            try {
                fr.close();
            } catch (Exception _) {
            }
            run.setEnabled(codes.getText().contains("public static void main(String[] "));
        });
        this.CP.setViewportView(Console);
        this.setLayout(new BorderLayout());
        codeBar.setLocation(this.getWidth() / 2, this.getHeight() / 2);
        compile.addActionListener((ActionEvent e) -> {
            compile();
        });
        codeBar.setSize(500, Integer.MAX_VALUE - 10);
        compile.setLocation(this.getWidth() / 2, this.getHeight());
        codes.setEditable(true);
        save.addActionListener((ActionEvent e) -> {
            saveAs("save");
            run.setEnabled(codes.getText().contains("public static void main(String[] "));
        });
        run.addActionListener((ActionEvent e) -> {
            Console.setText("");
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("java", this.saveAs("get").getPath());
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                Thread n = new Thread(() -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    try {
                        stop.setEnabled(true);
                        run.setEnabled(false);
                        while ((line = reader.readLine()) != null) {
                            Console.append(line + "\n");
                            if (stopping) {
                                process.destroy();
                                Console.append("[System message] Process was destoryed");
                                stopping = false;
                                break;
                            }
                        }
                        stop.setEnabled(false);
                        int exitCode = process.waitFor();
                        Console.append("\n\n\n[Java Virtual Machine] Exited with code: " + exitCode);
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
        stop.setEnabled(false);
        Console.setEditable(false);
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
        codeBar.setAutoscrolls(true);
        CP.setAutoscrolls(true);
        mainp.add(codep, BorderLayout.NORTH);
        mainp.add(con, BorderLayout.CENTER);
        this.add(mainp, BorderLayout.CENTER);
        this.add(japan, BorderLayout.NORTH);
        this.setVisible(true);
    }

    //Refresh button texts
    public void Ref() {
        stop.setText(ls.ls[l.getlangnum(Main.lang, false)][11]);
        compile.setText(ls.ls[l.getlangnum(Main.lang, false)][1]);
        run.setText(ls.ls[l.getlangnum(Main.lang, false)][2]);
        save.setText(ls.ls[l.getlangnum(Main.lang, false)][3]);
        load.setText(ls.ls[l.getlangnum(Main.lang, false)][4]);
        settings.setText(ls.ls[l.getlangnum(Main.lang, false)][5]);
    }
}