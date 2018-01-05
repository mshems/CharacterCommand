package jterminal.optional.menu;

import jterminal.core.CommandAction;
import jterminal.core.JTerminal;
import jterminal.core.event.QueryEvent;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Map;

public class ActionMenu extends ListMenu<CommandAction>{
    private LinkedList<JLabel> labels;
    private Map<String, CommandAction> actionMap;
    private int selection;
    /**
     * Creates a new instance of an ActionMenu from a collection of objects
     * @param term the JTerminal on which to display the menu
     * @param actionMap A map of Strings to CommandActions, the Strings being the labels for entries in the menu
     * @param direction defines menu layout
     */
    ActionMenu(String title, JTerminal term, Map<String, CommandAction> actionMap, int direction){
        super(title, term);
        this.labels = new LinkedList<>();
        this.actionMap = actionMap;
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
        labelPanel.setLayout(new FlowLayout());
        if(menuTitle!=null) this.add(new JLabel(menuTitle));
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
        for(String str: actionMap.keySet()){
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
    public CommandAction getSelectedItem(){
        if(cancelled){
            cancelled = false;
            return null;
        }
        return this.actionMap.get(labels.get(selection).getText());
    }
}
