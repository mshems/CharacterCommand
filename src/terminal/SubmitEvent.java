package terminal;

import java.awt.event.ActionEvent;

public class SubmitEvent extends ActionEvent {
    String commandString;

    public SubmitEvent(Object source, int id, String commandID, String commandString ) {
        super(source, id, commandID);
        this.commandString = commandString;
    }
}
