package core.magic;

import core.TaggedObject;

import java.util.Collections;
import java.util.LinkedList;

public class Spell extends TaggedObject{
    private String spellName;
    private int spellLevel;
    private boolean prepared;
    private LinkedList<String> tags;

    public static final int CANTRIP = 0;

    public Spell(String spellName, int spellLevel, String...t){
        super(t);
        this.spellName = spellName;
        this.spellLevel = spellLevel;
        this.prepared = false;
    }

    public boolean cast(boolean requirePrep) {
        return !requirePrep || prepared;
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

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    public String getSpellName() {
        return spellName;
    }
}