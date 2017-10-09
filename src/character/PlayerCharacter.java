package character;
import app.CharacterCommand;
import magic.Spell;
import magic.SpellBook;
import magic.SpellSlot;
import items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@SuppressWarnings("unused")
public class PlayerCharacter implements Serializable{
	private static final long serialVersionUID = CharacterCommand.VERSION;

	private String name;
	private String className;
	private String raceName;
	private String description;
	//private String notes;

	private LinkedHashMap<String, Ability> abilities;
	private LinkedHashMap<String, Stat> attributes;
	private LinkedHashMap<String, Skill> skills;
	private LinkedHashMap<String, Stat> allStats;
	private LinkedHashMap<String, Stat> magicStats;

	private Inventory inventory;
	private SpellBook spellBook;
	private SpellSlot[] spellSlots;
	private Ability spellAbility;

	private boolean unPrepOnCast;
	private boolean spellcaster;
	private boolean armored;

	public PlayerCharacter(String name, String raceName, String className){
		this.name=name;
		this.className = className;
		this.raceName = raceName;
		inventory = new Inventory();
		spellBook = new SpellBook();
		initAbilities();
		initAttributes();
		initAllStats();
		initSkills();
		initSpellSlots();
		unPrepOnCast = false;
		spellcaster = false;
		armored = false;
	}

	private void initAbilities(){
		abilities = new LinkedHashMap<>();
		for(String s:Ability.ABILITY_NAMES){
			abilities.put(s.toLowerCase(), new Ability(s,10));
		}
	}

	private void initAttributes(){
		attributes = new LinkedHashMap<>();
		attributes.put("lvl", new Stat("Level", 1));
		attributes.put("hp",new CounterStat("Hit Points",10));
		attributes.put("nac",new Stat("Natural Armor", 10));
		attributes.put("ac",new Attribute("Armor Class",attributes.get("nac").getBaseVal() + abilities.get(Ability.DEX).getMod()));
		attributes.put("pb",new Stat ("Proficiency Bonus", 2));
		attributes.put("ini", new Stat("Initiative", abilities.get(Ability.DEX).getMod()));
		attributes.put("spd", new Stat("Speed",30));
		attributes.put("ap", new CounterStat("Ability Points", 0));
		attributes.put("hitdice", new CounterStat("Hit Dice", 1));
		attributes.put("pper",new Stat ("Passive Perception",10 + abilities.get(Ability.WIS).getMod() ));
	}

	private void initAllStats(){
	    this.allStats = new LinkedHashMap<>();
	    for(String s:this.abilities.keySet()){
	        allStats.put(s, this.abilities.get(s));
        }
        for(String s:this.attributes.keySet()){
	        allStats.put(s, this.attributes.get(s));
        }
    }
	
	private void initSpellSlots(){
		this.spellSlots = new SpellSlot[]{
			new SpellSlot(0,0),
			new SpellSlot(1,0),
			new SpellSlot(2,0),
			new SpellSlot(3,0),
			new SpellSlot(4,0),
			new SpellSlot(5,0),
			new SpellSlot(6,0),
			new SpellSlot(7,0),
			new SpellSlot(8,0),
			new SpellSlot(9,0)
		};
	}
	
	private void initSkills(){
		skills = new LinkedHashMap<>();
		skills.put("str saves",new Skill("STR Saves",this.abilities.get(Ability.STR),this));
		skills.put("athletics",new Skill("Athletics",this.abilities.get(Ability.STR),this));
		skills.put("dex saves",new Skill("DEX Saves",this.abilities.get(Ability.DEX),this));
		skills.put("acrobatics",new Skill("Acrobatics",this.abilities.get(Ability.DEX),this));
		skills.put("sleight of hand",new Skill("Sleight of Hand",this.abilities.get(Ability.DEX),this));
		skills.put("stealth",new Skill("Stealth",this.abilities.get(Ability.DEX),this));
		skills.put("con saves",new Skill("CON Saves",this.abilities.get(Ability.CON),this));
		skills.put("int saves",new Skill("INT Saves",this.abilities.get(Ability.INT),this));
		skills.put("arcana",new Skill("Arcana",this.abilities.get(Ability.INT),this));
		skills.put("history",new Skill("History",this.abilities.get(Ability.INT),this));
		skills.put("investigation",new Skill("Investigation",this.abilities.get(Ability.INT),this));
		skills.put("nature",new Skill("Nature",this.abilities.get(Ability.INT),this));
		skills.put("religion",new Skill("Religion",this.abilities.get(Ability.INT),this));
		skills.put("wis saves",new Skill("WIS Saves",this.abilities.get(Ability.WIS),this));
		skills.put("animal handling",new Skill("Animal Handling",this.abilities.get(Ability.WIS),this));
		skills.put("insight",new Skill("Insight",this.abilities.get(Ability.WIS),this));
		skills.put("medicine",new Skill("Medicine",this.abilities.get(Ability.WIS),this));
		skills.put("perception",new Skill("Perception",this.abilities.get(Ability.WIS),this));
		skills.put("survival",new Skill("Survival",this.abilities.get(Ability.WIS),this));
		skills.put("cha saves",new Skill("CHA Saves",this.abilities.get(Ability.CHA),this));
		skills.put("deception",new Skill("Deception",this.abilities.get(Ability.CHA),this));
		skills.put("intimidation",new Skill("Intimidation",this.abilities.get(Ability.CHA),this));
		skills.put("performance",new Skill("Performance",this.abilities.get(Ability.CHA),this));
		skills.put("persuasion",new Skill("Persuasion",this.abilities.get(Ability.CHA),this));
	}

