package app.io;

import app.CharacterCommand;
import app.core.character.PlayerCharacter;
import app.core.items.Item;
import app.utils.Help;
import app.utils.Message;

public class ItemIO {
    public static Item getItemByName(PlayerCharacter pc) {
        Item item;
        while (true) {
            String itemName = CharacterCommand.terminal.queryString("Item name: ", false);
            if (itemName.equalsIgnoreCase("cancel")) {
                return null;
            } else {
                for (Item coin : pc.getCurrency()) {
                    if (coin.getName().equalsIgnoreCase(itemName)) {
                        return coin;
                    }
                }
                item = pc.getItem(itemName);
                if (item == null) {
                    /*int matches = 0;
                    for(String str:pc.getInventory().getContents().keySet()){
                        if(str.startsWith(itemName.toLowerCase())){
                            item = pc.getItem(str);
                            matches++;
                        }
                    }
                    if(matches == 1){
                        return item;
                    }*/
                    CharacterCommand.terminal.println(Message.MSG_NO_ITEM);
                } else {
                    return item;
                }
            }
        }
    }
    public static void use(PlayerCharacter pc) {
        CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            useParser(pc);
        } else {
            Item item = getItemByName(pc);
            if (item != null) {
                if (item.isConsumable()) {
                    int amount = CharacterCommand.terminal.queryInteger("Amount: ", false);
                    item.use(amount);
                    CharacterCommand.terminal.println("Used " + amount + "x " + item.getName());
                    if (item.getCount() <= 0) {
                        pc.removeItem(item);
                    }
                } else {
                    CharacterCommand.terminal.println(Message.ERROR_NOT_CON);
                }
            }
        }
    }

    private static void useParser(PlayerCharacter pc) {
        Item item;
        StringBuilder nameBuilder = new StringBuilder();
        Integer amount = 1; //default
        boolean help = false;
        while (!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-c":
                case "--count":
                    CharacterCommand.tokens.pop();
                    if (CharacterCommand.tokens.isEmpty()) {
                        CharacterCommand.terminal.println(Message.ERROR_NO_ARG + ": amount");
                    } else {
                        amount = CharacterCommand.getIntToken();
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
                    break;
            }
        }
        if (!help) {
            String itemName = nameBuilder.toString().trim();
            item = pc.getItem(itemName);
            if (item != null && amount != null) {
                if (item.isConsumable()) {
                    item.use(amount);
                    CharacterCommand.terminal.println("Used " + amount + "x " + item.getName());
                    if (item.getCount() <= 0) {
                        pc.removeItem(item);
                    }
                } else {
                    CharacterCommand.terminal.println(Message.ERROR_NOT_CON);
                }
            } else {
                CharacterCommand.terminal.println(Message.MSG_NO_ITEM);
            }
        } else {
            CharacterCommand.terminal.println(Help.USE);
        }
    }

    public static void equip(PlayerCharacter pc) {
        String command = CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()) {
            equipParser(pc, command);
        } else {
            Item item = getItemByName(pc);
            if (item != null) {
                if (item.isEquippable()) {
                    if (command.equalsIgnoreCase("equip")) {
                        if (!item.isEquipped()) {
                            pc.equip(item);
                        } else {
                            CharacterCommand.terminal.println("ERROR: Item already equipped");
                        }
                    } else {
                        if (item.isEquipped()) {
                            pc.dequip(item);
                        } else {
                            CharacterCommand.terminal.println("ERROR: Item not equipped");
                        }
                    }
                } else {
                    CharacterCommand.terminal.println(Message.ERROR_NOT_EQUIP);
                }
            } else {
                CharacterCommand.terminal.println(Message.MSG_NO_ITEM);
            }
        }
    }


    private static void equipParser(PlayerCharacter pc, String command) {
        Item item;
        StringBuilder nameBuilder = new StringBuilder();
        boolean help = false;

        while (!CharacterCommand.tokens.isEmpty()) {
            if (CharacterCommand.tokens.peek().equals("--help")) {
                help = true;
                CharacterCommand.tokens.pop();
            }
            nameBuilder.append(CharacterCommand.tokens.pop());
            nameBuilder.append(" ");
        }
        if (!help) {
            String itemName = nameBuilder.toString().trim();
            item = pc.getItem(itemName);
            if (item != null) {
                if (item.isEquippable()) {
                    if (command.equalsIgnoreCase("equip")) {
                        if (!item.isEquipped()) {
                            pc.equip(item);
                            CharacterCommand.terminal.println(item.getName()+" equipped");
                        } else {
                            CharacterCommand.terminal.println("ERROR: Item already equipped");
                        }
                    } else {
                        if (item.isEquipped()) {
                            pc.dequip(item);
                            CharacterCommand.terminal.println(item.getName()+" dequipped");
                        } else {
                            CharacterCommand.terminal.println("ERROR: Item not equipped");
                        }
                    }
                } else {
                    CharacterCommand.terminal.println(Message.ERROR_NOT_EQUIP);
                }
            } else {
                CharacterCommand.terminal.println(Message.MSG_NO_ITEM);
            }
        } else {
            if (command.equalsIgnoreCase("equip")) {
                CharacterCommand.terminal.println(Help.EQUIP);
            } else {
                CharacterCommand.terminal.println(Help.DEQUIP);
            }
        }
    }
}
