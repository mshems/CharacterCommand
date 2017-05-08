package items;

import character.PlayerCharacter;

import java.util.ArrayList;

public class Equippable extends Item {
	private boolean equipped;
	private ArrayList<ItemEffect> effects;

	public Equippable(String name) {
		super(name);
		this.setEquipped(false);
		this.setEquippable(true);
	}
	
	public Equippable(String name, int count) {
		super(name, count);
		this.setEquipped(false);
		this.setEquippable(true);
	}

	@Override
	public void equip(PlayerCharacter c){
		this.setEquipped(true);
		if(effects!=null){
            for (ItemEffect e : effects){
                if (e != null){
                    e.getTarget().addBonusVal(e.getBonus());
                }
            }
        }
	}

	@Override
	public void dequip(PlayerCharacter c){
		this.setEquipped(false);
		if(effects!=null){
            for (ItemEffect e : effects){
                if (e != null){
                    e.getTarget().addBonusVal(-e.getBonus());
                }
            }
        }
	}
	@Override
	public boolean isEquipped() {
		return equipped;
	}
	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}

	public ArrayList<ItemEffect> getEffects() {
		return effects;
	}
	public void setEffects(ArrayList<ItemEffect> effects) {
		this.effects = effects;
	}
	public void addEffect(ItemEffect effect) {
		if(this.effects==null){
		    this.effects = new ArrayList<>();
        }
	    this.effects.add(effect);
	}
	
	/*@Override
	public String toString(){
		String s = String.format("%dx %s", this.getCount(), this.getName());
		return s;
	}*/
}
