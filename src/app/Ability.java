package app;

import java.io.Serializable;

public class Ability extends Attribute implements Serializable{
	public static final int STR = 0;
	public static final int DEX = 1;
	public static final int CON = 2;
	public static final int WIS = 3;
	public static final int INT = 4;
	public static final int CHA = 5;
	
	protected String name;
	protected double base;
	protected double bonus;
	@SuppressWarnings("unused")
	protected double mod;
	protected String[] matches;
	
	public Ability(double baseVal, String name, String[] matches){
		super(baseVal, name, matches);
		this.name = name;
		this.mod = this.getMod();
		this.bonus = 0;
	}
	
	public String getName(){
		return this.name;
	}
	
	public double getMod(){
		double m;
		m = Math.floor((this.base + this.bonus - 10) / 2);
		return m;
	}
	
	public void setBaseVal(double d){
		this.base = d;
	}
	
	public double getBaseVal(){
		return this.base;
	}
	
	public double total(){
		return this.base + this.bonus;
	}
	
	public double getBonus() {
		return bonus;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}
	
	public String toString(){
		String s = String.format("%s: %.0f (%+.0f) ", this.name, this.base+this.bonus, this.getMod());
		return s;
	}
	
	public String detail(){
		String s = String.format("[%s DETAIL]\nBase: %.0f\nBonus: %+.0f\nTotal: %.0f", this.name, this.base, this.bonus, this.base+this.bonus);
		return s;
	}	
}
