package items;

public class Consumable extends Item{

	public Consumable(String name) {
		super(name);
	}
	
	public Consumable(String name, int count) {
		super(name, count);
	}
	
	public void use(){
		this.setCount(getCount()-1);
	}

}
