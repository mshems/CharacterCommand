package magic;

import app.CharacterCommand;
import character.PlayerCharacter;
import character.PlayerCreator;
import utils.Help;
import utils.Message;

public class SpellSlotIO {
    public static void spellSlots(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "--get":
                case "--charge":
                    charge(pc);
                    break;
                case "--set":
                    setSlots(pc);
                    break;
                case "--help":
                    CharacterCommand.terminal.println(Help.SPELLSLOTS);
                    break;
                default:
                    CharacterCommand.terminal.println(Message.ERROR_INPUT);
                    break;
            }
        } else {
            CharacterCommand.terminal.println(pc.spellSlotsToString());
        }
    }

    private static void setSlots(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            setSlotsParser(pc);
        } else {
            int level = CharacterCommand.terminal.queryInteger("Enter spell slot level: ", false);
            int max = CharacterCommand.terminal.queryInteger("Enter new spell slot maximum: ", false);
            pc.getSpellSlots()[level].setMaxVal(max);
            CharacterCommand.terminal.println("Maximum level " + level + " spell slots set to " + max);
        }
    }

    private static void setSlotsParser(PlayerCharacter pc) {
        Integer level = null;
        Integer max = null;
        boolean help = false;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-l":
                case "--level":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": level");
                    } else {
                        level = CharacterCommand.getIntToken();
                        if (level > Spell.MAX_LEVEL) {
                            level = Spell.MAX_LEVEL;
                        }
                        if (level < Spell.CANTRIP) {
                            level = Spell.CANTRIP;
                        }
                    }
                    break;
                case "-m":
                case "--max":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": count");
                    } else {
                        max = CharacterCommand.getIntToken();
                    }
                    break;
                case "--help":
                    CharacterCommand.tokens.pop();
                    help = true;
                    break;
                default:
                    if (CharacterCommand.tokens.peek().startsWith("-")) {
                        CharacterCommand.terminal.println("ERROR: Invalid flag '" + CharacterCommand.tokens.pop() + "'");
                    } else {
                        CharacterCommand.tokens.pop();
                    }
                    break;
            }
        }
        if (help) {
            CharacterCommand.terminal.println(Help.SETSLOTS);
        } else {
            if (level != null && max != null) {
                pc.getSpellSlots()[level].setMaxVal(max);
                CharacterCommand.terminal.println("Maximum level " + level + " spell slots set to " + max);
            }
        }
    }

    public static void charge(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            chargeParser(pc);
        } else {
            int level = CharacterCommand.terminal.queryInteger("Enter spell slot level: ", false);
            int count = CharacterCommand.terminal.queryInteger("Enter number of slots to recharge: ", false);
            pc.getSpellSlots()[level].charge(count);
            CharacterCommand.terminal.println("Recharged " + count + " level " + level + " spell slots");
        }
    }

    private static void chargeParser(PlayerCharacter pc) {
        boolean all = false;
        boolean help = false;
        Integer level = null;
        Integer count = null;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-l":
                case "--level":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": level");
                    } else {
                        level = CharacterCommand.getIntToken();
                        if (level > Spell.MAX_LEVEL) {
                            level = Spell.MAX_LEVEL;
                        }
                        if (level < Spell.CANTRIP) {
                            level = Spell.CANTRIP;
                        }
                    }
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
                    } else {
                        CharacterCommand.tokens.pop();
                    }
                    break;
            }
        }
        if (help) {
            CharacterCommand.terminal.println(Help.CHARGE);
        } else {
            if (level == null && count == null) {
                if (all) {
                    for (SpellSlot s : pc.getSpellSlots()) {
                        if (s.getMaxVal() > 0) {
                            s.fullCharge();
                        }
                    }
                    CharacterCommand.terminal.println("All spell slots recharged");
                } else {
                    CharacterCommand.terminal.println(Message.ERROR_NO_VALUE);
                }
            } else if (level != null && count == null) {
                if (all) {
                    pc.getSpellSlots()[level].fullCharge();
                    CharacterCommand.terminal.println("Level " + level + " spell slots fully recharged");
                } else {
                    pc.getSpellSlots()[level].charge();
                    CharacterCommand.terminal.println("Level " + level + " spell slot recharged");
                }
            }
            if (level == null && count != null) {
                if (all) {
                    for (SpellSlot s : pc.getSpellSlots()) {
                        if (s.getMaxVal() > 0) {
                            s.charge(count);
                        }
                    }
                    CharacterCommand.terminal.println("Recharged " + count + " spell slots of each level known");
                } else {
                    CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": level");
                }
            } else if (level != null && count != null) {
                pc.getSpellSlots()[level].charge(count);
                CharacterCommand.terminal.println("Recharged " + count + " level " + level + " spell slots");
            }
        }
    }
}
