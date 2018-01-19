package app.ui.useraction;

import app.CharacterCommand;
import app.ui.CCExtensions;
import core.items.Item;
import core.constants.EquipConstants;
import jterminal.core.IllegalTokenException;

public class EquipAction {
    public static void equip(CharacterCommand cc){
        String key = "";
        Item item =null;
        if(!cc.terminal.hasTokens()){
            key = cc.terminal.queryString("Item name: ");
            item = cc.getActiveCharacter().getInventory().getItem(key);
        } else {
            while (cc.terminal.hasTokens()) {
                try{
                    if(cc.terminal.peekToken().startsWith("\"")){
                        key = CCExtensions.buildNameFromTokens(cc.terminal);
                        if(key == null) throw new IllegalTokenException(key);
                        item = cc.getActiveCharacter().getInventory().getItem(key);
                    }
                } catch (IllegalTokenException e){
                    if(e.getToken() == null){
                        cc.terminal.out.println("ERROR: No item name specified");
                    } else {
                        cc.terminal.out.println("ERROR: Illegal token: \"" + e.getToken()+"\"");
                    }
                }
            }
        }
        if(item!=null){
            cc.terminal.out.println(equipResultMessage(cc.getActiveCharacter().equipBehavior.equip(item), item));
        } else {
            cc.terminal.out.println("Item \""+key+"\" not found in inventory");
        }
    }

    public static void dequip(CharacterCommand cc){
        String key = "";
        Item item =null;
        if(!cc.terminal.hasTokens()){
            key = cc.terminal.queryString("Item name: ");
            item = cc.getActiveCharacter().getInventory().getItem(key);
        } else {
            while (cc.terminal.hasTokens()) {
                try{
                    if(cc.terminal.peekToken().startsWith("\"")){
                        key = CCExtensions.buildNameFromTokens(cc.terminal);
                        if(key == null) throw new IllegalTokenException(key);
                        item = cc.getActiveCharacter().getInventory().getItem(key);
                    }
                } catch (IllegalTokenException e){
                    if(e.getToken() == null){
                        cc.terminal.out.println("ERROR: No item name specified");
                    } else {
                        cc.terminal.out.println("ERROR: Illegal token: \"" + e.getToken()+"\"");
                    }
                }
            }
        }
        if(item!=null){
            cc.terminal.out.println(dequipResultMessage(cc.getActiveCharacter().equipBehavior.dequip(item), item));
        } else {
            cc.terminal.out.println("Item \""+key+"\" not found in inventory");
        }
    }

    public static String dequipResultMessage(int equipResult, Item item){
        switch (equipResult){
            case EquipConstants.SUCCESSFUL_DEQUIP:
                return "\""+item.getItemName()+"\" dequipped";
            case EquipConstants.NOT_EQUIPPABLE:
                return "Not an equippable item";
            case EquipConstants.ALREADY_EQUIPPED:
                return "\""+item.getItemName()+"\" not equipped";
            default:
                return "ERROR";
        }
    }

    public static String equipResultMessage(int equipResult, Item item){
        switch (equipResult){
            case EquipConstants.SUCCESSFUL_DEQUIP:
                return "\""+item.getItemName()+"\" equipped";
            case EquipConstants.NOT_EQUIPPABLE:
                return "Not an equippable item";
            case EquipConstants.ALREADY_EQUIPPED:
                return "\""+item.getItemName()+"\" already equipped";
            default:
                return "ERROR";
        }
    }

}
