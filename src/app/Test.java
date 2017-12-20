package app;

import core.actions.DiceRoll;
import core.character.PlayerCharacter;
import core.character.inventory.GearSlot;
import core.items.consumable.Consumable;
import core.items.equippable.armor.Armor;
import core.items.equippable.armor.ArmorType;
import core.items.equippable.weapon.DamageRoll;
import core.items.equippable.weapon.Weapon;
import core.items.equippable.weapon.WeaponDamage;
import core.items.magic.MagicEffect;

public class Test {

    public static void main(String[] args) throws InterruptedException{
        CharacterCommand app = new CharacterCommand();
        addTestPC(app);
        app.start();
    }

    private static void addTestPC(CharacterCommand app){
        PlayerCharacter pc = new PlayerCharacter("CharacterName");
        Armor a = new Armor("Chainmail Armor of Bluntness", 14, ArmorType.MEDIUM, GearSlot.BODY,".hidden-tag");
        a.addEffect(new MagicEffect(pc.getAbilities().CON(), +2));
        a.addEffect(new MagicEffect(pc.getAbilities().INT(), -2));

        Weapon w1 = new Weapon("Holy Longsword", new WeaponDamage(
                new DamageRoll(1,8,"slashing"),
                new DamageRoll(1,4,+2, "radiant")));
        w1.setAttackBonus(+2);
        w1.autoTag();
        w1.addEffect(new MagicEffect(pc.getAbilities().STR(), +2));
        w1.setItemDescription("A longsword blazing with radiant energy. Seems to bolster the wielder's strength.");

        Weapon w2 = new Weapon("Holy Dagger", new WeaponDamage(
                new DamageRoll(1,6,0, "piercing"),
                new DamageRoll(1,6,0, "radiant")));
        w2.setAttackBonus(+1);
        w2.autoTag();
        w2.addEffect(new MagicEffect(pc.getAbilities().DEX(), +1));

        pc.inventoryBehavior.add(w1, 1);
        pc.inventoryBehavior.add(a,  1);
        pc.inventoryBehavior.add(w2, 1);
        pc.inventoryBehavior.add(w2, 1);
        pc.inventoryBehavior.add(
                new Consumable("Potion of Minor Healing", (activeCharacter)->
                                activeCharacter.healthBehavior.heal(DiceRoll.doRoll(2,4,0))), 5);
        app.loadCharacter(pc);
        app.setActiveCharacter(pc);
    }
}
