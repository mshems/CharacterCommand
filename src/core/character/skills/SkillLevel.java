package core.character.skills;

public enum SkillLevel {
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
