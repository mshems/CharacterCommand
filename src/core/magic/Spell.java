package core.magic;

import core.TaggedObject;
import core.constants.SpellConstants;

import java.io.Serializable;

public class Spell extends TaggedObject implements Comparable<Spell>, Serializable{
    private String spellName;
    private int spellLevel;
    private boolean prepared;

    private static final int CANTRIP_SPELL = 0;

    public Spell(String spellName, int spellLevel, String...t){
        super(t);
        this.spellName = spellName;
        this.spellLevel = spellLevel;
        this.prepared = false;
    }

    public int cast(boolean requirePrep) {
        if(requirePrep && prepared) return SpellConstants.SUCCESSFUL_CAST;
        if(requirePrep && !prepared) return SpellConstants.NOT_PREPARED;
        return SpellConstants.SUCCESSFUL_CAST;
    }

    public void setSpellName(String spellName) {
        this.spellName = spellName;
    }

    public int getSpellLevel() {
        return spellLevel;
    }

    public void setSpellLevel(int spellLevel) {
        this.spellLevel = spellLevel;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public boolean isCantrip() {
        return spellLevel== CANTRIP_SPELL;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    public String getSpellName() {
        return spellName;
    }

    //TODO:
    @Override
    public String toString() {
        String s="";
        if(this.isCantrip()){
            s+= "Cantrip - ";
        } else {
            s+= spellLevel;
            if(isPrepared()) {
                s += " # ";
            } else {
                s += " - ";
            }
        }
        s += spellName;
        return s;
    }

    @Override
    public int compareTo(Spell otherSpell) {
        return this.spellLevel - otherSpell.spellLevel;
    }
}