package core.character.behaivor;

import core.character.PlayerCharacter;
import core.character.skills.Skill;
import core.character.skills.SkillLevel;

public class SkillBehavior extends Behavior {
    public SkillBehavior(PlayerCharacter pc) {
        super(pc);
    }

    public boolean trainSkill(String k, SkillLevel skillLevel){
        Skill skill = pc.getSkill(k);
        if(skill!=null){
            skill.setSkillLevel(skillLevel);
            return true;
        } else return false;
    }
}
