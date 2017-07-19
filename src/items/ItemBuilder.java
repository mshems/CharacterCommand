package items;

import app.DiceRoll;

import java.util.ArrayList;

/**
 * Created by mshem_000 on 7/19/2017.
 */
public class ItemBuilder{
    public String itemName;
    public String itemDescription;
    public Item.ItemType itemType = Item.ItemType.ITEM;
    public int itemCount = 1;
    public int itemValue;
    //public boolean equippable;
    //public boolean consumable;
    public Armor.ArmorType armorType;
    public int armorClass;
    public DiceRoll damage;

    private ArrayList<ItemEffect> itemEffects;

    public ItemBuilder(){
        itemEffects = new ArrayList<>();
    }

    public void addEffect(character.Stat target, int bonus){
        itemEffects.add(new ItemEffect(target,bonus));
    }

    public Armor toArmor(){
        Armor armor = new Armor(itemName, itemCount);
        armor.setAC(armorClass)
                .setArmorType(armorType)
                .setValue(itemValue)
                .setDescription(itemDescription);
        armor.setEffects(itemEffects);
    return armor;
    }

    public Consumable toConsumable(){
        Consumable consumable = new Consumable(itemName, itemCount);
        consumable.setDescription(itemDescription)
                .setValue(itemValue);
        return consumable;
    }

    public Equippable toEquippable(){
        Equippable equippable = new Equippable(itemName, itemCount);
        equippable.setDescription(itemDescription)
                .setValue(itemValue);
        equippable.setEffects(itemEffects);
        return equippable;
    }

    public Item toItem(){
        Item item = new Item(itemName, itemCount);
        item.setDescription(itemDescription)
                .setValue(itemValue);
        return item;
    }

    public Weapon toWeapon(){
        Weapon weapon = new Weapon(itemName, itemCount);
        weapon.setDamage(damage)
                .setDescription(itemDescription)
                .setValue(itemValue);
        return weapon;
    }
}
