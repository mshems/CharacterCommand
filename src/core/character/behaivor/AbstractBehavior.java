package core.character.behaivor;

import core.character.PlayerCharacter;

import java.io.Serializable;

public abstract class AbstractBehavior implements Serializable{
    protected PlayerCharacter pc;

    public AbstractBehavior(PlayerCharacter pc){
        this.pc = pc;
    }
}
