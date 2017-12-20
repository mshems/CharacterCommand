package core.character;

import core.character.stats.Ability;
import core.character.stats.LinkedStat;
import core.magic.SpellBook;

public class MagicCharacter extends AbstractCharacter{
    private SpellBook spellBook;
    private Ability spellAbility;
    private boolean spellcaster = false;

    MagicCharacter(String name) {
        super(name);
    }


    private double SSDC(){
        if(spellcaster) return 8 + spellAbility.getModifier() + PB();
        else return 0;
    }

    private double SAM(){
        if(spellcaster) return spellAbility.getModifier() + PB();
        else return 0;
    }

    public void makeCaster(Ability a){
        if(!isSpellcaster()) {
            setSpellAbility(a);
            setSpellcaster(true);
            setSpellBook(new SpellBook());
            getStatBlock().put("ssdc", new LinkedStat("Spell Save DC", this::SSDC));
            getStatBlock().put("sam",  new LinkedStat("Spell Attack Modifier", this::SAM));
            getStatBlock().put("pb",   new LinkedStat("Proficiency Bonus", this::PB));
        }
    }

    public SpellBook getSpellBook() {
        return spellBook;
    }

    public void setSpellBook(SpellBook spellBook) {
        this.spellBook = spellBook;
    }

    public void setSpellAbility(Ability spellAbility) {
        this.spellAbility = spellAbility;
    }

    public boolean isSpellcaster() {
        return spellcaster;
    }

    public void setSpellcaster(boolean spellcaster) {
        this.spellcaster = spellcaster;
    }
}
