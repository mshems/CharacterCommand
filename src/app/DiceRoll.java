package app;

import utils.Help;
import utils.Message;

import java.io.Serializable;
import java.util.Random;

public class DiceRoll implements Serializable{
	private static final long serialVersionUID = App.VERSION;
	protected int count;
	protected int sides;
	
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

	static Integer doRoll(){
		App.tokens.pop();
		int sides;
		int num;
		int total=0;
		int mod=0;
		String result="";
		if(!App.tokens.isEmpty()){
			if (App.tokens.contains("--help")){
				System.out.println(Help.ROLL);
				return 0;
			} else {
				String token = App.tokens.pop();
				if(token.matches("(\\d+d\\d+)|(\\d+d\\d+[\\+|\\-]\\d+)")){
					String[] a = App.input[1].split("(d)|(?=[+|-])");
					num = Integer.parseInt(a[0]);
					sides = Integer.parseInt(a[1]);
					if (a.length == 3){
						mod = Integer.parseInt(a[2]);
					}
				}else {
					System.out.println(Message.ERROR_SYNTAX);
					return 0;
				}
			}
		} else {
			num = App.getValidInt("Enter number of dice: ");
			sides = App.getValidInt("Enter number of sides: ");
			mod = App.getValidInt("Enter any bonuses: ");
		}

		Random random = new Random();
		for (int i = 0; i < num; i++){
			int val = random.nextInt(sides) + 1;
			result += val;
			if (i < num - 1){
				result += " + ";
			}
			total += val;
		}
		if (mod != 0){
			System.out.println(String.format("Rolling %dd%d%+d", num, sides, mod));
			System.out.println(String.format("%s (%+d) = %d", result, mod, total + mod));
		} else {
			System.out.println(String.format("Rolling %dd%d", num, sides));
			if (num > 1){
				System.out.println(result + " = " + total);
			}
		}
		return total + mod;
	}

	private static Integer doRoll(int num, int sides, int mod){
		DiceRoll roll = new DiceRoll(num, sides);
		int total = roll.roll();
		return total+mod;
	}
	
	public String toString(){
		String s = String.format("%dd%d", this.count, this.sides);
		return s;		
	}
}
