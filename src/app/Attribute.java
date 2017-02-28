package app;

import java.io.Serializable;

public class Attribute implements Serializable{
	public static final int STR = 0;
	public static final int DEX = 1;
	public static final int CON = 2;
	public static final int WIS = 3;
	public static final int INT = 4;
	public static final int CHA = 5;
	public static final int HP = 0;
	public static final int AC = 1;
	public static final int SPD = 2;
	public static final int PER = 3;
	public static final int PRO = 4;
	public static final int INI = 5;
	public static final int SSDC = 6;
	public static final int SAM = 7;
	
	private String name;
	private double base;
	private double bonus;
	@SuppressWarnings("unused")
	private double mod;
	protected String[] matches;
	
	public Attribute(double baseVal, String name, String[] matches){
		this.name = name;
		this.base = baseVal;
		this.mod = this.getMod();
		this.matches = matches;
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
