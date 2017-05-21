package character;

import app.App;

import java.io.Serializable;
public class Ability extends Attribute implements Serializable{
	private static final long serialVersionUID = App.VERSION;
	private double mod;
	public static final String[] ABILITY_NAMES = new String[]{"STR", "DEX", "CON", "INT", "WIS", "CHA"};
	public static final String STR = "str";
	public static final String DEX = "dex";
	public static final String CON = "con";
	public static final String INT = "int";
	public static final String WIS = "wis";
	public static final String CHA = "cha";
	
	public Ability(){
		super();
		this.mod = getMod();
	}

	public Ability(String name, double baseVal){
		super(name, baseVal);
		this.mod=getMod();
	}
	
	public double getMod(){
		this.mod = Math.floor((this.getBaseVal() + this.getBonusVal() - 10) / 2);
		return mod;
	}
	
	@Override 
	public String toString(){
		return String.format("%s: %.0f (%+.0f)", this.getName(), this.getTotal(), this.getMod());
	}

	public String detailString() {
		String newLine = System.lineSeparator();
		return String.format("%s"+newLine+"Base value: %.0f"+newLine+"Bonuses: %.0f"+newLine+"Total: %.0f (%+.0f)",
				this.getName(),
				this.getBaseVal(),
				this.getBonusVal(),
				this.getTotal(),
				this.getMod()
		);
	}
}
