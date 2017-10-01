/**
 * Creator: Matthew Shems
 *
 */

package terminal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

public class TerminalInputComponent extends JTextArea{
    private TerminalEventListener listener;
    private boolean allowBackSpace;
    private boolean multiline;
    private boolean querying;
    private int lastPromptPos;

    private String prompt;
    private static final String USER_NAME = System.getProperty("user.name");
    private static final String DEFAULT_PROMPT = USER_NAME+"@terminal > ";
    private LinkedList<String> history;
    private int historyPointer = 0;


    public TerminalInputComponent(){
        this.setMargin(new Insets(5,5,5,5));
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setFont(new Font("MONOSPACED", Font.PLAIN, 18));
        this.setPrompt(DEFAULT_PROMPT);
        this.setMultiline(true);
        this.remapEnterKey();
        this.remapArrows();
        this.history = new LinkedList<>();
        this.addKeyListener(new TerminalKeylistener(this));
        this.setQuerying(false);
        this.setAllowBackSpace(false);
        //this.eventDispatcher = new terminal.TerminalEventDispatcher(this);
    }

    void start(){
        this.setEditable(true);
        this.println("-------- Terminal --------");
        this.prompt();
        this.advanceCaret();
    }

    public void append(String s){
        this.setText(this.getText()+s);
    }

    void print(String s){
        this.append(s);
        //this.advanceCaret();
    }

    void println(String s){
        this.append(s+System.lineSeparator());
        //this.advanceCaret();
    }

    void newLine(){
        if(multiline) {
            this.append(System.lineSeparator());
        } else {
            this.clear();
        }
    }

    void advance(){
        if(multiline) {
            this.appendPrompt();
        } else {
            this.prompt();
        }
        this.advanceCaret();
    }

    void advanceCaret(){
        this.lastPromptPos = getText().lastIndexOf(getPrompt())+getPrompt().length();
        this.setCaretPosition(lastPromptPos);
    }

    public String getCommand(){
        return this.getText().substring(lastPromptPos);
    }

    private void prompt(){
        this.setText(getPrompt());
    }

    private void appendPrompt(){
        this.append("\n" + getPrompt());
        this.advanceCaret();
    }

    void clear(){
        this.setText("");
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

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt){
        this.prompt = prompt;
    }

    public void resetPrompt(){
        this.prompt = DEFAULT_PROMPT;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public int getLastPromptPos() {
        return lastPromptPos;
    }

    public void setLastPromptPos(int lastPromptPos) {
        this.lastPromptPos = lastPromptPos;
    }

    public boolean isAllowBackSpace() {
        return allowBackSpace;
    }

    public void setAllowBackSpace(boolean allowBackSpace) {
        this.allowBackSpace = allowBackSpace;
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
