package character;

import app.CharacterCommand;
import items.*;
import utils.Help;
import utils.Message;

public class InventoryIO {
    public static void addDrop(PlayerCharacter pc) {
        Item item;
        String addDrop = CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            addDrop(pc, addDrop);
        } else {
            Integer itemCount;
            item = ItemIO.getItemByName(pc);
            itemCount = CharacterCommand.terminal.queryInteger("Amount: ", false);
            if (item != null) {
                switch (item.getName().toLowerCase()) {
                    case "pp":
                    case "platinum":
                        addDropCoin(pc, Inventory.indexPL, itemCount, false, addDrop);
                        break;
                    case "gp":
                    case "gold":
                        addDropCoin(pc, Inventory.indexGP, itemCount, false, addDrop);
                        break;
                    case "sp":
                    case "silver":
                        addDropCoin(pc, Inventory.indexSP, itemCount, false, addDrop);
                        break;
                    case "cp":
                    case "copper":
                        addDropCoin(pc, Inventory.indexCP, itemCount, false, addDrop);
                        break;
                    default:
                        addDropItem(pc, item, itemCount, false, addDrop);
                        break;
                }
            }
        }
    }

    private static void addDrop(PlayerCharacter pc, String command) {
        Item item = null;
        StringBuilder nameBuilder = new StringBuilder();
        Integer itemCount = null;
        boolean dropAll = false;
        boolean help = false;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-c":
                case "-count":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": count");
                    } else {
                        itemCount = CharacterCommand.getIntToken();
                    }
                    break;
                case "--all":
                    CharacterCommand.tokens.pop();
                    dropAll = true;
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

		/*DEFAULT VALUES****************/
        if (itemCount == null) {
            itemCount = 1;
        }

        if (!help) {
            String itemName = nameBuilder.toString().trim();
            switch (itemName.toLowerCase()) {
                case "pp":
                case "platinum":
                    addDropCoin(pc, Inventory.indexPL, itemCount, dropAll, command);
                    break;
                case "gp":
                case "gold":
                    addDropCoin(pc, Inventory.indexGP, itemCount, dropAll, command);
                    break;
                case "sp":
                case "silver":
                    addDropCoin(pc, Inventory.indexSP, itemCount, dropAll, command);
                    break;
                case "cp":
                case "copper":
                    addDropCoin(pc, Inventory.indexCP, itemCount, dropAll, command);
                    break;
                default:
                    item = pc.getItem(itemName);
                    break;
            }
            if (item != null) {
                addDropItem(pc, item, itemCount, dropAll, command);
            }
        } else {
            if (command.equals("add")) {
                CharacterCommand.terminal.println(Help.ADD);
            }
            if (command.equals("drop")) {
                CharacterCommand.terminal.println(Help.DROP);
            }
        }
    }

    private static void addDropCoin(PlayerCharacter pc, int coinType, Integer itemCount, boolean dropAll, String command) {
        String itemName = "";
        switch (coinType) {
            case Inventory.indexPL:
                itemName = "Platinum";
                break;
            case Inventory.indexGP:
                itemName = "Gold";
                break;
            case Inventory.indexSP:
                itemName = "Silver";
                break;
            case Inventory.indexCP:
                itemName = "Copper";
                break;
        }
        if (command.equals("drop") && !dropAll) {
            itemCount = -itemCount;
            CharacterCommand.terminal.println(String.format("Dropped %dx %s", -itemCount, itemName));
        }
        if (dropAll) {
            pc.getCurrency(coinType).setCount(0);
            CharacterCommand.terminal.println(String.format("Dropped all %s", itemName));
        } else {
            pc.addCurrency(coinType, itemCount);
            if (command.equals("add")) {
                CharacterCommand.terminal.println(String.format("Added %dx %s", itemCount, itemName));
            }
        }
    }

    private static void addDropItem(PlayerCharacter pc, Item item, Integer itemCount, boolean dropAll, String command) {
        if (command.equals("drop") && !dropAll) {
            itemCount = -itemCount;
            CharacterCommand.terminal.println(String.format("Dropped %dx \"%s\"", -itemCount, item.getName()));
        }
        if (dropAll) {
            pc.removeItem(item);
            CharacterCommand.terminal.println(String.format("Dropped all \"%s\"", item.getName()));
        } else {
            pc.addDropItem(item, itemCount);
            if (command.equals("add")) {
                CharacterCommand.terminal.println(String.format("Added %dx \"%s\"", itemCount, item.getName()));
            }
        }
    }

    public static void get(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            getParser(pc);
        } else {
            ItemBuilder itemBuilder = new ItemBuilder();
            ItemBuilderIO.buildItem(pc, itemBuilder);
            if(itemBuilder.itemType!=null) {
                getItem(pc, itemBuilder);
            }
        }
    }

    private static void getParser(PlayerCharacter pc) {
        StringBuilder nameBuilder = new StringBuilder();
        ItemBuilder itemBuilder = new ItemBuilder();

        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-c":
                case "--count":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": count");
                        return;
                    }
                    itemBuilder.itemCount = CharacterCommand.getIntToken();
                    break;
                case "--armor":
                    CharacterCommand.tokens.pop();
                    itemBuilder.itemType = Item.ItemType.ARMOR;
                    break;
                case "--consumable":
                    CharacterCommand.tokens.pop();
                    itemBuilder.itemType = Item.ItemType.CONSUMABLE;
                    break;
                case "--equippable":
                    CharacterCommand.tokens.pop();
                    itemBuilder.itemType = Item.ItemType.EQUIPPABLE;
                    break;
                case "--item":
                    CharacterCommand.tokens.pop();
                    itemBuilder.itemType = Item.ItemType.ITEM;
                    break;
                case "--weapon":
                    CharacterCommand.tokens.pop();
                    itemBuilder.itemType = Item.ItemType.WEAPON;
                    break;
                case "-t":
                case "--type":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": type");
                        return;
                    }
                    itemBuilder.itemType = Item.parseItemType(CharacterCommand.tokens.pop());
                    if (itemBuilder.itemType == null) {
                        CharacterCommand.terminal.println(Message.ERROR_ITEM_TYPE);
                        return;
                    }
                    break;
                case "-d":
                case "--damage":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": damage");
                        return;
                    }
                    itemBuilder.damage = CharacterCommand.getDiceRoll(CharacterCommand.tokens.pop());
                    break;
                case "-e":
                case "--enchant":
                case "--effect":
                    CharacterCommand.tokens.pop();
                    Stat target;
                    if (!CharacterCommand.tokens.isEmpty()) {
                        target = pc.getStat(CharacterCommand.tokens.pop());
                    } else {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": effect target");
                        return;
                    }
                    if (target != null) {
                        Integer bonus = CharacterCommand.getIntToken();
                        if (bonus != null) {
                            itemBuilder.addEffect(target, bonus);
                        } else {
                            CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": effect bonus");
                            return;
                        }
                    } else {
                        CharacterCommand.terminal.println("ERROR: Effect target not found");
                        return;
                    }
                    break;
                case "-ac":
                case "--armorclass":
                    CharacterCommand.tokens.pop();
                    itemBuilder.armorClass = CharacterCommand.getIntToken();
                    break;
                case "-at":
                case "--armortype":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println("ERROR: No armor type specified");
                        return;
                    }
                    itemBuilder.armorType = Armor.parseType(CharacterCommand.tokens.peek().toLowerCase());
                    if (itemBuilder.armorType == null) {
                        CharacterCommand.terminal.println("ERROR: Not a valid armor type");
                        return;
                    }
                    break;
                case "--help":
                    CharacterCommand.tokens.pop();
                    CharacterCommand.terminal.println(Help.GET);
                    return;
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
        itemBuilder.itemName = nameBuilder.toString().trim();
        if (itemBuilder.itemName.isEmpty()) {
            CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": item_name");
            return;
        }
        if (Inventory.isCurrency(itemBuilder.itemName)) {
            getCoins(pc, itemBuilder);
            return;
        }
        getItem(pc, itemBuilder);
    }

    public static void getItem(PlayerCharacter pc, ItemBuilder itemBuilder) {
        switch (itemBuilder.itemType) {
            case ITEM:
                Item item = itemBuilder.toItem();
                pc.addNewItem(item);
                break;
            case CONSUMABLE:
                Consumable consumable = itemBuilder.toConsumable();
                pc.addNewItem(consumable);
                break;
            case EQUIPPABLE:
                Equippable equippable = itemBuilder.toEquippable();
                pc.addNewItem(equippable);
                break;
            case WEAPON:
                Weapon weapon = itemBuilder.toWeapon();
                pc.addNewItem(weapon);
                break;
            case ARMOR:
                Armor armor = itemBuilder.toArmor();
                pc.addNewItem(armor);
                break;
            default:
                return;
        }
        CharacterCommand.terminal.println(String.format("Got %dx %s", itemBuilder.itemCount, itemBuilder.itemName));
    }

    public static void getCoins(PlayerCharacter pc, ItemBuilder itemBuilder) {
        switch (itemBuilder.itemName.toLowerCase()) {
            case "pp":
            case "platinum":
                InventoryIO.addDropCoin(pc, Inventory.indexPL, itemBuilder.itemCount, false, "add");
                break;
            case "gp":
            case "gold":
                InventoryIO.addDropCoin(pc, Inventory.indexGP, itemBuilder.itemCount, false, "add");
                break;
            case "sp":
            case "silver":
                InventoryIO.addDropCoin(pc, Inventory.indexSP, itemBuilder.itemCount, false, "add");
                break;
            case "cp":
            case "copper":
                InventoryIO.addDropCoin(pc, Inventory.indexCP, itemBuilder.itemCount, false, "add");
                break;
            default:
                break;
        }
    }
}
