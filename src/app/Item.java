package app;

import java.io.Serializable;
import java.util.Scanner;

public class Item implements Serializable, Comparable<Item> {
	private static final long serialVersionUID = 1;
	public String itemName;
	protected String description;
	protected int value=0;
	public int itemCount;
	protected boolean equippable = false;
	protected boolean equipped;
	
	//guided item creation
	public Item(){
		System.out.print("[New Item]");
		Scanner scanner = new Scanner(System.in);
		System.out.print("\nEnter item name: ");
		this.itemName = scanner.nextLine().trim();
	}
	
	public Item(String itemName){
		this.itemName = itemName;
		this.itemCount = 1;
	}
	
	public Item(String itemName, int itemCount){
		this.itemName = itemName;
		this.itemCount = itemCount;
	}
	
	public String getItemName(){
		return this.itemName;
	}
	
	public void setItemName(String s){
		this.itemName = s;
	}
	
	public int getItemCount() {
		return itemCount;
	}
	
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	public void addItemCount(int addCount) {
		this.itemCount += addCount;
	}

	public boolean isEquippable() {
		return equippable;
	}

	public void setEquippable(boolean equippable) {
		this.equippable = equippable;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}
	
	public String toString(){
		String s = String.format("- %dx %s",  this.itemCount, this.itemName);
		return s;
	}
	
	public String details(){
		String s = String.format("[%s]", this.itemName);
		if (this.description!=null){
			s+="\n"+this.description;
		}
		if (this.value!=0){
			s+="\n[Value: "+this.value+"]";
		}
		return s;
	}
	
	public void equip(Character c){}
	
	public void dequip(Character c){}

	@Override
	public int compareTo(Item item) {
		return this.itemName.compareToIgnoreCase(item.itemName);
	}
}
