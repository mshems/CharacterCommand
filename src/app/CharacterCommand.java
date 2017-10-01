package app;

import character.*;
import items.*;
import magic.*;
import terminal.Terminal;
import utils.*;

import java.util.*;

//@SuppressWarnings("unused")
public class CharacterCommand {
    /**
     * version 0.2.4
     */
    public static final long VERSION = 203L;
    private static final String splash = "CharacterCommand v0.2.4";
    public static PlayerCharacter activeChar;
    public static LinkedHashMap<String, PlayerCharacter> characterList;
    public static Scanner scanner;
    public static String[] input;
    public static LinkedList<String> tokens;
    public static PropertiesHandler propertiesHandler;
    public static ReadWriteHandler readWriteHandler;
    public static Terminal terminal;

    public static PlayerCharacter getActiveChar(){
        return activeChar;
    }

    public static void main(String[] args) {
        initApp();

        if (args.length > 0) {
            switch (args[0]) {
                case "--test":
                    makeTestCharacter();
                    break;
                default:
                    break;
            }
        }

        makeTestCharacter();

        terminal = new Terminal(splash, "CharacterCommand@~ > ");
        terminal.start();
    }

    private static void initApp() {
        //System.out.println("---" + splash + "---");
        propertiesHandler = new PropertiesHandler();
        //commandHandler = new CommandHandler();
        readWriteHandler = new ReadWriteHandler();
        ReadWriteHandler.checkDirs();
        tokens = new LinkedList<>();
        scanner = new Scanner(System.in);
        characterList = new LinkedHashMap<>();
        readWriteHandler.importAll(false);
        if (propertiesHandler.isResume()) {
            String key = propertiesHandler.getLast();
            if (characterList.containsKey(key)) {
                activeChar = characterList.get(key);
            }
        }
    }

    public static void quit(){
        boolean quit = terminal.queryYN("Are you sure? Unsaved data will be lost [Y/N] : ");
        if(quit){
            CharacterCommand.closeApp();
            System.exit(0);
        }
    }

    public static void closeApp(){
        if (activeChar == null) {
            propertiesHandler.setLast("");
        } else {
            propertiesHandler.setLast(activeChar.getName().toLowerCase());
        }
        propertiesHandler.writeProperties();
    }

    /**
     * SPELL SLOTS
     *********************************************************************************************************/
    public static void spellSlots() {
        tokens.pop();
        if (!tokens.isEmpty()) {
            switch (tokens.peek()) {
                case "--charge":
                    charge();
                    break;
                case "--get":
                case "--set":
                    setSlots();
                    break;
                case "--help":
                    System.out.println(Help.SPELLSLOTS);
                    break;
                default:
                    System.out.println(Message.ERROR_INPUT);
                    break;
            }
        } else {
            System.out.println(activeChar.spellSlotsToString());
        }
    }

    private static void setSlots() {
        tokens.pop();
        if (!tokens.isEmpty()) {
            setSlotsParser();
        } else {
            int level = getValidInt("Enter spell slot level: ");
            int max = getValidInt("Enter new spell slot maximum: ");
            activeChar.getSpellSlots()[level].setMaxVal(max);
            System.out.println("Maximum level " + level + " spell slots set to " + max);
        }
    }

