package character;
import app.App;
import items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Inventory implements Serializable {
	private static final long serialVersionUID = App.VERSION;
	private LinkedHashMap<String, Item> contents;
	private ArrayList<Item> currency;
	
	public static final int indexPL=0;
	public static final int indexGP=1;
	public static final int indexSP=2;
	public static final int indexCP=3;
	
	public Inventory(){
		this.contents = new LinkedHashMap<String, Item>();
		this.currency = new ArrayList<Item>();
		currency.add(new Item("pp",0));
		currency.add(new Item("gp",0));
		currency.add(new Item("sp",0));
		currency.add(new Item("cp",0));
	}
	
	public void add(Item item){
		this.contents.put(item.getName().toLowerCase(), item);
	}
	public void remove(Item item){
		this.contents.remove(item.getName().toLowerCase());
	}
	public Item get(String itemName){
		return this.contents.get(itemName);
	}
	public Item getCurrency(int coin){
		return this.currency.get(coin);
	}
	public void addCurrency(int coin, int amt){
		this.currency.get(coin).addCount(amt);
	}
	public ArrayList<Item> getCurrency(){
		return this.currency;
	}

	public boolean contains(String key){
		return this.contents.containsKey(key);
	}
	public boolean contains(Item i){
		return this.contents.containsValue(i);
	}
	
	public static boolean isCurrency(String itemName){
		switch(itemName){
			case "pp":
			case "platinum":
			case "gp":
			case "gold":
			case "sp":
			case "silver":
			case "cp":
			case "copper":
				return true;
			default:
				return false;
		}
	}

	public String toString(){
		//String s="\033[1;33mInventory: \033[0m";
		String newLine = System.lineSeparator();
		String s="---- INVENTORY -----------------"+newLine;
		for(Item i:currency){
			if (i.getCount()!=0){
				//s+=String.format("%s: %d  ", i.getName(), i.getCount());
				s+=String.format(" %d%s ", i.getCount(), i.getName());
			}
		}
		if(!s.isEmpty()){
			//s=newLine+s;
		}
		//s = "Inventory: "+s;
		if (contents.isEmpty()){
			s+=" Empty";
		} else {
			for (Item item : contents.values()){
                if (item.isEquipped()){
                    s += newLine+" e "+item;
                } else {
                    s += newLine+" - "+item;
                }
			}
		}
		return s+newLine+"--------------------------------";
	}
	
}
