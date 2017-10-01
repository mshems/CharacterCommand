package character;

import app.CharacterCommand;
import utils.Help;

public class StatIO {

    public static void stats(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            statsParser(pc);
        } else {
            CharacterCommand.terminal.printOut("view | edit | cancel");
            //System.out.println("view | edit | cancel");
            boolean exit = false;
            while (!exit) {
                //System.out.print("Action: ");
                //String action = scanner.nextLine().toLowerCase().trim();
                String action = CharacterCommand.terminal.queryString("Action: ", false).toLowerCase();
                switch (action) {
                    case "v":
                    case "view":
                        Stat stat = CharacterCommand.getStatByName();
                        if (stat != null) {
                            CharacterCommand.terminal.printOut(stat.detailString());
                            //System.out.println(stat.detailString());
                        }
                        exit = true;
                        break;
                    case "e":
                    case "edit":
                        StatEditor.edit(pc);
                        exit = true;
                        break;
                    case "cancel":
                        exit = true;
                        break;
                }
            }
        }
    }

    private static void statsParser(PlayerCharacter pc) {
        StringBuilder nameBuilder = new StringBuilder();
        boolean help = false;
        boolean view = true;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-e":
                case "--edit":
                    StatEditor.edit(pc);
                    view = false;
                    //tokens.pop();
                    break;
                case "--help":
                    CharacterCommand.tokens.pop();
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
            System.out.println(Help.STATS);
        } else {
            String statName = nameBuilder.toString().trim();
            Stat stat = CharacterCommand.activeChar.getStat(statName);
            if (stat != null && view) {
                System.out.println(stat.detailString());
            }
        }
    }
}
