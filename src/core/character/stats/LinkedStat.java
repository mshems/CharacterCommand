package core.character.stats;

import java.io.Serializable;

public class LinkedStat extends Stat implements Serializable{
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
