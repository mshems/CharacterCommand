package core.character;

import core.TaggedObject;
import core.character.stats.AbilityScoreTable;
import core.character.stats.CounterStat;
import core.character.stats.LinkedStat;
import core.character.stats.Stat;

import java.util.HashMap;

public abstract class AbstractCharacter extends TaggedObject{
    private String characterName;
    AbilityScoreTable abilities;
    private HashMap<String, Stat> statBlock;

    AbstractCharacter(String name){
        characterName = name;
        initStatBlock();
    }

    private void initStatBlock(){
        abilities = new AbilityScoreTable();
        statBlock = new HashMap<>();
        for(String key:abilities.keySet()){
            statBlock.put(key, abilities.get(key));
        }
        statBlock.put("lvl",  new Stat ("Level", 1));
        statBlock.put("nac",  new Stat("Natural Armor", 10));
        statBlock.put("spd",  new Stat("Speed", 30));
        statBlock.put("ac",   new Stat("Armor Class",10+abilities.get("dex").getModifier()));

        statBlock.put("hp",   new CounterStat("Hit Points", 3));
        //statBlock.put("ap",   new CounterStat("Ability Points", 0));

        statBlock.put("per",  new LinkedStat("Passive Perception", this::PER));
        statBlock.put("ini",  new LinkedStat("Initiative", this::INI));
    }

    public double PB(){
        return Math.floor(statBlock.get("lvl").getTotal()/4)+2;
    }

    public CounterStat HP(){
        return (CounterStat) statBlock.get("hp");
    }

    public Stat AC(){
        return statBlock.get("ac");
    }

    public Stat LVL() {
        return statBlock.get("lvl");
    }

    public double PER(){
        return 10+abilities.WIS().getModifier();
    }

    public double INI(){
        return abilities.get("dex").getModifier();
    }


    public AbilityScoreTable getAbilities(){
        return abilities;
    }

    public HashMap<String, Stat> getStatBlock(){
        return statBlock;
    }

    public Stat getStat(String key){
        return statBlock.get(key.toLowerCase());
    }

    public String getName(){
        return characterName;
    }

    public void setName(String name){
        this.characterName = name;
    }
}
