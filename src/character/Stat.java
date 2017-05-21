package character;

import app.App;

import java.io.Serializable;

public class Stat implements Serializable {
	private static final long serialVersionUID = App.version;
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
		return (this.baseVal+this.bonusVal);
	}
	
	@Override 
	public String toString(){
		return String.format("%s: %.0f", this.name, this.getTotal());
	}

	public String detailString() {
		String newLine = System.lineSeparator();
		return String.format("%s"+newLine+"Base value: %.0f"+newLine+"Bonuses: %.0f"+newLine+"Total: %.0f",
				this.name,
				this.baseVal,
				this.bonusVal,
				this.getTotal()
		);
	}
}


