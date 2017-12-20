package app.ui.terminal.core.event;

import java.awt.event.ActionEvent;

/**
 * Event object that is fired to notify that input has been received in response to a query.
 */
public class QueryEvent extends ActionEvent {
    public boolean cancelledQuery = false;

    public QueryEvent(Object source) {
        super(source, 1, "query-event");
    }

    public QueryEvent(Object source, boolean cancelledQuery) {
        super(source, 1, "query-event");
    }
}
