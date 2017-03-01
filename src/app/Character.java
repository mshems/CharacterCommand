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
	protected boolean caster = false;
	protected int casterType;
	protected int exp;
	
	public static final int gp = 0;
	public static final int sp = 1;
	public static final int cp = 2;
	public static final int wisCaster = Attribute.WIS;
	public static final int intCaster = Attribute.INT;
	public static final int chaCaster = Attribute.CHA;
	
	
	public Character(String n, String c){
		this.playerLevel = 1;
		this.playerName = n;
		this.playerClass = c;
		initCharacter();
	}
	
	public Character(){
		initCharacter();
	}
	
	private void initCharacter(){
		this.initAbilityScores();
		this.initPlayerStats();
		this.initInventory();
		this.initPlayerSkills();
		this.initSpellSlots();
		this.initSpellBook();
		this.notes = "";
		
		
		this.updateStats();
	}
	
	private void initAbilityScores(){
		this.abilityScores = new Attribute[]{
				new Attribute(10, "STR", Utils.STR),
				new Attribute(10, "DEX", Utils.DEX),
				new Attribute(10, "CON", Utils.CON),
				new Attribute(10, "INT", Utils.INT),
				new Attribute(10, "WIS", Utils.WIS),
				new Attribute(10, "CHA", Utils.CHA),
				
			};
	}
	
	private void initPlayerStats(){
		this.playerStats = new Attribute[]{
				new Attribute(10, "Hit Point Maximum", Utils.HP_MAX),
				new Attribute(10+this.abilityScores[Attribute.DEX].getMod(), "Armor Class", Utils.AC),
				new Attribute(30, "Speed", Utils.SPD),
				new Attribute(10, "Passive Perception", Utils.PER),
				new Attribute(2, "Proficiency Bonus", Utils.PRO),
				new Attribute(this.abilityScores[1].getMod(), "Initiative", Utils.INI),
				new Attribute(0, "Spell Save DC", Utils.SSDC),
				new Attribute(0, "Spell Attack Modifier", Utils.SAM),
			};
		this.currHP = this.playerStats[Attribute.HP].total();
		updateStats();
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
				new Skill("Persuasion",5, Utils.PERSUASION),
		};
		updateSkills();
	}
	
	protected void updateStats(){
		this.playerStats[Attribute.INI].setBaseVal(this.abilityScores[Attribute.DEX].getMod());
		this.playerStats[Attribute.SSDC].setBaseVal(8 + this.playerStats[Attribute.PRO].total() + this.abilityScores[this.casterType].getMod());
		this.playerStats[Attribute.SAM].setBaseVal(this.playerStats[Attribute.PRO].total() + this.abilityScores[this.casterType].getMod());
	}
	
	protected void updateSkills(){
		for (Skill s : this.playerSkills){
			s.skillMod = this.abilityScores[s.skillAttribute].getMod();
			if (s.proficient){
				s.skillMod += this.playerStats[Attribute.PRO].total();
				if (s.expertise){
					s.skillMod += this.playerStats[Attribute.PRO].total();	
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
		double hpStart = this.currHP;
		int amount = 0;
		try {
			amount = Integer.parseInt(s);
			this.currHP+=amount;
			System.out.println("[Gained "+amount+" HP]");
			if (hpStart <=0 && currHP >0){
				System.out.println("["+this.playerName+" is no longer unconscious]");
			}
		} catch (NumberFormatException e) {
			System.out.println("[Not a number]");
		}
	}
	
	public void heal(int n){
		double hpStart = this.currHP;

		this.currHP+=n;
		System.out.println("[Gained "+n+" HP]");
		if (hpStart <=0 && currHP >0){
			System.out.println("["+this.playerName+" is no longer unconscious]");
		}
	}
	
	public void hurt(int n){
		this.currHP-=n;
		System.out.println("[Lost "+n+" HP]");
		if (this.currHP <= 0){
			System.out.println("["+this.playerName+" is unconscious]");
		}
	}
	
	public void hurt(String s){
		int amount = 0;
		try {
			amount = Integer.parseInt(s);
			this.currHP-=amount;
			System.out.println("[Lost "+amount+" HP]");
			if (this.currHP <= 0){
				System.out.println("["+this.playerName+" is unconscious]");
			}
		} catch (NumberFormatException e) {
			System.out.println("[Not a number]");
		}
	}
	
	protected void updateProficiency(){
		if (this.playerLevel > 16){
			this.playerStats[Attribute.PRO].setBaseVal(6);
		} else if (this.playerLevel > 12){
			this.playerStats[Attribute.PRO].setBaseVal(5);
		} else if (this.playerLevel > 8){
			this.playerStats[Attribute.PRO].setBaseVal(4);
		} else if (this.playerLevel > 4){
			this.playerStats[Attribute.PRO].setBaseVal(3);
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
		return this.playerStats[Attribute.PRO].total();
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
		String s = "";
			if (this.caster == true && this.spellbook.size()!=0){
				s+= String.format("[Spell Save DC: %.0f] [Spell Attack Modifier: %+.0f]\n", this.playerStats[Attribute.SSDC].total(), this.playerStats[Attribute.SAM].total());
					s += "[Spellbook]";
				for (Spell spell : this.spellbook){
					s += "\n"+spell;
				}
			} else {
				s += "[No Spells Known]";
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
		updateStats();
		String s = String.format("[%s - Level %d %s %s]\n", this.playerName, this.playerLevel, this.race, this.playerClass);
		s += String.format("[%.0f/%.0f HP]  [AC: %.0f] [Speed: %.0fft] [Initiative: %+.0f] [Proficiency Bonus: %+.0f]\n", 
			this.currHP, this.playerStats[Attribute.HP].total(), this.playerStats[Attribute.AC].total(), 
			this.playerStats[Attribute.SPD].total(), this.playerStats[Attribute.INI].total(), this.playerStats[Attribute.PRO].total());
		if (this.caster == true){
			s+= String.format("[Spell Save DC: %.0f] [Spell Attack Modifier: %+.0f]\n", this.playerStats[Attribute.SSDC].total(), this.playerStats[Attribute.SAM].total());
		}
		int n = 0;
		for (Attribute a : this.abilityScores){
			n++;
			s += a;
			if (n == 3) s+= "\n";
		}
		return s;
	}
	
	public String toExport(){
		String s = 
				this.toString()+"\n"+this.playerInventory.toString()+"\n"+this.skillsToString()+"\n"+this.spellBookToString()+
				"\n"+this.spellSlotsToString();
		s.replaceAll("\n", System.getProperty("line.separator"));
		return s;
	}
	
	public double getCurrHP() {
		return currHP;
	}

	public void setCurrHP(double currHP) {
		this.currHP = currHP;
	}

	
}
