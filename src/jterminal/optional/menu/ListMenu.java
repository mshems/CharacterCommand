package jterminal.optional.menu;

import jterminal.core.*;
import jterminal.core.event.QueryEvent;
import jterminal.core.theme.Theme;
import jterminal.core.theme.ThemedComponent;
import jterminal.optional.menu.*;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class for all list-based menus
 * @param <E> type of entries in the menu
 */
public abstract class ListMenu<E> extends JPanel implements ThemedComponent {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    protected JTerminal terminal;
    String menuTitle;
    boolean cancelled = false;

    public abstract E getSelectedItem();
    public abstract int getSelection();
    public abstract void selectItem(int index);
    public abstract void deselectItem();
    public abstract int getNumLabels();
    public void setMenuTitle(String title){
        this.menuTitle = title;
    }

    public ListMenu(String title, JTerminal terminal){
        this.menuTitle = title;
        this.terminal = terminal;
    }

    public abstract void fireEvent(QueryEvent e);

    void initLayout(Theme theme){
        this.applyTheme(theme);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        if(menuTitle!=null){
            JLabel label = new JLabel(menuTitle);
            label.setBackground(terminal.getTheme().backgroundColor);
            label.setForeground(terminal.getTheme().foregroundColor);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(terminal.getTheme().backgroundColor, 3));
            Font f = terminal.getTheme().font;
            label.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));
            this.add(label);
        }
    }

    /**
     * Displays a menu on a JTerminal and returns the user's selection.
     * @param menu the menu to be displayed
     * @param <E> the type of the items in the menu
     * @return the user's selection from the menu
     */
    public static synchronized <E> E queryMenu(ListMenu<E> menu){
        E obj = null;
        MenuKeyListener menuKeyListener = new MenuKeyListener(menu);
        //jterminal.getInputComponent().removeKeyListener(jterminal.getInputComponent().getKeyListeners()[0]);
        menu.terminal.getInputComponent().addKeyListener(menuKeyListener);
        menu.terminal.getInputComponent().setText(
                menu.terminal.getInputComponent().getText().trim());
        menu.terminal.getInputComponent().setEditable(false);
        menu.terminal.getScrollPaneView().add(menu);
        menu.terminal.getScrollPaneView().revalidate();
        menu.terminal.getFrame().revalidate();
        menu.terminal.getScrollPane().repaint();
        menu.terminal.getScrollPane().getViewport().setViewPosition(new Point(0, menu.terminal.getScrollPane().getHeight()));
        menu.requestFocusInWindow();
        menu.addKeyListener(menuKeyListener);
        try {
            menu.terminal.wait();
            obj = menu.getSelectedItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        menu.terminal.getInputComponent().removeKeyListener(menuKeyListener);
        //jterminal.getInputComponent().addKeyListener(new JTerminalKeylistener(jterminal.getInputComponent()));
        menu.terminal.getInputComponent().setEditable(true);
        menu.terminal.getScrollPaneView().remove(menu);
        menu.terminal.getFrame().revalidate();
        menu.terminal.getScrollPane().revalidate();
        menu.terminal.getScrollPane().repaint();
        menu.terminal.getScrollPane().getViewport().setViewPosition(new Point(0, menu.terminal.getScrollPane().getHeight()));
        menu.terminal.getInputComponent().requestFocusInWindow();
        menu.terminal.newLine();
        return obj;
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(terminal.getOutputComponent().getPreferredSize().width, super.getPreferredSize().height);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(terminal.getOutputComponent().getPreferredSize().width, super.getPreferredSize().height);
    }

    @Override
    public void applyTheme(Theme theme){
        this.setBackground(theme.backgroundColor);
        this.setForeground(theme.foregroundColor);
    }
}
