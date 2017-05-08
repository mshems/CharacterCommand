package items;

import character.PlayerCharacter;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer value;
	private String name;
	private String description="";
	private int count;
	private boolean equipped = false;
	private boolean equippable;
	private boolean consumable;

	public static final String[] types = new String[] {
		"item","consumable","equippable","weapon","armor"
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
	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public void addCount(int count){
		this.count+=count;
	}

	public Integer getValue(){
		return value;
	}
	public void setValue(Integer value){
		this.value = value;
	}

	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description = description;
	}

	public void equip(PlayerCharacter c){};
	public void dequip(PlayerCharacter c){};
	public void use(int amount){};
	
	public boolean isEquippable() {
		return equippable;
	}
	public void setEquippable(boolean equippable) {
		this.equippable = equippable;
	}
	public boolean isEquipped(){
		return equipped;
	}
	public boolean isConsumable(){
		return consumable;
	}
	public void setConsumable(boolean consumable){
		this.consumable = consumable;
	}

	public String toString(){
		String s = String.format("%dx %s", this.getCount(), this.getName());
		return s;
	}
}
