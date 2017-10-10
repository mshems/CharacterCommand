package app;

import app.io.*;
import app.core.character.*;
import app.core.items.*;
import app.core.magic.*;
import app.terminal.CommandExecutor;
import app.terminal.Terminal;
import app.utils.*;

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

        terminal = new Terminal(splash);
        tokens = terminal.getCommandTokens();

        terminal.setDefaultPrompt("CharacterCommand ~ ");
        if(activeChar!= null){
            terminal.getOutputComponent().setCurrPrompt(activeChar.getName()+" @ CharacterCommand ~ ");
        }
        initCommands();
        terminal.println(splash, Terminal.CENTERED);
        terminal.start();
    }

    private static void initApp() {
        propertiesHandler = new PropertiesHandler();
        readWriteHandler = new ReadWriteHandler();
        ReadWriteHandler.checkDirs();
        characterList = new LinkedHashMap<>();
        readWriteHandler.importAll(false);
        if (propertiesHandler.isResume()) {
            String key = propertiesHandler.getLast();
            if (characterList.containsKey(key)) {
                activeChar = characterList.get(key);
            }
        }
        //makeTestCharacter();
    }

    public static void quit(){
        boolean quit = terminal.queryYN("Are you sure? Unsaved data will be lost [Y/N] : ");
        if(quit){
            closeApp();
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

    private static void initCommands(){
        terminal.setCommandExecutor(new CommandExecutor(){
            public void doCommand(Terminal terminal, String token) {
                if (activeChar != null || loadNotRequired(token)) {
                    super.doCommand(terminal, token);
                } else {
                    tokens.clear();
                }
                if(activeChar!=null){
                    terminal.getOutputComponent().setCurrPrompt(activeChar.getName()+" @ CharacterCommand ~ ");
                } else {
                    terminal.getOutputComponent().resetPrompt();
                }
            }
        });
        terminal.putCommand("import", ()->readWriteHandler.importCharacter());

        terminal.putCommand("export", ()->readWriteHandler.export());

        terminal.putCommand("load", ()->readWriteHandler.loadChar());

        terminal.putCommand("list", ()->dispCharacterList());

        terminal.putCommand("save",()->readWriteHandler.saveChar(true));

        terminal.putCommand("prefs", ()->propertiesHandler.prefs());

        terminal.putCommand("new", ()->PlayerCreator.createCharacter());

        terminal.putCommand("view", ()->terminal.println(activeChar.toString()));
        terminal.putCommand("v", terminal.getCommand("view"));

        terminal.putCommand("inv", ()->terminal.println(activeChar.getInventory().toString()));
        terminal.putCommand("i", terminal.getCommand("inv"));

        terminal.putCommand("get", ()->InventoryIO.get(activeChar));

        terminal.putCommand("add", ()->InventoryIO.addDrop(activeChar));
        terminal.putCommand("drop", terminal.getCommand("add"));

        terminal.putCommand("equip", ()-> ItemIO.equip(activeChar));
        terminal.putCommand("dequip", terminal.getCommand("equip"));

        terminal.putCommand("use", ()->ItemIO.use(activeChar));

        terminal.putCommand("stats", ()->StatIO.stats(activeChar));

        terminal.putCommand("stat", terminal.getCommand("stats"));

        terminal.putCommand("edit", ()->StatEditor.edit(activeChar));

        terminal.putCommand("skills", ()->terminal.println(activeChar.skillsToString()));
        terminal.putCommand("skill", ()-> SkillIO.skills(activeChar));

        terminal.putCommand("ap", ()-> AbilityPointsIO.abilityPoints(activeChar));

        terminal.putCommand("spells", ()->terminal.println(activeChar.getSpellBook().toString()));
        terminal.putCommand("spell", ()-> SpellIO.spells(activeChar));

        terminal.putCommand("spellslots", ()->terminal.println(activeChar.spellSlotsToString()));
        terminal.putCommand("spellslot", ()-> SpellSlotIO.spellSlots(activeChar));

        terminal.putCommand("charge", ()->SpellSlotIO.charge(activeChar));

        terminal.putCommand("cast", ()->SpellIO.cast(activeChar));

        terminal.putCommand("learn", ()-> SpellBookIO.learn(activeChar));

        terminal.putCommand("forget", ()->SpellBookIO.forget(activeChar));

        terminal.putCommand("heal", ()->PlayerHealer.heal(activeChar));
        terminal.putCommand("hurt", terminal.getCommand("heal"));

        terminal.putCommand("levelup", ()->PlayerLeveler.levelUp(activeChar));
        terminal.putCommand("lvl", terminal.getCommand("levelup"));

        terminal.putCommand("help", ()->Help.helpMenu(terminal));

        terminal.putCommand("quit", ()->quit());
        terminal.putCommand("q", terminal.getCommand("quit"));

        terminal.putCommand("sq", ()->{
            readWriteHandler.saveChar(false);
            closeApp();
            System.exit(0);
        });
    }

    private static boolean loadNotRequired(String token){
        if(terminal.getCommand(token) == null){
            return true;
        }
        switch (token.toLowerCase()){
            case "import":
            case "load":
            case "list":
            case "prefs":
            case "new":
            case "quit":
            case "help":
            case "credits":
            case "terminal-config":
                return true;
            default:
                terminal.newLine();
                terminal.println(Message.ERROR_NO_LOAD);
                return false;
        }
    }

    public static Integer getIntToken() {
        Integer n = null;
        try {
            if (tokens.isEmpty()) {
                terminal.println(Message.ERROR_NO_VALUE);
            } else {
                n = Integer.parseInt(tokens.pop());
            }
        } catch (NumberFormatException e) {
            terminal.println(Message.ERROR_NOT_INT);
        }
        return n;
    }

    public static DiceRoll getDiceRoll(String message) {
        while (true) {
            String s = terminal.queryString(message, false);
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

    public static DiceRoll toDiceRoll(String s) {
        if (s.matches("(\\d+d\\d+)")) {
            String[] a = s.split("d");
            return new DiceRoll(Integer.parseInt(a[0]), Integer.parseInt(a[1]));
        } else {
            return null;
        }
    }

    private static void dispCharacterList() {
        if (!characterList.isEmpty()) {
            terminal.println("Characters:");
            for (PlayerCharacter c : characterList.values()) {
                terminal.println("- " + c.getName());
            }
        } else {
            terminal.println("No characters available");
        }
    }

    public static boolean checkCaster(PlayerCharacter pc) {
        if (pc.isSpellcaster()) {
            return true;
        } else {
            terminal.println(Message.MSG_NOT_CAST);
            return false;
        }
    }

    public static boolean isValidName(String name) {
        if (name.isEmpty() || name.matches("\\s+") || name.matches(".*[^a-zA-Z0-9)(\\s+].*")) {
            terminal.println("Not a valid name");
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
