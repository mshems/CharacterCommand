package app.ui.terminal.optional.menu;

import app.ui.terminal.core.CommandAction;
import app.ui.terminal.core.JTerminal;

import java.util.Collection;
import java.util.Map;

public class MenuBuilder{
    private int direction = ListMenu.HORIZONTAL;

    public MenuBuilder setDirection(int direction){
        this.direction = direction;
        return this;
    }

    public BasicMenu buildBasicMenu(JTerminal terminal, String[] labels){
        return new BasicMenu(terminal, labels, this.direction);
    }

    public ActionMenu buildActionMenu(JTerminal terminal, Map<String, CommandAction> map){
        return new ActionMenu(terminal, map, this.direction);
    }

    public <E> ObjectMenu<E> buildObjectMenu(JTerminal terminal, Map<String, E> map){
        return new ObjectMenu<>(terminal, map, this.direction);
    }

    public <E> ObjectMenu<E> buildObjectMenu(JTerminal terminal, Collection<E> list, LabelFactory<E> labelFactory){
        return new ObjectMenu<>(terminal, list, this.direction, labelFactory);
    }
}