package terminal;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Terminal implements TerminalEventListener {
    private Dimension windowSize = new Dimension(850, 650);
    private TerminalIOComponent inputComponent;
    private TerminalIOComponent outputComponent;
    private JFrame frame;
    private LinkedBlockingQueue<String> commandQueue;
    private LinkedList<String> commandTokens;
    private CommandMap commandMap;
    private boolean dualDisplay;
    private CommandExecutor commandExecutor;

    private Properties properties;

    public static final Color background_color = new Color(33, 33, 33);
    public static final Color foreground_color = new Color(245, 245, 245);
    public static final Color highlight_color = new Color(220, 220, 220);

    public static final int LEFT_ALIGNED = 0;
    public static final int CENTERED = 1;
    public static final int RIGHT_ALIGNED = 2;

    public Terminal(String title) {
        this.dualDisplay = false;
        commandQueue = new LinkedBlockingQueue<>();
        commandTokens = new LinkedList<>();
        commandMap = new CommandMap();
        commandExecutor = new CommandExecutor();
        properties = new Properties();
        addDefaultCommands();
        initGUI(title);
        PropertyHandler.initProperties(this);
    }


    private void initGUI(String title) {
        frame = new JFrame(title);
        frame.setMinimumSize(windowSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        /*if(dualDisplay){
            inputComponent = new TerminalIOComponent(false);
            inputComponent.setTerminalEventListener(this);
            outputComponent = new TerminalDisplayComponent();
            frame.add(inputComponent, BorderLayout.SOUTH);
        } else {*/
        inputComponent = new TerminalIOComponent(true);
        inputComponent.setTerminalEventListener(this);
        outputComponent = inputComponent;
        //}

        JScrollPane scrollPane = new JScrollPane(outputComponent);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
    }

    public synchronized void start() {
        frame.setVisible(true);
        inputComponent.start();
        while (true) {
            try {
                wait();
                if (!commandQueue.isEmpty()) {
                    tokenize(commandQueue.take());
                    commandExecutor.doCommand(this, commandTokens.peek());
                }
                inputComponent.advance();
            } catch (InterruptedException e) {
                //e.printStackTrace();
                break;
            }
        }
    }

    public void close(){
        PropertyHandler.writeProperties(this);
        frame.setVisible(false);
    }

    private void tokenize(String command) {
        String[] input = command
                .trim()
                .split("\\s+");
        Collections.addAll(commandTokens, input);
    }

    private synchronized String query(String queryPrompt) {
        String input = "";
        inputComponent.setCurrPrompt(queryPrompt);
        inputComponent.advance();
        inputComponent.setQuerying(true);
        synchronized (this) {
            try {
                this.wait();
                input = inputComponent.getInput();
            } catch (InterruptedException ex) {
                //ex.printStackTrace();
            }
        }
        inputComponent.resetPrompt();
        newLine();
        return input.trim();
    }

    public String queryString(String queryPrompt, boolean allowEmptyString) {
        while (true) {
            String input = query(queryPrompt);
            if (input.isEmpty() && allowEmptyString) {
                return input;
            } else if (!input.isEmpty()) {
                return input;
            }
            println("Empty input not allowed");
        }
    }

    public boolean queryYN(String queryPrompt) {
        switch (query(queryPrompt).toLowerCase()) {
            case "y":
            case "yes":
                return true;
            default:
                return false;
        }
    }

    public Integer queryInteger(String queryPrompt, boolean allowNull) {
        while (true) {
            try {
                return Integer.parseInt(query(queryPrompt));
            } catch (NumberFormatException e) {
                if (allowNull) {
                    break;
                }
                println("Not an integer value");
            }
        }
        return null;
    }

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

    public Boolean queryBoolean(String queryPrompt, boolean allowNull) {
        while (true) {
            switch (query(queryPrompt).toLowerCase()) {
                case "t":
                case "true":
                    return true;
                case "f":
                case "false":
                    return false;
                default:
                    if (allowNull) {
                        return null;
                    }
                    println("Not a boolean value");
            }
        }
    }

    public void newLine() {
        inputComponent.newLine();
    }

    public void clear() {
        if (dualDisplay) {
            outputComponent.clear();
        } else {
            inputComponent.clear();
        }
    }

    public void printf(String format, Object... args) {
        outputComponent.print(String.format(format, args));
    }

    public void print(String str) {
        outputComponent.print(str);
    }

    public void print(Integer n) {
        outputComponent.print(n.toString());
    }

    public void print(Double d) {
        outputComponent.print(d.toString());
    }

    public void print(Boolean b) {
        outputComponent.print(b.toString());
    }

    public void print(Object o) {
        outputComponent.print(o.toString());
    }

    public void println(String str) {
        outputComponent.println(str);
    }

    public void println(Integer n) {
        outputComponent.println(n.toString());
    }

    public void println(Double d) {
        outputComponent.println(d.toString());
    }

    public void println(Boolean b) {
        outputComponent.println(b.toString());
    }

    public void println(Object o) {
        outputComponent.println(o.toString());
    }

    public void println(String str, int PRINT_FORMAT) {
        switch (PRINT_FORMAT) {
            case Terminal.LEFT_ALIGNED:
                outputComponent.print(str);
                break;
            case Terminal.CENTERED:
                outputComponent.printCentered(str);
                break;
            case Terminal.RIGHT_ALIGNED:
                outputComponent.printRightAligned(str);
                break;
            default:
                outputComponent.print(str);
                break;
        }
    }

    public synchronized void submitActionPerformed(SubmitEvent e) {
        this.notifyAll();
        try {
            commandQueue.put(e.inputString);
            inputComponent.updateHistory(e.inputString);
        } catch (InterruptedException ex) {
            //ex.printStackTrace();
        }
    }

    public synchronized void queryActionPerformed(QueryEvent e) {
        this.notifyAll();
    }

    public synchronized void menuActionPerformed(MenuEvent e) {
        this.notifyAll();
    }

    public void addDefaultCommands() {
        commandMap.put("", () -> {
        });
        commandMap.put("clear", this::clear);
        commandMap.put("terminal-config", this::config);
    }

    private void config() {
        commandTokens.pop();
        switch (commandTokens.pop()) {
            case "font-size":
                if(!commandTokens.isEmpty()) {
                    try {
                        int fontSize = Integer.parseInt(commandTokens.pop());
                        setFontSize(fontSize);
                        properties.setProperty("font-size", Integer.toString(fontSize));
                    } catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        PropertyHandler.writeProperties(this);
    }

    public void removeDefaultCommands() {
        commandMap.remove("");
        commandMap.remove("clear");
        commandMap.remove("terminal-config");
    }

    public void putCommand(String key, TerminalCommand command) {
        commandMap.put(key, command);
    }

    public void replaceCommand(String key, TerminalCommand command) {
        commandMap.replace(key, command);
    }

    public void removeCommand(String key, TerminalCommand command) {
        commandMap.remove(key, command);
    }

    public void removeCommand(String key) {
        commandMap.remove(key);
    }

    public TerminalCommand getCommand(String key) {
        return this.commandMap.get(key);
    }

    public CommandMap getCommandMap() {
        return commandMap;
    }

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public TerminalIOComponent getInputComponent() {
        return inputComponent;
    }

    public TerminalIOComponent getOutputComponent() {
        return outputComponent;
    }

    public void setDefaultPrompt(String prompt) {
        inputComponent.setDefaultPrompt(prompt);
        inputComponent.setCurrPrompt(prompt);
    }

    public LinkedList<String> getCommandTokens() {
        return commandTokens;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public int getFontSize() {
        return outputComponent.getFontSize();

    }

    public void setFontSize(int fontSize) {
        this.inputComponent.setFontSize(fontSize);
        this.outputComponent.setFontSize(fontSize);
    }
}