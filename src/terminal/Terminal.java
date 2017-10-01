package terminal;

import app.CharacterCommand;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Terminal implements TerminalEventListener, TerminalInterface{
    private Dimension windowSize = new Dimension(800, 600);
    private TerminalInputComponent inputComponent;
    private JFrame frame;
    private CommandHandler commandHandler;
    private LinkedBlockingQueue<String> commandQueue;

    public Terminal(String title){
        commandHandler = new CommandHandler(this);
        commandQueue = new LinkedBlockingQueue<>();
        initFrame(title);
    }

    private void initFrame(String title){
        frame = new JFrame(title);
        frame.setSize(windowSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        inputComponent = new TerminalInputComponent();
        frame.add(inputComponent);
        inputComponent.setTerminalEventListener(this);
    }

    @Override
    public synchronized void start(){
        frame.setVisible(true);
        inputComponent.start();
        while(true) {
             try {
                 wait();
                 if (!commandQueue.isEmpty()) {
                     commandHandler.processCommand(commandQueue.take());
                 }
             } catch (InterruptedException e) {
                //e.printStackTrace();
             }
         }
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
                //ex.printStackTrace();
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
        switch(getQueryResponse(query).toLowerCase()){
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
                this.printBlock(() -> this.print("Not a valid integer"));
                if(allowNull){
                    break;
                }
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
                this.printBlock(() -> this.print("Not a valid double"));
                if (allowNull) {
                    break;
                }
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
            //ex.printStackTrace();
        }
    }

    @Override
    public synchronized void queryActionPerformed(QueryEvent e) {
        this.notifyAll();
    }
}
