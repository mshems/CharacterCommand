package app.terminal;

import java.awt.event.ActionEvent;

public class MenuEvent extends ActionEvent {

    public MenuEvent(Object source, int id, String command) {
        super(source, id, command);
    }
}
