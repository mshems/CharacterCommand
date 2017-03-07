package app;

import java.io.Serializable;

public class Spell implements Comparable<Spell>, Serializable {
	protected String spellName;
	protected int spellLevel;
	protected boolean spellPrepared;
	
	public static final int defaultLevel = -2;
	public static final int error = -1;
	public static final int cantrip = 0;
	
	public Spell(){
		
	}
	
	public Spell(int level, String name){
		this.spellLevel = level;
		this.spellName = name;
	}
	
	public String toString(){
		String s;
		if (this.spellLevel == 0){
			s = "Cantrip - "; 
		} else {
			s = "Level "+this.spellLevel+" - ";
		}
		s +=  String.format("%s", this.spellName);
		if (spellPrepared){
			s += " [Prepared]";
		}
		return s;
	}

	public int getSpellLevel(){
		return this.spellLevel;
	}
	
	//enables ordering spellbook by level
	@Override
	public int compareTo(Spell compareSpell) {
		int compareLevel = compareSpell.getSpellLevel(); 
		return this.spellLevel-compareLevel;
	}
}
