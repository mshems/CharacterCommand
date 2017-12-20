package app.ui.terminal.core;

import javax.swing.*;
import java.awt.*;

public class IOContainerPanel extends JPanel{
    private JComponent component;

    IOContainerPanel(JComponent c){
        this.component = c;
        //this.add(c);
    }

    @Override
    public Dimension getMaximumSize() {
        return component.getMaximumSize();
        //return new Dimension(ioComponent.getMaximumSize().width, ioComponent.getMaximumSize().height);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(component.getPreferredSize().width, component.getPreferredSize().height);
        //return component.getPreferredSize();
    }
}
