package core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class TaggedObject implements Serializable{
    private HashSet<String> tags;

    public TaggedObject(String...t){
        this.tags = new HashSet<>();
        if(t!=null) Collections.addAll(tags, t);
    }

    public boolean hasTag(String tag){
        return tags.contains(tag);
    }

    public void addTag(String...t) {
        for(String tag:t){
            if(!tags.contains(tag)){
                tags.add(tag);
            }
        }
    }

    public void addTags(Collection<String> t) {
        if(t!=null) {
            for (String tag : t) {
                addTag(tag);
            }
        }
    }

    public void removeTag(String tag){
        tags.remove(tag);
    }

    public String tagsToString(){
        String str = "";
        if(tags.isEmpty()) return str + "Tags: (none)";
        for(String tag:tags){
            if(!tag.startsWith(".")){
                if(str.isEmpty()){
                    str = "Tags: "+tag;
                } else {
                    str+=", "+tag;
                }
            }
        }
        return str;
    }
}
