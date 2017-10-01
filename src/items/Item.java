package items;

import app.CharacterCommand;
import character.PlayerCharacter;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long serialVersionUID = CharacterCommand.VERSION;
	private String name;
	private int count;
	private String description="";
	private Integer value;

	private boolean equipped = false;
	private boolean equippable;
	private boolean consumable;

	public enum ItemType {ARMOR, CONSUMABLE, EQUIPPABLE, ITEM, WEAPON, COIN};

	public static final String[] types = new String[] {
			"item","consumable","equippable","weapon","armor","c","i","e","w","a"
	};
	
	public Item(String name){
		this.name = name;
		this.equippable = false;
		this.count = 1;
	}
	public Item(String name, int count){
		this.name = name;
		this.equippable = false;
		this.count = count;
	}
	
	public String getName() {
		return name;
	}
	public Item setName(String name) {
		this.name = name;
		return this;
	}

	public int getCount() {
		return count;
	}
	public Item setCount(int count) {
		this.count = count;
		return this;
	}

	public void addCount(int count){
		this.count+=count;
	}

	public Integer getValue(){
		return value;
	}
	public Item setValue(Integer value){
		this.value = value;
		return this;
	}

	public String getDescription(){
		return description;
	}
	public Item setDescription(String description){
		this.description = description;
		return this;
	}

	public void equip(PlayerCharacter c){};
	public void dequip(PlayerCharacter c){};
	public void use(int amount){};
	
	public boolean isEquippable() {
		return equippable;
	}
	public Item setEquippable(boolean equippable) {
		this.equippable = equippable;
		return this;
	}
	public boolean isEquipped(){
		return equipped;
	}
	public boolean isConsumable(){
		return consumable;
	}
	public Item setConsumable(boolean consumable){
		this.consumable = consumable;
        return this;
	}

	public String toString(){
		String s = String.format("%dx %s", this.getCount(), this.getName());
		return s;
	}

	public static ItemType parseItemType(String s){
	    switch(s){
            case"a":
            case "armor":
                return ItemType.ARMOR;
            case "c":
            case "consumable":
                return ItemType.CONSUMABLE;
            case "e":
            case "equippable":
                return ItemType.EQUIPPABLE;
            case "i":
            case "item":
                return ItemType.ITEM;
            case "w":
            case "weapon":
                return ItemType.WEAPON;
            case "currency":
            case "coin":
                return ItemType.COIN;
            default:
                return null;
        }
    }
}
