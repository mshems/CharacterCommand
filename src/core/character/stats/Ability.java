package core.character.stats;

import java.io.Serializable;

public class Ability extends Stat implements Serializable{

    public Ability(String name, double val){
        super(name, val);
        ability = true;
    }

    public double getModifier(){
        return Math.floor((getTotal()-10)/2);
    }

    @Override
    public double getTotal() {
        return baseValue+getBonusValue();
    }

    @Override
    public String toString(){
        return String.format("%s: %.0f (%+.0f)", statName, getTotal(), getModifier());
    }

    public String valueToString(){
        return String.format("%.0f (%+.0f)", getTotal(), getModifier());
    }
}
