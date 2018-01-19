package app.ui.useraction;

import app.CharacterCommand;
import app.ui.CCExtensions;
import core.constants.SpellConstants;
import jterminal.core.IllegalTokenException;
import core.magic.*;

public class SpellAction {
    public static void cast(CharacterCommand cc){
        if(!cc.getActiveCharacter().isSpellcaster()){
            cc.terminal.out.println(cc.getActiveCharacter().getName()+" is not a spellcaster!");
            return;
        }
        Spell spell = null;
        Integer castLevel = null;
        boolean requirePrep = false;
        if(!cc.terminal.hasTokens()){
            while(true){
                String spellName = cc.terminal.queryString("Spell name: ");
                if(spellName.equalsIgnoreCase("cancel")) return;
                spell = cc.getActiveCharacter().getSpellBook().get(spellName);
                if(spell != null) break;
                else cc.terminal.out.println("No spell by that name");
            }
            if(cc.terminal.queryYN("Cast at a higher level? [Y/N]: ")) {
                castLevel = cc.terminal.queryInteger("Level to cast at: ");
            } else {
                castLevel = spell.getSpellLevel();
            }
        } else {
            while (cc.terminal.hasTokens()) {
                try {
                    if (cc.terminal.peekToken().startsWith("\"")) {
                        String spellName = CCExtensions.buildNameFromTokens(cc.terminal);
                        spell = cc.getActiveCharacter().getSpellBook().get(spellName);
                    } else switch (cc.terminal.nextToken()) {
                        case "-l":
                        case "--level":
                            castLevel = cc.terminal.nextIntToken();
                            break;
                        case "-rp":
                        case "--require-prep":
                            requirePrep = true;
                            break;
                        case "--help":
                            cc.terminal.out.println("HELP MENU WIP");
                            break;
                        default:
                            cc.terminal.nextToken();
                            break;
                    }
                } catch(IllegalTokenException e){
                    cc.terminal.out.println("ERROR: Illegal token: \"" + e.getToken() + "\"\n  Enter \"cast --help\" for help");
                    return;
                }
            }
            if(spell == null){
                cc.terminal.out.println("No spell by that name");
                return;
            }
            if(castLevel == null){
                castLevel = spell.getSpellLevel();
            }
        }
        switch(cc.getActiveCharacter().magicBehavior.cast(cc.getActiveCharacter(), spell, castLevel, requirePrep)){
            case SpellConstants.SUCCESSFUL_CAST:
                cc.terminal.out.println("\"" + spell.getSpellName() + "\" cast at level " + castLevel);
                break;
            case SpellConstants.NO_SLOTS:
                cc.terminal.out.println("No level "+castLevel+" spell slots available");
                break;
            case SpellConstants.NOT_PREPARED:
                cc.terminal.out.println("\"" + spell.getSpellName() + "\" not prepared");
                break;
            case SpellConstants.LVL_TOO_LOW:
                cc.terminal.out.println("\"" + spell.getSpellName() + "\" cannot be cast at level " + castLevel);
                break;
            default:
                break;
        }
    }
}
