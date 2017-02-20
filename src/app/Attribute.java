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
	
	private String name;
	private double value;
	private double mod;
	protected String[] matches;
	
	public Attribute(double value, String name, String[] matches){
		this.name = name;
		this.value = value;
		this.mod = this.getMod();
		this.matches = matches;
	}
	
	public String getName(){
		return this.name;
	}
	
	public double getMod(){
		double m;
		m = Math.floor((this.value - 10) / 2);
		return m;
	}
	
	public void setValue(double d){
		this.value = d;
	}
	
	public double getValue(){
		return this.value;
	}
	
	
	public String toString(){
		String s = String.format("%s: %.0f (%+.0f) ", this.name, this.value, this.getMod());
		return s;
	}
}
