package jterminal.optional.menu;

import jterminal.core.event.QueryEvent;
import jterminal.core.JTerminal;

import javax.swing.*;
import java.awt.*;

/**
 * Basic Menu constructed using a String array as the entries in the menu.
 */
public class BasicMenu extends ListMenu<String> {
    private String[] strings;
    private JLabel[] labels;
    private int selection;

    /**
     * Creates a new instance of a BasicMenu.
     * @param term JTerminal on which to display the menu
     * @param strings String array of the menu's entries
     * @param direction constants ListMenu.VERTICAL or ListMenu.HORIZONTAL specifying the layout of the menu
     */
    BasicMenu(String title, JTerminal term, String[] strings, int direction){
        super(title, term);
        this.labels = new JLabel[strings.length];
        this.strings = strings;
        initLayout(terminal.getTheme());

        if(direction==VERTICAL){
            initVerticalMenu();
        } else {
            initHorizontalMenu();
        }
        selectItem(0);
    }

    private void initHorizontalMenu(){
        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(terminal.getTheme().backgroundColor);
        labelPanel.setForeground(terminal.getTheme().foregroundColor);
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        makeLabels();
        for (JLabel l:labels) {
            labelPanel.add(l);
            labelPanel.add(Box.createHorizontalStrut(3));
        }

        this.add(labelPanel);
    }

    private void initVerticalMenu(){
        /*JPanel menuPanel = new JPanel();
        menuPanel.setBackground(jterminal.getTheme().backgroundColor);
        menuPanel.setForeground(jterminal.getTheme().foregroundColor);
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));*/

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(terminal.getTheme().backgroundColor);
        labelPanel.setForeground(terminal.getTheme().foregroundColor);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        //menuPanel.add(labelPanel);

        makeLabels();
        for(JLabel l:labels){
            labelPanel.add(l);
        }
        this.add(labelPanel);
    }

    private void makeLabels(){
        for(int i=0; i<strings.length; i++){
            JLabel label = new JLabel(strings[i]);
            label.setBackground(terminal.getTheme().backgroundColor);
            label.setForeground(terminal.getTheme().foregroundColor);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(terminal.getTheme().backgroundColor, 3));
            Font f = terminal.getTheme().font;
            label.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));
            labels[i] = label;
        }
    }

    @Override
    public void selectItem(int n){
        selection = n;
        labels[selection].setForeground(terminal.getTheme().backgroundColor);
        labels[selection].setBackground(terminal.getTheme().highlightColor);
        labels[selection].setBorder(BorderFactory.createLineBorder(terminal.getTheme().highlightColor, 3));
    }
    @Override
    public void deselectItem(){
        labels[selection].setBackground(terminal.getTheme().backgroundColor);
        labels[selection].setForeground(terminal.getTheme().foregroundColor);
        labels[selection].setBorder(BorderFactory.createLineBorder(terminal.getTheme().backgroundColor, 3));

    }

    @Override
    public int getNumLabels() {
        return strings.length;
    }

    @Override
    public void fireEvent (QueryEvent e){
        if(e.cancelledQuery) cancelled = true;
        if(terminal !=null){
            terminal.queryActionPerformed(e);
        }
    }

    @Override
    public String getSelectedItem(){
        if(cancelled){
            cancelled = false;
            return null;
        }
        return this.strings[selection];
    }

    @Override
    public int getSelection() {
        return selection;
    }


}
