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
    private static PlayerCharacter activeChar;
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
    public static void setActiveChar(PlayerCharacter pc){
        activeChar = pc;
    }
    public static boolean hasActiveChar(){
        if(activeChar !=null){
            return true;
        }
        return false;
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

        //makeTestCharacter();

        terminal = new Terminal(splash, "CharacterCommand ~ ");
        terminal.start();
    }

    private static void initApp() {
        propertiesHandler = new PropertiesHandler();
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
     * UTILITY METHODS
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
