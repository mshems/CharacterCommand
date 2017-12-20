package core.items.equippable.weapon;

import core.character.PlayerCharacter;
import core.character.inventory.GearSlot;
import core.items.equippable.EquippableItem;
import core.items.ItemTags;

public class Weapon extends EquippableItem {
    private WeaponDamage weaponDamage;
    private WeaponDamage alternateDamage;
    private double attackBonus;

    public Weapon(String name, String...tags){
        super(name, tags);
        gearSlot = GearSlot.MAIN_HAND;
        attackBonus = 0;
        addTag("weapon");
        addTag(".TYPE:WEAPON");
    }

    public Weapon(String name, WeaponDamage dmg, String...tags){
        this(name, tags);
        weaponDamage = dmg;
    }

    @Override
    public void equip(PlayerCharacter pc){
        this.equipped = true;
        if(hasTag("light")){
            doEffects();
            if(!pc.getGearSet().hasGear(GearSlot.MAIN_HAND)){
                pc.getGearSet().putGear(GearSlot.MAIN_HAND, this);
            } else {
                if (this != pc.getGearSet().getGear(GearSlot.MAIN_HAND)) {
                    if (pc.getGearSet().hasGear(GearSlot.OFF_HAND)) {
                        pc.getGearSet().getGear(GearSlot.OFF_HAND).dequip(pc);
                        pc.getGearSet().putGear(GearSlot.OFF_HAND, this);
                    } else {
                        pc.getGearSet().putGear(GearSlot.OFF_HAND, this);
                    }
                }
            }
        } else {
            super.equip(pc);
        }
    }

    @Override
    public void dequip(PlayerCharacter pc){
        this.equipped = false;
        if(hasTag("light")){
            undoEffects();
            if(this == pc.getGearSet().getGear(GearSlot.MAIN_HAND)){
                pc.getGearSet().removeGear(GearSlot.MAIN_HAND);
            }
            if(this == pc.getGearSet().getGear(GearSlot.OFF_HAND)){
                pc.getGearSet().removeGear(GearSlot.OFF_HAND);
            }
        } else {
            super.dequip(pc);
        }
    }

    public void makeVersatile(WeaponDamage altDamage){
        alternateDamage = altDamage;
        addTag("versatile");
    }

    public void autoTag(){
        autoTag(ItemTags.simpleWeaponTags, "simple");
        autoTag(ItemTags.martialWeaponTags, "martial");
        autoTag(ItemTags.simpleRangedTags, "simple", "ranged");
        autoTag(ItemTags.martialRangedTags, "martial", "ranged");
    }

    public double rollDamage(PlayerCharacter pc){
        if(weaponDamage!=null) {
            double dmg = weaponDamage.rollDamage();
            if (hasTag("finesse")) return dmg + (int) pc.getAbilities().DEX().getModifier();
            return dmg + (int) pc.getAbilities().STR().getModifier();
        } else return 0;
    }

    public double rollAltDamage(PlayerCharacter pc){
        if(alternateDamage!=null) {
            double dmg = alternateDamage.rollDamage();
            if (hasTag("finesse")) return dmg + (int) pc.getAbilities().DEX().getModifier();
            return dmg + (int) pc.getAbilities().STR().getModifier();
        } else return 0;
    }

    public double getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(double attackBonus) {
        this.attackBonus = attackBonus;
    }

    @Override
    public String toString(){
        String str = itemName;
        if(equipped) str = "(e) "+str;
        if(attackBonus!=0){
            str += String.format(" (%+.0f)", attackBonus);
        }
        //put in description
        /*if(weaponDamage!=null){
            str += " ["+weaponDamage.toString();
            str+="]";
        }*/
        return str;
    }

    //TODO: custom description
}
