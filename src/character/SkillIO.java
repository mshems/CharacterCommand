package character;

import app.CharacterCommand;
import utils.Help;
import utils.Message;

public class SkillIO {

    public static void skills(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        String action;
        if (!CharacterCommand.tokens.isEmpty()) {
            skillsParser(pc);
        } else {
            Skill skill;
            boolean exit = false;
            while (!exit) {
                CharacterCommand.terminal.printOut("view | train | forget | expert | view all");
                action = CharacterCommand.terminal.queryString("Action: ",false).toLowerCase();
                switch (action) {
                    case "v":
                    case "view":
                        skill = CharacterCommand.getSkillByName();
                        if (skill != null) {
                            CharacterCommand.terminal.printOut(skill.toString());
                        }
                        exit = true;
                        break;
                    case "t":
                    case "train":
                        skill = CharacterCommand.getSkillByName();
                        if (skill != null) {
                            skill.train(pc);
                            CharacterCommand.terminal.printOut("Gained proficiency in " + skill.getName());
                        }
                        exit = true;
                        break;
                    case "f":
                    case "forget":
                        skill = CharacterCommand.getSkillByName();
                        if (skill != null) {
                            skill.untrain(pc);
                            CharacterCommand.terminal.printOut("Lost proficiency in " + skill.getName());
                        }
                        exit = true;
                        break;
                    case "e":
                    case "expert":
                        skill = CharacterCommand.getSkillByName();
                        if (skill != null) {
                            skill.expert(pc);
                            CharacterCommand.terminal.printOut("Gained expertise in " + skill.getName());
                        }
                        exit = true;
                        break;
                    case "va":
                    case "viewall":
                    case "view all":
                        CharacterCommand.terminal.printOut(pc.skillsToString());
                        exit = true;
                        break;
                    case "cancel":
                        exit = true;
                        break;
                    default:
                        CharacterCommand.terminal.printOut(Message.ERROR_SYNTAX+"\nEnter 'cancel' to exit");
                        exit = false;
                        break;
                }
            }
        }
    }

    private static void skillsParser(PlayerCharacter pc) {
        StringBuilder nameBuilder = new StringBuilder();
        Skill skill;
        boolean expert = false;
        boolean forget = false;
        boolean train = false;
        boolean view = false;
        boolean viewAll = false;
        boolean help = false;

        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-e":
                case "--expert":
                    expert = true;
                    CharacterCommand.tokens.pop();
                    break;
                case "-t":
                case "--train":
                    train = true;
                    CharacterCommand.tokens.pop();
                    break;
                case "-f":
                case "--forget":
                    forget = true;
                    CharacterCommand.tokens.pop();
                    break;
                case "-v":
                case "--view":
                    view = true;
                    CharacterCommand.tokens.pop();
                    break;
                case "-va":
                case "--viewall":
                    CharacterCommand.tokens.pop();
                    viewAll = true;
                    break;
                case "--help":
                    CharacterCommand.tokens.pop();
                    help = true;
                    break;
                default:
                    if (CharacterCommand.tokens.peek().startsWith("-")) {
                        CharacterCommand.terminal.printOut("ERROR: Invalid flag '" + CharacterCommand.tokens.pop() + "'");
                    } else {
                        nameBuilder.append(CharacterCommand.tokens.pop());
                        nameBuilder.append(" ");
                    }
                    break;
            }
        }
        if (help) {
            System.out.println(Help.SKILL);
        } else {
            String skillName = nameBuilder.toString().trim();
            skill = pc.getSkill(skillName);
            if (viewAll) {
                CharacterCommand.terminal.printOut(pc.skillsToString());
            }
            if (skill != null) {
                if (!forget) {
                    if (expert) {
                        skill.expert(pc);
                        CharacterCommand.terminal.printOut("Gained expertise in " + skill.getName());
                    } else if (train) {
                        skill.train(pc);
                        CharacterCommand.terminal.printOut("Gained proficiency in " + skill.getName());
                    }
                } else {
                    skill.untrain(pc);
                    CharacterCommand.terminal.printOut("Lost proficiency in " + skill.getName());
                }
                if (view) {
                    CharacterCommand.terminal.printOut(skill.toString());
                }
            } else {
                if (skillName.equals("") && !viewAll) {
                    CharacterCommand.terminal.printOut("ERROR: Missing argument: skill name");
                } else {
                    if (!viewAll) {
                        CharacterCommand.terminal.printOut("ERROR: No skill by that name");
                        //System.out.println(Message.ERROR_SYNTAX);
                    }
                }
            }
        }
    }
}
