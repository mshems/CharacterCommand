package magic;

import app.App;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class SpellBook implements Serializable {
	private static final long serialVersionUID = App.VERSION;
	private LinkedHashMap<String, Spell> spellIndex;
	private ArrayList<Spell> spellList;
	
	public SpellBook(){
		spellIndex = new LinkedHashMap<String, Spell>();
		spellList = new ArrayList<Spell>();
	}
	
	public boolean contains(Spell spell){
		if(spellIndex.containsKey(spell.getSpellName().toLowerCase())){
			return true;
		} else {
			return false;
		}
	}

	public boolean isEmpty(){
		return spellIndex.isEmpty();
	}
	
	public void learn(Spell spell){
		String key = spell.getSpellName().toLowerCase();
		if(!spellIndex.containsKey(key)){
			spellIndex.put(key, spell);
			spellList.add(spellIndex.get(key));
			Collections.sort(spellList);
		}
	}
	
	public void forget(Spell spell){
		spellList.remove(spell);
		spellIndex.remove(spell.getSpellName().toLowerCase());
	}
	
	public Spell get(String spellName){
		return spellIndex.get(spellName.toLowerCase());
	}
	
	public String toString(){
		String newLine = System.lineSeparator();
		String s="Spellbook:";
		for(Spell spell:spellList){
			s+=newLine+spell;
		}
		return s;
	}
}
