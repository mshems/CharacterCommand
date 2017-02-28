package items;

import app.*;

public class Items {
	public static final Item gp = new Item("Gold");
	public static final Item sp = new Item("Silver");
	public static final Item cp = new Item("Copper");
	
	public static final Object[] steadfastShield = new Object[]{"Steadfast Shield", new int[]{Attribute.CON}, new int[]{2}, Armor.shield, 2};
}
