package core.character.inventory;

import core.items.Item;

public class ItemStack{
    private Item item;
    private int count;

    public ItemStack(Item item, int count){
        this.item = item;
        this.count = count;
    }

    public ItemStack(Item item){
        this.item = item;
        this.count = 1;
    }

    public Item getItem(){
        return item;
    }

    public void setItem(Item item){
        this.item = item;
    }

    public int getCount(){
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }

    public void incrementCount(int n){
        this.count += n;
    }

    public void decrementCount(int n){
        this.count -= n;
    }

    public String toString(){
        return String.format("%dx %s", count, item.getItemName());
    }
}
