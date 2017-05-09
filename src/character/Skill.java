package character;

import java.io.Serializable;

public class Skill implements Serializable {
	private static final long serialVersionUID = 1L;
	private String skillName;
	private Ability skillAbility;
	private double skillMod;
	private boolean trained;
	private boolean expert;
	
	public Skill(String name, Ability a, PlayerCharacter pc){
		this.skillName=name;
		this.skillAbility = a;
		this.skillMod = 0;
		this.trained = false;
		this.expert = false;
	}
	
	public void update(PlayerCharacter pc){
		this.skillMod=0;
		if (this.trained){
			this.skillMod += pc.getStat(Attribute.PB).getBaseVal();
		}
		if (this.expert){
			this.skillMod += pc.getStat(Attribute.PB).getBaseVal();
		}
	}
	
	public void train(PlayerCharacter pc){
		this.trained = true;
		this.update(pc);
	}
	public void untrain(PlayerCharacter pc){
		this.trained = false;
		this.expert = false;
		this.update(pc);
	}
	public void expert(PlayerCharacter pc){
		this.expert = true;
		this.trained = true;
		this.update(pc);
	}
	public void unexpert(PlayerCharacter pc){
		this.expert = false;
		this.update(pc);
	}
	
	public String getName(){
		return this.skillName;
	}
	
	public String toString(){
		return String.format("%s (%+.0f)", this.skillName, this.skillAbility.getMod()+this.skillMod);
	}
}
