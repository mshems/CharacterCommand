package core.actions;

import java.io.Serializable;
import java.util.Random;

public class DiceRoll implements Serializable{
    private double sides;
    private double num;
    private double mod;

    public DiceRoll(){}

    public DiceRoll(double num, double sides){
        this.sides=sides;
        this.num=num;
        this.mod=0;
    }

    public DiceRoll(double num, double sides, double mod){
        this(num, sides);
        this.mod=mod;
    }

    public double doRoll(){
        return doRoll(num, sides, mod);
    }


    public static double doRoll(double num, double sides, double mod){
        if(sides <= 0)return 0+mod;
        Random random = new Random();
        int result = 0;
        for(int i=0; i<num; i++) {
            result += 1+(sides)*random.nextDouble();
        }
        return result+mod;
    }

    @Override
    public String toString() {
        String str = String.format("%.0fd%.0f", num, sides);
        if(mod != 0) str += String.format("%+.0f", mod);
        return str;
    }
}
