package items;

import character.Ability;
import character.Stat;
import character.Attribute;
import character.PlayerCharacter;

public class Armor extends Equippable{

	private Integer AC;
    public ArmorType armorType;

	public enum ArmorType {L_ARMOR, M_ARMOR, H_ARMOR, SHIELD, OTHER};

	public Armor(String name){
		super(name);
	}
	
	public Armor(String name, int count){
		super(name, count);
	}

	public void setArmorType(ArmorType type){
	    this.armorType = type;
    }
    public void setAC(int ac){
	    this.AC = ac;
    }

	@Override
    public void equip(PlayerCharacter c){
        this.setEquipped(true);
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
                    playerAC.setBaseVal(AC);
                    playerAC.addBonusVal(dexMod);
                    break;
                case M_ARMOR:
                    playerAC.setBaseVal(AC);
                    if (dexMod < 2){
                        playerAC.addBonusVal(dexMod);
                    } else {
                        playerAC.addBonusVal(2);
                    }
                    break;
                case H_ARMOR:
                    playerAC.setBaseVal(AC);
                    break;
                case SHIELD:
                    playerAC.addBonusVal(2);
                    break;
                case OTHER:
                    playerAC.addBonusVal(AC);
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
                    break;
                case M_ARMOR:
                    playerAC.setBaseVal(c.getAttributes().get(Attribute.NAC).getBaseVal());
                    if (dexMod < 2){
                        playerAC.decBonusVal(dexMod);
                    } else {
                        playerAC.decBonusVal(2);
                    }
                    break;
                case H_ARMOR:
                    playerAC.setBaseVal(c.getAttributes().get(Attribute.NAC).getBaseVal());
                    break;
                case SHIELD:
                    playerAC.decBonusVal(2);
                    break;
                case OTHER:
                    playerAC.decBonusVal(AC);
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
                    s += " (L)";
                    break;
                case M_ARMOR:
                    s += " (M)";
                    break;
                case H_ARMOR:
                    s += " (H)";
                    break;
                case SHIELD:
                    s += " (S)";
                    break;
                case OTHER:
                    s += " (U)";
                    break;
            }
        }
        return s;
    }
}
