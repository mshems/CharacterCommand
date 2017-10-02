package terminal;

import app.CharacterCommand;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Terminal implements TerminalEventListener, TerminalInterface{
    private Dimension windowSize = new Dimension(850, 650);
    private TerminalInputComponent inputComponent;
    private JFrame frame;
    private CommandHandler commandHandler;
    private LinkedBlockingQueue<String> commandQueue;
    private int maxLines = 256;

    public Terminal(String title, String default_prompt){
        commandHandler = new CommandHandler(this);
        commandQueue = new LinkedBlockingQueue<>();
        initFrame(title, default_prompt);
    }

    private void initFrame(String title, String default_prompt){
        frame = new JFrame(title);
        frame.setLayout(new BorderLayout());
        frame.setSize(windowSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        inputComponent = new TerminalInputComponent(default_prompt);
        JScrollPane scrollPane = new JScrollPane(inputComponent);
        frame.add(scrollPane, BorderLayout.CENTER);
        //frame.add(inputComponent);
        inputComponent.setTerminalEventListener(this);
    }

    @Override
    public synchronized void start(){
        frame.setVisible(true);
        if(CharacterCommand.hasActiveChar()){
            String charprompt = CharacterCommand.getActiveChar().getName()+"@CharacterCommand ~ ";
            inputComponent.setPrompt(charprompt);
        } else {
            inputComponent.setPrompt("CharacterCommand ~ ");
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
             } catch (InterruptedException e) {
                 e.printStackTrace();
                 break;
             }
         }
    }

    public void setPrompt(String prompt){
        inputComponent.setPrompt(prompt);
    }

    public void clear(){
        inputComponent.clearAndReprompt();
    }

    @Override
    public void advance(){
        inputComponent.advance();
    }

    @Override
    public void printBlock(TerminalPrinter terminalPrinter){
        inputComponent.newLine();
        terminalPrinter.printBlock();
    }
    public String makeQueryInline(){
        inputComponent.newLine();
        return "";
    }
    @Override
    public void printf(String format, Object... args){
        inputComponent.print(String.format(format, args));
    }

    public void printOut(String message){
        printBlock(()->
                this.print(message)
        );
    }

    @Override
    public void print(String s){
        inputComponent.print(s);
    }
    @Override
    public void print(Integer n){
        inputComponent.print(n.toString());
    }
    @Override
    public void print(Double d){
        inputComponent.print(d.toString());
    }
    @Override
    public void print(Boolean b){
        inputComponent.print(b.toString());
    }
    @Override
    public void println(String s){
        inputComponent.println(s);
    }
    @Override
    public void println(Integer n){
        inputComponent.println(n.toString());
    }
    @Override
    public void println(Double d){
        inputComponent.println(d.toString());
    }
    @Override
    public void println(Boolean b){
        inputComponent.println(b.toString());
    }

    private synchronized String getQueryResponse(String query){
        String s="";
        inputComponent.setPrompt(query);
        inputComponent.advance();
        inputComponent.setQuerying(true);
        synchronized (this) {
            try{
                this.wait();
                s = inputComponent.getCommand();
                inputComponent.resetPrompt();
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
        return s.trim();
    }

    @Override
    public String queryString(String query, boolean allowEmptyString){
        while(true) {
            String s = getQueryResponse(query);
            if (s.isEmpty() && allowEmptyString) {
                return s;
            } else if (!s.isEmpty()){
                return s;
            }
            this.printBlock(() -> this.print("Please enter a string"));
        }
    }

    @Override
    public boolean queryYN(String query){
        String s = getQueryResponse(query).toLowerCase();
        switch(s){
            case "y":
            case "yes":
                return true;
            default:
                return false;
        }

    }

    @Override
    public Integer queryInteger(String query, boolean allowNull){
        while(true) {
            try {
                return Integer.parseInt(getQueryResponse(query));
            } catch (NumberFormatException e) {
                if(allowNull){
                    break;
                }
                this.printBlock(() -> this.print("Not a valid integer"));
            }
        }
        return null;
    }

    @Override
    public Boolean queryBoolean(String query, boolean allowNull){
        while(true) {
            switch (getQueryResponse(query).toLowerCase()){
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
                    this.printBlock(() -> this.print("Not a valid boolean"));
            }
        }
    }

    @Override
    public Double queryDouble(String query, boolean allowNull) {
        while (true) {
            try {
                return Double.parseDouble(getQueryResponse(query));
            } catch (NumberFormatException e) {
                if (allowNull) {
                    break;
                }
                this.printBlock(() -> this.print("Not a valid double"));
            }
        }
        return null;
    }

    @Override
    public synchronized void submitActionPerformed(SubmitEvent e) {
        this.notifyAll();
        try {
            commandQueue.put(e.commandString);
            inputComponent.updateHistory(e.commandString);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public synchronized void queryActionPerformed(QueryEvent e) {
        this.notifyAll();
    }
}
