package app.ui.terminal.core;

public class UnknownCommandException extends Exception{
    private String command;

    public UnknownCommandException(String command){
        this.command = command;
    }

    public String getCommand(){
        return command;
    }
}
