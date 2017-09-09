package emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class CTTextArea extends JTextArea{
    private String prompt = " test@cterm > ";
    CTerm parent;
    private boolean allowBackSpace = true;
    private boolean singleLine;
    private int lastPromptPos;
    private LinkedList<String>history;
    private int historyPointer = 0;

    CTTextArea(CTerm parent, boolean singleLine){
        this.singleLine = singleLine;
        this.parent = parent;
        this.history = new LinkedList<>();

        this.remapEnterKey();
        this.remapArrows();

        this.addKeyListener(new TextAreaKeyListener());

        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);


    }

    public void start(){
        this.setEditable(true);
        this.setText(prompt);
        //this.setText(getText()+"\n"+prompt);
        this.advanceCaret();
    }
    public void setPrompt(String prompt){
        this.prompt = prompt;
    }

    private String getCommand(){
        return this.getText().substring(lastPromptPos);
    }

    private void advanceCaret(){
        this.lastPromptPos = getText().lastIndexOf(prompt)+prompt.length();
        this.setCaretPosition(lastPromptPos);
    }

    private void clear(){
        this.setText(prompt);
    }

    void print(String text){
        append(text);
        //setText(getText()+text);
    }

    void println(String text){
        append("\n"+text);
        //setText(getText()+"\n"+text);
    }

    void advance(){
        String command = getCommand();
        updateHistory(command);
        this.parent.processCommand(command);
        if(singleLine){
            clear();
        } else {
            this.append("\n" + prompt);
            //setText(getText()+"\n"+prompt);
        }
        this.advanceCaret();
    }

    private void updateHistory(String command){
        if(history.size()>=25){
            history.removeLast();
        }
        history.addFirst(command);
        historyPointer = 0;
    }

    class TextAreaKeyListener implements KeyListener{
        @Override
        public void keyPressed(KeyEvent e){
            if(getCaretPosition() < prompt.length() && e.getKeyCode() != KeyEvent.VK_RIGHT){
                advanceCaret();
            }
            if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                if(getCaretPosition() <= lastPromptPos){
                    disableBackSpace();
                } else {
                    if(!allowBackSpace){
                        enableBackSpace();
                    }
                }
            }
            /*if(e.getKeyCode() == KeyEvent.VK_LEFT){
                if(getCaretPosition() <= lastPromptPos){
                        disableBackArrow();
                } else {
                    if(!allowBackArrow){
                        enableBackArrow();
                    }
                }
            }*/
        }

        @Override
        public void keyReleased(KeyEvent e){

        }

        @Override
        public void keyTyped(KeyEvent e){

        }
    }

     /*void disableBackArrow(){
        this.allowBackArrow = false;
       this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)), "none");
    }

    void enableBackArrow(){
        this.allowBackArrow = true;
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)), "caret-backward");
    }*/

    private void disableBackSpace(){
        this.allowBackSpace = false;
        this.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
    }

    private void enableBackSpace(){
        this.allowBackSpace = true;
        this.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "delete-previous");
    }

    private void remapEnterKey(){
        this.getActionMap().put("enter-command", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                advance();
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)), "enter-command");
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

                }
            }
        });
        this.getInputMap().put((KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)), "downArrowAction");
    }
}
