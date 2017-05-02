package items;

import character.PlayerCharacter;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private int count;
	private boolean equippable;

	public static final String[] types = new String[] {
		"item","consumable","equippable","weapon","item*","consumable*","armor","equippable*","weapon*","armor*"
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
	
	public void equip(PlayerCharacter c){};
	public void dequip(PlayerCharacter c){};
	
	public boolean isEquippable() {
		return equippable;
	}
	public void setEquippable(boolean equippable) {
		this.equippable = equippable;
	}

	public String toString(){
		String s = String.format("\n- %dx %s", this.getCount(), this.getName());
		return s;
	}
}
