package core.items.equippable;

import core.character.PlayerCharacter;
import core.character.inventory.GearSlot;
import core.constants.EquipConstants;

public class Shield extends EquippableItem {
    private double acBonus;

    public Shield(String name, double acBonus){
        super(name);
        addTag("shield");
        addTag(".TYPE:SHIELD");
        this.gearSlot = GearSlot.OFF_HAND;
        this.acBonus = acBonus;
    }

    @Override
    public int equip(PlayerCharacter pc){
        if(!equipped){
            pc.getStatBlock().get("ac").incrementBonusValue(acBonus);
            return super.equip(pc);
        } else return EquipConstants.ALREADY_EQUIPPED;
    }

    @Override
    public int dequip(PlayerCharacter pc){
        if(equipped){
            pc.getStatBlock().get("ac").decrementBonusValue(acBonus);
            return super.dequip(pc);
        } else return EquipConstants.NOT_EQUIPPED;
    }
}
