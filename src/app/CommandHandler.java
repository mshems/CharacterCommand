package app;

import character.PlayerCharacter;
import utils.Help;
import utils.Message;

class CommandHandler{

    /*void doCommand(String command, PlayerCharacter activeChar){
        switch(command){
            case "n":
            case "new":
                PlayerCharacter.createCharacter();
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
                    Help.helpMenu(CharacterCommand.tokens.pop());
                } else {
                    System.out.println(Help.COMMANDS_LIST);
                }
                break;
            case "q":
            case "quit":
                if(CharacterCommand.getYN("Are you sure? Unsaved data will be lost ")){
                    CharacterCommand.QUIT_ALL = true;
                    CharacterCommand.scanner.close();
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
                            System.out.println(activeChar);
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
                            if(CharacterCommand.checkCaster(CharacterCommand.activeChar)){
                                CharacterCommand.spellSlots();
                            }
                            break;
                        case "charge":
                            if(CharacterCommand.checkCaster(CharacterCommand.activeChar)){
                                CharacterCommand.charge();
                            }
                            break;
                        case "spell":
                        case "spells":
                            if(CharacterCommand.checkCaster(CharacterCommand.activeChar)){
                                CharacterCommand.spells();
                            }
                            break;
                        case "learn":
                                CharacterCommand.learn();
                            break;
                        case "forget":
                            if(CharacterCommand.checkCaster(CharacterCommand.activeChar)){
                                CharacterCommand.forget();
                            }
                            break;
                        case "spellbook":
                            if(CharacterCommand.checkCaster(CharacterCommand.activeChar)){
                                System.out.println(activeChar.getSpellBook());
                            }
                            break;
                        case "cast":
                            if(CharacterCommand.checkCaster(CharacterCommand.activeChar)){
                                CharacterCommand.cast();
                            }
                            break;
                        case "lvl":
                        case "levelup":
                            CharacterCommand.levelUp();
                            break;
                        case "skill":
                        case "skills":
                            CharacterCommand.skills();
                            break;
                        case "i":
                        case "inv":
                            System.out.println(activeChar.getInventory());
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
                            CharacterCommand.heal();
                            break;
                        case "sq":
                            CharacterCommand.ioHandler.saveChar();
                            CharacterCommand.QUIT_ALL = true;
                            CharacterCommand.scanner.close();
                            break;
                        default:
                            System.out.println(Message.ERROR_NO_COMMAND);
                            break;
                    }
                } else {
                        System.out.println(Message.ERROR_NO_LOAD);
                }
                break;
        }
        CharacterCommand.tokens.clear();
    }*/
}
