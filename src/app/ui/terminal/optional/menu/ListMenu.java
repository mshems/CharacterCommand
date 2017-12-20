package app.ui.terminal.optional.menu;

import app.ui.terminal.core.*;
import app.ui.terminal.core.event.QueryEvent;
import app.ui.terminal.core.theme.Theme;
import app.ui.terminal.core.theme.ThemedComponent;

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
    protected boolean cancelled = false;

    public abstract E getSelectedItem();
    public abstract int getSelection();
    public abstract void selectItem(int index);
    public abstract void deselectItem();
    public abstract int getNumLabels();
    public abstract void fireEvent(QueryEvent e);

    ListMenu(JTerminal t){
            this.terminal=t;
    }

    void initLayout(Theme theme){
        this.applyTheme(theme);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Displays a menu on a JTerminal and returns the user's selection.
     * @param terminal the JTerminal to display the menu
     * @param menu the menu to be displayed
     * @param <E> the type of the items in the menu
     * @return the user's selection from the menu
     */
    public static synchronized <E> E queryMenu(JTerminal terminal, ListMenu<E> menu){
        E obj = null;
        MenuKeyListener menuKeyListener = new MenuKeyListener(menu);
        terminal.getInputComponent().removeKeyListener(terminal.getInputComponent().getKeyListeners()[0]);
        terminal.getInputComponent().addKeyListener(menuKeyListener);
        terminal.getInputComponent().setEditable(false);
        terminal.getScrollPaneView().add(menu);
        //terminal.getTextPanel().revalidate();
        terminal.getFrame().revalidate();
        terminal.getScrollPane().repaint();
        terminal.getScrollPane().getViewport().setViewPosition(new Point(0, terminal.getScrollPane().getHeight()));
        menu.requestFocusInWindow();
        menu.addKeyListener(menuKeyListener);
        try {
            terminal.wait();
            obj = menu.getSelectedItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        terminal.getInputComponent().removeKeyListener(menuKeyListener);
        terminal.getInputComponent().addKeyListener(new JTerminalKeylistener(terminal.getInputComponent()));
        terminal.getInputComponent().setEditable(true);
        terminal.getScrollPaneView().remove(menu);
        terminal.getFrame().revalidate();
        terminal.getScrollPane().repaint();
        terminal.getScrollPane().getViewport().setViewPosition(new Point(0, terminal.getScrollPane().getHeight()));
        terminal.getOutputComponent().requestFocusInWindow();
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
