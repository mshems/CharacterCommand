package character;

import app.CharacterCommand;

public class CounterStat extends Stat {
	private static final long serialVersionUID = CharacterCommand.VERSION;
	private double currVal;
	
	public CounterStat(){
		super();
		this.currVal = this.getMaxVal();
	}
	public CounterStat(String name, double baseVal){
		super(name, baseVal);
		this.currVal = this.getMaxVal();
	}
	
	public void countUp(){
		if(this.currVal<getMaxVal()){
			this.currVal++;
		}
	}
	public void countDown(){
		if(this.currVal>0){
			this.currVal--;
		}
	}
	public void countUp(double amt){
		if(this.currVal+amt<getMaxVal()){
			this.currVal += amt;
		} else {
			currVal = getMaxVal();
		}
	}
	public void countDown(double amt){
		if(this.currVal-amt>0){
			this.currVal -= amt;
		} else {
			this.currVal = 0;
		}
	}

	public void setCurrVal(double val){
		this.currVal = val;
	}
	public double getCurrVal(){
		return this.currVal;
	}

	public double getMaxVal(){
		return this.getBaseVal();
	}
	public void setMaxVal(double val){
		this.setBaseVal(val);
		if(this.currVal>getMaxVal()){
			this.currVal = getMaxVal();
		}
	}
	
	@Override 
	public String toString(){
		return String.format("%s: %.0f/%.0f", this.getName(), this.getCurrVal(), this.getMaxVal());
	}
	public String detailString() {
		String newLine = System.lineSeparator();
		return String.format("---- %s" +
                    newLine+"Current value: %.0f"+
                    newLine+"Max value:     %.0f"+
                    newLine+"--------------------------------",
				this.getName(),
				this.currVal,
				this.getMaxVal()
		);
	}
}

