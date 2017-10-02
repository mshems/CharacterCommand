package terminal;

import character.*;
import app.CharacterCommand;
import app.DiceRoll;
import items.Item;
import items.ItemIO;
import magic.SpellBookIO;
import magic.SpellIO;
import magic.SpellSlotIO;
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
        this.doCommand(CharacterCommand.tokens.peek(), CharacterCommand.getActiveChar());
    }

    private void doCommand(String command, PlayerCharacter activeChar){
        switch(command){
            case "clear":
                terminal.clear();
                break;
            case "n":
            case "new":
                PlayerCreator.createCharacter();
                break;
            case "import":
                CharacterCommand.readWriteHandler.importCharacter();
                break;
            case "list":
                CharacterCommand.dispCharacterList();
                break;
            case "load":
                CharacterCommand.readWriteHandler.loadChar();
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
                    terminal.println(Help.COMMANDS_LIST);
                }
                break;
            case "q":
            case "quit":
                CharacterCommand.quit();
                break;
            default:
                if(activeChar!=null){
                    switch (command){
                        case "s":
                        case "save":
                            CharacterCommand.readWriteHandler.saveChar(true);
                            break;
                        case "export":
                            CharacterCommand.readWriteHandler.export();
                            break;
                        case "v":
                        case "view":
                            terminal.println(activeChar.toString());
                            break;
                        //case "set":
                        case "edit":
                            StatEditor.edit(CharacterCommand.getActiveChar());
                            break;
                        case "stat":
                        case "stats":
                            StatIO.stats(CharacterCommand.getActiveChar());
                            break;
                        case "equip":
                        case "dequip":
                            ItemIO.equip(CharacterCommand.getActiveChar());
                            break;
                        case "use":
                            ItemIO.use(CharacterCommand.getActiveChar());
                            break;
                        case "ap":
                            AbilityPointsIO.abilityPoints(CharacterCommand.getActiveChar());
                            break;
                        case "ss":
                        case "spellslot":
                        case "spellslots":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                SpellSlotIO.spellSlots(CharacterCommand.getActiveChar());
                            }
                            break;
                        case "charge":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                SpellSlotIO.charge(CharacterCommand.getActiveChar());
                            }
                            break;
                        case "spell":
                        case "spells":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                SpellIO.spells(CharacterCommand.getActiveChar());
                            }
                            break;
                        case "learn":
                            SpellBookIO.learn(CharacterCommand.getActiveChar());
                            break;
                        case "forget":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                SpellBookIO.forget(CharacterCommand.getActiveChar());
                            }
                            break;
                        case "spellbook":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                terminal.println(activeChar.getSpellBook().toString());
                            }
                            break;
                        case "cast":
                            if(CharacterCommand.checkCaster(CharacterCommand.getActiveChar())){
                                SpellIO.cast(CharacterCommand.getActiveChar());
                            }
                            break;
                        case "lvl":
                        case "levelup":
                            PlayerLeveler.levelUp(CharacterCommand.getActiveChar());
                            break;
                        case "skill":
                        case "skills":
                            SkillIO.skills(CharacterCommand.getActiveChar());
                            break;
                        case "i":
                        case "inv":
                            terminal.println(activeChar.getInventory().toString());
                            break;
                        case "get":
                            InventoryIO.get(CharacterCommand.getActiveChar());
                            break;
                        case "add":
                        case "drop":
                            InventoryIO.addDrop(CharacterCommand.getActiveChar());
                            break;
                        case "heal":
                        case "hurt":
                            PlayerHealer.heal(CharacterCommand.getActiveChar());
                            break;
                        case "sq":
                            CharacterCommand.readWriteHandler.saveChar(false);
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
        if(CharacterCommand.hasActiveChar()){
            String charprompt = CharacterCommand.getActiveChar().getName()+" @ CharacterCommand ~ ";
            terminal.setPrompt(charprompt);
        } else {
            terminal.setPrompt("CharacterCommand ~ ");
        }
    }
}
