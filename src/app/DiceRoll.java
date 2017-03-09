package app;

import java.io.Serializable;
import java.util.Random;

public class DiceRoll implements Serializable {
	protected int count;
	protected int sides;
	
	public DiceRoll(int c, int s){
		this.count = c;
		this.sides = s;
	}
	
	public int roll(){
		int result=0;
		Random random = new Random();
		for (int i=0; i<count; i++){
			result += random.nextInt(sides)+1;
		}
		return result;
	}
	
	public String toString(){
		String s = String.format("%dd%d", this.count, this.sides);
		return s;		
	}
}
