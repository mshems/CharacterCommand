package jterminal.core;

import javax.swing.*;
import java.awt.*;

public class IOContainerPanel extends JPanel{
    private JTerminalIOComponent component;
    private JComponent parentComponent;

    IOContainerPanel(JTerminalIOComponent c, JComponent parentComponent){
        this.component = c;
        this.parentComponent = parentComponent;
    }


    @Override
    public Dimension getPreferredSize(){
        return component.getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return component.getMaximumSize();
    }
}
