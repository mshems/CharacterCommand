package app;

import app.io.IOUtils;
import app.io.Reader;
import core.actions.DiceRoll;
import core.character.PlayerCharacter;
import core.character.inventory.GearSlot;
import core.items.consumable.Consumable;
import core.items.consumable.ConsumableEffect;
import core.items.equippable.armor.Armor;
import core.items.equippable.armor.ArmorType;
import core.items.equippable.weapon.DamageRoll;
import core.items.equippable.weapon.Weapon;
import core.items.equippable.weapon.WeaponDamage;
import core.items.magic.MagicEffect;
import core.magic.Spell;
import core.magic.SpellSlot;

import java.io.Serializable;

public class Test {

    public static void main(String[] args){
        CharacterCommand app = new CharacterCommand();

        PlayerCharacter pc = addTestPC(app);
        app.loadCharacter(pc);
        app.loadCharacter(new PlayerCharacter("Clart"));
        app.loadCharacter(new PlayerCharacter("Murpy"));
        app.setActiveCharacter(pc);

        app.start();
    }

    private static PlayerCharacter addTestPC(CharacterCommand app){
        PlayerCharacter pc = new PlayerCharacter("Morris Stormraven");
        Armor a = new Armor("Chainmail Armor of Bluntness", 14, ArmorType.MEDIUM, GearSlot.BODY,".hidden-tag");
        a.addEffect(new MagicEffect(pc.getAbilities().CON(), +2));
        a.addEffect(new MagicEffect(pc.getAbilities().INT(), -2));
        a.setItemDescription("A suit of chainmail armor that makes the wearer more hardy, if a bit dim-witted.");

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
                new Consumable("Potion of Minor Healing", (ConsumableEffect & Serializable)(activeCharacter)->
                                activeCharacter.healthBehavior.heal(DiceRoll.doRoll(2,4,0))), 5);

        pc.makeCaster(pc.getAbilities().CHA());
        pc.getSpellSlot(1).setMaxSlots(4);
        pc.getSpellSlot(2).setMaxSlots(3);
        pc.getSpellSlot(3).setMaxSlots(2);

        for(SpellSlot s:pc.getSpellSlots()){
            if(s.getMaxValue()>0){
                s.recharge();
            }
        }
        pc.magicBehavior.learnSpell(new Spell("Firebolt", 0));
        pc.magicBehavior.learnSpell(new Spell("Magic Missile", 1));
        pc.magicBehavior.learnSpell(new Spell("Shatter", 2));
        pc.magicBehavior.learnSpell(new Spell("Lightning Bolt", 3));
        pc.magicBehavior.learnSpell(new Spell("Fireball", 3));

        return pc;
    }
}
