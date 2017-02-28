package app;

public class Armor extends Item{
	protected int[] attributeTargets;
	protected int[] attributeBonuses;
	protected int acBase = 0;
	protected int armorWeight = 0;
	
	public static final int none = 0 ;
	public static final int light = 1;
	public static final int medium = 2;
	public static final int heavy = 3;
	public static final int shield = 4;
	public static final int bonus = 5;

	public Armor(){
		super();
		this.equippable = true;
	}
	
	public Armor(String n) {
		super(n);
		this.equippable = true;
	}
	
	//create from premade
	public Armor(Object[] a){
		super((String)a[0]);
		this.attributeTargets = (int[]) a[1];
		this.attributeBonuses = (int[]) a[2];
		this.armorWeight = (int) a[3];
		this.acBase = (int) a[4];
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
				c.abilityScores[n].setBonus(c.abilityScores[n].getBonus()+this.attributeBonuses[i]);	
				i++;
			}
		}
		if (this.acBase != 0 && this.armorWeight != Armor.none){
			int ac=this.acBase;
			int dexMod = (int) c.abilityScores[Attribute.DEX].getMod();
			if (this.armorWeight == Armor.light){
				c.playerStats[Attribute.AC].setValue(ac+=dexMod);
			} else if (this.armorWeight == Armor.medium){
				if (dexMod < 2){
				ac += dexMod;
				} else {
					ac +=2;
				}
				c.playerStats[Attribute.AC].setValue(ac);
			}else if (this.armorWeight == Armor.heavy) {
				c.playerStats[Attribute.AC].setValue(ac);
			} else if (this.armorWeight == Armor.bonus){
				c.playerStats[Attribute.AC].setBonus(c.playerStats[Attribute.AC].getBonus()+ac);
			} else if (this.armorWeight == Armor.shield){
				c.playerStats[Attribute.AC].setBonus(c.playerStats[Attribute.AC].getBonus()+2);
			} 
			
		}
	}
	
	@Override
	public void dequip(Character c){
		this.equipped = false;
		if (this.attributeBonuses != null && this.attributeTargets != null){
			int i=0;
			for (int n : this.attributeTargets){
				c.abilityScores[n].setBonus(c.abilityScores[n].getBonus()-this.attributeBonuses[i]);	
				i++;
			}
		}
		if (this.acBase != 0 && this.armorWeight != Armor.none){
			int ac=this.acBase;
			int dexMod = (int) c.abilityScores[Attribute.DEX].getMod();
			if (this.armorWeight == Armor.light){
				c.playerStats[Attribute.AC].setValue(ac-=dexMod);
			} else if (this.armorWeight == Armor.medium){
				if (dexMod < 2){
				ac -= dexMod;
				} else {
					ac -=2;
				}
				c.playerStats[Attribute.AC].setValue(ac);
			}else if (this.armorWeight == Armor.heavy) {
				c.playerStats[Attribute.AC].setValue(ac);
			} else if (this.armorWeight == Armor.bonus){
				c.playerStats[Attribute.AC].setBonus(c.playerStats[Attribute.AC].getBonus()-ac);
			} else if (this.armorWeight == Armor.shield){
				c.playerStats[Attribute.AC].setBonus(c.playerStats[Attribute.AC].getBonus()-2);
			} 
			
		}
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
