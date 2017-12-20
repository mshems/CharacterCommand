package app.ui.terminal.core.behavior;

import app.ui.terminal.core.CommandAction;
import app.ui.terminal.core.JTerminal;
import app.ui.terminal.core.UnknownCommandException;

public class CommandExecutor {
    private JTerminal terminal;

    public CommandExecutor(JTerminal terminal){
        this.terminal = terminal;
    }

    /**
     * Executes the command mapped to the input token.
     * This method defines the behavior of the JTerminal when processing input.
     * @param token the token used to get the mapped command
     * @throws UnknownCommandException if no command is mapped to the input token
     */
    public void doCommand(String token) throws UnknownCommandException {
        CommandAction commandAction = terminal.getCommand(token);
        if(commandAction != null) {
            commandAction.executeCommand();
            terminal.clearBuffer();
        } else {
            terminal.clearBuffer();
            throw new UnknownCommandException(token);
        }
    }
}
