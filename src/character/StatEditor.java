package character;

import app.CharacterCommand;
import utils.Help;
import utils.Message;

public class StatEditor {
    public static void edit(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            editParser(pc);
        } else {
            Stat stat = StatIO.getStatByName(pc);
            if (stat != null) {
                int val = CharacterCommand.terminal.queryInteger(stat.getName()+" value: ", false);
                //int val = getValidInt(stat.getName() + " value: ");
                stat.setBaseVal(val);
                pc.updateStats();
                System.out.println("Updated " + stat.getName());
            }
        }
    }

    private static void editParser(PlayerCharacter pc) {
        StringBuilder nameBuilder = new StringBuilder();
        Integer bonus = null;
        Integer value = null;
        boolean help = false;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-v":
                case "--value":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        System.out.println(Message.ERROR_NO_ARG + ": stat value");
                    } else {
                        value = CharacterCommand.getIntToken();
                    }
                    break;
                case "-b":
                case "--bonus":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        System.out.println(Message.ERROR_NO_ARG + ": bonus");
                    } else {
                        bonus = CharacterCommand.getIntToken();
                    }
                    break;
                case "--help":
                    help = true;
                    break;
                default:
                    if (CharacterCommand.tokens.peek().startsWith("-")) {
                        System.out.println("ERROR: Invalid flag '" + CharacterCommand.tokens.pop() + "'");
                    } else {
                        nameBuilder.append(CharacterCommand.tokens.pop());
                        nameBuilder.append(" ");
                    }
                    break;
            }
        }
        if (help) {
            System.out.println(Help.EDIT);
        } else {
            String statName = nameBuilder.toString().trim();
            Stat stat = pc.getStat(statName);
            if (value != null) {
                if (stat != null) {
                    if (bonus != null) {
                        stat.setBonusVal(bonus);
                        System.out.println("Updated " + stat.getName());
                    }
                    stat.setBaseVal(value);
                    pc.updateStats();
                    System.out.println("Updated " + stat.getName());
                } else {
                    System.out.println(Message.MSG_NO_STAT);
                }
            } else {
                System.out.println(Message.ERROR_INPUT);
            }
        }
    }
}
