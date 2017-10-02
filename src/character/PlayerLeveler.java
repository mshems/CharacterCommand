package character;

import app.CharacterCommand;
import utils.Help;
import utils.Message;

import java.util.LinkedList;

public class PlayerLeveler {
    public static void levelUp(PlayerCharacter activeChar) {
        LinkedList<String> tokens = CharacterCommand.tokens;
        tokens.pop();
        boolean help = false;
        if (tokens.isEmpty()) {
            activeChar.levelUp();
            CharacterCommand.terminal.println(
                    String.format("%s is now level %.0f", activeChar.getName(), activeChar.getLevel().getBaseVal())
            );
        } else {
            Integer level = null;
            while (!tokens.isEmpty()) {
                switch (tokens.peek()) {
                    case "-l":
                    case "--level":
                        tokens.pop();
                        if (tokens.isEmpty()) {
                            CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": level");
                        } else {
                            level = CharacterCommand.getIntToken();
                        }
                        break;
                    case "--help":
                        tokens.pop();
                        help = true;
                        break;
                    default:
                        if (tokens.peek().startsWith("-")) {
                            CharacterCommand.terminal.println("ERROR: Invalid flag '" + tokens.pop() + "'");
                        } else {
                            tokens.pop();
                        }
                        break;
                }
            }
            if (!help) {
                if (level != null) {
                    activeChar.levelUp(level);
                    CharacterCommand.terminal.println(String.format(
                            "%s is now level %.0f", activeChar.getName(), activeChar.getLevel().getBaseVal())
                    );
                } else {
                    Message.errorMessage(CharacterCommand.terminal, Message.ERROR_INPUT);
                }
            } else {
                CharacterCommand.terminal.println(Help.LEVELUP);
            }
        }
    }

}
