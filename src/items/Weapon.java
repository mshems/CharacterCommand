package items;

import app.App;
import app.DiceRoll;

public class Weapon extends Equippable{
	private static final long serialVersionUID = App.version;
	private DiceRoll damage;
	
	public Weapon(String name){
		super(name);
	}
	
	public Weapon(String name, int count){
		super(name, count);
	}
	
	public Weapon(String name, DiceRoll dmg){
		super(name);
		this.damage = dmg;
	}
	
	@Override
	public String toString(){
		String s =super.toString();
		if (damage!=null){
			s+=" ["+damage+"]";
		}
		return s;
	}
}
