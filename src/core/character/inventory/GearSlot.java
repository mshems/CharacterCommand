package core.character.inventory;

import jterminal.core.IllegalTokenException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public enum GearSlot implements Serializable{
    HEAD, LEGS, BODY, HANDS, FEET, MAIN_HAND, OFF_HAND, UNSLOTTED;

    public static List<GearSlot> SlotList(){
        return Arrays.asList(HEAD, BODY, HANDS, LEGS, FEET, MAIN_HAND, OFF_HAND, UNSLOTTED);
    }

    public static List<GearSlot> ArmorSlotList(){
        return Arrays.asList(BODY, HEAD, HANDS, LEGS, FEET, UNSLOTTED);
    }

    public static GearSlot parseSlot(String s) throws IllegalTokenException{
        switch(s.toLowerCase()){
            case "head":
                return HEAD;
            case "l":
            case "legs":
                return LEGS;
            case "b":
            case "body":
                return BODY;
            case "hands":
                return HANDS;
            case "f":
            case "feet":
                return FEET;
            case "m":
            case "mh":
            case "main-hand":
                return MAIN_HAND;
            case "o":
            case "oh":
            case "off-hand":
                return OFF_HAND;
            case "x":
            case "u":
            case "none":
            case "unslotted":
                return UNSLOTTED;
            default:
                throw new IllegalTokenException(s);
        }
    }
}