    private static void setSlotsParser() {
        Integer level = null;
        Integer max = null;
        boolean help = false;
        while (!tokens.isEmpty()) {
            switch (tokens.peek()) {
                case "-l":
                case "--level":
                    tokens.pop();
                    if (tokens.isEmpty()) {
                        System.out.println(Message.ERROR_NO_ARG + ": level");
                    } else {
                        level = getIntToken();
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
                    tokens.pop();
                    if (tokens.isEmpty()) {
                        System.out.println(Message.ERROR_NO_ARG + ": count");
                    } else {
                        max = getIntToken();
                    }
                    break;
                case "--help":
                    tokens.pop();
                    help = true;
                    break;
                default:
                    if (tokens.peek().startsWith("-")) {
                        System.out.println("ERROR: Invalid flag '" + tokens.pop() + "'");
                    } else {
                        tokens.pop();
                    }
                    break;
            }
        }
        if (help) {
            System.out.println(Help.SETSLOTS);
        } else {
            if (level != null && max != null) {
                activeChar.getSpellSlots()[level].setMaxVal(max);
                System.out.println("Maximum level " + level + " spell slots set to " + max);
            }
        }
    }


    public static void charge() {
        tokens.pop();
        if (!tokens.isEmpty()) {
            chargeParser();
        } else {
            int level = getValidInt("Enter spell slot level: ");
            int count = getValidInt("Enter number of slots to recharge: ");
            System.out.println("Recharged " + count + " level " + level + " spell slots");
        }
    }

    /**
     * CHARGE
     **************************************************************************************************************/
    private static void chargeParser() {
        boolean all = false;
        boolean help = false;
        Integer level = null;
        Integer count = null;
        while (!tokens.isEmpty()) {
            switch (tokens.peek()) {
                case "-l":
                case "--level":
                    tokens.pop();
                    if (tokens.isEmpty()) {
                        System.out.println(Message.ERROR_NO_ARG + ": level");
                    } else {
                        level = getIntToken();
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
                    tokens.pop();
                    if (tokens.isEmpty()) {
                        System.out.println(Message.ERROR_NO_ARG + ": count");
                    } else {
                        count = getIntToken();
                    }
                    break;
                case "--all":
                    tokens.pop();
                    all = true;
                    break;
                case "--help":
                    tokens.pop();
                    help = true;
                    break;
                default:
                    if (tokens.peek().startsWith("-")) {
                        System.out.println("ERROR: Invalid flag '" + tokens.pop() + "'");
                    } else {
                        tokens.pop();
                    }
                    break;
            }
        }
        if (help) {
            System.out.println(Help.CHARGE);
        } else {
            if (level == null && count == null) {
                if (all) {
                    for (SpellSlot s : activeChar.getSpellSlots()) {
                        if (s.getMaxVal() > 0) {
                            s.fullCharge();
                        }
                    }
                    System.out.println("All spell slots recharged");
                } else {
                    System.out.println(Message.ERROR_NO_VALUE);
                }
            } else if (level != null && count == null) {
                if (all) {
                    activeChar.getSpellSlots()[level].fullCharge();
                    System.out.println("Level " + level + " spell slots fully recharged");
                } else {
                    activeChar.getSpellSlots()[level].charge();
                    System.out.println("Level " + level + " spell slot recharged");
                }
            }
            if (level == null && count != null) {
                if (all) {
                    for (SpellSlot s : activeChar.getSpellSlots()) {
                        if (s.getMaxVal() > 0) {
                            s.charge(count);
                        }
                    }
                    System.out.println("Recharged " + count + " spell slots of each level known");
                } else {
                    System.out.println(Message.ERROR_NO_ARG + ": level");
                }
            } else if (level != null && count != null) {
                activeChar.getSpellSlots()[level].charge(count);
                System.out.println("Recharged " + count + " level " + level + " spell slots");
            }
        }
    }


    /**
     * SPELLS
     **************************************************************************************************************/
    public static void spells() {
        tokens.pop();
        if (!tokens.isEmpty()) {
            switch (tokens.peek()) {
                case "--learn":
                    learn();
                    break;
                case "--forget":
                    forget();
                    break;
                case "--cast":
                    cast();
                    break;
                case "--stats":
                    System.out.println(activeChar.spellStatsToString());
                    break;
                case "--slots":
                    spellSlots();
                    break;
                case "--help":
                    System.out.println(Help.SPELLS);
                    break;
                default:
                    System.out.println("Error: command syntax");
                    break;
            }
        } else {
            System.out.println(activeChar.getSpellBook());
        }
    }

    /**
     * LEARN
     ***************************************************************************************************************/
    public static void learn() {
        tokens.pop();
        if (!tokens.isEmpty()) {
            learnParser();
        } else {
            String spellName;
            int spellLevel;
            System.out.print("Spell name: ");
            spellName = scanner.nextLine().trim();
            spellLevel = getValidInt("Spell level: ");
            learnSpell(new Spell(spellName, spellLevel));
        }
    }

    private static void learnParser() {
        StringBuilder nameBuilder = new StringBuilder();
        Integer spellLevel = null;
        boolean learnmagic = false;
        boolean help = false;
        while (!tokens.isEmpty()) {
            switch (tokens.peek()) {
                case "-l":
                case "--level":
                    tokens.pop();
                    if (tokens.isEmpty()) {
                        System.out.println(Message.ERROR_NO_ARG + ": level");
                        spellLevel = null;
                    } else {
                        spellLevel = getIntToken();
                    }
                    break;
                case "-m":
                case "--magic":
                    tokens.pop();
                    learnmagic = true;
                    break;
                case "--help":
                    tokens.pop();
                    help = true;
                    break;
                default:
                    if (tokens.peek().startsWith("-")) {
                        System.out.println("ERROR: Invalid flag '" + tokens.pop() + "'");
                    } else {
                        nameBuilder.append(tokens.pop());
                        nameBuilder.append(" ");
                    }
                    break;
            }
        }
        if (learnmagic) {
            learnMagic();
        }
        if (activeChar.isSpellcaster()) {
            if (spellLevel == null) {
                spellLevel = Spell.CANTRIP;    //default level
            }
            if (help) {
                System.out.println(Help.LEARN);
            } else {
                String spellName = nameBuilder.toString().trim();
                if (!spellName.isEmpty()) {
                    Spell spell = new Spell(spellName, spellLevel);
                    learnSpell(spell);
                }
            }
        } else {
            System.out.println(Message.MSG_NOT_CAST);
        }
    }

    private static void learnMagic() {
        Ability spellAbility = null;
        while (spellAbility == null) {
            System.out.print("Spellcasting ability: ");
            String abilityName = scanner.nextLine();
            if (abilityName.equalsIgnoreCase("cancel")) {
                break;
            } else {
                spellAbility = activeChar.getAbilities().get(abilityName);
                if (spellAbility == null) {
                    System.out.println("ERROR: Ability not found");
                } else {
                    activeChar.setSpellcaster(true);
                    activeChar.initMagicStats(spellAbility);
                }
            }
        }
    }

    private static void learnSpell(Spell spell) {
        activeChar.getSpellBook().learn(spell);
        if (spell.isCantrip()) {
            System.out.println("Learned cantrip " + spell.getSpellName());
        } else {
            System.out.println("Learned level " + spell.getSpellLevel() + " spell " + spell.getSpellName());
        }
    }

    /**
     * FORGET
     **************************************************************************************************************/
    public static void forget() {
        tokens.pop();
        if (!tokens.isEmpty()) {
            forgetParser();
        } else {
            Spell spell = getSpellByName();
            if (spell != null) {
                forgetSpell(spell);
            }
        }
    }

    private static void forgetParser() {
        StringBuilder nameBuilder = new StringBuilder();
        boolean help = false;
        while (!tokens.isEmpty()) {
            switch (tokens.peek()) {
                case "--help":
                    tokens.pop();
                    help = true;
                    break;
                default:
                    if (tokens.peek().startsWith("-")) {
                        System.out.println("ERROR: Invalid flag '" + tokens.pop() + "'");
                    } else {
                        nameBuilder.append(tokens.pop());
                        nameBuilder.append(" ");
                    }
                    break;
            }
        }
        if (help) {
            System.out.println(Help.FORGET);
        } else {
            Spell spell = activeChar.getSpell(nameBuilder.toString().trim());
            if (spell != null) {
                forgetSpell(spell);
            } else {
                System.out.println(Message.MSG_NO_SPELL);
            }
        }
    }

    private static void forgetSpell(Spell spell) {
        activeChar.getSpellBook().forget(spell);
        if (spell.isCantrip()) {
            System.out.println("Forgot cantrip " + spell.getSpellName());
        } else {
            System.out.println("Forgot level " + spell.getSpellLevel() + " spell " + spell.getSpellName());
        }
    }

    /**
     * CAST
     ****************************************************************************************************************/
    public static void cast() {
        tokens.pop();
        if (!tokens.isEmpty()) {
            castParser();
        } else {
            Integer castLevel = -1;
            Spell spell = getSpellByName();
            if (spell != null) {
                if (!spell.isCantrip()) {
                    castLevel = getValidInt("Cast at level: ");
                }
                castSpell(spell, "spellName", castLevel);
            }
        }
    }

    private static void castParser() {
        Spell spell;
        StringBuilder nameBuilder = new StringBuilder();
        Integer castLevel = -1;
        boolean help = false;
        while (!tokens.isEmpty()) {
            switch (tokens.peek()) {
                case "-l":
                case "--level":
                    tokens.pop();
                    if (tokens.isEmpty()) {
                        System.out.println(Message.ERROR_NO_ARG + ": level");
                        castLevel = null;
                    } else {
                        castLevel = getIntToken();
                    }
                    break;
                case "--help":
                    help = true;
                    tokens.pop();
                    break;
                default:
                    if (tokens.peek().startsWith("-")) {
                        System.out.println("ERROR: Invalid flag '" + tokens.pop() + "'");
                    } else {
                        nameBuilder.append(tokens.pop());
                        nameBuilder.append(" ");
                    }
            }
        }
        if (help) {
            System.out.println(Help.CAST);
        } else {
            String spellName = nameBuilder.toString().trim();
            spell = activeChar.getSpell(spellName);
            castSpell(spell, spellName, castLevel);
        }
    }

    private static void castSpell(Spell spell, String spellName, Integer castLevel) {
        if (spell != null && castLevel != null) {
            if (castLevel == -1) {
                castLevel = spell.getSpellLevel();
            }
            castLevel = activeChar.cast(spell, castLevel);
            if (spell.isCantrip()) {
                System.out.println("Cast '" + spell.getSpellName() + "' as a cantrip");
            } else {
                if (castLevel < 0) {
                    System.out.println("No level " + (-castLevel) + " spell slots remaining");
                } else {
                    System.out.println("Cast '" + spell.getSpellName() + "' at level " + castLevel);
                }
            }
        } else {
            if (spellName.equals("")) {
                System.out.println("ERROR: Missing argument: spell name");
            } else if (spell == null) {
                System.out.println("No spell by that name");
            }
        }
    }

    /**
     * I/O & UTILITIES
     *****************************************************************************************************/
    public static Integer getIntToken() {
        Integer n = null;
        try {
            if (tokens.isEmpty()) {
                terminal.printOut(Message.ERROR_NO_VALUE);
            } else {
                n = Integer.parseInt(tokens.pop());
            }
        } catch (NumberFormatException e) {
            terminal.printOut(Message.ERROR_NOT_INT);
        }
        return n;
    }

    public static Skill getSkillByName() {
        Skill skill;
        while (true) {
            String skillName = terminal.queryString("Stat name: ", false);
            if (skillName.equalsIgnoreCase("cancel")) {
                return null;
            } else {
                skill = activeChar.getSkill(skillName);
                if (skill == null) {
                    terminal.printOut(Message.MSG_NO_SKILL);
                } else {
                    return skill;
                }
            }
        }
    }

    public static Stat getStatByName() {
        Stat stat;
        while (true) {
            String statName = terminal.queryString("Stat name: ", false);
            if (statName.equalsIgnoreCase("cancel")) {
                return null;
            } else {
                stat = activeChar.getStat(statName);
                if (stat == null) {
                    terminal.printOut(Message.MSG_NO_SKILL);
                } else {
                    return stat;
                }
            }
        }
    }

    public static Spell getSpellByName() {
        Spell spell;
        while (true) {
            String spellName = terminal.queryString("Spell name: ", false);
            if (spellName.equalsIgnoreCase("cancel")) {
                return null;
            } else {
                spell = activeChar.getSpell(spellName);
                if (spell == null) {
                    terminal.printOut(Message.MSG_NO_SPELL);
                } else {
                    return spell;
                }
            }
        }
    }

    public static Item getItemByName() {
        Item item;
        while (true) {
            String itemName = terminal.queryString("Item name: ", false);
            if (itemName.equalsIgnoreCase("cancel")) {
                return null;
            } else {
                for (Item coin : activeChar.getCurrency()) {
                    if (coin.getName().equalsIgnoreCase(itemName)) {
                        return coin;
                    }
                }
                item = activeChar.getItem(itemName);
                if (item == null) {
                    terminal.printOut(Message.MSG_NO_ITEM);
                } else {
                    return item;
                }
            }
        }
    }

    public static DiceRoll getDiceRoll() {
        while (true) {
            String s = terminal.queryString("Dice roll: ", false);
            if (s.matches("(\\d+d\\d+)")) {
                String[] a = s.split("d");
                return new DiceRoll(Integer.parseInt(a[0]), Integer.parseInt(a[1]));
            } else {
                if (s.equalsIgnoreCase("cancel")) {
                    return null;
                }
            }
        }
    }

    public static DiceRoll getDiceRoll(String s) {
        if (s.matches("(\\d+d\\d+)")) {
            String[] a = s.split("d");
            return new DiceRoll(Integer.parseInt(a[0]), Integer.parseInt(a[1]));
        } else {
            return null;
        }
    }

    public static int getValidInt(String message) {
        int val;
        while (true) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                val = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println(Message.ERROR_NOT_INT);
                scanner.nextLine();
            }
        }
        return val;
    }

