package app;

import java.io.Serializable;

public class SpellSlot implements Serializable {
	protected int level;
	protected int total;
	protected int charges;
	
	public SpellSlot(int level, int total){
		this.level = level;
		this.total = this.charges = total;
	}
	
	public String toString(){
		return String.format("Level %d - %d/%d", this.level, this.charges, this.total);
	}
}
