package terminal;

import java.awt.event.ActionEvent;

public class QueryEvent extends ActionEvent {
    public String inputString;

    public QueryEvent(Object source, int id, String command, String inputString) {
        super(source, id, command);
        this.inputString = inputString;
    }
}
