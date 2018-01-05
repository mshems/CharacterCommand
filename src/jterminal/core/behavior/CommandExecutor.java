package jterminal.core.behavior;

import jterminal.core.CommandAction;
import jterminal.core.JTerminal;
import jterminal.core.UnknownCommandException;

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
