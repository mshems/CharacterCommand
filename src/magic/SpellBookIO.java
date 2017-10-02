package magic;

import app.CharacterCommand;
import character.Ability;
import character.PlayerCharacter;
import utils.Help;
import utils.Message;

public class SpellBookIO {
    public static void learn(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            learnParser(pc);
        } else {
            String spellName;
            int spellLevel;
            spellName = CharacterCommand.terminal.queryString("Spell name: ",false);
            spellLevel = CharacterCommand.terminal.queryInteger("Spell level: ",false);
            learnSpell(pc, new Spell(spellName, spellLevel));
        }
    }

    private static void learnParser(PlayerCharacter pc) {
        StringBuilder nameBuilder = new StringBuilder();
        Integer spellLevel = null;
        boolean learnmagic = false;
        boolean help = false;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-l":
                case "--level":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": level");
                        spellLevel = null;
                    } else {
                        spellLevel = CharacterCommand.getIntToken();
                    }
                    break;
                case "-m":
                case "--magic":
                    CharacterCommand.tokens.pop();
                    learnmagic = true;
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
        if (learnmagic) {
            MagicIO.learnMagic(pc);
        }
        if (pc.isSpellcaster()) {
            if (spellLevel == null) {
                spellLevel = Spell.CANTRIP;    //default level
            }
            if (help) {
                CharacterCommand.terminal.println(Help.LEARN);
            } else {
                String spellName = nameBuilder.toString().trim();
                if (!spellName.isEmpty()) {
                    Spell spell = new Spell(spellName, spellLevel);
                    learnSpell(pc, spell);
                }
            }
        } else {
            CharacterCommand.terminal.println(Message.MSG_NOT_CAST);
        }
    }



    private static void learnSpell(PlayerCharacter pc, Spell spell) {
        pc.getSpellBook().learn(spell);
        if (spell.isCantrip()) {
            CharacterCommand.terminal.println("Learned cantrip " + spell.getSpellName());
        } else {
            CharacterCommand.terminal.println("Learned level " + spell.getSpellLevel() + " spell " + spell.getSpellName());
        }
    }

    public static void forget(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            forgetParser(pc);
        } else {
            Spell spell = SpellIO.getSpellByName(pc);
            if (spell != null) {
                forgetSpell(pc, spell);
            }
        }
    }

    private static void forgetParser(PlayerCharacter pc) {
        StringBuilder nameBuilder = new StringBuilder();
        boolean help = false;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
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
            CharacterCommand.terminal.println(Help.FORGET);
        } else {
            Spell spell = pc.getSpell(nameBuilder.toString().trim());
            if (spell != null) {
                forgetSpell(pc, spell);
            } else {
                CharacterCommand.terminal.println(Message.MSG_NO_SPELL);
            }
        }
    }

    private static void forgetSpell(PlayerCharacter pc, Spell spell) {
        pc.getSpellBook().forget(spell);
        if (spell.isCantrip()) {
            CharacterCommand.terminal.println("Forgot cantrip " + spell.getSpellName());
        } else {
            CharacterCommand.terminal.println("Forgot level " + spell.getSpellLevel() + " spell " + spell.getSpellName());
        }
    }
}
