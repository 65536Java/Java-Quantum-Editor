package assets;

import javax.swing.*;
import javax.tools.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;




public class TheWindow extends JFrame {
    static {
        Main.set.Load();
    }
    JMenuBar jm = new JMenuBar();
    JPanel japan = new JPanel();
    JMenu[] jms = new JMenu[]{
            new JMenu("File")
    };
    JPanel con = new JPanel();
    JLabel co = new JLabel("Console");
    JMenuItem[] jmis = new JMenuItem[]{
            new JMenuItem("save")
    };
    l ls = new l();
    JTextArea Console = new JTextArea("This is console.",30,50);
    JavaCompiler jcr = ToolProvider.getSystemJavaCompiler();
    public final String author = "iert_MCPL";
    public final String ver = "Beta 1.12.17";
    JScrollPane CP = new JScrollPane(Console);
    JButton settings;
    JButton load;
    JButton run;
    JButton compile;
    JButton save;
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
            }""",30,50);
    JScrollPane codeBar = new JScrollPane(codes);
    File f = null;
    private File saveAs(String mode){
        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
        }
        catch (Exception exception) {

        }
        if(mode == "get" && this.f != null){
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
        JFileChooser jf = new JFileChooser();
        jf.showDialog( null,"Save");
        this.f = jf.getSelectedFile();
        if(this.f.getPath() == "" || this.f == null){this.f = null;return this.f;}
        File f = new File(this.f.getPath());
        if(!f.exists()){
            try{
                f.createNewFile();
            }catch (Throwable t){}
        }
        char[] a = new char[]{};
        StringBuffer sb = new StringBuffer();
        try {
            fw = new FileWriter(f);
        }
        catch (Exception exception) {

        }
        try {
            BufferedWriter bf = new BufferedWriter(fw);
            bf.write(codes.getText());
            bf.flush();
            bf.close();
        } catch (IOException e) {}
        return f;
    }
    public static String fread(File ff){
        FileReader fr = null;
        try {
            fr = new FileReader(ff);
        }
        catch (Exception exception) {

        }
        char[] a = new char[]{};
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader bf = new BufferedReader(fr);
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line+"\n");
            }
        } catch (IOException e) {

        }
        return sb.toString();
    }
    private void compile(){
        Console.setText("");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("javac", this.saveAs("get").getPath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            Thread n = new Thread(()->{
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                try{
                    compile.setEnabled(false);
                    while ((line = reader.readLine()) != null) {
                        Console.append(line+"\n");
                    }
                    int exitCode = process.waitFor();
                    Console.append("\n\n\n[Java Compiler] Exited with code: " + exitCode);
                    compile.setEnabled(true);
                }catch (IOException | InterruptedException ioException){
                    throw new RuntimeException(ioException);
                }
            });
            n.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        File m = saveAs("get");
        int res = jcr.run( null, null, null,this.f.getPath());
        if (res == 0) {
            JOptionPane.showMessageDialog( null,"Compilation successful!","Java Compiler",JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog( null,"Compilation failed!","Java Compiler",JOptionPane.ERROR_MESSAGE);
        }
    }
    public TheWindow(){
        this.setBackground(Color.darkGray);
        codeBar.setAutoscrolls(true);
        save = new JButton(ls.ls[l.getlangnum(Main.lang,false)][3]);
        run = new JButton(ls.ls[l.getlangnum(Main.lang,false)][2]);
        load = new JButton(ls.ls[l.getlangnum(Main.lang,false)][4]);
        compile = new JButton(ls.ls[l.getlangnum(Main.lang,false)][1]);
        settings = new JButton(ls.ls[l.getlangnum(Main.lang,false)][5]);
        Console.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
        compile.setEnabled(Main.isJDK());
        con.setLayout(new BorderLayout());
        co.setFont(new Font(Font.SANS_SERIF,Font.ITALIC,15));
        con.add(co,BorderLayout.NORTH);
        run.setEnabled(Main.isJDK());
        codes.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        codeBar.setViewportView(codes);
        
        this.setTitle("Java Quantum Editor v"+ver+" by YouTube:"+author);
        codes.setFont(new Font(Font.SANS_SERIF,Font.BOLD,13));
        this.setSize(1000,800);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        load.addActionListener((ActionEvent e)->{
            JFileChooser jf = new JFileChooser();
            FileReader fr = null;
            do{
                jf.showDialog( null,"Open .java file");
                f = jf.getSelectedFile();
            }while (!f.getName().endsWith(".java"));

            try {
                codes.setText(fread(f));
            } catch (Exception ex) {}
            try {
                fr.close();
            } catch (Exception ex) {}
        });
        this.CP.setViewportView(Console);
        this.setLayout(new BorderLayout());

        codeBar.setLocation(this.getWidth() / 2,this.getHeight() / 2);

        compile.addActionListener((ActionEvent e)->{
            compile();
        } );
        codeBar.setSize(500,Integer.MAX_VALUE-10);
        compile.setLocation(this.getWidth() / 2,this.getHeight());
        codes.setEditable(true);
        save.addActionListener((ActionEvent e)->{
            saveAs("save");
        });
        run.addActionListener((ActionEvent e)->{
            Console.setText("");
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("java", this.saveAs("get").getPath());
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                Thread n = new Thread(()->{
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    try{
                        run.setEnabled(false);
                        while ((line = reader.readLine()) != null) {
                            Console.append(line+"\n");
                        }
                        int exitCode = process.waitFor();
                        Console.append("\n\n\n[Java Virtual Machine] Exited with code: " + exitCode);
                        run.setEnabled(true);
                    }catch (IOException | InterruptedException ioException){
                        throw new RuntimeException(ioException);
                    }
                });
                n.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        settings.addActionListener((actionEvent)->{
            new SettingsUI();
        });
        /*for(JMenu j : jms){
            jm.add(j);
        }*/
        con.add(CP,BorderLayout.CENTER);
        this.add(codeBar,BorderLayout.CENTER);
        this.add(con,BorderLayout.EAST);
        japan.add(load);
        japan.add(save);
        Console.setEditable(false);
        japan.add(run);
        japan.add(compile);
        japan.add(settings);
        japan.setBackground(Color.GRAY);
        this.add(japan,BorderLayout.NORTH);
        this.setVisible(true);
    }
    public void Ref(){
        save.setText(ls.ls[l.getlangnum(Main.lang,false)][3]);
        run.setText(ls.ls[l.getlangnum(Main.lang,false)][2]);
        load.setText(ls.ls[l.getlangnum(Main.lang,false)][4]);
        compile.setText(ls.ls[l.getlangnum(Main.lang,false)][1]);
        settings.setText(ls.ls[l.getlangnum(Main.lang,false)][5]);
    }
}
