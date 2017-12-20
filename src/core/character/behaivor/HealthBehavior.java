package core.character.behaivor;

import core.character.PlayerCharacter;

public class HealthBehavior extends Behavior{

    public HealthBehavior(PlayerCharacter pc) {
        super(pc);
    }

    public void heal(double amt){
        pc.HP().incrementCurrValue(amt);
    }

    public void hurt(double amt){
        pc.HP().decrementCurrValue(amt);
    }
}
