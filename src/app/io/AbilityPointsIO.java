package app.io;

import app.CharacterCommand;
import app.core.character.CounterStat;
import app.core.character.PlayerCharacter;
import app.utils.Help;
import app.utils.Message;

public class AbilityPointsIO {
    public static void abilityPoints(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            abilityPointsParser(pc);
        } else {
            CharacterCommand.terminal.println("use | get | set");
            String action = CharacterCommand.terminal.queryString("Action: ",false).toLowerCase();
            boolean exit = false;
            int amount;
            while (!exit) {
                switch (action) {
                    case "u":
                    case "use":
                        amount = CharacterCommand.terminal.queryInteger("Ability Points to use: ", false);
                        ((CounterStat) pc.getStat("ap")).countDown(amount);
                        CharacterCommand.terminal.println("Used " + amount + " ability points");
                        exit = true;
                        break;
                    case "g":
                    case "get":
                        amount = CharacterCommand.terminal.queryInteger("Ability Points gained: ", false);
                        ((CounterStat) pc.getStat("ap")).countUp(amount);
                        CharacterCommand.terminal.println("Gained " + amount + " ability points");
                        exit = true;
                        break;
                    case "s":
                    case "set":
                        amount = CharacterCommand.terminal.queryInteger("Ability Points maximum: ", false);
                        ((CounterStat) pc.getStat("ap")).setMaxVal(amount);
                        CharacterCommand.terminal.println("Ability Point maximum now " + amount);
                        exit = true;
                        break;
                    case "cancel":
                        exit = true;
                        break;
                }
            }
        }
    }

    private static void abilityPointsParser(PlayerCharacter pc) {
        boolean use = false;
        boolean get = false;
        boolean set = false;
        boolean help = false;
        boolean all = false;
        Integer count = 1;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-u":
                case "--use":
                    CharacterCommand.tokens.pop();
                    use = true;
                    get = false;
                    set = false;
                    break;
                case "-g":
                case "--get":
                    CharacterCommand.tokens.pop();
                    get = true;
                    use = false;
                    set = false;
                    break;
                case "-s":
                case "--set":
                    CharacterCommand.tokens.pop();
                    set = true;
                    get = false;
                    use = false;
                    break;
                case "-c":
                case "--count":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": count");
                    } else {
                        count = CharacterCommand.getIntToken();
                    }
                    break;
                case "--all":
                    CharacterCommand.tokens.pop();
                    all = true;
                    break;
                case "--help":
                    CharacterCommand.tokens.pop();
                    help = true;
                    break;
                default:
                    if (CharacterCommand.tokens.peek().startsWith("-")) {
                        CharacterCommand.terminal.println("ERROR: Invalid flag '" + CharacterCommand.tokens.pop() + "'");
                    }
                    break;
            }
        }
        if (help) {
            CharacterCommand.terminal.println(Help.AP);
        } else {
            if (count != null) {
                CounterStat ap = ((CounterStat) pc.getStat("ap"));
                if (use) {
                    if (all) {
                        ap.setCurrVal(0);
                        CharacterCommand.terminal.println("Used all ability points");
                    } else {
                        ap.countDown(count);
                        CharacterCommand.terminal.println("Used " + count + " ability points");
                    }
                } else if (get) {
                    if (all) {
                        ap.setCurrVal(ap.getMaxVal());
                        CharacterCommand.terminal.println("Gained all ability points");
                    } else {
                        ap.countUp(count);
                        CharacterCommand.terminal.println("Gained " + count + " ability points");
                    }
                } else if (set) {
                    ap.setMaxVal(count);
                    CharacterCommand.terminal.println("Ability Point maximum now " + count);
                } else {
                    CharacterCommand.terminal.println(Message.ERROR_SYNTAX);
                }
            }
        }
    }
}
