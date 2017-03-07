package app;

import java.io.Serializable;

public class DiceRoll implements Serializable {
	protected int count;
	protected int sides;
	
	public DiceRoll(int c, int s){
		this.count = c;
		this.sides = s;
	}
	
	public String toString(){
		String s = String.format("%dd%d", this.count, this.sides);
		return s;		
	}
}