	public void initMagicStats(Ability spellAbility){
		this.spellAbility = spellAbility;
		magicStats = new LinkedHashMap<>();
		magicStats.put("ssdc", new Stat("Spell Save DC", 8 + attributes.get(Attribute.PB).getTotal()+spellAbility.getMod()));
		magicStats.put("sam", new Stat("Spell Attack Mod",attributes.get(Attribute.PB).getTotal()+spellAbility.getMod()));
		allStats.put("ssdc", magicStats.get("ssdc"));
		allStats.put("sam", magicStats.get("sam"));
	}


	private void updateProficiency(){
		double lvl = attributes.get("lvl").getTotal();
		double pb = Math.floor(lvl/4)+2;
		this.attributes.get(Attribute.PB).setBaseVal(pb);
		updateSkills();
		updateStats();
	}

	private void updateSkills(){
		for(Skill skill:skills.values()){
			skill.update(this);
		}
	}

	public void updateStats(){
		attributes.get("ini").setBaseVal(abilities.get(Ability.DEX).getMod());
		attributes.get("pper").setBaseVal(10 + abilities.get(Ability.WIS).getMod());
		attributes.get("hitdice").setBaseVal(attributes.get("lvl").getTotal());
		if(spellcaster){
			magicStats.get("ssdc").setBaseVal(8 + attributes.get(Attribute.PB).getTotal() + spellAbility.getMod());
			magicStats.get("sam").setBaseVal(attributes.get(Attribute.PB).getTotal() + spellAbility.getMod());
		}
	}
	
	public void heal(int amt){
		CounterStat hp = (CounterStat)this.attributes.get(Attribute.HP);
		hp.countUp(amt);
	}
	public void hurt(int amt){
		CounterStat hp = (CounterStat)this.attributes.get(Attribute.HP);
		hp.countDown(amt);
	}
	public void fullHeal(){
		CounterStat hp = (CounterStat)this.attributes.get(Attribute.HP);
		hp.setCurrVal(hp.getMaxVal());
	}
	public void fullHurt(){
		CounterStat hp = (CounterStat)this.attributes.get(Attribute.HP);
		hp.setCurrVal(0);
	}
	
	public void levelUp(){
		attributes.get("lvl").incrementBaseVal();
		updateProficiency();
	}
	public void levelUp(int lvl){
		attributes.get("lvl").setBaseVal(lvl);
		updateProficiency();
	}
	
	public void equip(Item i){
		i.equip(this);
	}
	
	public void dequip(Item i){
		i.dequip(this);
	}
	
	public Integer cast(Spell spell, int lvl){
		if(lvl > spell.getSpellLevel() || spell.isCantrip() || lvl <= 0){
			lvl = spell.getSpellLevel();
		}
		if(spellSlots[lvl].getCurrVal() > 0 && !spell.isCantrip() ){
			spellSlots[lvl].countDown();
			if(unPrepOnCast){
				spell.unprep();
			}
		} else if (spell.isCantrip()){
			return Spell.CANTRIP;
		} else {
			return -lvl;
		}
		return lvl;
	}

	public void addNewItem(Item item){
		this.inventory.add(item);
	}

	public void addCurrency(int coin, int amt){
		this.inventory.addCurrency(coin, amt);
	}

	public void addDropItem(Item i, int amount){
		if (i != null){
			int c = i.getCount()+amount;
			if (c>0){
				i.setCount(c);
			} else {
				if(i.isEquippable() && i.isEquipped()){
					i.dequip(this);
				}
				this.inventory.remove(i);
			}
		}
	}
	
	public void removeItem(Item i){
		if(i.isEquippable() && i.isEquipped()){
			i.dequip(this);
		}
		this.inventory.remove(i);
	}

    public String spellStatsToString(){
		//String newLine = System.lineSeparator();
		return String.format("Spell Save DC: %.0f  Spell Atk Mod: %.0f", magicStats.get("ssdc").getTotal(), magicStats.get("sam").getTotal());

	}

