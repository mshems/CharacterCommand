package app;

public class Equippable extends Item{
	protected int[] attributeTargets;
	protected int[] attributeBonuses;
	
	public Equippable(){
		super();
		this.equippable = true;
	}
	
	public Equippable(String n) {
		super(n);
		this.equippable = true;
	}
	
	public Equippable(String n, int[] aT, int[] aB) {
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
				c.abilityScores[n].setBaseVal(c.abilityScores[n].total()+this.attributeBonuses[i]);	
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
				c.abilityScores[n].setBaseVal(c.abilityScores[n].total()-this.attributeBonuses[i]);	
				i++;
			}
		}
	}
	
	public String toString(){
		String s = String.format("%dx %s",  this.getItemCount(), this.getItemName());
		if (this.equipped==true){
			s= "e "+s;
		} else {
			s= "- "+s;
		}
		return s;
	}

}
