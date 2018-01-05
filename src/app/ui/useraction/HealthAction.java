package app.ui.useraction;

import app.CharacterCommand;
import jterminal.core.IllegalTokenException;

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
                            if (e.getToken() == null){
                                cc.terminal.out.println("No amount specified");
                            } else {
                                cc.terminal.out.println("Invalid token \""+e.getToken()+"\"");
                            }
                            return;
                        }
                        break;
                    case "--help":
                        //TODO: helpmenu
                        break;
                    default:
                        cc.terminal.nextToken();
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
                            if(e.getToken()==null){
                                cc.terminal.out.println("No amount specified");
                            } else {
                                cc.terminal.out.println("Invalid token \""+e.getToken()+"\"");
                            }
                            return;
                        }
                        break;
                    case "--help":
                        //TODO: helpmenu
                        break;
                    default:
                        cc.terminal.nextToken();
                }
            }
        }
        cc.getActiveCharacter().healthBehavior.hurt(amt);
        cc.terminal.out.println("Lost " + amt + " hp");
    }
}
