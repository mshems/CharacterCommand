package core.character.skills;

import core.character.PlayerCharacter;

import java.util.LinkedHashMap;

public class SkillSet extends LinkedHashMap<String, Skill> {
    
    public SkillSet(PlayerCharacter pc){
        this.put("strength saves",      new Skill("STR Saves",       pc.getAbilities().STR()));
        this.put("athletics",           new Skill("Athletics",       pc.getAbilities().STR()));
        this.put("dexterity saves",     new Skill("DEX Saves",       pc.getAbilities().DEX()));
        this.put("acrobatics",          new Skill("Acrobatics",      pc.getAbilities().DEX()));
        this.put("sleight of hand",     new Skill("Sleight of Hand", pc.getAbilities().DEX()));
        this.put("stealth",             new Skill("Stealth",         pc.getAbilities().DEX()));
        this.put("constitution saves",  new Skill("CON Saves",       pc.getAbilities().CON()));
        this.put("intelligence saves",  new Skill("INT Saves",       pc.getAbilities().INT()));
        this.put("arcana",              new Skill("Arcana",          pc.getAbilities().INT()));
        this.put("history",             new Skill("History",         pc.getAbilities().INT()));
        this.put("investigation",       new Skill("Investigation",   pc.getAbilities().INT()));
        this.put("nature",              new Skill("Nature",          pc.getAbilities().INT()));
        this.put("religion",            new Skill("Religion",        pc.getAbilities().INT()));
        this.put("wisdom saves",        new Skill("WIS Saves",       pc.getAbilities().WIS()));
        this.put("animal handling",     new Skill("Animal Handling", pc.getAbilities().WIS()));
        this.put("insight",             new Skill("Insight",         pc.getAbilities().WIS()));
        this.put("medicine",            new Skill("Medicine",        pc.getAbilities().WIS()));
        this.put("perception",          new Skill("Perception",      pc.getAbilities().WIS()));
        this.put("survival",            new Skill("Survival",        pc.getAbilities().WIS()));
        this.put("charisma saves",      new Skill("CHA Saves",       pc.getAbilities().CHA()));
        this.put("deception",           new Skill("Deception",       pc.getAbilities().CHA()));
        this.put("intimidation",        new Skill("Intimidation",    pc.getAbilities().CHA()));
        this.put("performance",         new Skill("Performance",     pc.getAbilities().CHA()));
        this.put("persuasion",          new Skill("Persuasion",      pc.getAbilities().CHA()));
    }
    
    public void putAliases(Skill skill, String...aliases){
        if(this.containsValue(skill)){
            for(String a:aliases){
                this.put(a, skill);
            }
        }
    }

    public Skill getSkill(String k){
        String key = k.toLowerCase();
        if(get(key)!=null) return get(key);
        else {
            String match = "";
            int matches = 0;
            for(String s:keySet()){
                if(s.startsWith(key)){
                    matches++;
                    match = s;
                }
            }
            if(matches == 1){
                return get(match);
            }
        }
        return null;
    }
}
