package core.items.equippable.weapon;

import core.actions.DiceRoll;

public class DamageRoll extends DiceRoll{
    private String damageType;

    public DamageRoll(double num, double sides, double mod) {
        super(num,sides,mod);
    }

    public DamageRoll(double num, double sides, double mod, String damageType){
        this(num, sides, mod);
        this.damageType = damageType;
    }

    public DamageRoll(double num, double sides) {
        super(num,sides);
    }

    public DamageRoll(double num, double sides, String damageType){
        this(num, sides);
        this.damageType = damageType;
    }

    public double doRoll(){
        return super.doRoll();
    }

    public String getDamageType() {
        return damageType;
    }

    public void setDamageType(String damageType) {
        this.damageType = damageType;
    }

    @Override
    public String toString() {
        String str = super.toString();
        if(damageType!=null) str += " "+damageType;
        return str;
    }
}
