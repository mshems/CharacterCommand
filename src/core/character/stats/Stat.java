package core.character.stats;

import java.io.Serializable;

public class Stat extends AbstractStat implements Serializable{
    private double bonusValue;
    protected boolean counter = false;
    protected boolean ability = false;

    public Stat(String name, double val){
        statName = name;
        baseValue = val;
        bonusValue = 0;
    }

    public void incrementBaseValue(double d){
        baseValue+=d;
    }

    public void decrementBaseValue(double d){
        baseValue-=d;
    }

    public void incrementBonusValue(double d){
        bonusValue+=d;
    }

    public void decrementBonusValue(double d){
        bonusValue-=d;
    }

    public double getBonusValue(){
        return bonusValue;
    }

    public void setBonusValue(double bonusValue){
        this.bonusValue = bonusValue;
    }

    @Override
    public double getTotal(){
        return baseValue+bonusValue;
    }

    public boolean isCounter() {
        return counter;
    }

    public boolean isAbility() {
        return ability;
    }

    @Override
    public String toString() {
        return String.format("%s: %.0f", statName, getTotal());
//        return String.format("%s: %.0f (%.0f+%.0f)", statName, getTotal(), baseValue, bonusValue);
    }
}
