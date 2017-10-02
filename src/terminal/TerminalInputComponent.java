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

    private String currPrompt;
    private static final String USER_NAME = System.getProperty("user.name");
    private static final String DEFAULT_PROMPT = "CharacterCommand ~ "; //USER_NAME+"@terminal ~ ";
    private LinkedList<String> history;
    private int historyPointer = 0;


    public TerminalInputComponent(boolean multiline){
        this.history = new LinkedList<>();
        this.addKeyListener(new TerminalKeylistener(this));
        this.remapEnterKey();
        this.remapArrows();

        this.setMargin(new Insets(5,5,5,5));
        this.setBackground(new Color(33,33,33));
        this.setForeground(new Color(245,245,245));
        this.setCaretColor(new Color(245,245,245));
        this.setFont(new Font("consolas", Font.PLAIN, 18));

        this.multiline = multiline;
        this.currPrompt = DEFAULT_PROMPT;
        this.querying = false;
        this.allowBackSpace = false;
    }

    void start(){
        this.setEditable(true);
        this.prompt();
        this.advanceCaret();
    }

    public String getCommand(){
        return this.getText().substring(lastPromptPos);
    }

    private boolean isOnNewLine(){
        return this.getText().endsWith(System.lineSeparator());
    }

    void newLine(){
        if(multiline) {
            this.append(System.lineSeparator());
        } else {
            this.clear();
        }
    }

    void advance(){
        this.prompt();
        this.advanceCaret();
    }

    private void prompt(){
        if(multiline){
            if(this.isOnNewLine() || this.isClear()){
                this.append(getCurrPrompt());
            } else {
                this.append(System.lineSeparator() + getCurrPrompt());
            }
        } else {
            this.setText(getCurrPrompt());
        }
    }

    void advanceCaret(){
        this.lastPromptPos = getText().lastIndexOf(getCurrPrompt()) + getCurrPrompt().length();
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

    public String getCurrPrompt() {
        return currPrompt;
    }

    public void setCurrPrompt(String currPrompt){
        this.currPrompt = currPrompt;
    }

    public void resetPrompt(){
        this.currPrompt = DEFAULT_PROMPT;
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
