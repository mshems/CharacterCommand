package items;

import app.*;

public class Items {
	public static final Item gp = new Item("Gold");
	public static final Item sp = new Item("Silver");
	public static final Item cp = new Item("Copper");
	
	public static final Object[] premade1 = new Object[]{"STR Shield", new int[Attribute.STR], new int[2], Armor.shield, 2};
}
