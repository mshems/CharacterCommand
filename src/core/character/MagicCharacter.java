package core.character;

import core.character.stats.Ability;
import core.character.stats.LinkedStat;
import core.character.stats.StatLink;
import core.magic.SpellBook;
import core.magic.SpellSlot;

import java.io.Serializable;

public class MagicCharacter extends AbstractCharacter implements Serializable{
    private SpellBook spellBook;
    private Ability spellAbility;
    private boolean spellcaster = false;
    private SpellSlot[] spellSlots;

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
            getStatBlock().put("ssdc", new LinkedStat("Spell Save DC", (StatLink & Serializable) this::SSDC));
            getStatBlock().put("sam",  new LinkedStat("Spell Attack Modifier", (StatLink & Serializable) this::SAM));
            getStatBlock().put("pb",   new LinkedStat("Proficiency Bonus", (StatLink & Serializable) this::PB));
            if(spellSlots==null){
                spellSlots = new SpellSlot[]{
                        new SpellSlot(1, 0),
                        new SpellSlot(2, 0),
                        new SpellSlot(3, 0),
                        new SpellSlot(4, 0),
                        new SpellSlot(5, 0),
                        new SpellSlot(6, 0),
                        new SpellSlot(7, 0),
                        new SpellSlot(8, 0),
                        new SpellSlot(9, 0)
                };
            }
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

    public SpellSlot[] getSpellSlots() {
        return spellSlots;
    }

    public SpellSlot getSpellSlot(int slotLevel){
        return spellSlots[slotLevel-1];
    }
}
