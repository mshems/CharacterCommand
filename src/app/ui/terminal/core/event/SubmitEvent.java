package app.ui.terminal.core.event;

import java.awt.event.ActionEvent;

/**
 * Event object that is fired to notify that input has been received.
 */
public class SubmitEvent extends ActionEvent {

    public SubmitEvent(Object source, String command) {
        super(source, 0, command);
    }
}
