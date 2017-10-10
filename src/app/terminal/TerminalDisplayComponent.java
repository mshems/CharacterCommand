package app.terminal;

import java.awt.*;

public class TerminalDisplayComponent extends TerminalIOComponent {
    public TerminalDisplayComponent(){
        this.setEditable(false);
        this.setMargin(new Insets(5,5,5,5));
        this.setBackground(new Color(33,33,33));
        this.setForeground(new Color(245,245,245));
        this.setCaretColor(new Color(245,245,245));
        this.setFont(new Font("consolas", Font.PLAIN, 17));
        this.setFocusable(false);
    }
}
