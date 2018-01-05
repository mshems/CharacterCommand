package jterminal.core;

import jterminal.core.behavior.*;
import jterminal.core.event.*;
import jterminal.core.theme.*;
import jterminal.core.util.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Command-line interface built using Swing.
 * @version 0.1.0
 * @author Matthew Shems
 */

@SuppressWarnings("unused")

public class JTerminal extends AbstractTerminal implements JTerminalEventListener, ThemedComponent {
    private final Dimension defaultWindowSize = new Dimension(800, 600);
    public final JTerminalPrinter out = new JTerminalPrinter(this);

    private String title;
    private Theme theme;
    private JFrame frame;
    private IOContainerPanel textPanel;
    private JScrollPane scrollPane;
    private JTerminalIOComponent inputComponent;
    private JTerminalIOComponent outputComponent;
    private LinkedBlockingQueue<String> commandQueue;
    private LinkedList<String> tokenBuffer;
    private CommandMap commandMap;
    private JPanel scrollPaneView;

    //defines behavior when executing a command
    private CommandExecutor commandExecutor;
    //defines behavior when making tokens from input
    private CommandTokenizer commandTokenizer;
    //defines behavior when UnknownCommandExceptions are thrown
    private ExceptionHandler exceptionHandler;
    //defines behavior on startup
    private StartBehavior startBehavior;
    //defines behavior on closing
    private CloseBehavior closeBehavior;

    /**
     * Creates a new instance of a JTerminal.
     * @param title title of the JTerminal window
     */
    public JTerminal(String title) {
        commandQueue = new LinkedBlockingQueue<>();
        tokenBuffer = new LinkedList<>();
        commandMap = new CommandMap();
        commandExecutor = new CommandExecutor(this);
        commandTokenizer = new CommandTokenizer(this);
        exceptionHandler = new ExceptionHandler(this);
        this.theme = Theme.DEFAULT_THEME();
        this.title = title;
        addDefaultCommands();
        initUI();
    }

