package core.character.inventory;

import core.items.Item;

import java.util.LinkedHashMap;

public class Inventory{
    private LinkedHashMap<String, ItemStack> contents;

    public Inventory(){
        this.contents = new LinkedHashMap<>();
    }

    public Item getItem(String key){
        if(this.contents.get(key.toLowerCase())!=null) {
            return this.contents.get(key.toLowerCase()).getItem();
        } else return null;
    }

    public ItemStack getItemStack(String key){
        return this.contents.get(key.toLowerCase());
    }

    public int getItemCount(String key){
        return this.contents.get(key.toLowerCase()).getCount();
    }

    public void addItem(Item item, int count){
        String key = item.getItemName().toLowerCase();
        if(!this.contents.containsKey(key)){
            this.contents.put(key, new ItemStack(item, count));
        } else {
            this.contents.get(key).incrementCount(count);
        }
    }

    public boolean dropAll(String k){
        String key = k.toLowerCase();
        if(this.contents.containsKey(key)){
            this.contents.remove(key, this.contents.get(key));
            return true;
        } else return false;
    }

    public boolean dropAll(Item item){
        String key = item.getItemName().toLowerCase();
        if(this.contents.containsKey(key)){
            this.contents.remove(key, this.contents.get(key));
            return true;
        } else return false;
    }

    public boolean dropItem(String k, int count){
        String key = k.toLowerCase();
        if(this.contents.containsKey(key)){
            this.contents.get(key).decrementCount(count);
            return true;
        } else return false;
    }

    public boolean dropItem(Item item, int count){
        String key = item.getItemName().toLowerCase();
        if(this.contents.containsKey(key)){
            this.contents.get(key).decrementCount(count);
            return true;
        } else return false;
    }

    public Inventory filterByTag(String tag){
        Inventory inv = new Inventory();
        for(ItemStack itemStack:contents.values()){
            if(itemStack.getItem().hasTag(tag)){
                inv.addItem(itemStack.getItem(), itemStack.getCount());
            }
        }
        return inv;
    }

    public boolean contains(Item item){
        return contents.get(item.getItemName().toLowerCase())!=null;
    }

    public boolean contains(String key){
        return contents.get(key.toLowerCase())!=null;
    }

    public boolean isEmpty(){
        return contents.isEmpty();
    }

    public void removeItem(Item item){
        this.contents.remove(item.getItemName().toLowerCase(), item);
    }

    public LinkedHashMap<String, ItemStack> getContents(){
        return contents;
    }

    @Override
    public String toString() {
        String str = "Inventory:";
        for(ItemStack itemStack:contents.values()){
            str+=String.format("\n  %dx %s", itemStack.getCount(), itemStack.getItem().toString());
        }
        return str;
    }
}
