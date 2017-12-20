package core.character.stats;

public class Ability extends Stat{

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
}
