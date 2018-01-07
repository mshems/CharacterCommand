package core.character;

import core.character.behaivor.*;
import core.character.inventory.GearSet;
import core.character.inventory.Inventory;
import core.character.skills.Skill;
import core.character.skills.SkillSet;
import core.character.stats.CounterStat;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PlayerCharacter extends MagicCharacter implements Serializable{



    private SkillSet skillSet;
    private Inventory inventory;
    private LinkedHashMap<String, CounterStat> counters;
    private GearSet gearSet;
    private HashSet<String> proficiencies;

    public EquipBehavior equipBehavior = new EquipBehavior(this);
    public HealthBehavior healthBehavior = new HealthBehavior(this);
    public MagicBehavior magicBehavior = new MagicBehavior(this);
    public InventoryBehavior inventoryBehavior = new InventoryBehavior(this);
    public SkillBehavior skillBehavior = new SkillBehavior(this);
    public AttackBehavior attackBehavior = new AttackBehavior(this);

    public PlayerCharacter(String characterName){
        super(characterName);
        skillSet = new SkillSet(this);
        inventory = new Inventory();
        gearSet = new GearSet();
        counters = new LinkedHashMap<>();
        proficiencies = new HashSet<>();
    }

    public void levelUp(int amt){
        this.LVL().incrementBaseValue(amt);
    }

    public void addCounter(CounterStat c){
        counters.put(c.getStatName().toLowerCase(), c);
    }

    public void removeCounter(String key){
        counters.remove(key);
    }

    public CounterStat getCounter(String key){
        return counters.get(key);
    }

    public void addProficiency(String prof){
        proficiencies.add(prof);
    }

    public boolean hasProficiency(String prof){
        return proficiencies.contains(prof);
    }

    public Skill getSkill(String key){
        return skillSet.getSkill(key);
    }

    public GearSet getGearSet() {
        return gearSet;
    }

    public Inventory getInventory(){
        return inventory;
    }

    public LinkedHashMap<String, Skill> getSkillSet() {
        return skillSet;
    }

    @Override
    public String toString() {
        return String.format("" +
                        "%s -- Level %.0f %s %s\n" +
                        "HP: %s  AC: %.0f\n" +
                        "%s %.0f (%+.0f)  %s %.0f (%+.0f)  %s %.0f (%+.0f)\n" +
                        "%s %.0f (%+.0f)  %s %.0f (%+.0f)  %s %.0f (%+.0f)",
                this.characterName, this.LVL().getTotal(),
                this.characterRace, this.characterClass,
                this.HP().countToString(), this.AC().getTotal(),
                "STR", this.getAbilities().STR().getTotal(), this.getAbilities().STR().getModifier(),
                "DEX", this.getAbilities().DEX().getTotal(), this.getAbilities().DEX().getModifier(),
                "CON", this.getAbilities().CON().getTotal(), this.getAbilities().CON().getModifier(),
                "WIS", this.getAbilities().WIS().getTotal(), this.getAbilities().WIS().getModifier(),
                "INT", this.getAbilities().INT().getTotal(), this.getAbilities().INT().getModifier(),
                "CHA", this.getAbilities().CHA().getTotal(), this.getAbilities().CHA().getModifier()
        );
    }
}
