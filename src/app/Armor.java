package app;

public class Armor extends Item{
	protected int[] attributeTargets;
	protected int[] attributeBonuses;
	protected int armorAbility;
	protected int acMax;
	protected int armorWeight;
	
	protected static final int light = 0;
	protected static final int medium = 1;
	protected static final int heavy = 2;

	public Armor(){
		super();
		this.equippable = true;
	}
	
	public Armor(String n) {
		super(n);
		this.equippable = true;
	}
	
	public Armor(String n, int[] aT, int[] aB) {
		super(n);
		this.attributeTargets = aT;
		this.attributeBonuses = aB;
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
