package app;

import java.io.Serializable;

public class Skill implements Serializable{
	protected String skillName;
	protected int skillAttribute;
	protected double skillMod;
	protected boolean proficient;
	protected boolean expertise;
	protected String[] matches;
	
	public Skill(String name, int skillAttribute, String[] matches){
		this.skillName = name;
		this.skillAttribute = skillAttribute;
		this.proficient = false;
		this.expertise = false;
		this.skillMod = 0;
		this.matches = matches;
	}
	
	public String toString(){
		return String.format("%s (%+.0f)", this.skillName, this.skillMod);
	}
}
