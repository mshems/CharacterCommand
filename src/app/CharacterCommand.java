package app;

import app.ui.terminal.core.*;
import app.ui.terminal.core.behavior.*;
import app.ui.useraction.NewCharacterAction;
import core.character.PlayerCharacter;
import app.ui.useraction.HealthAction;
import app.ui.useraction.InventoryAction;
import app.ui.terminal.optional.properties.*;

import java.util.HashMap;

public class CharacterCommand {
    public JTerminal terminal;
    private PlayerCharacter activeCharacter;
    private HashMap<String, PlayerCharacter> loadedCharacters = new HashMap<>();

    public CharacterCommand(){
        initTerminal();
        addCommands();
    }

    public synchronized void start(){
        terminal.start();
    }

    private void initTerminal(){
        this.terminal = new JTerminal("CharacterCommand");
        terminal.setDefaultPrompt("CharacterCommand ~ ");
        PropertiesManager.addPropertiesManager(terminal);

        terminal.setStartBehavior((terminal)->{
            if(activeCharacter!=null){
                terminal.setPrompt(activeCharacter.getName()+" @ CharacterCommand ~ ");
            } else {
                terminal.resetPrompt();
            }
            try{
                int fontSize = Integer.parseInt(PropertiesManager.getProperty("font-size"));
                terminal.setFontSize(fontSize);
            } catch (NumberFormatException e){
                terminal.setFontSize(16);
            }
        });


        terminal.setCommandExecutor(new CommandExecutor(terminal){
            @Override
            public void doCommand(String token) throws UnknownCommandException{
                if (activeCharacter != null){
                    super.doCommand(token);
                }
                if(activeCharacter!=null){
                    terminal.setPrompt(activeCharacter.getName()+" @ CharacterCommand ~ ");
                } else {
                    terminal.resetPrompt();
                }
            }
        });
    }

    private void addCommands(){
        terminal.addDefaultCommands();
        terminal.putCommand("new",  ()->{
            PlayerCharacter pc = NewCharacterAction.newCharacter(this);
            activeCharacter = loadedCharacters.get(pc.getName().toLowerCase());
        });
        terminal.putCommand("heal", ()-> HealthAction.heal(this));
        terminal.putCommand("hurt", ()-> HealthAction.hurt(this));
        terminal.putCommand("add",  ()->InventoryAction.add(this));
        terminal.putCommand("get",  ()->InventoryAction.add(this));
        terminal.putCommand("view", ()->terminal.out.println(activeCharacter),"v");
        terminal.putCommand("inv",  ()->InventoryAction.viewInventory(this), "i");
        terminal.putCommand("I",    ()->InventoryAction.viewInventoryMenu(this));
        terminal.putCommand("quit", ()->{
                if(terminal.hasTokens()){
                    String token = terminal.nextToken();
                    if(token.equalsIgnoreCase("-f") || token.equalsIgnoreCase("--force")){
                        terminal.close();
                        System.exit(0);
                    }
                } else if(terminal.queryYN("Are you sure? [Y/N] : ")) {
                    terminal.close();
                    System.exit(0);
                }
            }, "q");
    }

    public void loadCharacter(PlayerCharacter pc){
        loadedCharacters.put(pc.getName().toLowerCase(), pc);
    }

    public PlayerCharacter getActiveCharacter() {
        return activeCharacter;
    }

    public void setActiveCharacter(PlayerCharacter activeCharacter) {
        this.activeCharacter = activeCharacter;
    }
}
