package app;

import java.util.ArrayList;

public class Equippable extends Item{
	//protected int[] attributeTargets;
	//protected int[] attributeBonuses;
	
	protected ArrayList<Integer> attributeTargets;
	protected ArrayList<Integer> attributeBonuses;
	
	public Equippable(){
		super();
		this.equippable = true;
	}
	
	public Equippable(String n) {
		super(n);
		this.equippable = true;
	}
	
	public Equippable(String n, ArrayList<Integer> aT, ArrayList<Integer> aB) {
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
				c.abilityScores[n].setBaseVal(c.abilityScores[n].total()+this.attributeBonuses.get(i));	
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
				c.abilityScores[n].setBaseVal(c.abilityScores[n].total()-this.attributeBonuses.get(i));	
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
	
	public String effectsToString(){
		String s="Effects:";
		for (int i=0; i<attributeBonuses.size(); i++){
			s+="\n["+(i+1)+"] "+Utils.getAbilNameByIndex(i)+String.format(" %+d ", attributeBonuses.get(i));
		}
		return s;
	}
	
	public String details(){
		String s = String.format("[----- %s -----]", this.getItemName());
		if (this.description!=null){
			s+="\nDescription: "+this.description;
		}
		if (this.attributeBonuses!=null && this.attributeTargets!=null){
			s+=this.effectsToString();
		}
		if (this.value!=0){
			s+="\n[Value: "+this.value+"]";
		}
		return s;
	}

}
