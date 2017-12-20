package core.character.behaivor;

import core.character.PlayerCharacter;
import core.magic.Spell;

public class MagicBehavior extends Behavior{

    public MagicBehavior(PlayerCharacter pc) {
        super(pc);
    }

    public boolean cast(PlayerCharacter pc, Spell spell){
        return true;
    }

    public boolean learnSpell(Spell spell){
        return pc.getSpellBook().add(spell);
    }

    public boolean forgetSpell(Spell spell){
        return pc.getSpellBook().remove(spell);
    }
}
