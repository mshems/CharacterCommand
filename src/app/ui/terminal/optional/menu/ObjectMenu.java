package app.ui.terminal.optional.menu;

import app.ui.terminal.core.event.QueryEvent;
import app.ui.terminal.core.JTerminal;

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
    ObjectMenu(JTerminal term, Collection<E> objects, int direction, LabelFactory<E> labelFactory){
        super(term);
        this.terminal = term;
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
    ObjectMenu(JTerminal term, Map<String, E> itemMap, int direction){
        super(term);
        this.terminal = term;
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
        //JTextArea textArea = terminal.getOutputComponent();
        //Font f = terminal.getTheme().font;
        //textArea.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(terminal.getTheme().backgroundColor);
        labelPanel.setForeground(terminal.getTheme().foregroundColor);
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        for(JLabel l:labels){
            labelPanel.add(l);
        }
        //this.add(textArea);
        this.add(labelPanel);
    }

    private void initVerticalMenu(){
        /*JTextArea textArea=terminal.getOutputComponent();
        Font f = terminal.getTheme().font;
        textArea.setFont(new Font(f.getName(), f.getStyle(), terminal.getFontSize()));*/

        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(terminal.getTheme().backgroundColor);
        menuPanel.setForeground(terminal.getTheme().foregroundColor);
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel labelPanel = new JPanel();
        labelPanel.setBackground(terminal.getTheme().backgroundColor);
        labelPanel.setForeground(terminal.getTheme().foregroundColor);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        menuPanel.add(labelPanel);

        for(JLabel l:labels){
            labelPanel.add(l);
        }
        //this.add(textArea);
        this.add(menuPanel);
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