    /**
     * Initializes UI elements.
     */
    private void initUI() {
        frame = new JFrame(title);
        frame.setPreferredSize(defaultWindowSize);
        frame.setMinimumSize(new Dimension(400,400));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setBackground(theme.backgroundColor);

        scrollPane = new JScrollPane();
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        //disable arrow key scrolling
        scrollPane.getActionMap().put("unitScrollRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { }});
        scrollPane.getActionMap().put("unitScrollDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { }});
        scrollPane.getActionMap().put("unitScrollUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) { }});
        scrollPane.setBorder(BorderFactory.createLineBorder(theme.backgroundColor,8));
        //scrollPane.setBackground(Color.RED);

        scrollPaneView = new JPanel();
        scrollPaneView.setLayout(new BoxLayout(scrollPaneView, BoxLayout.Y_AXIS));
        scrollPaneView.setBackground(theme.backgroundColor);
        //scrollPaneView.setBackground(Color.GREEN);

        inputComponent = new JTerminalIOComponent(this, true);
        inputComponent.setTerminalEventListener(this);
        outputComponent = inputComponent;

        textPanel = new IOContainerPanel(outputComponent, scrollPaneView);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setHgap(4);
        flowLayout.setVgap(0);
        textPanel.setLayout(flowLayout);
        textPanel.setBackground(theme.backgroundColor);
        //textPanel.setBackground(Color.BLUE);

        textPanel.add(outputComponent);
        scrollPaneView.add(textPanel);
        scrollPane.setViewportView(scrollPaneView);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.pack();
    }

    /**
     * Starts the JTerminal. If <code>startBehavior != null</code>, executes that behavior first.
     */
    public synchronized void start() {
        if(startBehavior !=null) startBehavior.doBehavior(this);
        frame.setVisible(true);
        inputComponent.start();
        while (true) {
            try {
                if(commandQueue.isEmpty()) wait();
                newLine();
                if (!commandQueue.isEmpty()) {
                    commandTokenizer.tokenize(commandQueue.take());
                    try {
                        commandExecutor.doCommand(tokenBuffer.pop());
                    } catch (UnknownCommandException e){
                        exceptionHandler.handleException(e);
                    }
                }
                inputComponent.advance();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Closes the JTerminal. If <code>closeBehavior != null</code>, executes that behavior before disposing.
     */
    public void close(){
        this.clearBuffer();
        this.clear();
        if(closeBehavior!=null) closeBehavior.doBehavior(this);
        frame.dispose();
    }

    /**
     * Returns whether JTerminal has tokens in its token buffer.
     * @return true if token buffer is not empty.
     */
    public boolean hasTokens(){
        return !tokenBuffer.isEmpty();
    }

    /**
     * Returns the next input token in the token buffer.
     * @return <code>String</code> token at top of token buffer.
     */
    public String peekToken(){
        if(hasTokens()){
            return tokenBuffer.peek();
        } else {
            return null;
        }
    }

    /**
     * Returns and consumes the next input token in the token buffer.
     * @return <code>String</code> token at top of token buffer.
     */
    public String nextToken(){
        if(hasTokens()){
            return tokenBuffer.pop();
        } else {
            return null;
        }
    }

    /**
     * Returns consumes the next input token in the token buffer as an <code>int</code>.
     * @return <code>int</code> token from buffer
     * @throws IllegalTokenException if token can not be parsed into an <code>int</code>
     */
    public int nextIntToken() throws IllegalTokenException {
        String token = nextToken();
        try{
            return Integer.parseInt(token);
        } catch (NumberFormatException e){
            throw new IllegalTokenException(token);
        }
    }

    /**
     * Returns consumes the next input token in the token buffer as a <code>double</code>.
     * @return <code>double</code> token from buffer
     * @throws IllegalTokenException if token can not be parsed into a <code>double</code>
     */
    public double nextDoubleToken() throws  IllegalTokenException{
        String token = nextToken();
        try{
            return Double.parseDouble(token);
        } catch (NumberFormatException e){
            throw new IllegalTokenException(token);
        }
    }

    /**
     * Returns consumes the next input token in the token buffer as a <code>boolean</code>.
     * @return <code>boolean</code> token from buffer
     */
    public boolean nextBooleanToken(){
        return Boolean.parseBoolean(nextToken());
    }

    /**
     * Waits for and then reads input from user.
     * @return <code>String</code> input from user
     */
    private synchronized String nextInput(){
        String input="";
        inputComponent.setQuerying(true);
        inputComponent.setCaretPosition(inputComponent.getText().length());
        int position = inputComponent.getCaretPosition();
        JTerminalIOComponent.lockLeftArrow(inputComponent, position);
        synchronized (this) {
            try {
                this.wait();
                input = inputComponent.getText().substring(position, inputComponent.getText().length());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        JTerminalIOComponent.unlockLeftArrow(inputComponent);
        newLine();
        return input.trim();
    }

    /**
     * Waits for and then reads input from user.
     * @return <code>String</code> input from user
     */
    public String next(){
        return nextInput();
    }

    /**
     * Waits for and then reads input from user.
     * @return input from user as an <code>int</code>
     * @throws NumberFormatException if input cannot be parsed as an <code>int</code>
     */
    public int nextInt() throws NumberFormatException{
        return Integer.parseInt(this.nextInput());
    }

    /**
     * Waits for and then reads input from user.
     * @return input from user as a <code>double</code>
     * @throws NumberFormatException if input cannot be parsed as a <code>double</code>
     */
    public double nextDouble() throws NumberFormatException{
        return Double.parseDouble(this.nextInput());
    }

    /**
     * Waits for and then reads input from user.
     * @return input from user as a <code>boolear</code>
     */
    public boolean nextBoolean(){
        return Boolean.parseBoolean(this.nextInput());
    }

    /**
     * Displays a prompt, then waits for and reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as a <code>String</code>
     */
    private synchronized String query(String queryPrompt) {
        String input = "";
        inputComponent.setPrompt(queryPrompt);
        inputComponent.advance();
        inputComponent.setQuerying(true);
        synchronized (this) {
            try {
                this.wait();
                input = inputComponent.getInput().trim();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        inputComponent.resetPrompt();
        newLine();
        return input;
    }

    /**
     * Displays a prompt, then waits for and reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as a <code>String</code>
     */
    public String queryString(String queryPrompt) {
        while (true) {
            String input = query(queryPrompt);
            if (!input.isEmpty()) {
                return input;
            } else {
                out.println(Strings.ERROR_EMPTY);
            }
        }
    }

    /**
     * Displays a prompt, then waits for and reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as an <code>int</code>
     */
    public int queryInteger(String queryPrompt) {
        while (true) {
            try {
                return Integer.parseInt(query(queryPrompt));
            } catch (NumberFormatException e) {
                out.println(Strings.ERROR_INTEGER);
            }
        }
    }

    /**
     * Displays a prompt, then waits for and reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as a <code>double</code>
     */
    public double queryDouble(String queryPrompt) {
        while (true) {
            try {
                return Double.parseDouble(query(queryPrompt));
            } catch (NumberFormatException e) {
                out.println(Strings.ERROR_DOUBLE);
            }
        }
    }

    /**
     * Displays a prompt, then waits for and reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return input from user as a <code>boolean</code>
     */
    public boolean queryBoolean(String queryPrompt){
        return Boolean.parseBoolean(query(queryPrompt));
    }

    /**
     * Displays a prompt, then waits for and reads input from the user.
     * @param queryPrompt the text to be displayed
     * @return true if input is "y" or "yes" (non-case-sensitive)
     */
    public boolean queryYN(String queryPrompt) {
        switch (query(queryPrompt).toLowerCase()) {
            case Strings.MATCH_YES:
            case Strings.MATCH_YES_SHORT:
                return true;
            default:
                return false;
        }
    }

    public void newLine() {
        inputComponent.newLine();
    }

    public void clear(){
        outputComponent.clear();
    }

    public void clearBuffer(){
        this.tokenBuffer.clear();
    }

    /**
     * Maps default commands.
     */
    private void addDefaultCommands() {
        commandMap.put("", ()->{});
        commandMap.put(Strings.COMMAND_CLEAR, this::clear);
    }

    /**
     * Un-maps default commands
     */
    public void removeDefaultCommands() {
        commandMap.remove("", commandMap.get(""));
        commandMap.remove(Strings.COMMAND_CLEAR, commandMap.get(Strings.COMMAND_CLEAR));
    }

    /**
     * Method fired after receiving a <code>SubmitEvent</code>.
     * Notifies that input has been recieved, then puts the input into the command queue and command history.
     * @param e event recieved
     */
    @Override
    public synchronized void submitActionPerformed(SubmitEvent e) {
        this.notifyAll();
        try {
            commandQueue.put(e.getActionCommand());
            inputComponent.updateHistory(e.getActionCommand());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method fired after recieving a <code>QueryEvent</code>.
     * Notifies that input has been recieved as response to a query.
     * @param e QueryEvent recieved
     */
    @Override
    public synchronized void queryActionPerformed(QueryEvent e) {
        this.notifyAll();
    }

    /**
     * Maps a command.
     * @param key the command name
     * @param commandAction the command to be mapped
     * @param aliases alternate command names
     */
    public void putCommand(String key, CommandAction commandAction, String...aliases) {
        commandMap.put(key, commandAction);
        for(String a:aliases){
            commandMap.put(a, commandAction);
        }
    }

    public void replaceCommand(String key, CommandAction commandAction) { commandMap.replace(key, commandAction); }

    public void removeCommand(String key, CommandAction commandAction) { commandMap.remove(key, commandAction); }

    public CommandAction getCommand(String key) { return this.commandMap.get(key); }

    public CommandMap getCommandMap() { return commandMap; }

    public CommandExecutor getCommandExecutor(){ return commandExecutor; }

    public void setCommandExecutor(CommandExecutor commandExecutor) { this.commandExecutor = commandExecutor; }

    public CommandTokenizer getCommandTokenizer(){ return commandTokenizer; }

    public void setCommandTokenizer(CommandTokenizer commandTokenizer){ this.commandTokenizer = commandTokenizer; }

    public ExceptionHandler getExceptionHandler() { return exceptionHandler; }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) { this.exceptionHandler = exceptionHandler; }

    public StartBehavior getStartBehavior() { return startBehavior; }

    public void setStartBehavior(StartBehavior startBehavior) { this.startBehavior = startBehavior; }

    public CloseBehavior getCloseBehavior() { return closeBehavior; }

    public void setCloseBehavior(CloseBehavior closeBehavior) { this.closeBehavior = closeBehavior; }

    public JTerminalIOComponent getInputComponent() { return inputComponent; }

    public JTerminalIOComponent getOutputComponent() { return outputComponent; }

    public JFrame getFrame() { return frame;}

    public JScrollPane getScrollPane(){ return scrollPane; }

    public IOContainerPanel getTextPanel() {
        return textPanel;
    }

    public String getDefaultPrompt(){ return inputComponent.getDefaultPrompt(); }

    public void setDefaultPrompt(String prompt) { inputComponent.setDefaultPrompt(prompt); }

    public String getPrompt(String prompt) { return inputComponent.getPrompt(); }

    public void setPrompt(String prompt) { inputComponent.setPrompt(prompt); }

    public void resetPrompt() { inputComponent.resetPrompt(); }

    public LinkedList<String> getTokenBuffer() { return tokenBuffer; }

    public int getFontSize() { return outputComponent.getFontSize(); }

    public void setFontSize(int fontSize) {
        this.inputComponent.setFontSize(fontSize);
        if(inputComponent!=outputComponent) this.outputComponent.setFontSize(fontSize);
    }

    public JPanel getScrollPaneView() {
        return scrollPaneView;
    }

    public Theme getTheme(){ return theme; }

    public void setTheme(Theme theme){
        this.theme = theme;
        this.applyTheme(this.theme);
    }

    public void setAppIcon(String pathToIcon){
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(pathToIcon));
    }

    @Override
    public void applyTheme(Theme theme) {
        scrollPane.setBorder(BorderFactory.createLineBorder(theme.backgroundColor,8));
        scrollPaneView.setBackground(theme.backgroundColor);
        textPanel.setBackground(theme.backgroundColor);
        inputComponent.applyTheme(theme);
        outputComponent.applyTheme(theme);
        //frame.repaint();
    }
}