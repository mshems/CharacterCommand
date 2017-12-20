package core.items.magic;

import core.items.Item;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public abstract class MagicItem extends Item {
    protected LinkedList<MagicEffect> effects;

    public MagicItem(String name, String...tags){
        super(name, tags);
    }

    public void addEffect(MagicEffect magicEffect){
        if(effects==null){
            addTag("enchanted");
            effects = new LinkedList<>();
        }
        effects.add(magicEffect);

    }

    public MagicItem addEffects(Collection<MagicEffect> effects){
        if(effects!=null) {
            for (MagicEffect effect : effects) {
                addEffect(effect);
            }
        }
        return this;
    }

    public LinkedList<MagicEffect> getEffects(){return effects;}
}
