package assets;

import assets.libs.apis.ChatResponse;
import assets.libs.apis.GeminiChatAPI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AIChat extends JFrame {
    GeminiChatAPI gemini = new GeminiChatAPI("AIzaSyBeL-gL6YDJFVxfgfFAT3sSCEfT7M_wHXA");
    ChatResponse cr;
    JPanel ai = new JPanel(new BorderLayout());
    JPanel ai2 = new JPanel(new BorderLayout());
    JTextArea ask = new JTextArea(5,3);
    JTextArea resultAI = new JTextArea();
    JScrollPane jsp = new JScrollPane(resultAI);
    JScrollPane jsp2 =new JScrollPane(ask);
    JButton clearHis;
    JButton send;
    public AIChat() {
        clearHis = new JButton();
        send = new JButton();
        refreshUI();
        resultAI.setEditable(false);
        resultAI.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
        ask.setFont(new Font(Font.MONOSPACED,Font.BOLD,15));
        ask.setEditable(true);
        ai2.add(jsp2,BorderLayout.CENTER);
        ai2.add(send,BorderLayout.EAST);
        ai.add(jsp,BorderLayout.CENTER);
        ai.add(ai2,BorderLayout.SOUTH);
        ai.setVisible(true);
        send.addActionListener((e)->{
            Thread th = new Thread(()->{
                resultAI.setText("Please wait...");
                cr = gemini.sendMessage(ask.getText(),true);
                resultAI.setText(cr.getContent());
            });
            th.start();
        });
        clearHis.addActionListener((w)->{

            gemini.clearHistory();
        });
        this.add(ai,BorderLayout.CENTER);
        this.add(clearHis,BorderLayout.SOUTH);
        this.setSize(500,370);
        this.setTitle("Gemini");
        this.setVisible(true);
    }
    public void refreshUI(){
        clearHis.setText(Main.LT.translateWithArray("Clear history",Main.language));
        send.setText(Main.LT.translateWithArray("Send message",Main.language));
    }
}
