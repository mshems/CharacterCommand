package core.character.behaivor;

import core.character.PlayerCharacter;
import core.constants.EquipConstants;
import core.items.Item;

public class EquipBehavior extends AbstractBehavior {

    public EquipBehavior(PlayerCharacter pc) {
        super(pc);
    }

    public int equip(Item item){
        if(item.isEquipped()) return EquipConstants.ALREADY_EQUIPPED;
        if(item.hasTag("equippable")){
            return item.equip(pc);
        } else return EquipConstants.NOT_EQUIPPABLE;
    }

    public int dequip(Item item){
        if(!item.isEquipped()) return EquipConstants.NOT_EQUIPPED;
        if(item.hasTag("equippable")){
            return item.dequip(pc);
        } else return EquipConstants.NOT_EQUIPPABLE;
    }
}
