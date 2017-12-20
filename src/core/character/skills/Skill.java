package core.character.skills;

import core.character.PlayerCharacter;
import core.character.stats.Ability;

public class Skill {
    private String skillName;
    private Ability skillAbility;
    private SkillLevel skillLevel;

    public Skill(String name, Ability ability){
        this.skillName = name;
        this.skillAbility = ability;
        this.skillLevel = SkillLevel.UNTRAINED;
    }

    public void setSkillLevel(SkillLevel skillLevel){
        this.skillLevel = skillLevel;
    }

    public double getSkillMod(PlayerCharacter pc){
        switch (skillLevel){
            case UNTRAINED:
                return skillAbility.getModifier();
            case PROFICIENT:
                return skillAbility.getModifier()+pc.getStat("pb").getTotal();
            case EXPERTISE:
                return skillAbility.getModifier()+(pc.getStat("pb").getTotal()*2);
            default:
                return 0;
        }
    }

    public String toString(){
        return skillName;
    }
}
