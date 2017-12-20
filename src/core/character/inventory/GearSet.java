package core.character.inventory;

import core.items.Item;

import java.util.HashMap;

public class GearSet {
    private HashMap<GearSlot, Item> equippedGear;

    public GearSet(){
       equippedGear = new HashMap<>();
    }

    public void putGear(GearSlot gearSlot, Item item){
        equippedGear.put(gearSlot, item);
    }

    public void removeGear(GearSlot gearSlot){
        equippedGear.remove(gearSlot);
    }

    public boolean hasGear(GearSlot gearSlot){
        return equippedGear.get(gearSlot) != null;
    }

    public Item getGear(GearSlot gearSlot){
        return equippedGear.get(gearSlot);
    }
}
