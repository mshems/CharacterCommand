package character;
import magic.Spell;
import magic.SpellBook;
import magic.SpellSlot;
import items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@SuppressWarnings("unused")
public class PlayerCharacter implements Serializable{
	/**
	 * version 0.1.0
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String className;
	private String raceName;
	private Stat level;
	private LinkedHashMap<String, Ability> abilities;
	private LinkedHashMap<String, Stat> attributes;
	private LinkedHashMap<String, Skill> skills;
	private LinkedHashMap<String, Stat> allStats;
	private Inventory inventory;
	private SpellBook spellBook;
	private SpellSlot[] spellSlots;
	private boolean unPrepOnCast;
	
	public PlayerCharacter(String name, String raceName, String className){
		this.name=name;
		this.className = className;
		this.raceName = raceName;
		level = new Stat("Level", 1);
		initCharacter();
	}
	
	private void initCharacter(){
		inventory = new Inventory();
		spellBook = new SpellBook();
		unPrepOnCast = false;
		initAbilities();
		initAttributes();
		initAllStats();
		initSkills();
		initSpellSlots();	
	}
	
	private void initAbilities(){
		abilities = new LinkedHashMap<>();
		for(String s:Ability.ABILITY_NAMES){
			abilities.put(s.toLowerCase(), new Ability(s,10));
		}
	}
	
	private void initAttributes(){
		attributes = new LinkedHashMap<>();
		attributes.put("hp",new CounterStat("HP",10));
		attributes.put("ac",new Attribute("AC",10+this.abilities.get(Ability.DEX).getMod()));
		attributes.put("pb",new Stat ("Proficiency Bonus", 2));
		attributes.put("ini", new Stat("INI",this.abilities.get(Ability.DEX).getMod()));
		attributes.put("spd", new Stat("Speed",30));
		attributes.put("nac",new Stat("Natural Armor", 10));
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
		skills.put("str saves",new Skill("STR Saves",this.abilities.get(Ability.STR)));
		skills.put("athletics",new Skill("Athletics",this.abilities.get(Ability.STR)));
		skills.put("dex saves",new Skill("DEX Saves",this.abilities.get(Ability.DEX)));
		skills.put("acrobatics",new Skill("Acrobatics",this.abilities.get(Ability.DEX)));
		skills.put("sleight of hand",new Skill("Sleight of Hand",this.abilities.get(Ability.DEX)));
		skills.put("stealth",new Skill("Stealth",this.abilities.get(Ability.DEX)));
		skills.put("con saves",new Skill("CON Saves",this.abilities.get(Ability.CON)));
		skills.put("int saves",new Skill("INT Saves",this.abilities.get(Ability.INT)));
		skills.put("arcana",new Skill("Arcana",this.abilities.get(Ability.INT)));
		skills.put("history",new Skill("History",this.abilities.get(Ability.INT)));
		skills.put("investigation",new Skill("Investigation",this.abilities.get(Ability.INT)));
		skills.put("nature",new Skill("Nature",this.abilities.get(Ability.INT)));
		skills.put("religion",new Skill("Religion",this.abilities.get(Ability.INT)));
		skills.put("wis saves",new Skill("WIS Saves",this.abilities.get(Ability.WIS)));
		skills.put("animal handling",new Skill("Animal Handling",this.abilities.get(Ability.WIS)));
		skills.put("insight",new Skill("Insight",this.abilities.get(Ability.WIS)));
		skills.put("medicine",new Skill("Medicine",this.abilities.get(Ability.WIS)));
		skills.put("perception",new Skill("Perception",this.abilities.get(Ability.WIS)));
		skills.put("survival",new Skill("Survival",this.abilities.get(Ability.WIS)));
		skills.put("cha saves",new Skill("CHA Saves",this.abilities.get(Ability.CHA)));
		skills.put("deception",new Skill("Deception",this.abilities.get(Ability.CHA)));
		skills.put("intimidation",new Skill("Intimidation",this.abilities.get(Ability.CHA)));
		skills.put("performance",new Skill("Performance",this.abilities.get(Ability.CHA)));
		skills.put("persuasion",new Skill("Persuasion",this.abilities.get(Ability.CHA)));
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
		level.incrementBaseVal();
		updateProficiency();
	}
	public void level(int lvl){
		level.setBaseVal(lvl);
		updateProficiency();
	}
	
	private void updateProficiency(){
		double lvl = level.getTotal();
		double pb = Math.floor(lvl/4)+2;
		this.attributes.get(Attribute.PB).setBaseVal(pb);
		/*if (this.level.getBaseVal() > 16){
			this.attributes.get("pb").setBaseVal(6);
		} else if (this.level.getBaseVal() > 12){
			this.attributes.get("pb").setBaseVal(5);
		} else if (this.level.getBaseVal() > 8){
			this.attributes.get("pb").setBaseVal(4);
		} else if (this.level.getBaseVal() > 4){
			this.attributes.get("pb").setBaseVal(3);
		}*/
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
				this.inventory.remove(i);
			}
		}
	}
	
	public void removeItem(Item i){
		this.inventory.remove(i);
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
        return level;
    }

    public void setLevel(Stat level){
        this.level = level;
    }

    public LinkedHashMap<String, Ability> getAbilities(){
        return abilities;
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

    public boolean isUnPrepOnCast(){
        return unPrepOnCast;
    }

    public void setUnPrepOnCast(boolean unPrepOnCast){
        this.unPrepOnCast = unPrepOnCast;
    }

    public Stat getStat(String statName){
        return this.allStats.get(statName.toLowerCase());
    }

    public String skillsToString(){
        String newLine = System.lineSeparator();
        String s = "Skills: ";
        for(Skill skill:skills.values()){
            s += newLine+"- "+skill;
        }
        return s;
    }

    @Override
	public String toString(){
        String newLine = System.lineSeparator();
        //String s=String.format("\033[1;33m%s -- Level %.0f %s\033[0m\n", name, level.getTotal(), className);
		String s=String.format("%s -- Level %.0f %s %s"+newLine, name, level.getTotal(), raceName, className);
		s+=String.format("%s  %s  INI: %+.0f  SPD: %+.0f"+newLine, attributes.get("hp"), attributes.get("ac"), attributes.get("ini").getTotal(), attributes.get("spd").getTotal());
		int i=0;	
		for(String key:abilities.keySet()){
            s+=(abilities.get(key))+"  ";
            if (i == 2){
                s+=newLine;
            }
            i++;
        }
		return s;
	}

	public String toTextFile(){
        String newLine = System.lineSeparator();
        return this+newLine+inventory+newLine+skillsToString()+newLine+spellBook;
    }
}
