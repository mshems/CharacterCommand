package core.items;

import jterminal.core.IllegalTokenException;

import java.util.Arrays;
import java.util.List;

public enum ItemType {
    ITEM, CONSUMABLE, EQUIPMENT, ARMOR, WEAPON, SHIELD;

    public static List<ItemType> TypeList(){
        return Arrays.asList(ITEM,CONSUMABLE,EQUIPMENT,ARMOR,WEAPON,SHIELD);
    }

    public static ItemType parseType(String s) throws IllegalTokenException{
        switch(s.toLowerCase()){
            case "i":
            case "item":
                return ITEM;
            case "c":
            case "consumable":
                return CONSUMABLE;
            case "e":
            case "equipment":
                return EQUIPMENT;
            case "a":
            case "armor":
                return ARMOR;
            case "w":
            case "weapon":
                return WEAPON;
            case "s":
            case "shield":
                return SHIELD;
            default:
                throw new IllegalTokenException(s);
        }
    }
}
