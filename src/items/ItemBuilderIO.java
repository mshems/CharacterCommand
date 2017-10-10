package items;

import app.CharacterCommand;
import character.Inventory;
import character.InventoryIO;
import character.PlayerCharacter;
import character.Stat;
import utils.Message;

public class ItemBuilderIO {
    public static void buildItem(PlayerCharacter pc, ItemBuilder itemBuilder){
        inputItemInfo(itemBuilder);
        if(itemBuilder.itemName.equalsIgnoreCase("cancel")){
            itemBuilder.itemType=null;
            return;
        }
        if(pc.getInventory().contains(itemBuilder.itemName.toLowerCase())){
            if(itemBuilder.itemType == Item.ItemType.COIN){
                int coinType = -1;
                switch (itemBuilder.itemName) {
                    case "pp":
                    case "platinum":
                        coinType = Inventory.indexPL;
                        break;
                    case "gp":
                    case "gold":
                        coinType = Inventory.indexGP;
                        break;
                    case "sp":
                    case "silver":
                        coinType = Inventory.indexCP;
                        break;
                    case "cp":
                    case "copper":
                        coinType = Inventory.indexCP;
                        break;
                }
                if(coinType!= -1) {
                    InventoryIO.addDropCoin(pc, Inventory.indexCP, itemBuilder.itemCount, false, "add");
                }
            } else {
                InventoryIO.addDropItem(pc, pc.getItem(itemBuilder.itemName.toLowerCase()), itemBuilder.itemCount, false, "add");
            }
            itemBuilder.itemType = null;
            return;
        }

        CharacterCommand.terminal.println("item | equippable | weapon | armor | consumable | coin");
        while (true) {
            String type = CharacterCommand.terminal.queryString("Item type: ", false);
            if (type.equals("cancel")) {
                itemBuilder.itemType = null;
                return;
            }
            itemBuilder.itemType = Item.parseItemType(type);
            if (itemBuilder.itemType != null) {
                break;
            }
            CharacterCommand.terminal.println(Message.ERROR_ITEM_TYPE);
        }

        /*inputItemInfo(itemBuilder);
        if(pc.getInventory().contains(itemBuilder.itemName.toLowerCase())){
            if(itemBuilder.itemType == Item.ItemType.COIN){
                int coinType = -1;
                switch (itemBuilder.itemName) {
                    case "pp":
                    case "platinum":
                        coinType = Inventory.indexPL;
                        break;
                    case "gp":
                    case "gold":
                        coinType = Inventory.indexGP;
                        break;
                    case "sp":
                    case "silver":
                        coinType = Inventory.indexCP;
                        break;
                    case "cp":
                    case "copper":
                        coinType = Inventory.indexCP;
                        break;
                }
                if(coinType!= -1) {
                    InventoryIO.addDropCoin(pc, Inventory.indexCP, itemBuilder.itemCount, false, "add");
                }
            } else {
                InventoryIO.addDropItem(pc, pc.getItem(itemBuilder.itemName.toLowerCase()), itemBuilder.itemCount, false, "add");
            }
            itemBuilder.itemType = null;
            return;
        }
        */
        switch (itemBuilder.itemType) {
            case ARMOR:

                CharacterCommand.terminal.println("light | medium | heavy | shield | other");
                while (itemBuilder.armorType == null) {
                    itemBuilder.armorType = Armor.parseType(
                            CharacterCommand.terminal.queryString("Armor type: ", false)
                    );
                    if (itemBuilder.armorType == null) {
                        CharacterCommand.terminal.println("ERROR: Not a valid armor type");
                    }
                    itemBuilder.armorClass = CharacterCommand.terminal.queryInteger("AC: ", false);
                }
                inputEffects(pc,itemBuilder);
                break;
            case COIN:
                //itemBuilder.itemName = CharacterCommand.terminal.queryString("Coin type: ",false);
                //itemBuilder.itemCount = CharacterCommand.terminal.queryInteger("Amount: ", false);
                InventoryIO.getCoins(pc, itemBuilder);
                return;
            case CONSUMABLE:
            case ITEM:
                //inputItemInfo(itemBuilder);
                break;
            case WEAPON:
                //inputItemInfo(itemBuilder);
                itemBuilder.damage = CharacterCommand.getDiceRoll("Weapon damage: ");
                inputEffects(pc, itemBuilder);
                break;
            case EQUIPPABLE:
                //inputItemInfo(itemBuilder);
                inputEffects(pc,itemBuilder);
                break;
            default:
                break;
        }
    }

    private static void inputItemInfo(ItemBuilder itemBuilder) {
        itemBuilder.itemName = CharacterCommand.terminal.queryString("Item name: ",false);
        if(!itemBuilder.itemName.equalsIgnoreCase("cancel")) {
            itemBuilder.itemCount = CharacterCommand.terminal.queryInteger("Count: ", false);
        }
    }

    private static void inputEffects(PlayerCharacter pc, ItemBuilder itemBuilder) {
        while (CharacterCommand.terminal.queryYN("Add effect? [Y/N] : ")) {
            String statName = CharacterCommand.terminal.queryString("Effect Target: ",false);
            if (!statName.equalsIgnoreCase("cancel")) {
                Stat target = pc.getStat(statName);
                if (target == null) {
                    CharacterCommand.terminal.println("ERROR: Effect target not found");
                } else {
                    itemBuilder.addEffect(target, CharacterCommand.terminal.queryInteger("Stat Bonus: ", false));
                }
            }
        }
    }
}
