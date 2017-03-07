package app;

import java.util.ArrayList;

public class Inventory extends ArrayList<Item> {

	public String toString(){
		String s = String.format("[Inventory]\n[$] %s %s %s", this.get(0), this.get(1), this.get(2));
		int n = 0;
		for (Item i : this){
			n++;
			if (n>3){
				s += "\n["+(n-3)+"] "+i;
			}
		}
		 return s;
	}
}
