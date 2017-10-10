package app.terminal;

public class CommandExecutor {
    public void doCommand(Terminal terminal, String token){
        TerminalCommand command = terminal.getCommandMap().get(token);
        if(command != null) {
            terminal.newLine();
            command.executeCommand();
        } else {
            terminal.newLine();
            terminal.println("Command '"+token+"' not found");
        }
        terminal.getCommandTokens().clear();
    }
}
