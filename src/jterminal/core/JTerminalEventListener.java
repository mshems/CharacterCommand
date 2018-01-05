package jterminal.core;

import jterminal.core.event.QueryEvent;
import jterminal.core.event.SubmitEvent;

public interface JTerminalEventListener {
    void submitActionPerformed(SubmitEvent e);
    void queryActionPerformed(QueryEvent e);
}