    public static void dispCharacterList() {
        if (!characterList.isEmpty()) {
            terminal.printBlock(()-> {
                terminal.println("Characters:");
                for (PlayerCharacter c : characterList.values()) {
                    terminal.println("- " + c.getName());
                }
            });
        } else {
            terminal.printOut("No characters available");
        }
    }

    public static boolean checkCaster(PlayerCharacter pc) {
        if (pc.isSpellcaster()) {
            return true;
        } else {
            terminal.printOut(Message.MSG_NOT_CAST);
            return false;
        }
    }

    public static boolean isValidName(String name) {
        if (name.isEmpty() || name.matches("\\s+") || name.matches(".*[^a-zA-Z0-9)(\\s+].*")) {
            terminal.printOut("Not a valid name");
            return false;
        } else {
            return true;
        }
    }

    /**
     * TEST CHARACTER
     ******************************************************************************************************/
    private static void makeTestCharacter() {
        PlayerCharacter frodo;
        frodo = new PlayerCharacter("Frodo Baggins", "Halfling", "Paladin");
        frodo.addNewItem(new Consumable("Rations", 5));

		/*create some enchanted items*/
        Weapon sting = new Weapon("Sting");
        sting.addEffect(new ItemEffect(frodo.getStat(Ability.STR), 2));
        sting.setDamage(new DiceRoll(1, 8));
        frodo.addNewItem(sting);
        sting.equip(frodo);
        Armor mith = new Armor("Mithril Chainmail");
        mith.addEffect(new ItemEffect(frodo.getStat(Ability.CON), 2));
        mith.setArmorType(Armor.ArmorType.L_ARMOR);
        mith.setAC(14);
        frodo.addNewItem(mith);
        mith.equip(frodo);
        Equippable ring = new Equippable("Ring of Power");
        ring.addEffect(new ItemEffect(frodo.getStat(Attribute.HP), -5));
        frodo.addNewItem(ring);

		/*learn a spell*/
        frodo.getSpellBook().learn(new Spell("Invisibility", Spell.CANTRIP));
        frodo.getSpellBook().learn(new Spell("Blight", 3));

		/*get spellslots*/
        SpellSlot[] spellSlots = new SpellSlot[]{
                new SpellSlot(0, 0),
                new SpellSlot(1, 4),
                new SpellSlot(2, 3),
                new SpellSlot(3, 2),
                new SpellSlot(4, 0),
                new SpellSlot(5, 0),
                new SpellSlot(6, 0),
                new SpellSlot(7, 0),
                new SpellSlot(8, 0),
                new SpellSlot(9, 0)
        };
        frodo.setSpellSlots(spellSlots);
        frodo.initMagicStats(frodo.getAbilities().get(Ability.WIS));
        frodo.setSpellcaster(true);

		/*Add currency*/
        frodo.addCurrency(Inventory.indexGP, 10);
        frodo.addCurrency(Inventory.indexSP, 35);
        frodo.addCurrency(Inventory.indexCP, 4);

        characterList.put("frodo baggins", frodo);
        activeChar = characterList.get("frodo baggins");
    }
}
