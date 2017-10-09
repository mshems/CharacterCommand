package character;

import app.CharacterCommand;
import utils.Help;
import utils.Message;

public class StatIO {
    public static Stat getStatByName(PlayerCharacter pc) {
        Stat stat;
        while (true) {
            String statName = CharacterCommand.terminal.queryString("Stat name: ", false);
            if (statName.equalsIgnoreCase("cancel")) {
                return null;
            } else {
                stat = pc.getStat(statName);
                if (stat == null) {
                    int matches = 0;
                    for(String str:pc.getAllStats().keySet()){
                        if(str.startsWith(statName.toLowerCase())){
                            stat = pc.getAllStats().get(str);
                            matches++;
                        }
                    }
                    if(matches == 1){
                        return stat;
                    }
                    CharacterCommand.terminal.println(Message.MSG_NO_STAT);
                } else {
                    return stat;
                }
            }
        }
    }
    public static void stats(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            statsParser(pc);
        } else {
            CharacterCommand.terminal.println("view | edit | cancel");
            //CharacterCommand.terminal.println("view | edit | cancel");
            boolean exit = false;
            while (!exit) {
                //CharacterCommand.terminal.print("Action: ");
                //String action = scanner.nextLine().toLowerCase().trim();
                String action = CharacterCommand.terminal.queryString("Action: ", false).toLowerCase();
                switch (action) {
                    case "v":
                    case "view":
                        Stat stat = getStatByName(pc);
                        if (stat != null) {
                            CharacterCommand.terminal.println(stat.detailString());
                            //CharacterCommand.terminal.println(stat.detailString());
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
                        CharacterCommand.terminal.println("ERROR: Invalid flag '" + CharacterCommand.tokens.pop() + "'");
                    } else {
                        nameBuilder.append(CharacterCommand.tokens.pop());
                        nameBuilder.append(" ");
                    }
                    break;
            }
        }
        if (help) {
            CharacterCommand.terminal.println(Help.STATS);
        } else {
            String statName = nameBuilder.toString().trim();
            Stat stat = CharacterCommand.getActiveChar().getStat(statName);
            if (stat != null && view) {
                CharacterCommand.terminal.println(stat.detailString());
            }
        }
    }
}
