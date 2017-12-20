package core.items.equippable;

public class Equipment extends EquippableItem {

    public Equipment(String name, String...tags){
        super(name, tags);
        addTag("equippable");
        addTag(".TYPE:EQUIP");
    }
}
