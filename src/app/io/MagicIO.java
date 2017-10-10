package app.io;

import app.CharacterCommand;
import app.core.character.Ability;
import app.core.character.PlayerCharacter;

public class MagicIO {
    public static void learnMagic(PlayerCharacter pc) {
        Ability spellAbility = null;
        while (spellAbility == null) {
            String abilityName = CharacterCommand.terminal.queryString("Spellcasting ability: ",false);
            if (abilityName.equalsIgnoreCase("cancel")) {
                break;
            } else {
                spellAbility = pc.getAbilities().get(abilityName);
                if (spellAbility == null) {
                    CharacterCommand.terminal.println("ERROR: Ability not found");
                } else {
                    pc.setSpellcaster(true);
                    pc.initMagicStats(spellAbility);
                }
            }
        }
    }
}
