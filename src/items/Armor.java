package items;

import app.CharacterCommand;
import character.Ability;
import character.Stat;
import character.Attribute;
import character.PlayerCharacter;

public class Armor extends Equippable{
    private static final long serialVersionUID = CharacterCommand.VERSION;
	private Integer AC;
    public ArmorType armorType;

	public enum ArmorType {L_ARMOR, M_ARMOR, H_ARMOR, SHIELD, OTHER};

	public Armor(String name){
		super(name);
	}
	
	public Armor(String name, int count){
		super(name, count);
	}

	public Armor setArmorType(ArmorType type){
	    this.armorType = type;
	    return this;
    }
    public Armor setAC(int ac){
	    this.AC = ac;
        return this;
    }

	@Override
    public void equip(PlayerCharacter c){
        if(getEffects()!=null){
            for (ItemEffect e : this.getEffects()){
                if (e != null){
                    e.getTarget().addBonusVal(e.getBonus());
                }
            }
        }
        double dexMod = c.getAbilities().get(Ability.DEX).getMod();
        Stat playerAC = c.getAttributes().get(Attribute.AC);
        if((AC!=null && armorType != null) || armorType == ArmorType.SHIELD){
            switch (armorType){
                case L_ARMOR:
                    if(!c.isArmored()){
                        playerAC.setBaseVal(AC);
                        playerAC.addBonusVal(dexMod);
                        c.setArmored(true);
                        this.setEquipped(true);
                        System.out.println(this.getName()+" equipped");
                    } else {
                        System.out.println("ERROR: Already wearing armor");
                    }
                    break;
                case M_ARMOR:
                    if(!c.isArmored()){
                        playerAC.setBaseVal(AC);
                        if (dexMod < 2){
                            playerAC.addBonusVal(dexMod);
                        } else {
                            playerAC.addBonusVal(2);
                        }
                        c.setArmored(true);
                        this.setEquipped(true);
                        System.out.println(this.getName()+" equipped");
                    } else {
                        System.out.println("ERROR: Already wearing armor");
                    }
                    break;
                case H_ARMOR:
                    if(!c.isArmored()){
                        playerAC.setBaseVal(AC);
                        c.setArmored(true);
                        this.setEquipped(true);
                        System.out.println(this.getName()+" equipped");
                    } else {
                        System.out.println("ERROR: Already wearing armor");
                    }
                    break;
                case SHIELD:
                    playerAC.addBonusVal(2);
                    this.setEquipped(true);
                    System.out.println(this.getName()+" equipped");
                    break;
                case OTHER:
                    playerAC.addBonusVal(AC);
                    this.setEquipped(true);
                    System.out.println(this.getName()+" equipped");
                    break;
            }
        }
    }

    @Override
    public void dequip(PlayerCharacter c){
        this.setEquipped(false);
        double dexMod = c.getAbilities().get(Ability.DEX).getMod();
        Stat playerAC = c.getAttributes().get(Attribute.AC);
        if((AC!=null && armorType != null) || armorType == ArmorType.SHIELD){
            switch (armorType){
                case L_ARMOR:
                    playerAC.setBaseVal(c.getAttributes().get(Attribute.NAC).getBaseVal());
                    playerAC.decBonusVal(dexMod);
                    c.setArmored(false);
                    System.out.println(this.getName()+" dequipped");
                    break;
                case M_ARMOR:
                    playerAC.setBaseVal(c.getAttributes().get(Attribute.NAC).getBaseVal());
                    if (dexMod < 2){
                        playerAC.decBonusVal(dexMod);
                    } else {
                        playerAC.decBonusVal(2);
                    }
                    c.setArmored(false);
                    System.out.println(this.getName()+" dequipped");
                    break;
                case H_ARMOR:
                    playerAC.setBaseVal(c.getAttributes().get(Attribute.NAC).getBaseVal());
                    c.setArmored(false);
                    System.out.println(this.getName()+" dequipped");
                    break;
                case SHIELD:
                    playerAC.decBonusVal(2);
                    System.out.println(this.getName()+" dequipped");
                    break;
                case OTHER:
                    playerAC.decBonusVal(AC);
                    System.out.println(this.getName()+" dequipped");
                    break;
            }
            if(getEffects()!=null){
                for (ItemEffect e : this.getEffects()){
                    if (e != null){
                        e.getTarget().decBonusVal(e.getBonus());
                    }
                }
            }
        }
    }

    @Override
    public String toString(){
        String s = super.toString();
        if((AC!=null && armorType != null) || armorType == ArmorType.SHIELD){
            switch (armorType){
                case L_ARMOR:
                    s += " [Light]";
                    break;
                case M_ARMOR:
                    s += " [Medium]";
                    break;
                case H_ARMOR:
                    s += " [Heavy]";
                    break;
                case SHIELD:
                    s += " [Shield]";
                    break;
                case OTHER:
                    s += " [Other]";
                    break;
            }
        }
        return s;
    }

    public static ArmorType parseType(String s){
        switch(s){
            case"l":
            case "light":
                return ArmorType.L_ARMOR;
            case"m":
            case "medium":
                 return ArmorType.M_ARMOR;
            case "h":
            case "heavy":
                return ArmorType.H_ARMOR;
            case "s":
            case "shield":
                return ArmorType.SHIELD;
            case "o":
            case "other":
                return ArmorType.OTHER;
            default:
                return null;
        }
    }
}
