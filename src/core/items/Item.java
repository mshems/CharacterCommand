package core.items;

import core.character.PlayerCharacter;

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
    public void equip(PlayerCharacter pc) {}

    @Override
    public void dequip(PlayerCharacter pc) {}

    @Override
    public void doEffects() {}

    @Override
    public void undoEffects() {}

    @Override
    public String toString(){
        return itemName;
    }

    public String details(){
        if(!getItemDescription().isEmpty()) return toString()+"\n  "+tagsToString()+"\n  "+getItemDescription();
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
