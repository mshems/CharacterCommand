package core.items.equippable.weapon;

import java.util.Collections;
import java.util.LinkedList;

public class WeaponDamage extends LinkedList<DamageRoll>{

    public WeaponDamage(DamageRoll...damageRolls){
        Collections.addAll(this, damageRolls);
    }

    public double rollDamage(){
        double result = 0;
        for(DamageRoll d:this){
            result += d.doRoll();
        }
        return result;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i=0; i<this.size()-1; i++){
            str+=this.get(i).toString()+", ";
        }
        return str + this.get(this.size()-1).toString();
    }
}
