package core.character.behaivor;

import core.character.PlayerCharacter;
import core.items.Item;

public class InventoryBehavior extends AbstractBehavior {
    public InventoryBehavior(PlayerCharacter pc) {
        super(pc);
    }

    public void add(Item item, int amt){
        pc.getInventory().addItem(item, amt);
    }

    public boolean drop(String k, int amt){
        return pc.getInventory().dropItem(k, amt);
    }

    public boolean dropAll(String k){
        return pc.getInventory().dropAll(k);
    }
}
