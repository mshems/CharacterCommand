package jterminal.core;

import jterminal.core.event.QueryEvent;
import jterminal.core.event.SubmitEvent;
import jterminal.core.theme.Theme;
import jterminal.core.theme.ThemedComponent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class JTerminalIOComponent extends JTextArea implements ThemedComponent {
    private JTerminal listener;
    private boolean allowBackSpace;
    private final boolean multiline;
    private boolean querying;
    private int lastPromptPos;
    private String prompt;
    private String defaultPrompt;
    private LinkedList<String> history;
    private int historyPointer = 0;
    private static final int MAX_LINES = 256;
    private static final int MAX_HISTORY_SIZE = 64;
    private int fontSize = 17;

    private static final String USER_NAME = System.getProperty("user.name");
    private static final String DEFAULT_PROMPT = USER_NAME+" ~ ";

    JTerminalIOComponent(JTerminal terminal, boolean multi){
        this.addKeyListener(new JTerminalKeylistener(this));
        this.remapEnterKey();
        this.remapArrows();
        this.setLineWrap(true);
        listener = terminal;
        history = new LinkedList<>();
        multiline = multi;
        defaultPrompt = DEFAULT_PROMPT;
        prompt = defaultPrompt;
        querying = false;
        allowBackSpace = false;
        this.applyTheme(listener.getTheme());
        this.setMargin(new Insets(4,4,4,4));
    }

    void start(){
        this.setSize(getPreferredSize());
        this.setEditable(true);
        this.prompt();
        this.advanceCaret();
    }

    String getInput(){
        return this.getText().substring(lastPromptPos);
    }

    private boolean isOnNewLine(){
        return (this.getText().endsWith(System.lineSeparator()) || this.getText().endsWith("\n"));
    }

    void newLine(){
        if(multiline) {
            this.append(System.lineSeparator());
        } else {
            this.clear();
        }
    }

    void advance(){
        if(this.getLineCount()>=MAX_LINES){
            int linesToRemove = this.getLineCount()-MAX_LINES;
            try {
                this.replaceRange("", this.getLineStartOffset(0), this.getLineEndOffset(linesToRemove));
            } catch (BadLocationException e){
                e.printStackTrace();
            }
        }
        this.prompt();
        this.advanceCaret();
    }

    private void prompt(){
        if(multiline){
            if(this.isOnNewLine() || this.isClear()){
                this.append(prompt);
            } else {
                this.append(System.lineSeparator() + prompt);
            }
        } else {
            this.setText(prompt);
        }
    }

    private void advanceCaret(){
        this.lastPromptPos = getText().lastIndexOf(prompt) + prompt.length();
        this.setCaretPosition(lastPromptPos);
    }

    void clear(){
        this.setText("");
    }

    private boolean isClear(){
        return this.getText().isEmpty();
    }

    void print(String s){
        this.append(s);
    }

    void println(String s){
        this.append(s+System.lineSeparator());
    }

    void printCentered(String str){
        int width = ((this.getWidth()/this.getColumnWidth())-str.length());
        for(int i=0; i<(width-1)/2; i++){
            str = " "+str+" ";
        }
        this.println(str);
    }

    void printRightAligned(String str){
        int width = ((this.getWidth()/this.getColumnWidth())-str.length());
        for(int i=0; i<width-1; i++){
            str = " "+str;
        }
        this.println(str);
    }

    void updateHistory(String command){
        if(history.size() >= MAX_HISTORY_SIZE){
            history.removeLast();
        }
        if(history.isEmpty()){
            history.addFirst(command);
        } else if(!history.getFirst().equals(command) && !command.isEmpty()) {
            history.addFirst(command);
        }
        historyPointer = 0;
    }

    void fireEvent(SubmitEvent e){
        if(listener!=null){
            listener.submitActionPerformed(e);
        }
    }

    void fireEvent(QueryEvent e){
        if(listener!=null){
            listener.queryActionPerformed(e);
        }
    }

    void disableBackSpace(){
        allowBackSpace = false;
        this.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
    }

    void enableBackSpace(){
        allowBackSpace = true;
        this.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "delete-previous");
    }

    private void remapEnterKey(){
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)), "");
    }

    /*public void unmapArrows(){
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)), "");
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)), "");
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)), "");
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)), "");
    }*/

    private void remapArrows(){
        //LEFT ARROW
        this.getActionMap().put("leftArrowAction", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(getCaretPosition() > lastPromptPos){
                    setCaretPosition(getCaretPosition()-1);
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)), "leftArrowAction");

        //RIGHT ARROW
        this.getActionMap().put("rightArrowAction", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(getCaretPosition() < getText().length()){
                    setCaretPosition(getCaretPosition()+1);
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)), "rightArrowAction");


        //UP ARROW
        this.getActionMap().put("upArrowAction", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){

                if(!history.isEmpty() && historyPointer < history.size()) {
                    historyPointer++;
                    setText(getText().substring(0, lastPromptPos) + history.get(historyPointer - 1));
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)), "upArrowAction");

        //DOWN ARROW
        this.getActionMap().put("downArrowAction", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(historyPointer > 1 ){
                    historyPointer--;
                    setText(getText().substring(0,lastPromptPos)+history.get(historyPointer-1));
                } else if(historyPointer == 1){
                    setText(getText().substring(0,lastPromptPos)+"");
                    historyPointer =0;
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)), "downArrowAction");
    }

    private void remapLeftArrow(){
        this.getActionMap().put("leftArrowAction", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(getCaretPosition() > lastPromptPos){
                    setCaretPosition(getCaretPosition()-1);
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)), "leftArrowAction");
    }

    static void lockLeftArrow(JTerminalIOComponent io, int position){
        io.getActionMap().put("locked-left-arrow", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(io.getCaretPosition() > position){
                    io.setCaretPosition(io.getCaretPosition()-1);
                }
            }
        });
        io.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)), "locked-left-arrow");
    }

    static void unlockLeftArrow(JTerminalIOComponent io){
        io.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        io.getActionMap().remove("locked-left-arrow");
        io.remapLeftArrow();
    }

    int getFontSize() {
        return fontSize;
    }

    void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        Font f = listener.getTheme().font;
        this.setFont(new Font(f.getName(), f.getStyle(), fontSize));
    }

    String getPrompt() {
        return prompt;
    }

    void setPrompt(String currPrompt){
        this.prompt = currPrompt;
    }

    void resetPrompt(){
        this.prompt = defaultPrompt;
    }

    String getDefaultPrompt() {
        return defaultPrompt;
    }

    void setDefaultPrompt(String prompt){
        this.defaultPrompt = prompt;
    }

    int getLastPromptPos() {
        return lastPromptPos;
    }

    boolean allowsBackspace() {
        return allowBackSpace;
    }

    boolean isQuerying() {
        return querying;
    }

    void setQuerying(boolean querying) {
        this.querying = querying;
    }

    void setTerminalEventListener(JTerminal listener){
        this.listener = listener;
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(listener.getScrollPaneView().getWidth()-4, super.getPreferredSize().height);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(listener.getScrollPaneView().getWidth(), super.getPreferredSize().height);
    }

    @Override
    public void applyTheme(Theme theme) {
        this.setBackground(theme.backgroundColor);
        this.setForeground(theme.foregroundColor);
        this.setCaretColor(theme.caretColor);
        Font f = theme.font;
        this.setFont(new Font(f.getName(), f.getStyle(), fontSize));
    }
}
