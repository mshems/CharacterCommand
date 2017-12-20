package core.magic;

import java.util.HashMap;

public class SpellBook {
    private HashMap<String, Spell> contents;

    public SpellBook(){
        this.contents = new HashMap<>();
    }

    public boolean add(Spell spell){
        if(contains(spell.getSpellName().toLowerCase())) return false;
        contents.put(spell.getSpellName().toLowerCase(), spell);
        return true;
    }

    public boolean remove(Spell spell){
        if(!contains(spell.getSpellName().toLowerCase())) return false;
        contents.remove(spell.getSpellName().toLowerCase(), spell);
        return true;
    }

    public Spell get(String spellName){
        return contents.get(spellName.toLowerCase());
    }

    public boolean contains(String spellName){
        return contents.containsKey(spellName.toLowerCase());
    }
}
