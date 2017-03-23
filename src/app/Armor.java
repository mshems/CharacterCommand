package app;

import java.util.ArrayList;

public class Armor extends Equippable{
	protected ArrayList<Integer> attributeTargets;
	protected ArrayList<Integer> attributeBonuses;
	protected int acBase = 0;
	protected int armorWeight = 0;
	
	public static final int none = 0 ;
	public static final int light = 1;
	public static final int medium = 2;
	public static final int heavy = 3;
	public static final int shield = 4;
	public static final int other = 5;

	public Armor(){
		super();
		this.attributeTargets = new ArrayList<Integer>();
		this.attributeBonuses = new ArrayList<Integer>();
		this.equippable = true;
	}
	
	public Armor(String n) {
		super(n);
		this.attributeTargets = new ArrayList<Integer>();
		this.attributeBonuses = new ArrayList<Integer>();
		this.equippable = true;
	}
	
	//create from premade
	@SuppressWarnings("unchecked")
	public Armor(Object[] a){
		super((String)a[0]);
		this.attributeTargets = (ArrayList<Integer>) a[1];
		this.attributeBonuses = (ArrayList<Integer>) a[2];
		this.armorWeight = (int) a[3];
		this.acBase = (int) a[4];
		this.equippable = true;
	}
	
	public Armor(String n, ArrayList<Integer> aT, ArrayList<Integer> aB, int armorWeight, int acBase) {
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
				c.abilityScores[n].setBonus(c.abilityScores[n].getBonus()+this.attributeBonuses.get(i));	
				i++;
			}
		}
		if (this.acBase != 0 && this.armorWeight != Armor.none){
			int ac=this.acBase;
			int dexMod = (int) c.abilityScores[Ability.DEX].getMod();
			if (this.armorWeight == Armor.light){
				c.playerStats[Ability.AC].base = (ac+=dexMod);
			} else if (this.armorWeight == Armor.medium){
				if (dexMod < 2){
				ac += dexMod;
				} else {
					ac +=2;
				}
				c.playerStats[Ability.AC].base=(ac);
			}else if (this.armorWeight == Armor.heavy) {
				c.playerStats[Ability.AC].base=(ac);
			} else if (this.armorWeight == Armor.other){
				c.playerStats[Ability.AC].bonus+=ac;
			} else if (this.armorWeight == Armor.shield){
				c.playerStats[Ability.AC].bonus+=2;
			} 
			
		}
	}
	
	@Override
	public void dequip(Character c){
		this.equipped = false;
		if (this.attributeBonuses != null && this.attributeTargets != null){
			int i=0;
			for (int n : this.attributeTargets){
				c.abilityScores[n].setBonus(c.abilityScores[n].getBonus()-this.attributeBonuses.get(i));	
				i++;
			}
		}
		if (this.acBase != 0 && this.armorWeight != Armor.none){
			int ac=this.acBase;
			int dexMod = (int) c.abilityScores[Ability.DEX].getMod();
			if (this.armorWeight == Armor.light){
				c.playerStats[Ability.AC].base=(ac-=dexMod);
			} else if (this.armorWeight == Armor.medium){
				if (dexMod < 2){
				ac -= dexMod;
				} else {
					ac -=2;
				}
				c.playerStats[Ability.AC].base=(ac);
			}else if (this.armorWeight == Armor.heavy) {
				c.playerStats[Ability.AC].base=(ac);
			} else if (this.armorWeight == Armor.other){
				c.playerStats[Ability.AC].bonus-=ac;
			} else if (this.armorWeight == Armor.shield){
				c.playerStats[Ability.AC].bonus-=2;
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
		if (this.attributeBonuses!=null && this.attributeTargets!=null){
			s+="*";
		}
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
		if (this.description!=null){
			s+="\nDescription: "+this.description;
		}
		if (this.attributeBonuses!=null && this.attributeTargets!=null){
			s+="\n"+this.effectsToString();
		}
		if (this.value!=0){
			s+="\n[Value: "+this.value+"]";
		}
		return s;
	}

}
