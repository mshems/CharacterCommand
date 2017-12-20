package core.items;

import java.util.Arrays;
import java.util.List;

public class ItemTags {

    public static void autoTag(List<String> tagSet, Item item){
        for(String tag:tagSet){
            if(item.itemName.toLowerCase().contains(tag)){
                item.addTag(tag);
            }
        }
    }

    public static List<String> simpleWeaponTags = Arrays.asList("dagger","club","staff","greatclub","javelin",
            "handaxe","light hammer","mace","sickle","spear"
    );

    public static List<String> martialWeaponTags = Arrays.asList("battleaxe","flail","glaive","greataxe","greatsword",
            "halberd","lance", "longsword","maul","morningstar","pike","rapier","scimitar","shortsword", "trident",
            "war pick","warhammer","whip"
    );

    public static List<String> simpleRangedTags = Arrays.asList("light crossbow","dart","shortbow","sling");

    public static List<String> martialRangedTags = Arrays.asList("heavy crossbow","hand crossbow","blowgun","longbow","net");

    public static List<String> potionTags = Arrays.asList("potion", "philter");

    public static List<String> armorTags = Arrays.asList("plate", "chain", "splint", "studded", "leather");

}
