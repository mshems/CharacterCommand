package core.character.stats;

import java.util.LinkedHashMap;

public class AbilityScoreTable extends LinkedHashMap<String, Ability>{

    public AbilityScoreTable(){
        this.put("str", new Ability("STR", 10));
        this.put("dex", new Ability("DEX", 10));
        this.put("con", new Ability("CON", 10));
        this.put("wis", new Ability("WIS", 10));
        this.put("int", new Ability("INT", 10));
        this.put("cha", new Ability("CHA", 10));
    }

    public Ability STR(){
        return this.get("str");
    }

    public Ability DEX(){
        return this.get("dex");
    }

    public Ability CON(){
        return this.get("con");
    }

    public Ability WIS(){
        return this.get("wis");
    }

    public Ability INT(){
        return this.get("int");
    }

    public Ability CHA(){
        return this.get("cha");
    }
}
