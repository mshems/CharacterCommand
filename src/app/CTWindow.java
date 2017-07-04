package app;

import javax.swing.*;
import java.awt.*;

public class CTWindow extends JFrame{
    CTerm parent;
    String title;
    CTTextArea textArea;
    Dimension windowSize = new Dimension(800, 600);

    public CTWindow(String title, CTerm parent){
        this.parent = parent;
        this.title = title;
        this.setTitle(this.title);
        this.setSize(windowSize);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        //No Scroll window
        /*this.textArea = new terminal.CTTextArea(parent, false);
        this.add(textArea, BorderLayout.CENTER);*/

        //Scroll window
        this.textArea = new CTTextArea(parent, false);
        JScrollPane sp = new JScrollPane(textArea);
        sp.setUI(null);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.add(sp, BorderLayout.CENTER);

        this.setVisible(true);
    }



}
