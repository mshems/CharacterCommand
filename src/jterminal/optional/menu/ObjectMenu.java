package jterminal.optional.menu;

import jterminal.core.event.QueryEvent;
import jterminal.core.JTerminal;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ObjectMenu<E> extends ListMenu<E> {
    private LinkedList<JLabel> labels;
    private Map<String,  E> itemMap;
    private int selection;

    /**
     * Creates a new instance of an ObjectMenu from a collection of objects
     * @param term the JTerminal on which to display the menu
     * @param objects collection of objects to populate the menu
     * @param direction defines menu layout
     * @param labelFactory specifies behavior for converting objects to strings for the menu entries
     */
    ObjectMenu(String title, JTerminal term, Collection<E> objects, int direction, LabelFactory<E> labelFactory){
        super(title, term);
        this.labels = new LinkedList<>();
        itemMap = new LinkedHashMap<String, E>();
        initLayout(terminal.getTheme());
        for(E o:objects){
            itemMap.put(labelFactory.toLabel(o), o);
        }
        makeLabels();
        if(direction==VERTICAL){
            initVerticalMenu();
        } else {
            initHorizontalMenu();
        }
        selectItem(0);
    }

    /**
     * Creates a new instance of an ObjectMenu from a collection of objects
     * @param term the JTerminal on which to display the menu
     * @param itemMap A map of Strings to Objects, the Strings being the labels for entries in the menu
     * @param direction defines menu layout
     */
    ObjectMenu(String title, JTerminal term, Map<String, E> itemMap, int direction){
        super(title, term);
        this.labels = new LinkedList<>();
        this.itemMap = itemMap;
        initLayout(terminal.getTheme());
        makeLabels();

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
        for(JLabel l:labels){
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
        for(JLabel l:labels){
            labelPanel.add(l);
        }
        this.add(labelPanel);
    }

    private void makeLabels(){
        for(String str:itemMap.keySet()){
            JLabel label = new JLabel(str);
            label.setBackground(terminal.getTheme().backgroundColor);
            label.setForeground(terminal.getTheme().foregroundColor);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createLineBorder(terminal.getTheme().backgroundColor, 3));
            Font f = terminal.getTheme().font;
            label.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));
            labels.add(label);
        }
    }

    @Override
    public void selectItem(int n){
        selection = n;
        labels.get(selection).setForeground(terminal.getTheme().backgroundColor);
        labels.get(selection).setBackground(terminal.getTheme().highlightColor);
        labels.get(selection).setBorder(BorderFactory.createLineBorder(terminal.getTheme().highlightColor, 3));
    }

    @Override
    public void deselectItem(){
        labels.get(selection).setBackground(terminal.getTheme().backgroundColor);
        labels.get(selection).setForeground(terminal.getTheme().foregroundColor);
        labels.get(selection).setBorder(BorderFactory.createLineBorder(terminal.getTheme().backgroundColor, 3));

    }

    @Override
    public void fireEvent (QueryEvent e){
        if(e.cancelledQuery) cancelled = true;
        if(terminal !=null){
            terminal.queryActionPerformed(e);
        }
    }

    @Override
    public int getNumLabels(){
        return labels.size();
    }

    @Override
    public int getSelection() {
        return selection;
    }

    @Override
    public E getSelectedItem(){
        if(cancelled){
            cancelled = false;
            return null;
        } else {
            return this.itemMap.get(labels.get(selection).getText());
        }
    }
}
