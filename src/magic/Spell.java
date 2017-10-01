package magic;

import app.CharacterCommand;

import java.io.Serializable;

public class Spell implements Comparable<Spell>, Serializable{
	private static final long serialVersionUID = CharacterCommand.VERSION;
	private String spellName;
	private int spellLevel;
	protected boolean spellPrepared;
	
	public static final int CANTRIP = 0;
	public static final int MAX_LEVEL = 9;

	public Spell(String name, int level){
		if(level > MAX_LEVEL){
			level = MAX_LEVEL;
		}
		if(level < CANTRIP){
			level = CANTRIP;
		}
		this.spellLevel = level;
		this.spellName = name;
	}
	
	public void prepare(){
		this.spellPrepared = true;
	}
	
	public void unprep(){
		this.spellPrepared = false;
	}
	
	public int getSpellLevel(){
		return this.spellLevel;
	}
	
	public String getSpellName(){
		return this.spellName;
	}

	public boolean isCantrip(){
		if (spellLevel == Spell.CANTRIP){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(Spell spell) {
		return spellLevel - spell.getSpellLevel();
	}
	
	@Override
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
	
	
}
