package jterminal.core.behavior;

import jterminal.core.JTerminal;

/**
 * This interface is used to define the behavior of a JTerminal when its <code>start()</code> method is called.
 */
public interface StartBehavior {
    void doBehavior(JTerminal terminal);
}
