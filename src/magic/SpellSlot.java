package magic;

import character.CounterStat;

public class SpellSlot extends CounterStat{
	private int level;
	
	public SpellSlot(int level, int maxVal){
		this.setMaxVal(maxVal);
		this.setCurrVal(maxVal);
		this.level=level;
	}
	
	public void charge(){
		this.countUp();
	}
	
	public void discharge(){
		this.countDown();
	}
	
	@Override
	public String toString(){
		return String.format("Level %d: %.0f/%.0f", this.level, this.getCurrVal(), this.getBaseVal());
	}
}
