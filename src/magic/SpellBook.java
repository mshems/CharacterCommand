package magic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class SpellBook implements Serializable {
	private LinkedHashMap<String, Spell> spellIndex;
	private ArrayList<Spell> spellBook;
	
	public SpellBook(){
		spellIndex = new LinkedHashMap<String, Spell>();
		spellBook = new ArrayList<Spell>();
	}
	
	public boolean isKnown(Spell spell){
		if(spellIndex.containsKey(spell.getSpellName().toLowerCase())){
			return true;
		} else {
			return false;
		}
	}
	
	public void learn(Spell spell){
		if(!spellBook.contains(spell)){
			spellIndex.put(spell.getSpellName().toLowerCase(), spell);
			spellBook.add(spellIndex.get(spell.getSpellName().toLowerCase()));
			Collections.sort(spellBook);
		}
	}
	
	public void forget(Spell spell){
		spellBook.remove(spell);
		spellIndex.remove(spell.getSpellName().toLowerCase());
	}
	
	public Spell get(String spellName){
		return spellIndex.get(spellName.toLowerCase());
	}
	
	public String toString(){
		String s="Spellbook:";
		for(Spell spell:spellBook){
			s+="\n"+spell;
		}
		return s;
	}
}
