package core.magic;

import core.character.stats.CounterStat;

public class SpellSlot extends CounterStat{
    private int level;

    public SpellSlot(int lvl, double val) {
        super(".SPELLSLOT:"+lvl, val);
        this.level = lvl;
    }

    public void recharge(){
        this.setCurrValue(this.getMaxValue());
    }

    @Override
    public String toString() {
        return String.format("lvl: %d %.0f/%.0f", level, getCurrValue(), getMaxValue());
    }
}
