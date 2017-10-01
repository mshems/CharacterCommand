package terminal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TerminalKeylistener implements KeyListener {
    private TerminalInputComponent inputComponent;
    private static final int SUBMIT_EVENT_ID = 1;

    public TerminalKeylistener(TerminalInputComponent inputComponent){
        this.inputComponent = inputComponent;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(inputComponent.getCaretPosition() < inputComponent.getPrompt().length() && e.getKeyCode() != KeyEvent.VK_RIGHT){
            inputComponent.advanceCaret();
        }
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            if(inputComponent.getCaretPosition() <= inputComponent.getLastPromptPos()){
                inputComponent.disableBackSpace();
            } else {
                if(!inputComponent.isAllowBackSpace()){
                    inputComponent.enableBackSpace();
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(inputComponent.isQuerying()){
                //inputComponent.getEventDispatcher().fireEvent(new terminal.QueryEvent(inputComponent, SUBMIT_EVENT_ID, "submit-event", inputComponent.getCommand()));
                inputComponent.fireEvent(new QueryEvent(inputComponent, SUBMIT_EVENT_ID, "submit-event", inputComponent.getCommand()));
                inputComponent.setQuerying(false);
            } else {
                //inputComponent.getEventDispatcher().fireEvent(new terminal.SubmitEvent(inputComponent, SUBMIT_EVENT_ID, "submit-event", inputComponent.getCommand()));
                inputComponent.fireEvent(new SubmitEvent(inputComponent, SUBMIT_EVENT_ID, "submit-event", inputComponent.getCommand()));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
