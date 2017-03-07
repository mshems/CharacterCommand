package app;

public class Consumable extends Item {

	
	public void use(){
		this.addItemCount(-1);
	}
}
