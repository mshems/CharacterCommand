package core.items.magic;

import core.character.stats.Stat;

import java.io.Serializable;

public class MagicEffect implements Serializable{
    String effectName;
    private Stat stat;
    private double bonus;
    private boolean applied;

    public MagicEffect(Stat stat, double bonus){
        this.stat = stat;
        this.bonus = bonus;
        this.applied = false;
    }

    public MagicEffect(Stat stat, double bonus, String name){
        this.effectName = name;
        this.stat = stat;
        this.bonus = bonus;
        this.applied = false;
    }

    public void doEffect(){
        if(!applied){
            applied = true;
            stat.incrementBonusValue(bonus);
        }
    }

    public void undoEffect(){
        if(applied){
            applied = false;
                stat.incrementBonusValue(-bonus);
        }
    }

    @Override
    public String toString() {
        String str = String.format("%+.0f %s", bonus, stat.getStatName());
        if(effectName != null) return effectName+": "+str;
        return str;
    }
}
