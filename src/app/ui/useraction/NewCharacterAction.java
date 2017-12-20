package app.ui.useraction;

import app.CharacterCommand;
import core.character.PlayerCharacter;
import core.character.stats.Ability;

public class NewCharacterAction {

    public static PlayerCharacter newCharacter(CharacterCommand cc){
        PlayerCharacter pc = new PlayerCharacter(cc.terminal.queryString("Name: "));
        pc.getAbilities().STR().setBaseValue(cc.terminal.queryInteger("STR: "));
        pc.getAbilities().DEX().setBaseValue(cc.terminal.queryInteger("DEX: "));
        pc.getAbilities().CON().setBaseValue(cc.terminal.queryInteger("CON: "));
        pc.getAbilities().WIS().setBaseValue(cc.terminal.queryInteger("WIS: "));
        pc.getAbilities().INT().setBaseValue(cc.terminal.queryInteger("INT: "));
        pc.getAbilities().CHA().setBaseValue(cc.terminal.queryInteger("CHA: "));
        if(cc.terminal.queryYN("Spellcaster? [Y/N] : ")){
            Ability a;
            do {
                a = pc.getAbilities().get(cc.terminal.queryString("Spellcasting Ability: "));
                if(a==null)cc.terminal.out.println("No ability by that name");
            } while(a==null);
            pc.makeCaster(a);
        }
        cc.loadCharacter(pc);
        return pc;
    }
}
