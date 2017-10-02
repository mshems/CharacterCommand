package app;

import utils.Help;
import utils.Message;

import java.io.Serializable;
import java.util.Random;

public class DiceRoll implements Serializable{
	private static final long serialVersionUID = CharacterCommand.VERSION;
	private int count;
	private int sides;
	
	public DiceRoll(int count, int sides){
		this.count = count;
		this.sides = sides;
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
