package core.items.equippable.armor;

import jterminal.core.IllegalTokenException;

import java.util.Arrays;
import java.util.List;

public enum ArmorType{ LIGHT, MEDIUM, HEAVY;

    public static List<ArmorType> TypeList(){
        return Arrays.asList(LIGHT, MEDIUM, HEAVY);
    }

    public static ArmorType parseType(String str) throws IllegalTokenException{
        switch (str.toLowerCase()){
            case "l":
            case "light":
                return LIGHT;
            case "m":
            case "medium":
                return MEDIUM;
            case "h":
            case "heavy":
                return HEAVY;
            default:
                throw new IllegalTokenException(str);
        }
    }
}
