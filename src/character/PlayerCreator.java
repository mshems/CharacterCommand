package character;

import app.CharacterCommand;
import character.Ability;
import character.PlayerCharacter;

public class PlayerCreator {
    public static void createCharacter() {
        String name;
        while (true) {
            name = CharacterCommand.terminal.queryString("Character name: ", false);
            if (CharacterCommand.isValidName(name)) {
                break;
            }
        }
        String raceName = CharacterCommand.terminal.queryString("Race: ", false);
        String className = CharacterCommand.terminal.queryString("Class: ", false);
        PlayerCharacter c = new PlayerCharacter(name, raceName, className);
        for (String key : c.getAbilities().keySet()) {
            Ability a = c.getAbilities().get(key);
            a.setBaseVal(CharacterCommand.terminal.queryInteger("Enter "+a.getName() + " score: ", false));
        }
        if (CharacterCommand.terminal.queryYN("Spellcaster? [Y/N] : ")) {
            Ability spellAbility = null;
            while (spellAbility == null) {
                String abilityName = CharacterCommand.terminal.queryString("Spellcasting ability: ", false).toLowerCase();
                spellAbility = c.getAbilities().get(abilityName);
                if (spellAbility == null) {
                    CharacterCommand.terminal.println("ERROR: Ability not found");
                } else {
                    c.setSpellcaster(true);
                    c.initMagicStats(spellAbility);
                }
            }
        } else {
            c.setSpellcaster(false);
        }
        c.updateStats();
        CharacterCommand.characterList.put(c.getName().toLowerCase(), c);
        CharacterCommand.terminal.println("Created "+c.getName());
        CharacterCommand.setActiveChar(c);
    }
}
