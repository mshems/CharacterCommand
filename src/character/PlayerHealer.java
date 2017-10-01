package character;

import app.CharacterCommand;
import character.PlayerCharacter;
import utils.Help;
import utils.Message;

public class PlayerHealer {
    public static void heal(PlayerCharacter activeChar) {
        String command = CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            heal(activeChar, command);
        } else {
            Integer amount;
            if (command.equals("heal")) {
                amount = CharacterCommand.terminal.queryInteger("HP gained: ", false);
            } else {
                amount = CharacterCommand.terminal.queryInteger("HP lost: ", false);
            }
            heal(activeChar, command, amount);
        }
    }

    private static void heal(PlayerCharacter activeChar, String command) {
        Integer amount = null;
        boolean healAll = false;
        boolean help = false;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-hp":
                case "--health":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        System.out.println(Message.ERROR_NO_ARG + ": amount");
                    } else {
                        amount = CharacterCommand.getIntToken();
                    }
                    break;
                case "--all":
                    CharacterCommand.tokens.pop();
                    healAll = true;
                    break;
                case "--help":
                    CharacterCommand.tokens.pop();
                    help = true;
                    break;
                default:
                    if (CharacterCommand.tokens.peek().startsWith("-")) {
                        System.out.println("ERROR: Invalid flag '" + CharacterCommand.tokens.pop() + "'");
                    } else {
                        CharacterCommand.tokens.pop();
                    }
                    break;
            }
        }
        if (help) {
            if (command.equals("heal")) {
                System.out.println(Help.HEAL);
            }
            if (command.equals("hurt")) {
                System.out.println(Help.HURT);
            }
        } else if (healAll) {
            healAll(activeChar, command);
        } else if (amount != null) {
            heal(activeChar, command, amount);
        } else {
            System.out.println(Message.ERROR_SYNTAX);
        }
    }

    private static void heal(PlayerCharacter activeChar, String command, int amount) {
        switch (command) {
            case "heal":
                activeChar.heal(amount);
                System.out.println(String.format("Gained %d HP", amount));
                break;
            case "hurt":
                activeChar.hurt(amount);
                System.out.println(String.format("Lost %d HP", amount));
                break;
            default:
                break;
        }
    }

    private static void healAll(PlayerCharacter activeChar, String command) {
        switch (command) {
            case "heal":
                activeChar.fullHeal();
                System.out.println("HP fully restored");
                break;
            case "hurt":
                activeChar.fullHurt();
                System.out.println("No HP remaining");
                break;
            default:
                break;
        }
    }
}
