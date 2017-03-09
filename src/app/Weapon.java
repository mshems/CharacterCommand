package app;

import java.util.ArrayList;

public class Weapon extends Equippable {
	protected DiceRoll damageRoll;
	protected int attackAbility;
	
	protected ArrayList<Integer> attributeTargets;
	protected ArrayList<Integer> attributeBonuses;
	
	public Weapon(){
		super();
		this.attributeTargets = new ArrayList<Integer>();
		this.attributeBonuses = new ArrayList<Integer>();
		this.equippable = true;
	}
	
	public Weapon(String n, DiceRoll r){
		super(n);
		this.attributeTargets = new ArrayList<Integer>();
		this.attributeBonuses = new ArrayList<Integer>();
		this.damageRoll = r;
		this.equippable = true;
	}
	
	public Weapon(String n){
		super(n);
		this.attributeTargets = new ArrayList<Integer>();
		this.attributeBonuses = new ArrayList<Integer>();
		this.equippable = true;
	}
	
	@Override
	public void equip(Character c){
		this.equipped = true;
		if (this.attributeBonuses != null && this.attributeTargets != null){
			int i=0;
			for (int n : this.attributeTargets){
				c.abilityScores[n].setBaseVal(+this.attributeBonuses.get(i));	
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
				c.abilityScores[n].setBaseVal(-this.attributeBonuses.get(i));	
				i++;
			}
		}
	}
	public Weapon(String n, DiceRoll r, ArrayList<Integer> aT, ArrayList<Integer> aB) {
		super(n);
		this.damageRoll = r;
		this.attributeTargets = aT;
		this.attributeBonuses = aB;
		this.equippable = true;
	}
	
	public String toString(){
		String s = String.format("%dx %s", this.getItemCount(), this.getItemName());
		if (this.damageRoll != null){
			s += String.format(" (%s)", this.damageRoll);
		}
		if (this.attributeBonuses!=null && this.attributeTargets!=null){
			s+="*";
		}
		if (this.equipped==true){s= "e "+s;}else{s="- "+s;}
		return s;
	}
	
	public String effectsToString(){
		String s="Effects:";
		for (int i=0; i<attributeBonuses.size(); i++){
			s+="\n["+(i+1)+"] "+Utils.getAbilNameByIndex(attributeTargets.get(i))+String.format(" %+d ", attributeBonuses.get(i));
		}
		return s;
	}
	
	public String details(){
		String s = String.format("[----- %s -----]", this.getItemName());
		if (this.damageRoll != null){
			s += String.format("\nDamage Roll: %s", this.damageRoll);
		}
		if (this.description!=null){
			s+="\nDescription: "+this.description;
		}
		if (this.attributeBonuses!=null && this.attributeTargets!=null){
			s+="\n"+this.effectsToString();
		}
		if (this.value!=0){
			s+="\nValue: "+this.value;
		}
		return s;
	}
}
