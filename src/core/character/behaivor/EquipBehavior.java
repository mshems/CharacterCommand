package core.character.behaivor;

import core.character.PlayerCharacter;
import core.items.Item;

public class EquipBehavior extends AbstractBehavior {

    public EquipBehavior(PlayerCharacter pc) {
        super(pc);
    }

    public boolean equip(Item item){
        if(item.isEquipped()) return false;
        if(item.hasTag("equippable")){
            item.equip(pc);
            return true;
        } else return false;
    }

    public boolean dequip(Item item){
        if(!item.isEquipped()) return false;
        if(item.hasTag("equippable")){
            item.dequip(pc);
            return true;
        } else return false;
    }
}
