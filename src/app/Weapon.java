package app;

public class Weapon extends Item {
	protected DiceRoll damageRoll;
	protected int attackAbility;
	protected int[] attributeTargets;
	protected int[] attributeBonuses;
	
	public Weapon(){
		super();
		this.equippable = true;
	}
	
	public Weapon(String n, DiceRoll r){
		super(n);
		this.damageRoll = r;
		this.equippable = true;
	}
	
	public Weapon(String n){
		super(n);
		this.equippable = true;
	}
	
	@Override
	public void equip(Character c){
		this.equipped = true;
		if (this.attributeBonuses != null && this.attributeTargets != null){
			int i=0;
			for (int n : this.attributeTargets){
				c.abilityScores[n].setValue(+this.attributeBonuses[i]);	
				i++;
			}
		}
	}
	
	@Override
	public void dequip(Character c){
		this.equipped = false;
		if (this.attributeBonuses != null && this.attributeTargets != null){
			int i=0;
			for (int n : this.attributeTargets){
				c.abilityScores[n].setValue(-this.attributeBonuses[i]);	
				i++;
			}
		}
	}
	public Weapon(String n, DiceRoll r, int[] aT, int[] aB) {
		super(n);
		this.damageRoll = r;
		this.attributeTargets = aT;
		this.attributeBonuses = aB;
		this.equippable = true;
	}
	
	public String toString(){
		String s = String.format("%dx %s", this.getItemCount(), this.getItemName());
		if (this.damageRoll != null){
			//s += String.format(" (%s)", this.damageRoll);
		}
		
		if (this.equipped==true){s= "e "+s;}else{s="- "+s;}
		return s;
	}
}
