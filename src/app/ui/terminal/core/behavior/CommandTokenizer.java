package app.ui.terminal.core.behavior;

import app.ui.terminal.core.JTerminal;

import java.util.Collections;

public class CommandTokenizer {
    private JTerminal terminal;
    private String regex = "\\s+";

    public CommandTokenizer (JTerminal terminal){
        this.terminal = terminal;
    }

    /**
     * Splits the input into tokens and adds them to the token buffer.
     * This method defines the behavior of the JTerminal when making tokens out of an input String.
     * @param input the input from the user
     */
    public void tokenize(String input){
        String[] tokens = input
                .trim()
                .split(regex);
        Collections.addAll(terminal.getTokenBuffer(), tokens);
    }

    public CommandTokenizer setRegEx(String regex){
        this.regex = regex;
        return this;
    }
}
