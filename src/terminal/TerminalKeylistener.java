package terminal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TerminalKeylistener implements KeyListener {
    private TerminalIOComponent inputComponent;

    public TerminalKeylistener(TerminalIOComponent inputComponent){
        this.inputComponent = inputComponent;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(inputComponent.getCaretPosition() < inputComponent.getLastPromptPos()){
            inputComponent.setCaretPosition(inputComponent.getText().length());
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
                inputComponent.fireEvent(new QueryEvent(inputComponent, 1, "query-event", inputComponent.getInput()));
                inputComponent.setQuerying(false);
            } else {
                inputComponent.fireEvent(new SubmitEvent(inputComponent, 1, "submit-event", inputComponent.getInput()));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
