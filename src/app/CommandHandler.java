package app;

import character.PlayerCharacter;
import utils.Help;
import utils.Message;

class CommandHandler{

    void doCommand(String command, PlayerCharacter activeChar){
        switch(command){
            case "n":
            case "new":
                App.createCharacter();
                break;
            case "import":
                App.ioHandler.importCharacter();
                break;
            case "list":
                App.dispCharacterList();
                break;
            case "load":
                App.ioHandler.loadChar();
                break;
            case "roll":
                DiceRoll.doRoll();
                break;
            case "prefs":
                App.propertiesHandler.prefs();
                break;
            case "h":
            case "help":
                App.tokens.pop();
                if(!App.tokens.isEmpty()){
                    Help.helpMenu(App.tokens.pop());
                } else {
                    System.out.println(Help.COMMANDS_LIST);
                }
                break;
            case "q":
            case "quit":
                if(App.getYN("Are you sure? Unsaved data will be lost ")){
                    App.QUIT_ALL = true;
                    App.scanner.close();
                }
                break;
            default:
                if(activeChar!=null){
                    switch (command){
                        case "s":
                        case "save":
                            App.ioHandler.saveChar();
                            break;
                        case "export":
                            App.ioHandler.export();
                            break;
                        case "v":
                        case "view":
                            System.out.println(activeChar);
                            break;
                        //case "set":
                        case "edit":
                            App.edit();
                            break;
                        case "stat":
                        case "stats":
                            App.stats();
                            break;
                        case "equip":
                        case "dequip":
                            App.equip();
                            break;
                        case "use":
                            App.use();
                            break;
                        case "ap":
                            App.abilityPoints();
                            break;
                        case "ss":
                        case "spellslot":
                        case "spellslots":
                            if(App.checkCaster(App.activeChar)){
                                App.spellSlots();
                            }
                            break;
                        case "charge":
                            if(App.checkCaster(App.activeChar)){
                                App.charge();
                            }
                            break;
                        case "spell":
                        case "spells":
                            if(App.checkCaster(App.activeChar)){
                                App.spells();
                            }
                            break;
                        case "learn":
                                App.learn();
                            break;
                        case "forget":
                            if(App.checkCaster(App.activeChar)){
                                App.forget();
                            }
                            break;
                        case "spellbook":
                            if(App.checkCaster(App.activeChar)){
                                System.out.println(activeChar.getSpellBook());
                            }
                            break;
                        case "cast":
                            if(App.checkCaster(App.activeChar)){
                                App.cast();
                            }
                            break;
                        case "lvl":
                        case "levelup":
                            App.levelUp();
                            break;
                        case "skill":
                        case "skills":
                            App.skills();
                            break;
                        case "i":
                        case "inv":
                            System.out.println(activeChar.getInventory());
                            break;
                        case "get":
                            App.get();
                            break;
                        case "add":
                        case "drop":
                            App.addDrop();
                            break;
                        case "heal":
                        case "hurt":
                            App.heal();
                            break;
                        case "sq":
                            App.ioHandler.saveChar();
                            App.QUIT_ALL = true;
                            App.scanner.close();
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
        App.tokens.clear();
    }
}
