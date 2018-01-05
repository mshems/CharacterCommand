package app.ui.useraction;

import app.CharacterCommand;
import jterminal.core.IllegalTokenException;
import app.ui.CCExtensions;
import core.character.inventory.*;
import jterminal.optional.menu.*;
import core.character.stats.Stat;
import core.items.*;
import core.items.consumable.*;
import core.items.equippable.*;
import core.items.equippable.armor.*;
import core.items.equippable.weapon.*;
import core.items.magic.MagicEffect;
import core.items.magic.MagicItem;

import java.util.*;

public class InventoryAction {

    public static void viewInventory(CharacterCommand cc){
        if(!cc.terminal.hasTokens()) {
            cc.terminal.out.println(cc.getActiveCharacter().getInventory());
        } else {
            switch (cc.terminal.nextToken()) {
                case "-m":
                case "--menu":
                    viewInventoryMenu(cc);
                    break;
                case "-i":
                case "--item":
                    viewItem(cc);
                    break;
                case "-im":
                case "--item-menu":
                    itemMenu(cc);
                    break;
                case "--help":
                    cc.terminal.out.println("HELP MENU");
                    break;
            }
        }
    }

    public static void viewInventoryMenu(CharacterCommand cc){
        cc.terminal.out.println(
                ListMenu.queryMenu(
                        new MenuFactory()
                                .setDirection(ListMenu.VERTICAL)
                                .buildObjectMenu(
                                        cc.terminal,
                                        cc.getActiveCharacter().getInventory().getContents().values(),
                                        ItemStack::toString))
                        .getItem().details());
    }

    private static void viewItem(CharacterCommand cc){
        String itemName;
        itemName = CCExtensions.buildNameFromTokens(cc.terminal);
        if(itemName!=null) {
            Item i = cc.getActiveCharacter().getInventory().getItem(itemName.trim());
            if (i != null) {
                cc.terminal.out.println(i.details());
            }
        }
    }

    //TODO: item action menu
    private static void itemMenu(CharacterCommand cc){};

    public static void add(CharacterCommand cc){
        String itemName;
        int count;
        ItemType itemType;
        if(!cc.terminal.hasTokens()) {
             itemName = cc.terminal.queryString("Item name: ");
            //adding an item already in inventory
            if (cc.getActiveCharacter().getInventory().contains(itemName)) {
                cc.getActiveCharacter().inventoryBehavior.add(
                        cc.getActiveCharacter().getInventory().getItem(itemName),
                        cc.terminal.queryInteger("Amount to add: "));
            } else {
            //adding a new item
                count = cc.terminal.queryInteger("Item count: ");
                itemType = ListMenu.queryMenu(
                        new MenuFactory()
                                .setDirection(ListMenu.HORIZONTAL)
                                .buildObjectMenu(
                                        cc.terminal,
                                        Arrays.asList(ItemType.values()),
                                        ItemType::toString));
                if(itemType==null) return;
                MagicItem item;
                switch (itemType){
                    case ITEM:
                        cc.getActiveCharacter().inventoryBehavior.add(new Item(itemName), count);
                        break;
                    case CONSUMABLE:
                        cc.getActiveCharacter().inventoryBehavior.add(new Consumable(itemName), count);
                        break;
                    case EQUIPMENT:
                        item = enchantItem(cc, new Equipment(itemName));
                        cc.getActiveCharacter().inventoryBehavior.add(item, count);
                        break;
                    case SHIELD:
                        int acBonus = cc.terminal.queryInteger("Shield AC: ");
                        item = enchantItem(cc, new Shield(itemName, acBonus));
                        cc.getActiveCharacter().inventoryBehavior.add(item, count);
                        break;
                    case ARMOR:
                        int ac = cc.terminal.queryInteger("Armor AC: ");
                        ArmorType armorType = ListMenu.queryMenu(
                                new MenuFactory()
                                        .setDirection(ListMenu.HORIZONTAL)
                                        .buildObjectMenu(
                                                cc.terminal,
                                                Arrays.asList(ArmorType.values()),
                                                Enum::toString));
                        if(armorType==null) break;
                        GearSlot gearSlot = ListMenu.queryMenu(
                                new MenuFactory()
                                        .setDirection(ListMenu.HORIZONTAL)
                                        .buildObjectMenu(
                                                cc.terminal,
                                                GearSlot.ArmorSlotList(),
                                                GearSlot::toString));
                        if(gearSlot==null) break;
                        item = enchantItem(cc, new Armor(itemName, ac, armorType, gearSlot));
                        cc.getActiveCharacter().inventoryBehavior.add(item, count);
                        break;
                    case WEAPON:
                        if(cc.terminal.queryYN("Specify weapon damage? [Y/N]: ")){
                            //TODO
                            cc.terminal.out.println("[WORK IN PROGRESS]");
                        } else {
                            item = enchantItem(cc, new Weapon(itemName));
                            cc.getActiveCharacter().inventoryBehavior.add(item, count);
                        }
                        break;
                }
                cc.terminal.out.println(String.format("Added %dx \"%s\"", count, itemName));
            }
        } else {
            addParsedItem(cc);
        }
    }

