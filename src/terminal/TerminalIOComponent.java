/**
 * Creator: Matthew Shems
 *
 */

package terminal;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class TerminalIOComponent extends JTextArea{
    private TerminalEventListener listener;
    private boolean allowBackSpace;
    private boolean multiline;
    private boolean querying;
    private int lastPromptPos;
    private static final int MAXLINES = 256;
    private TerminalKeylistener terminalKeylistener;

    private String currPrompt;
    private static final String USER_NAME = System.getProperty("user.name");
    private static final String DEFAULT_PROMPT = "TEST ~ ";

    private String defaultPrompt;
    private LinkedList<String> history;
    private int historyPointer = 0;

    public TerminalIOComponent(){}

    public TerminalIOComponent(boolean multiline){
        this.history = new LinkedList<>();
        terminalKeylistener = new TerminalKeylistener(this);
        this.addKeyListener(terminalKeylistener);
        this.remapEnterKey();
        this.remapArrows();

        this.setMargin(new Insets(5,5,5,5));
        this.setBackground(new Color(33,33,33));
        this.setForeground(new Color(245,245,245));
        this.setCaretColor(new Color(245,245,245));
        this.setFont(new Font("consolas", Font.PLAIN, 17));
        this.multiline = multiline;
        this.defaultPrompt = DEFAULT_PROMPT;
        this.currPrompt = defaultPrompt;
        this.querying = false;
        this.allowBackSpace = false;
    }

    void start(){
        this.setEditable(true);
        this.prompt();
        this.advanceCaret();
    }

    public String getInput(){
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
        if(this.getLineCount()>=MAXLINES){
            int linesToRemove = this.getLineCount()-MAXLINES;
            try {
                this.replaceRange("",
                        this.getLineStartOffset(0),
                        this.getLineEndOffset(linesToRemove));
            } catch (BadLocationException e){
                //e.printStackTrace();
            }
        }
        this.prompt();
        this.advanceCaret();
    }

    private void prompt(){
        if(multiline){
            if(this.isOnNewLine() || this.isClear()){
                this.append(currPrompt);
            } else {
                this.append(System.lineSeparator() + currPrompt);
            }
        } else {
            this.setText(currPrompt);
        }
    }

    void advanceCaret(){
        this.lastPromptPos = getText().lastIndexOf(currPrompt) + currPrompt.length();
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
        int width = ((this.getWidth()/this.getColumnWidth())-str.length()-1);
        for(int i=0; i<(width)/2; i++){
            str = " "+str+" ";
        }
        println(str);
    }

    void printRightAligned(String str){
        int width = ((this.getWidth()/this.getColumnWidth())-str.length());
        for(int i=0; i<width-1; i++){
            str = " "+str;
        }
        println(str);
    }

    void updateHistory(String command){
        if(history.size()>=25){
            history.removeLast();
        }
        history.addFirst(command);
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
        this.allowBackSpace = false;
        this.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
    }

    void enableBackSpace(){
        this.allowBackSpace = true;
        this.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "delete-previous");
    }

    private void remapEnterKey(){
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)), "");
    }

    public void remapArrows(){
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
                if(!history.isEmpty() && historyPointer < history.size()){
                    historyPointer++;
                    setText(getText().substring(0,lastPromptPos)+history.get(historyPointer-1));
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
                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)), "downArrowAction");
    }

    public void unmapArrows(){
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)), "");
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0)), "");
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)), "");
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)), "");
    }

    public void removeTerminalKeyListener(){
        this.removeKeyListener(terminalKeylistener);
    }

    public void addTerminalKeyListener(){
        this.addKeyListener(terminalKeylistener);
    }

    public String getCurrPrompt() {
        return currPrompt;
    }

    public void setCurrPrompt(String currPrompt){
        this.currPrompt = currPrompt;
    }

    public void resetPrompt(){
        this.currPrompt = defaultPrompt;
    }

    public String getDefaultPrompt() {
        return defaultPrompt;
    }

    public void setDefaultPrompt(String defaultPrompt) {
        this.defaultPrompt = defaultPrompt;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public int getLastPromptPos() {
        return lastPromptPos;
    }

    public boolean isAllowBackSpace() {
        return allowBackSpace;
    }

    public boolean isQuerying() {
        return querying;
    }

    public void setQuerying(boolean querying) {
        this.querying = querying;
    }

    public void setTerminalEventListener(TerminalEventListener listener){
        this.listener = listener;
    }

    public TerminalEventListener getTerminalEventListener() {
        return this.listener;
    }
}