    public String spellSlotsToString(){
		String newLine = System.lineSeparator();
		String s="---- SPELL SLOTS -------------- ";
		for(SpellSlot spellSlot:spellSlots){
			if(spellSlot.getMaxVal()>0 && spellSlot.getLevel()>0){
				s += newLine + spellSlot;
			}
		}
    	return s+newLine+"-------------------------------";
	}

    public String skillsToString(){
        String newLine = System.lineSeparator();
        String s = "---- SKILLS --------------------";
        for(Skill skill:skills.values()){
            switch(skill.getName().toLowerCase()){
//				case "str saves":
				case "dex saves":
				case "con saves":
				case "wis saves":
				case "int saves":
				case "cha saves":
					s+=newLine;
					break;
			}
        	s += newLine+" - "+skill;
        }
        return s+newLine+"--------------------------------";
    }

    @Override
	public String toString(){
        String newLine = System.lineSeparator();
        //String s=String.format("\033[1;33m%s -- Level %.0f %s\033[0m\n", name, level.getTotal(), className);
		//String s=String.format("%s -- Level %.0f %s %s"+newLine, name, attributes.get("lvl").getTotal(), raceName, className);

        String s = "---- CHARACTER -----------------"+newLine;
		s+=String.format(" %s -- Level %.0f %s %s"+newLine, name, attributes.get("lvl").getTotal(), raceName, className);
		s+=String.format(" | %s  %s"+newLine+" | INI: %+.0f  SPD: %.0f  PB: %+.0f  PER: %.0f"+newLine,
				attributes.get("hp"),
				attributes.get("ac"),
				attributes.get("ini").getTotal(),
				attributes.get("spd").getTotal(),
				attributes.get("pb").getTotal(),
				attributes.get("pper").getTotal()
		);
		int i=0;
		s+=" | ";
		for(String key:abilities.keySet()){
            s+=(abilities.get(key))+"  ";
            if (i == 2){
                s+=newLine+" | ";
            }
            i++;
        }
        if(attributes.get("ap").getTotal()>0){
            s+=newLine+" | "+attributes.get("ap")+"  ";
        }
        if(spellcaster){
            s+="\n | "+spellStatsToString();
        }
		return s+newLine+"--------------------------------";
	}

	public String toTextFile(){
        String newLine = System.lineSeparator();
        String s = this+newLine+skillsToString()+newLine+inventory;
        if(!spellBook.isEmpty()&&spellcaster){
        	s+=newLine+spellBook;
        	s+=newLine+spellSlotsToString();
		}
        return s;
    }

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getClassName(){
		return className;
	}

	public void setClassName(String className){
		this.className = className;
	}

	public Stat getLevel(){
		return attributes.get("lvl");
	}

	public boolean isSpellcaster(){
		return spellcaster;
	}

	public void setSpellcaster(boolean spellcaster){
		this.spellcaster = spellcaster;
	}

	public LinkedHashMap<String, Ability> getAbilities(){
		return abilities;
	}

	public Ability getAbility(String abilityName){
		return abilities.get(abilityName.toUpperCase());
	}

	public LinkedHashMap<String, Stat> getAttributes(){
		return attributes;
	}

	public Skill getSkill(String skillName){
		return this.skills.get(skillName.toLowerCase());
	}

	public Item getItem(String itemName){
		return this.inventory.get(itemName.toLowerCase());
	}

	public Item getCurrency(int coin){
		return this.inventory.getCurrency(coin);
	}

	public Inventory getInventory(){
		return this.inventory;
	}

	public ArrayList<Item> getCurrency(){
		return this.inventory.getCurrency();
	}

	public Spell getSpell(String spellName){
		return this.spellBook.get(spellName.toLowerCase());
	}

	public SpellBook getSpellBook(){
		return spellBook;
	}

	public SpellSlot[] getSpellSlots(){
		return spellSlots;
	}

	public void setSpellSlots(SpellSlot[] spellSlots){
		this.spellSlots = spellSlots;
	}

	public void setSpellAbility(Ability spellAbility){
		this.spellAbility = spellAbility;
	}

	public boolean isUnPrepOnCast(){
		return unPrepOnCast;
	}

	public void setUnPrepOnCast(boolean unPrepOnCast){
		this.unPrepOnCast = unPrepOnCast;
	}

	public Stat getStat(String statName){
		return this.allStats.get(statName.toLowerCase());
	}

    public LinkedHashMap<String, Stat> getAllStats() {
        return allStats;
    }

    public LinkedHashMap<String, Skill> getSkillSet() {
        return skills;
    }

    public boolean isArmored(){
		return armored;
	}

	public void setArmored(boolean armored){
		this.armored = armored;
	}
}
