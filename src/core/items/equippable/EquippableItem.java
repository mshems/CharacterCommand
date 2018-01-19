package core.items.equippable;

import core.character.PlayerCharacter;
import core.character.inventory.GearSlot;
import core.constants.EquipConstants;
import core.items.magic.MagicEffect;
import core.items.magic.MagicItem;

public abstract class EquippableItem extends MagicItem {
    protected GearSlot gearSlot = null;

    public EquippableItem(String name, String...tags){
        super(name, tags);
        addTag("equippable");
        equipped = false;
    }

    public int equip(PlayerCharacter pc){
        if(equipped) return EquipConstants.ALREADY_EQUIPPED;
        if(gearSlot!=null && gearSlot!=GearSlot.UNSLOTTED) {
            if (pc.getGearSet().hasGear(gearSlot)) {
                pc.getGearSet().getGear(gearSlot).dequip(pc);
                pc.getGearSet().putGear(gearSlot, this);
            } else {
                pc.getGearSet().putGear(gearSlot, this);
            }
        }
        this.equipped = true;
        doEffects();
        return EquipConstants.SUCCESSFUL_EQUIP;
    }

    public int dequip(PlayerCharacter pc){
        if(!equipped) return EquipConstants.NOT_EQUIPPED;
        if(gearSlot!=null && gearSlot!=GearSlot.UNSLOTTED){
            pc.getGearSet().removeGear(gearSlot);
        }
        this.equipped = false;
        undoEffects();
        return EquipConstants.SUCCESSFUL_DEQUIP;
    }

    public void doEffects(){
        for (MagicEffect m : effects) {
            m.doEffect();
        }
    }

    public void undoEffects(){
        for (MagicEffect m : effects) {
            m.undoEffect();
        }
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public GearSlot getGearSlot() {
        return gearSlot;
    }

    public void setGearSlot(GearSlot gearSlot) {
        this.gearSlot = gearSlot;
        //this.addTag(".GEAR:"+gearSlot.toString());
    }

    @Override
    public String toString(){
        //if(equipped) return "(e) "+itemName;
        //else return itemName;
        return itemName;
    }

    @Override
    public String details(){
        if(effects==null){
            return super.details();
        } else{
          String str = "";
          for(MagicEffect e:effects){
              str += "\n  "+e.toString();
          }
            if(!getItemDescription().isEmpty()) return toString()+str+"\n  "+tagsToString()+"\n  "+getItemDescription();
            else return toString()+str+"\n  "+tagsToString();
        }
    }
}