package core.magic;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class SpellBook implements Serializable{
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

    public String toString() {
        LinkedList<Spell> list  = new LinkedList<>();
        list.addAll(this.contents.values());
        Collections.sort(list);
        String s = "Spells Known:";
        if(this.contents.isEmpty()){
         s += "...";
        } else {
            for (Spell spell : list) {
                s += "\n" + spell.toString();
            }
        }
        return s;
    }
}
