package app;

import character.PlayerCharacter;
import utils.Help;
import utils.Message;

public class CommandHandler{

    void doCommand(String command, PlayerCharacter activeChar){
        switch(command){
            case "n":
            case "new":
                App.createCharacter();
                break;
            case "import":
                App.importCharacter();
                break;
            case "importall":
                App.importAll(true);
                break;
            case "list":
                App.dispCharacterList();
                break;
            case "load":
                App.loadChar();
                break;
            case "roll":
                App.roll();
                break;
            case "q":
            case "quit":
                if(App.getYN("Are you sure? Unsaved data will be lost ")){
                    App.QUIT_ALL = true;
                }
                break;
            case "prefs":
                App.prefs();
                break;
            case "help":
                App.tokens.pop();
                if(!App.tokens.isEmpty()){
                    Help.helpMenu(App.tokens.pop());
                } else {
                    System.out.println("HELP :D");
                }
                break;
            default:
                if(activeChar!=null){
                    switch (command){
                        case "save":
                            App.saveChar(activeChar);
                            break;
                        case "v":
                        case "view":
                            System.out.println(activeChar);
                            System.out.println(activeChar.getInventory());
                            break;
                        case "stats":
                            System.out.println(activeChar);
                            break;
                        case "equip":
                        case "dequip":
                            App.equip();
                            break;
                        case "spell":
                        case "spells":
                            App.spells();
                            break;
                        case "spellbook":
                            System.out.println(activeChar.getSpellBook());
                            break;
                        case "cast":
                            App.cast();
                            break;
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
                        default:
                            System.out.println(Message.ERROR_NO_COMMAND);
                            break;
                    }
                } else {
                    System.out.println(Message.ERROR_NO_LOAD);
                }
                break;
        }
    }
}
