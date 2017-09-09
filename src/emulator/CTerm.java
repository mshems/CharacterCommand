package emulator;

import app.App;

public class CTerm{
    public CTWindow window;
    public CTerm(){
        this.window = new CTWindow("CharacterCommand", this);
    }

//    void processCommand(String command){
//        App.commandHandler.doCommand(command, App.activeChar);
//    }

    void setPrompt(String prompt){
        this.window.textArea.setPrompt(prompt);
    }

    void print(String text){
        this.window.textArea.print(text);
    }

    void println(String text){
        this.window.textArea.println(text);
    }

    public void start(){
        this.window.textArea.start();
    }
}
