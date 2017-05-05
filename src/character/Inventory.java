package character;
import items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Inventory implements Serializable {
	private LinkedHashMap<String, Item> contents;
	private ArrayList<Item> currency;
	
	public static final int indexPL=0;
	public static final int indexGP=1;
	public static final int indexSP=2;
	public static final int indexCP=3;
	
	public Inventory(){
		this.contents = new LinkedHashMap<String, Item>();
		this.currency = new ArrayList<Item>();
		currency.add(new Item("PP",0));
		currency.add(new Item("GP",0));
		currency.add(new Item("SP",0));
		currency.add(new Item("CP",0));
	}
	
	public void add(Item item){
		this.contents.put(item.getName().toLowerCase(), item);
	}
	public void remove(Item item){
		this.contents.remove(item.getName().toLowerCase());
	}
	public Item get(String itemName){
		return this.contents.get(itemName.toLowerCase());
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
	


	public String toString(){
		String c = "\n";
		String s="\033[1;33mInventory: \033[0m";
		for(Item i:currency){
			if (i.getCount()!=0){
				c+=String.format("%s: %d  ", i.getName(), i.getCount());
			}
		}
		if (!c.equals("\n")){
			s+=c;
		}
		for(String key:contents.keySet()){
			s+=contents.get(key);
		}
		if (contents.isEmpty()){
			s+="-Empty-";
		}
		return s;
	}
	
}
