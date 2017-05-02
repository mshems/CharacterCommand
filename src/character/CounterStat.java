package character;

public class CounterStat extends Stat {
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
		this.currVal++;
	}
	public void countDown(){
		this.currVal--;
	}
	public void countUp(double amt){
		this.currVal+=amt;
	}
	public void countDown(double amt){
		this.currVal-=amt;
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
	}
	
	@Override 
	public String toString(){
		return String.format("%s: %.0f/%.0f", this.getName(), this.getCurrVal(), this.getMaxVal());
	}
}

