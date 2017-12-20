package core.character.stats;

public class CounterStat extends Stat{
    private double currValue;


    public CounterStat(String name, double val){
        super(name, val);
        counter = true;
        currValue = getTotal();
    }

    public void incrementCurrValue(double d){
        this.currValue+=d;
    }

    public void decrementCurrValue(double d){
        this.currValue-=d;
    }

    public double getCurrValue(){
        return currValue;
    }

    public void setCurrValue(double currValue) {
        this.currValue = currValue;
    }

    public double getMaxValue(){
        return this.getTotal();
    }

    @Override
    public String toString() {
        return String.format("%s: %.0f/%.0f", statName, currValue, getTotal());
    }
}
