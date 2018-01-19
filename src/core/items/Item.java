package core.items;

import core.character.PlayerCharacter;
import core.constants.EquipConstants;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Item extends AbstractItem{

    public Item(String name, String...t){
        super(name, t);
        this.itemName = name;
    }

    public void autoTag(List<String> tagSet, String...matchTags){
        boolean match = false;
        for(String tag:tagSet){
            if(itemName.toLowerCase().contains(tag)){
                addTag(tag);
                match = true;
            }
        }
        if(match) {
            for(String tag:matchTags){
                addTag(tag);
            }
        }
    }

    @Override
    public int equip(PlayerCharacter pc) {return EquipConstants.NOT_EQUIPPABLE;}

    @Override
    public int dequip(PlayerCharacter pc) {return EquipConstants.NOT_EQUIPPABLE;}

    @Override
    public void doEffects() {}

    @Override
    public void undoEffects() {}

    @Override
    public String toString(){
        return itemName;
    }

    public String details(){
        if(!getItemDescription().isEmpty()) return toString()+"\n  "+getItemDescription()+"\n  "+tagsToString();
        else return toString()+"\n  "+tagsToString();
    }

    /*public String tagsToString(){
        Collections.sort(tags);
        String str = "Tags: ";
        if(tags.isEmpty()) return str + "none";
        for(int i=0; i<tags.size()-1; i++){
            if(!tags.get(i).startsWith(".")) {
                str += tags.get(i) + ", ";
            }
        }
        return str + tags.get(tags.size()-1);
    }*/
}
