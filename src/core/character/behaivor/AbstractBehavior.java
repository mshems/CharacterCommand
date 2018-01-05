package core.character.behaivor;

import core.character.PlayerCharacter;

public abstract class AbstractBehavior {
    protected PlayerCharacter pc;

    public AbstractBehavior(PlayerCharacter pc){
        this.pc = pc;
    }
}
