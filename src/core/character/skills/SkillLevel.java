package core.character.skills;

import java.io.Serializable;

public enum SkillLevel implements Serializable{
    UNTRAINED, PROFICIENT, EXPERTISE;

    public SkillLevel parseSkillLevel(String str){
        switch (str){
            case "u":
            case "untrained":
                return UNTRAINED;
            case "p":
            case "proficient":
                return PROFICIENT;
            case "e":
            case "expertise":
                return EXPERTISE;
            default:
                return null;
        }
    }
}
