package jterminal.optional.menu;

import jterminal.core.CommandAction;
import jterminal.core.JTerminal;

import java.util.Collection;
import java.util.Map;

public class MenuFactory {
    private int direction = ListMenu.HORIZONTAL;
    private String menuTitle=null;

    public MenuFactory setDirection(int direction){
        this.direction = direction;
        return this;
    }

    public MenuFactory setTitle(String title){
        this.menuTitle = title;
        return this;
    }

    public BasicMenu buildBasicMenu(JTerminal terminal, String[] labels){
        return new BasicMenu(menuTitle, terminal, labels, this.direction);
    }

    public ActionMenu buildActionMenu(JTerminal terminal, Map<String, CommandAction> map){
        return new ActionMenu(menuTitle, terminal, map, this.direction);
    }

    public <E> ObjectMenu<E> buildObjectMenu(JTerminal terminal, Map<String, E> map){
        return new ObjectMenu<>(menuTitle, terminal, map, this.direction);
    }

    public <E> ObjectMenu<E> buildObjectMenu(JTerminal terminal, Collection<E> list, LabelFactory<E> labelFactory){
        return new ObjectMenu<>(menuTitle, terminal, list, this.direction, labelFactory);
    }
}