package core.character.behaivor;

import core.character.PlayerCharacter;
import core.magic.Spell;
import core.constants.SpellConstants;

public class MagicBehavior extends AbstractBehavior {

    public MagicBehavior(PlayerCharacter pc) {
        super(pc);
    }

    public int cast(PlayerCharacter pc, Spell spell, int castLevel, boolean requirePrep){
        if (spell == null || pc == null)
            return SpellConstants.NULL_SPELL;
        if(!pc.isSpellcaster())
            return SpellConstants.NOT_CASTER;
        if(spell.isCantrip())
            return spell.cast(requirePrep);
        if(spell.getSpellLevel() > castLevel)
            return  SpellConstants.LVL_TOO_LOW;
        if(pc.getSpellSlot(castLevel).getCurrValue() > 0){
            pc.getSpellSlot(castLevel).decrementCurrValue(1);
            return spell.cast(requirePrep);
        } else {
            return SpellConstants.NO_SLOTS;
        }
    }

    public boolean learnSpell(Spell spell){
        return pc.getSpellBook().add(spell);
    }

    public boolean forgetSpell(Spell spell){
        return pc.getSpellBook().remove(spell);
    }
}
