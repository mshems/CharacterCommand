package app.terminal;

import java.awt.event.ActionEvent;

public class SubmitEvent extends ActionEvent {
    public String inputString;

    public SubmitEvent(Object source, int id, String commandID, String inputString ) {
        super(source, id, commandID);
        this.inputString = inputString;
    }
}
