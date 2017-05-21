package magic;

import app.App;
import character.CounterStat;

public class SpellSlot extends CounterStat{
	private static final long serialVersionUID = App.version;
	private int level;
	
	public SpellSlot(int level, int maxVal){
		this.setMaxVal(maxVal);
		this.setCurrVal(maxVal);
		this.level=level;
	}
	
	public void charge(){
		this.countUp();
	}
	public void charge(int amt){
		this.countUp(amt);
	}
	public void fullCharge(){
		this.setCurrVal(getMaxVal());
	}

	public void discharge(){
		this.countDown();
	}
	public void discharge(int amt){
		this.countDown(amt);
	}
	public void fullDischarge(){
		this.setCurrVal(0);
	}

	public int getLevel(){
		return level;
	}
	@Override
	public String toString(){
		return String.format("Level %d: %.0f/%.0f", this.level, this.getCurrVal(), this.getBaseVal());
	}
}
