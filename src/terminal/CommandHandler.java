package terminal;

import character.*;
import app.CharacterCommand;
import app.DiceRoll;
import utils.Help;
import utils.Message;

import java.util.Collections;

public class CommandHandler {
    private Terminal terminal;

    public CommandHandler(Terminal term){
        this.terminal = term;
    }

    public void processCommand(String command){
        String[] input = command
                .trim()
                .split("\\s+");
        Collections.addAll(CharacterCommand.tokens, input);
        this.doCommand(CharacterCommand.tokens.peek(), CharacterCommand.activeChar);
    }

    void doCommand(String command, PlayerCharacter activeChar){
        switch(command){
            case "n":
            case "new":
                PlayerCreator.createCharacter();
                break;
            case "import":
                CharacterCommand.ioHandler.importCharacter();
                break;
            case "list":
                CharacterCommand.dispCharacterList();
                break;
            case "load":
                CharacterCommand.ioHandler.loadChar();
                break;
            case "roll":
                DiceRoll.doRoll();
                break;
            case "prefs":
                CharacterCommand.propertiesHandler.prefs();
                break;
            case "h":
            case "help":
                CharacterCommand.tokens.pop();
                if(!CharacterCommand.tokens.isEmpty()){
                    Help.helpMenu(terminal, CharacterCommand.tokens.pop());
                } else {
                    terminal.printOut(Help.COMMANDS_LIST);
                }
                break;
            case "q":
            case "quit":
                if(terminal.queryYN("Are you sure? Unsaved data will be lost [Y/N] : ")){
                    CharacterCommand.closeApp();
                    System.exit(0);
                }
                break;
            default:
                if(activeChar!=null){
                    switch (command){
                        case "s":
                        case "save":
                            CharacterCommand.ioHandler.saveChar();
                            break;
                        case "export":
                            CharacterCommand.ioHandler.export();
                            break;
                        case "v":
                        case "view":
                            terminal.printOut(activeChar.toString());
                            break;
                        //case "set":
                        case "edit":
                            CharacterCommand.edit();
                            break;
                        case "stat":
                        case "stats":
                            CharacterCommand.stats();
                            break;
                        case "equip":
                        case "dequip":
                            CharacterCommand.equip();
                            break;
                        case "use":
                            CharacterCommand.use();
                            break;
                        case "ap":
                            CharacterCommand.abilityPoints();
                            break;
                        case "ss":
                        case "spellslot":
                        case "spellslots":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                CharacterCommand.spellSlots();
                            }
                            break;
                        case "charge":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                CharacterCommand.charge();
                            }
                            break;
                        case "spell":
                        case "spells":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                CharacterCommand.spells();
                            }
                            break;
                        case "learn":
                            CharacterCommand.learn();
                            break;
                        case "forget":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                CharacterCommand.forget();
                            }
                            break;
                        case "spellbook":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                terminal.printOut(activeChar.getSpellBook().toString());
                            }
                            break;
                        case "cast":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                CharacterCommand.cast();
                            }
                            break;
                        case "lvl":
                        case "levelup":
                            PlayerLeveler.levelUp(CharacterCommand.getActiveChar());
                            break;
                        case "skill":
                        case "skills":
                            CharacterCommand.skills();
                            break;
                        case "i":
                        case "inv":
                            terminal.printOut(activeChar.getInventory().toString());
                            break;
                        case "get":
                            CharacterCommand.get();
                            break;
                        case "add":
                        case "drop":
                            CharacterCommand.addDrop();
                            break;
                        case "heal":
                        case "hurt":
                            PlayerHealer.heal(CharacterCommand.getActiveChar());
                            break;
                        case "sq":
                            CharacterCommand.ioHandler.saveChar();
                            CharacterCommand.closeApp();
                            System.exit(0);
                            break;
                        default:
                            Message.errorMessage(terminal, Message.ERROR_NO_COMMAND);
                            break;
                    }
                } else {
                    Message.errorMessage(terminal, Message.ERROR_NO_LOAD);
                }
                break;
        }
        CharacterCommand.tokens.clear();
        terminal.advance();
    }
}
