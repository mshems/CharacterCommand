package items;

import java.util.ArrayList;

import app.Armor;
import app.Attribute;

public class Armors {
	public static ArrayList<Integer> aB = new ArrayList<Integer>(){{
			add(-2);
			add(-2);
	}};
	public static ArrayList<Integer> aT = new ArrayList<Integer>(){{
			add(Attribute.CON);
			add(Attribute.CHA);
	}};
	
	public static final Armor arm1 = new Armor("Shield of Cowardice", aT, aB, Armor.shield, 2);
}
