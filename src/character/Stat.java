package character;

import java.io.Serializable;

public class Stat implements Serializable {
	private String name;
	private double baseVal;
	private double bonusVal;
	
	public Stat(){
		this.name=null;
		this.baseVal = 0;
		this.bonusVal = 0;
	}
	public Stat(String name, double baseVal){
		this.name=name;
		this.baseVal=baseVal;
		this.bonusVal = 0;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public double getBaseVal() {
		return baseVal;
	}
	public void setBaseVal(double baseVal) {
		this.baseVal = baseVal;
	}

	public double getBonusVal(){
		return this.bonusVal;
	}
	public void setBonusVal(double val){
		this.bonusVal = val;
	}
	public void addBonusVal(double val){
		this.bonusVal += val;
	}
	public void decBonusVal(double val){
		this.bonusVal -= val;
	}

	public void incrementBaseVal(){
		this.baseVal++;
	}
	public void decrementBaseVal(){
		this.baseVal--;
	}
	public void incrementBaseVal(double val){
		this.baseVal+=val;
	}
	public void decrementBaseVal(double val){
		this.baseVal-=val;
	}

	public double getTotal(){
		return (this.baseVal);
	}
	
	@Override 
	public String toString(){
		return String.format("%s: %.0f", this.getName(), this.getTotal());
	}
}

