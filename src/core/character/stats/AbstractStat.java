package core.character.stats;

import java.io.Serializable;

public abstract class AbstractStat implements Serializable{
    String statName;
    double baseValue;

    public String getStatName(){
        return statName;
    }

    public void setStatName(String statName){
        this.statName = statName;
    }

    public double getBaseValue(){
        return baseValue;
    }

    public void setBaseValue(double baseValue){
        this.baseValue = baseValue;
    }

    public abstract double getTotal();
}