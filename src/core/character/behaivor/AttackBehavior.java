package core.character.behaivor;

import core.actions.DiceRoll;
import core.character.PlayerCharacter;
import core.items.equippable.weapon.Weapon;

public class AttackBehavior extends AbstractBehavior {
    public AttackBehavior(PlayerCharacter pc) {
        super(pc);
    }

    public double makeDEXAttackRoll(PlayerCharacter pc, Weapon weapon){
        return DiceRoll.doRoll(1,20,pc.getAbilities().DEX().getModifier()) + pc.PB() + weapon.getAttackBonus();
    }

    public double makeSTRAttackRoll(PlayerCharacter pc, Weapon weapon){
        return DiceRoll.doRoll(1,20,pc.getAbilities().STR().getModifier()) + pc.PB() + weapon.getAttackBonus();
    }
}
