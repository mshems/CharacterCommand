package core.items.equippable.armor;

import core.character.PlayerCharacter;
import core.character.inventory.GearSlot;
import core.constants.EquipConstants;
import core.items.equippable.EquippableItem;

public class Armor extends EquippableItem {
    private double ac;
    private ArmorType armorType;

    public Armor(String name, double ac, ArmorType type, GearSlot gearSlot, String...tags){
        super(name, tags);
        this.ac = ac;
        this.armorType = type;
        this.gearSlot = gearSlot;
        addTag("armor");
        addTag(".TYPE:ARMOR");
        addTag(".ARMOR:"+armorType);
        //addTag(".GEAR:"+gearSlot);

    }

    @Override
    public int equip(PlayerCharacter pc){
        if(!equipped){
            super.equip(pc);
            double dexMod = pc.getAbilities().DEX().getModifier();
            pc.AC().setBaseValue(ac);
            switch (armorType){
                case LIGHT:
                    pc.AC().incrementBonusValue(dexMod);
                    break;
                case MEDIUM:
                    pc.AC().incrementBonusValue(Math.min(dexMod, 2));
                    break;
                case HEAVY:
                    break;
            }
            return EquipConstants.SUCCESSFUL_EQUIP;
        }
        return EquipConstants.ALREADY_EQUIPPED;
    }

    @Override
    public int dequip(PlayerCharacter pc){
        if(equipped){
            super.dequip(pc);
            double dexMod = pc.getAbilities().DEX().getModifier();
            pc.AC().setBaseValue(pc.getStat("nac").getTotal());
            switch (armorType){
                case LIGHT:
                    pc.AC().decrementBonusValue(dexMod);
                    break;
                case MEDIUM:
                    pc.AC().decrementBonusValue(Math.min(dexMod, 2));
                    break;
                case HEAVY:
                    break;
            }
            return EquipConstants.SUCCESSFUL_DEQUIP;
        }
        return EquipConstants.NOT_EQUIPPED;
    }

    @Override
    public String toString() {
        return super.toString();
        //put in details
        /*switch (armorType) {
            case LIGHT:
                return super.toString()+" [Light Armor]";
            case MEDIUM:
                return super.toString()+" [Medium Armor]";
            case HEAVY:
                return super.toString()+" [Heavy Armor]";
            default:
        }*/
    }
}
