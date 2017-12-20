package core.items.equippable;

import core.character.PlayerCharacter;
import core.character.inventory.GearSlot;

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
    public void equip(PlayerCharacter pc){
        if(!equipped){
            pc.getStatBlock().get("ac").incrementBonusValue(acBonus);
            super.equip(pc);
        }
    }

    @Override
    public void dequip(PlayerCharacter pc){
        if(equipped){
            pc.getStatBlock().get("ac").decrementBonusValue(acBonus);
            super.dequip(pc);
        }
    }
}
