package app.ui.terminal.core.behavior;

import app.ui.terminal.core.JTerminal;

/**
 * This interface is used to define the behavior of a JTerminal when its <code>close()</code> method is called.
 */
public interface CloseBehavior {
    void doBehavior(JTerminal terminal);
}
