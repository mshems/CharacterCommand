package app;

public class Armor extends Item{
	protected int[] attributeTargets;
	protected int[] attributeBonuses;
	protected int acBase = 0;
	protected int armorWeight = 0;
	
	protected static final int none = 0 ;
	protected static final int light = 1;
	protected static final int medium = 2;
	protected static final int heavy = 3;
	protected static final int shield = 4;
	protected static final int bonus = 5;

	public Armor(){
		super();
		this.equippable = true;
	}
	
	public Armor(String n) {
		super(n);
		this.equippable = true;
	}
	
	public Armor(String n, int[] aT, int[] aB, int armorWeight, int acBase) {
		super(n);
		this.attributeTargets = aT;
		this.attributeBonuses = aB;
		this.armorWeight = armorWeight;
		this.acBase = acBase;
		this.equippable = true;
	}
	
	@Override
	public void equip(Character c){
		this.equipped = true;
		if (this.attributeBonuses != null && this.attributeTargets != null){
			int i=0;
			for (int n : this.attributeTargets){
				c.abilityScores[n].setValue(c.abilityScores[n].getValue()+this.attributeBonuses[i]);	
				i++;
			}
		}
		if (this.acBase != 0 && this.armorWeight != Armor.none){
			int ac=this.acBase;
			int dexMod = (int) c.abilityScores[Attribute.DEX].getMod();
			if (this.armorWeight == Armor.light){
				ac += dexMod;
			} else if (this.armorWeight == Armor.medium){
				if (dexMod < 2){
				ac += dexMod;
				} else {
					ac +=2;
				}
			} else if (this.armorWeight == Armor.bonus){
				ac = (int) c.playerStats[Attribute.AC].getValue() + this.acBase;
			} else if (this.armorWeight == Armor.shield){
				ac = (int) c.playerStats[Attribute.AC].getValue() + 2;	
			}
			c.playerStats[Attribute.AC].setValue(ac);
		}
	}
	
	@Override
	public void dequip(Character c){
		this.equipped = false;
		if (this.attributeBonuses != null && this.attributeTargets != null){
			int i=0;
			for (int n : this.attributeTargets){
				c.abilityScores[n].setValue(c.abilityScores[n].getValue()-this.attributeBonuses[i]);	
				i++;
			}
		}
		int dexMod = (int) c.abilityScores[Attribute.DEX].getMod();
		c.playerStats[Attribute.AC].setValue(10+dexMod);
	}
	
	public String toString(){
		String s = String.format("%dx %s",  this.getItemCount(), this.getItemName());
		if (this.equipped==true){
			s= "e "+s;
		} else {
			s="- "+s;
		}
		return s;
	}

}
