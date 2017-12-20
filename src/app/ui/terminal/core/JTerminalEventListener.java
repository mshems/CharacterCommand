package app.ui.terminal.core;

import app.ui.terminal.core.event.QueryEvent;
import app.ui.terminal.core.event.SubmitEvent;

public interface JTerminalEventListener {
    void submitActionPerformed(SubmitEvent e);
    void queryActionPerformed(QueryEvent e);
}
