package app;

import java.io.Serializable;

public class Attribute implements Serializable{
	public static final int HP = 0;
	public static final int AC = 1;
	public static final int SPD = 2;
	public static final int PER = 3;
	public static final int PRO = 4;
	public static final int INI = 5;
	public static final int SSDC = 6;
	public static final int SAM = 7;
	
	protected String name;
	protected double base;
	protected double bonus;
	protected String[] matches;
	
	public Attribute(double base, String name, String[] matches){
		this.name = name;
		this.base = base;
		this.matches = matches;
		this.bonus = 0;
	}
	
	public double total(){
		return this.base + this.bonus;
	}
	
	public String toString(){
		String s = String.format("%s: %.0f", this.name, this.base+this.bonus);
		return s;
	}
	
	public String detail(){
		String s = String.format("[%s DETAIL]\nBase: %.0f\nBonus: %+.0f\nTotal: %.0f", this.name, this.base, this.bonus, this.base+this.bonus);
		return s;
	}	
}