    private static MagicItem enchantItem(CharacterCommand cc, MagicItem item){
        if(cc.terminal.queryYN("Enchant item? [Y/N]: ")){
            do {
                Stat targetStat;
                do {
                   targetStat = cc.getActiveCharacter().getStat(cc.terminal.queryString("Stat to affect: "));
                } while(targetStat==null);
                int bonus = cc.terminal.queryInteger("Effect stat bonus: ");
                item.addEffect(new MagicEffect(targetStat, bonus));
            } while(cc.terminal.queryYN("Add another enchantment? [Y/N]: "));
        }
        return item;
    }

    private static void addParsedItem(CharacterCommand cc) {
        String itemName = null;
        int count = 1;
        ItemType itemType = ItemType.ITEM;
        Integer ac = null;
        GearSlot gearSlot = GearSlot.BODY;
        ArmorType armorType = null;
        LinkedList<MagicEffect> effects = null;
        ArrayList<String> tags = null;
        WeaponDamage dmg = null;
        try {
            while (cc.terminal.hasTokens()) {
                if (cc.terminal.peekToken().startsWith("\"")) {
                    itemName = CCExtensions.buildNameFromTokens(cc.terminal);
                } else switch (cc.terminal.nextToken()) {
                    case "-c":
                    case "--count":
                        count = cc.terminal.nextIntToken();
                        break;
                    case "-t":
                    case "--type":
                        itemType = ItemType.parseType(cc.terminal.nextToken());
                        break;
                    case "-ac":
                    case "--armor-class":
                        ac = cc.terminal.nextIntToken();
                        break;
                    case "-at":
                    case "--armor-type":
                        armorType = ArmorType.parseType(cc.terminal.nextToken());
                        break;
                    case "-g":
                    case "--gear-slot":
                        gearSlot = GearSlot.parseSlot(cc.terminal.nextToken());
                        break;
                    case "-e":
                    case "--enchantment":
                    case "--effect":
                        Stat targetStat = cc.getActiveCharacter().getStat(cc.terminal.nextToken());
                        if (targetStat != null) {
                            int effectBonus = cc.terminal.nextIntToken();
                            if(effects==null) effects = new LinkedList<>();
                            effects.add(new MagicEffect(targetStat, effectBonus));
                        } else {
                            cc.terminal.out.println("ERROR: Stat not found");
                            return;
                        }
                        break;
                    case "-d":
                    case "-dmg":
                    case "--damage":
                        //TODO
                        cc.terminal.out.println("[DAMAGE ROLL WIP]");
                        break;
                    case "--tags":
                        if(tags==null) tags = new ArrayList<>();
                        while(cc.terminal.hasTokens()){
                            tags.add(cc.terminal.nextToken());
                        }
                        break;
                    case "--help":
                        cc.terminal.out.println("HELP MENU WIP :D");
                        break;
                    default:
                        cc.terminal.nextToken();
                        break;
                }
            }
        } catch (IllegalTokenException e) {
            cc.terminal.out.println("ERROR: Illegal token: \"" + e.getToken() + "\"\n  Enter \"add --help\" for help");
            return;
        }
        if (itemName == null) {
            cc.terminal.out.println("ERROR: No name specified");
        } else {
            switch (itemType) {
                case ITEM:
                    cc.getActiveCharacter().inventoryBehavior.add(new Item(itemName), count);
                    break;
                case CONSUMABLE:
                    cc.getActiveCharacter().inventoryBehavior.add(new Consumable(itemName), count);
                    break;
                case ARMOR:
                    if (ac == null) {
                        cc.terminal.out.println("ERROR: AC not specified");
                    } else if (armorType == null) {
                        cc.terminal.out.println("ERROR: Armor Type not specified");
                    } else {
                        cc.getActiveCharacter().inventoryBehavior.add(
                                new Armor(itemName, ac, armorType, gearSlot).addEffects(effects), count);
                    }
                    break;
                case SHIELD:
                    if (ac == null) {
                        cc.terminal.out.println("ERROR: AC not specified");
                    } else {
                        cc.getActiveCharacter().inventoryBehavior.add(new Shield(itemName, ac).addEffects(effects), count);
                    }
                    break;
                case WEAPON:
                    cc.getActiveCharacter().inventoryBehavior.add(new Weapon(itemName, dmg).addEffects(effects), count);
                    break;
                case EQUIPMENT:
                    cc.getActiveCharacter().inventoryBehavior.add(new Equipment(itemName).addEffects(effects), count);
                    break;
            }
            cc.getActiveCharacter().getInventory().getItem(itemName).addTags(tags);
            cc.terminal.out.println(String.format("Added %dx \"%s\"", count, itemName));
        }
    }

}
