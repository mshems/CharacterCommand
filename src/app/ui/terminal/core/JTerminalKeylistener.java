package app.ui.terminal.core;

import app.ui.terminal.core.event.QueryEvent;
import app.ui.terminal.core.event.SubmitEvent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JTerminalKeylistener implements KeyListener {
    private final JTerminalIOComponent inputComponent;

    public JTerminalKeylistener(JTerminalIOComponent inputComponent){
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
                if(!inputComponent.allowsBackspace()){
                    inputComponent.enableBackSpace();
                }
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(inputComponent.isQuerying()){
                inputComponent.fireEvent(new QueryEvent(inputComponent));
                inputComponent.setQuerying(false);
            } else {
                inputComponent.fireEvent(new SubmitEvent(inputComponent, inputComponent.getInput()));
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE && inputComponent.isQuerying()){
            inputComponent.fireEvent(new QueryEvent(inputComponent, true));
            inputComponent.setQuerying(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
