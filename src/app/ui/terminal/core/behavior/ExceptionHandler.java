package app.ui.terminal.core.behavior;

import app.ui.terminal.core.JTerminal;
import app.ui.terminal.core.UnknownCommandException;

/**
 * This class defines the behavior of a JTerminal upon receiving an <code>UnknownCommandException</code>.
 */
public class ExceptionHandler {
    private JTerminal terminal;

    public ExceptionHandler(JTerminal term){
        this.terminal = term;
    }

    public void handleException(UnknownCommandException e){
        terminal.out.println("Unknown command: \""+e.getCommand()+"\"");
    }
}
