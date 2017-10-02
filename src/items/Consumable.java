package items;

import app.CharacterCommand;

public class Consumable extends Item{
	private static final long serialVersionUID = CharacterCommand.VERSION;
	public Consumable(String name) {
		super(name);
		this.setConsumable(true);
	}
	
	public Consumable(String name, int count) {
		super(name, count);
		this.setConsumable(true);
	}
	
	public void use(int amount){
		this.addCount(-amount);
	}

}
