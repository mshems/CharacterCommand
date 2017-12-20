package core.character.stats;

public class LinkedStat extends Stat{
    private StatLink statLink;

    public LinkedStat(String name, StatLink s){
        super(name, s.getValue());
        statLink = s;
    }

    @Override
    public double getBaseValue(){
        return statLink.getValue();
    }


    @Override
    public double getTotal() {
        return statLink.getValue()+getBonusValue();
    }

    @Override
    public String toString() {
        return String.format("%s: %.0f", statName, getTotal());
    }
}
