package core.items.consumable;

import core.character.PlayerCharacter;

import java.io.Serializable;

public interface ConsumableEffect extends Serializable{
    void doEffect(PlayerCharacter pc);
}
