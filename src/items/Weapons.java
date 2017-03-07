package items;
import app.*;

public class Weapons {
	public static final Weapon ssword = new Weapon("Shortsword", new DiceRoll(1,6));
	public static final Weapon straxe = new Weapon("Axe of Strength", new DiceRoll(1,10), new int[]{Attribute.STR}, new int[]{2});
	public static final Weapon lsword = new Weapon("Longsword", new DiceRoll(1,8));
}
