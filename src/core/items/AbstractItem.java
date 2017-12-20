package core.items;

import core.TaggedObject;
import core.character.PlayerCharacter;

import java.util.LinkedList;

public abstract class AbstractItem extends TaggedObject{
    protected String itemName;
    protected String itemDescription = "";
    protected double itemValue = 0;
    protected LinkedList<String> tags;
    protected boolean equipped;

    public AbstractItem(){}

    public AbstractItem(String itemName, String...tags){
        super(tags);
        this.itemName = itemName;
    }

    public abstract void equip(PlayerCharacter pc);

    public abstract void dequip(PlayerCharacter pc);

    public abstract void doEffects();

    public abstract void undoEffects();

    public void use(PlayerCharacter pc){};

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public String getItemName(){
        return itemName;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public String getItemDescription(){
        return itemDescription;
    }

    public void setItemDescription(String itemDescription){
        this.itemDescription = itemDescription;
    }

    public double getItemValue(){
        return itemValue;
    }

    public void setItemValue(double itemValue){
        this.itemValue = itemValue;
    }


    public abstract String toString();
}
