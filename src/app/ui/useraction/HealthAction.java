package app.ui.useraction;

import app.CharacterCommand;
import app.ui.terminal.core.IllegalTokenException;

public class HealthAction {

    public static void heal(CharacterCommand cc){
        int amt=0;
        if(!cc.terminal.hasTokens()){
           amt = cc.terminal.queryInteger("HP gained: ");
        } else {
            while(cc.terminal.hasTokens()) {
                switch(cc.terminal.nextToken()){
                    case "-hp":
                    case "--hp":
                        try {
                            amt = cc.terminal.nextIntToken();
                        } catch (IllegalTokenException e) {
                            return;
                        }
                        break;
                    case "--help":
                        //TODO: helpmenu
                        break;
                    default:
                        return;
                }
            }
        }
        cc.getActiveCharacter().healthBehavior.heal(amt);
        cc.terminal.out.println("Gained " + amt + " hp");
    }

    public static void hurt(CharacterCommand cc){
        int amt=0;
        if(!cc.terminal.hasTokens()){
             amt = cc.terminal.queryInteger("HP lost: ");
        } else {
            while(cc.terminal.hasTokens()) {
                switch(cc.terminal.nextToken()){
                    case "-hp":
                    case "--hp":
                        try {
                            amt = cc.terminal.nextIntToken();
                        } catch (IllegalTokenException e) {
                            return;
                        }
                        break;
                    case "--help":
                        //TODO: helpmenu
                        break;
                    default:
                        return;
                }
            }
        }
        cc.getActiveCharacter().healthBehavior.hurt(amt);
        cc.terminal.out.println("Lost " + amt + " hp");
    }
}
