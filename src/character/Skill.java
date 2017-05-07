package character;

import java.io.Serializable;

public class Skill implements Serializable {
	private static final long serialVersionUID = 1L;
	private String skillName;
	private Ability skillAbility;
	private double skillMod;
	private boolean trained;
	private boolean expert;
	
	public Skill(String name, Ability a){
		this.skillName=name;
		this.skillAbility = a;
		this.skillMod = 0;
		this.trained = false;
		this.expert = false;
	}
	
	public void update(PlayerCharacter c){
		this.skillMod=0;
		if (this.trained){
			this.skillMod += c.getAttributes().get("pb").getBaseVal();
		}
		if (this.expert){
			this.skillMod += c.getAttributes().get("pb").getBaseVal();
		}
	}
	
	public void train(PlayerCharacter c){
		this.trained = true;
		this.update(c);
	}
	public void untrain(PlayerCharacter c){
		this.trained = false;
		this.expert = false;
		this.update(c);
	}
	public void expert(PlayerCharacter c){
		this.expert = true;
		this.trained = true;
		this.update(c);
	}
	public void unexpert(PlayerCharacter c){
		this.expert = false;
		this.update(c);
	}
	
	public String getName(){
		return this.skillName;
	}
	
	public String toString(){
		return String.format("%s (%+.0f)", this.skillName, this.skillAbility.getMod()+this.skillMod);
	}
}
