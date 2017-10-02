package terminal;

import app.CharacterCommand;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Terminal implements TerminalEventListener, TerminalInterface{
    private Dimension windowSize = new Dimension(800, 600);
    private TerminalInputComponent inputComponent;
    private JFrame frame;
    private CommandHandler commandHandler;
    private LinkedBlockingQueue<String> commandQueue;
    private int maxLines = 256;

    private Color backgroundColor;
    private Color foregroundColor;
    private Color caretColor;
    private Font textFont;

    public Terminal(String title){
        commandHandler = new CommandHandler(this);
        commandQueue = new LinkedBlockingQueue<>();
        initFrame(title);
    }

    public void initFrame(String title){
        frame = new JFrame(title);
        frame.setSize(windowSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        inputComponent = new TerminalInputComponent(true);
        inputComponent.setTerminalEventListener(this);
        JScrollPane scrollPane = new JScrollPane(inputComponent);
        frame.add(scrollPane);
    }

    @Override
    public synchronized void start(){
        frame.setVisible(true);
        if(CharacterCommand.hasActiveChar()){
            String charprompt = CharacterCommand.getActiveChar().getName()+" @ CharacterCommand ~ ";
            this.setPrompt(charprompt);
        } else {
            this.setPrompt("CharacterCommand ~ ");
        }
        inputComponent.start();
        while(true) {
            try {
                wait();
                if (!commandQueue.isEmpty()) {
                    commandHandler.processCommand(commandQueue.take());
                    //remove lines above max line count
                    if(inputComponent.getLineCount()>maxLines){
                        int linesToRemove = inputComponent.getLineCount()-maxLines;
                        try {
                            inputComponent.replaceRange("",
                                    inputComponent.getLineStartOffset(0),
                                    inputComponent.getLineEndOffset(linesToRemove));
                        } catch (BadLocationException e){
                            e.printStackTrace();
                        }
                    }
                }
                advance();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }

    public void setPrompt(String prompt){
        inputComponent.setCurrPrompt(prompt);
    }

    @Override
    public synchronized String query(String queryPrompt){
        String s="";
        inputComponent.setCurrPrompt(queryPrompt);
        advance();
        inputComponent.setQuerying(true);
        synchronized (this) {
            try{
                this.wait();
                s = inputComponent.getCommand();
                inputComponent.resetPrompt();
            }catch (InterruptedException ex){
                //ex.printStackTrace();
            }
        }
        return s.trim();
    }

    @Override
    public String queryString(String queryPrompt, boolean allowEmptyString){
        while(true) {
            String s = query(queryPrompt);
            if (s.isEmpty() && allowEmptyString) {
                return s;
            } else if (!s.isEmpty()){
                return s;
            }
            println("Empty input not allowed");
        }
    }

    @Override
    public boolean queryYN(String queryPrompt){
        switch(query(queryPrompt).toLowerCase()){
            case "y":
            case "yes":
                return true;
            default:
                return false;
        }
    }

    @Override
    public Integer queryInteger(String queryPrompt, boolean allowNull){
        while(true) {
            try {
                return Integer.parseInt(query(queryPrompt));
            } catch (NumberFormatException e) {
                if(allowNull){
                    break;
                }
                println("Not an integer value");
            }
        }
        return null;
    }

    @Override
    public Double queryDouble(String queryPrompt, boolean allowNull) {
        while (true) {
            try {
                return Double.parseDouble(query(queryPrompt));
            } catch (NumberFormatException e) {
                if (allowNull) {
                    break;
                }
                println("Not a double value");
            }
        }
        return null;
    }

    @Override
    public Boolean queryBoolean(String queryPrompt, boolean allowNull){
        while(true) {
            switch (query(queryPrompt).toLowerCase()){
                case "t":
                case "true":
                    return true;
                case "f":
                case "false":
                    return false;
                default:
                    if(allowNull){
                        return null;
                    }
                    println("Not a boolean value");
            }
        }
    }

    @Override
    public void advance(){
        inputComponent.advance();
    }

    public void clear(){
        inputComponent.clear();
    }

    @Override
    public synchronized void submitActionPerformed(SubmitEvent e) {
        this.notifyAll();
        try {
            commandQueue.put(e.commandString);
            inputComponent.updateHistory(e.commandString);
            newLine();
        }catch (InterruptedException ex){
            //ex.printStackTrace();
        }
    }

    @Override
    public synchronized void queryActionPerformed(QueryEvent e) {
        newLine();
        this.notifyAll();
    }

    public void newLine(){
        inputComponent.newLine();
    }

    @Override
    public void printf(String format, Object... args){
        inputComponent.print(String.format(format, args));
    }

    @Override
    public void print(String s){
        inputComponent.print(s);
    }
    public void print(Integer n){
        inputComponent.print(n.toString());
    }
    public void print(Double d){
        inputComponent.print(d.toString());
    }
    public void print(Boolean b){
        inputComponent.print(b.toString());
    }

    @Override
    public void println(String s){
        inputComponent.println(s);
    }
    public void println(Integer n){
        inputComponent.println(n.toString());
    }
    public void println(Double d){
        inputComponent.println(d.toString());
    }
    public void println(Boolean b){
        inputComponent.println(b.toString());
    }
}
