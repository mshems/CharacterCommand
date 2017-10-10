package app.io;

import app.CharacterCommand;
import app.core.character.PlayerCharacter;
import app.core.magic.Spell;
import app.utils.Help;
import app.utils.Message;

public class SpellIO {
    public static Spell getSpellByName(PlayerCharacter pc) {
        Spell spell;
        while (true) {
            String spellName = CharacterCommand.terminal.queryString("Spell name: ", false);
            if (spellName.equalsIgnoreCase("cancel")) {
                return null;
            } else {
                spell = pc.getSpell(spellName);
                if (spell == null) {
                    CharacterCommand.terminal.println(Message.MSG_NO_SPELL);
                } else {
                    return spell;
                }
            }
        }
    }
    public static void spells(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "--learn":
                    SpellBookIO.learn(pc);
                    break;
                case "--forget":
                    SpellBookIO.forget(pc);
                    break;
                case "--cast":
                    cast(pc);
                    break;
                case "--stats":
                    CharacterCommand.terminal.println(pc.spellStatsToString());
                    break;
                case "--slots":
                    SpellSlotIO.spellSlots(pc);
                    break;
                case "--help":
                    CharacterCommand.terminal.println(Help.SPELLS);
                    break;
                default:
                    CharacterCommand.terminal.println("Error: command syntax");
                    break;
            }
        } else {
            CharacterCommand.terminal.println("learn | forget | cast | book | slots | stats");
            String action = CharacterCommand.terminal.queryString("Action: ", false);
            switch (action.toLowerCase()){
                case "l":
                case "learn":
                    SpellBookIO.learn(pc);
                    break;
                case "f":
                case "forget":
                    SpellBookIO.forget(pc);
                    break;
                case "c":
                case "cast":
                    cast(pc);
                    break;
                case "b":
                case "book":
                    CharacterCommand.terminal.println(pc.getSpellBook().toString());
                    break;
                case "slots":
                    SpellSlotIO.spellSlots(pc);
                    break;
                case "stats":
                    CharacterCommand.terminal.println(pc.spellStatsToString());
                    break;
            }

        }
    }

    public static void cast(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            castParser(pc);
        } else {
            Integer castLevel = -1;
            Spell spell = getSpellByName(pc);
            if (spell != null) {
                if (!spell.isCantrip()) {
                    castLevel = CharacterCommand.terminal.queryInteger("Cast at level: ", false);
                }
                castSpell(pc, spell, spell.getSpellName(), castLevel);
            }
        }
    }

    private static void castParser(PlayerCharacter pc) {
        Spell spell;
        StringBuilder nameBuilder = new StringBuilder();
        Integer castLevel = -1;
        boolean help = false;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-l":
                case "--level":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": level");
                        castLevel = null;
                    } else {
                        castLevel = CharacterCommand.getIntToken();
                    }
                    break;
                case "--help":
                    help = true;
                    CharacterCommand.tokens.pop();
                    break;
                default:
                    if (CharacterCommand.tokens.peek().startsWith("-")) {
                        CharacterCommand.terminal.println("ERROR: Invalid flag '" + CharacterCommand.tokens.pop() + "'");
                    } else {
                        nameBuilder.append(CharacterCommand.tokens.pop());
                        nameBuilder.append(" ");
                    }
            }
        }
        if (help) {
            CharacterCommand.terminal.println(Help.CAST);
        } else {
            String spellName = nameBuilder.toString().trim();
            spell = pc.getSpell(spellName);
            castSpell(pc, spell, spellName, castLevel);
        }
    }

    private static void castSpell(PlayerCharacter pc, Spell spell, String spellName, Integer castLevel) {
        if (spell != null && castLevel != null) {
            if (castLevel == -1) {
                castLevel = spell.getSpellLevel();
            }
            castLevel = pc.cast(spell, castLevel);
            if (spell.isCantrip()) {
                CharacterCommand.terminal.println("Cast '" + spell.getSpellName() + "' as a cantrip");
            } else {
                if (castLevel < 0) {
                    CharacterCommand.terminal.println("No level " + (-castLevel) + " spell slots remaining");
                } else {
                    CharacterCommand.terminal.println("Cast '" + spell.getSpellName() + "' at level " + castLevel);
                }
            }
        } else {
            if (spellName.equals("")) {
                CharacterCommand.terminal.println("ERROR: Missing argument: spell name");
            } else if (spell == null) {
                CharacterCommand.terminal.println("No spell by that name");
            }
        }
    }
}
