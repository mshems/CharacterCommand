package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Character implements Serializable{
	protected String playerName;
	protected int playerLevel;
	protected String playerClass;
	protected String race;
	protected ArrayList<Spell> spellbook;
	protected Attribute[] abilityScores;
	protected Attribute[] playerStats;
	protected Skill[] playerSkills;
	protected Inventory playerInventory;
	protected double currHP;
	protected String notes;
	protected SpellSlot[] spellSlots;
	protected boolean unprepOnCast = false;
	protected boolean requirePrep = false;
	
	public double getCurrHP() {
		return currHP;
	}

	public void setCurrHP(double currHP) {
		this.currHP = currHP;
	}

	public static final int gp = 0;
	public static final int sp = 1;
	public static final int cp = 2;
	
	public static final int hp = 0;
	public static final int ac = 1;
	public static final int spd = 2;
	public static final int per = 3;
	public static final int pro = 4;
	public static final int ini = 5;
	
	public Character(String n, String c){
		this.playerLevel = 1;
		this.playerName = n;
		this.playerClass = c;
		this.initAbilityScores();
		this.initPlayerStats();
		this.initInventory();
		this.initPlayerSkills();
		this.initSpellSlots();
		this.initSpellBook();
		this.notes = "";
	}
	
	public Character(){
		this.initAbilityScores();
		this.initPlayerStats();
		this.initInventory();
		this.initPlayerSkills();
		this.initSpellSlots();
		this.initSpellBook();
		this.notes = "";
	}
	
	private void initAbilityScores(){
		this.abilityScores = new Attribute[]{
				new Attribute(10, "STR", Utils.STR),
				new Attribute(10, "DEX", Utils.DEX),
				new Attribute(10, "CON", Utils.CON),
				new Attribute(10, "INT", Utils.INT),
				new Attribute(10, "WIS", Utils.WIS),
				new Attribute(10, "CHA", Utils.CHA)
			};
	}
	
	private void initPlayerStats(){
		this.playerStats = new Attribute[]{
				new Attribute(10, "Hit Point Maximum", Utils.HP_MAX),
				new Attribute(10, "Armor Class", Utils.AC),
				new Attribute(30, "Speed", Utils.SPD),
				new Attribute(10, "Passive Perception", Utils.PER),
				new Attribute(2, "Proficiency Bonus", Utils.PRO),
				new Attribute(this.abilityScores[1].getMod(), "Initiative", Utils.INI)
			};
		this.currHP = this.playerStats[hp].getValue();
	}
	
	private void initSpellSlots(){
		spellSlots = new SpellSlot[]{
				new SpellSlot(1,0),
				new SpellSlot(2,0),
				new SpellSlot(3,0),
				new SpellSlot(4,0),
				new SpellSlot(5,0),
				new SpellSlot(6,0),
				new SpellSlot(7,0),
				new SpellSlot(8,0),
				new SpellSlot(9,0),
		};
	}
	
	private void initSpellBook(){
		this.spellbook = new ArrayList<Spell>(); 
	}
	
	private void initPlayerSkills(){
		this.playerSkills = new Skill[]{
				new Skill("STR Throws", 0, Utils.STR_THROW),
				new Skill("Athletics",0, Utils.ATHLETICS),
				new Skill("DEX Throws",1, Utils.DEX_THROW),
				new Skill("Acrobatics",1, Utils.ACROBATICS),
				new Skill("Sleight of Hand",1, Utils.SLEIGHT_OF_HAND),
				new Skill("Stealth",1, Utils.STEALTH),
				new Skill("CON Throws",2, Utils.CON_THROWS),
				new Skill("INT Throws",3, Utils.INT_THROWS),
				new Skill("Arcana",3, Utils.ARCANA),
				new Skill("History",3, Utils.HISTORY),
				new Skill("Investigation",3, Utils.INVESTIGATION),
				new Skill("Nature",3, Utils.NATURE),
				new Skill("Religion",3, Utils.RELIGION),
				new Skill("WIS Throws",4, Utils.WIS_THROWS),
				new Skill("Animal Handling",4, Utils.ANIMAL_HANDLING),
				new Skill("Insight",4, Utils.INSIGHT),
				new Skill("Medicine",4, Utils.MEDICINE),
				new Skill("Perception",4, Utils.PERCEPTION),
				new Skill("Survival",4, Utils.SURVIVAL),
				new Skill("CHA Throws",5, Utils.CHA),
				new Skill("Deception",5, Utils.DECEPTION),
				new Skill("Intimidation",5, Utils.INTIMIDATION),
				new Skill("Performance",5, Utils.PERFORMANCE),
				new Skill("Persuasion",5, Utils.PERSUASION)
		};
		updateSkills();
	}
	
	protected void updateStats(){
		this.playerStats[ini].setValue(this.abilityScores[1].getMod());
	}
	
	protected void updateSkills(){
		for (Skill s : this.playerSkills){
			s.skillMod = this.abilityScores[s.skillAttribute].getMod();
			if (s.proficient){
				s.skillMod += this.playerStats[pro].getValue();
				if (s.expertise){
					s.skillMod += this.playerStats[pro].getValue();	
				}
			}
		}
	}
	
	private void initInventory(){
		this.playerInventory = new Inventory();
		this.playerInventory.add(new Item("Gold"));
		this.playerInventory.add(new Item("Silver"));
		this.playerInventory.add(new Item("Copper"));
	}
	
	public void heal(String s){
		int amount = 0;
		try {
			amount = Integer.parseInt(s);
			this.currHP+=amount;
			System.out.println("[Gained "+amount+" HP]");
		} catch (NumberFormatException e) {
			System.out.println("[Not a number]");
		}
	}
	
	public void hurt(String s){
		int amount = 0;
		try {
			amount = Integer.parseInt(s);
			this.currHP-=amount;
			System.out.println("[Lost "+amount+" HP]");
		} catch (NumberFormatException e) {
			System.out.println("[Not a number]");
		}
	}
	
	protected void updateProficiency(){
		if (this.playerLevel > 16){
			this.playerStats[pro].setValue(6);
		} else if (this.playerLevel > 12){
			this.playerStats[pro].setValue(5);
		} else if (this.playerLevel > 8){
			this.playerStats[pro].setValue(4);
		} else if (this.playerLevel > 4){
			this.playerStats[pro].setValue(3);
		}
		updateSkills();
	}
	
	protected void levelUp(String lvl){
		try {
			int level = Integer.parseInt(lvl);
			if (level == 0){
				this.playerLevel+=1;		
			} else {
				this.playerLevel = level;
			}
			System.out.println(String.format("[%s is now level %d]", this.playerName, this.playerLevel));
			updateProficiency();
		} catch (NumberFormatException e) {
			System.out.println("[Not a number]");
		}	
	}
		
	protected void addMoreItem(int itemIndex, int count){
		this.playerInventory.get(itemIndex).addItemCount(count);
		if (this.playerInventory.get(itemIndex).getItemCount() <= 0 && itemIndex>2){
			this.playerInventory.remove(itemIndex);
		}
	}
	
	protected void addNewItem(Item newItem, int count){
		newItem.setItemCount(count);
		this.playerInventory.add(newItem);
	}
	
	protected void addCoin(int coinType, int count){
		this.playerInventory.get(coinType).addItemCount(count);
	}
	
	protected void learnSpell(Spell spell){
		this.spellbook.add(spell);
		System.out.println(String.format("[Learned level %d spell \"%s\"]", spell.spellLevel, spell.spellName));
	}
	
	protected void castSpell(Spell spell, int lvl){
		if ((this.requirePrep && spell.spellPrepared)||!this.requirePrep){
			if (lvl == Spell.defaultLevel){
				lvl = spell.spellLevel;
			} 
			if (lvl == Spell.cantrip){
				System.out.println(String.format("[Cast spell \"%s\" as a cantrip]", spell.spellName));
			} else {
				if (lvl < spell.spellLevel){
					lvl = spell.spellLevel;
				}
				this.spellSlots[lvl-1].charges -= 1;
				System.out.println(String.format("[Cast spell \"%s\" at level %d]", spell.spellName, lvl));
			}
			if (this.unprepOnCast){
				spell.spellPrepared = false;
			}
		} else {
			System.out.println(String.format("[Spell \"%s\" not prepared]", spell.spellName));
		}
	}
	
	protected void chargeSpell(int lvl, int num){
		this.spellSlots[lvl-1].charges += num;
		System.out.println("[Charged "+num+" level "+lvl+" spell slots]");
	}
	
	protected void chargeAll(){
		for (SpellSlot slot : this.spellSlots){
			slot.charges = slot.total;
		}
	}
	
	protected void forgetSpell(Spell spell){
		this.spellbook.remove(spell);
		System.out.println(String.format("[Forgot how to cast \"%s\"]", spell.spellName));
	}
		
	protected void prepSpell(){}
	
	public double getProficiency(){
		return this.playerStats[pro].getValue();
	}
	
	public String skillsToString(){
		String s = "[Skills]";
		for (Skill skill : this.playerSkills){
			s += "\n"+skill;
		}
		return s;
	}
	
	public String spellBookToString(){
		Collections.sort(this.spellbook); //sort by level
		
		String s = "[Spellbook]";
		for (Spell spell : this.spellbook){
			s += "\n"+spell;
		}
		return s;
	}
	
	public String spellSlotsToString(){
		String s = "[Spell Slots]";
		for (SpellSlot slot : this.spellSlots){
			s += "\n"+slot;
		}
		return s;
	}
	
	public String toString(){
		String s = String.format("[%s - Level %d %s %s]\n", this.playerName, this.playerLevel, this.race, this.playerClass);
		s += String.format("[%.0f/%.0f HP]  [AC: %.0f] [Speed: %.0fft] [Initiative: %+.0f] [Proficiency Bonus: %+.0f]\n", 
			this.currHP, this.playerStats[hp].getValue(), this.playerStats[ac].getValue(), 
			this.playerStats[spd].getValue(), this.playerStats[ini].getValue(), this.playerStats[pro].getValue());
		int n = 0;
		for (Attribute a : this.abilityScores){
			n++;
			s += a;
			if (n == 3) s+= "\n";
		}
		return s;
	}
	
}
