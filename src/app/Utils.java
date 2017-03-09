package app;

public class Utils {
	public static final String line = "---------------------------------------------------";
	public static final void divider(){System.out.println(line);}
	
	public static final String getAbilNameByIndex(int index){
		String s="";
		switch(index){
		case Attribute.STR:
			return "STR";
		case Attribute.DEX:
			return "DEX";
		case Attribute.CON:
			return "CON";
		case Attribute.WIS:
			return "WIS";
		case Attribute.INT:
			return "INT";
		case Attribute.CHA:
			return "CHA";
		}
		return s;
	}
	
	public static final int getAbilIndexByName(String s){
		s = s.toLowerCase().trim().substring(0, 3);
		int index=-1;
		switch(s){
		case "str":
			return 0;
		case "dex":
			return 1;
		case "con":
			return 2;
		case "wis":
			return 3;
		case "int":
			return 4;
		case "cha":
			return 5;
		}
		return index;
	}
	
	
	public static final String[] STR = new String[]{
			"str","strength"
	};
	public static final String[] DEX = new String[]{
			"dex","dexterity"
	};
	public static final String[] CON = new String[]{
			"con","constitution"
	};
	public static final String[] INT = new String[]{
			"int","intelligence"
	};
	public static final String[] WIS = new String[]{
			"wis","wisdom"
	};
	public static final String[] CHA = new String[]{
			"cha","charisma"
	};
	
	
	public static final String[] HP = new String[]{
			"hp","hit points","health"
	};
	public static final String[] HP_MAX = new String[]{
			"hpm","hp max","hit point max","max hp","maxhp","max health"
	};
	public static final String[] AC = new String[]{
			"ac","armor","armor class","armor check"
	};
	public static final String[] SPD = new String[]{
			"speed","spd"
	};
	public static final String[] PER = new String[]{
			"passive perception","pp","pass per","per","perception"
	};
	public static final String[] PRO = new String[]{
			"pb","prof","bonus","proficiency bonus","proficiency"
	};
	public static final String[] INI = new String[]{
			"initiative","ini","init"
	};
	public static final String[] SSDC = new String[]{
			"spell save","spell dc", "spell save dc", "ssdc"
	};
	public static final String[] SAM = new String[]{
			"spell mod","spell attack", "spell attack mod", "spell modifier", "sam"
	};
	
	
	
	public static final String[] STR_THROW = new String[]{
			"str throw","str save","str saving throw"
	};
	public static final String[] ATHLETICS = new String[]{
			"athletics"
	};
	public static final String[] DEX_THROW = new String[]{
			"dex save", "dex throw", "dex saving throw"
	};
	public static final String[] ACROBATICS = new String[]{
			"acrobatics"
	};
	public static final String[] SLEIGHT_OF_HAND = new String[]{
			"sleight of hand","soh"
	};
	public static final String[] STEALTH = new String[]{
			"stealth","sneak"
	};
	public static final String[] CON_THROWS = new String[]{
			"con save","con throw","con saving throw"
	};
	public static final String[] INT_THROWS = new String[]{
			"int save","int throw","int saving throw"
	};
	public static final String[] ARCANA = new String[]{
			"arcana"
	};
	public static final String[] HISTORY = new String[]{
			"history"
	};
	public static final String[] INVESTIGATION = new String[]{
			"investigations"
	};
	public static final String[] NATURE = new String[]{
			"nature"
	};
	public static final String[] RELIGION = new String[]{
			"religion"
	};
	public static final String[] WIS_THROWS = new String[]{
			"wis throw","wis save","wis saving throw"
	};
	public static final String[] ANIMAL_HANDLING = new String[]{
			"animal handling"
	};
	public static final String[] INSIGHT = new String[]{
			"insight"
	};
	public static final String[] MEDICINE = new String[]{
			"medicine"
	};
	public static final String[] PERCEPTION = new String[]{
			"perception"
	};
	public static final String[] SURVIVAL = new String[]{
			"survival"
	};
	public static final String[] CHA_THROWS = new String[]{
			"cha throw","cha save","cha saving throw"
	};
	public static final String[] DECEPTION = new String[]{
			"deception"
	};
	public static final String[] INTIMIDATION = new String[]{
			"intimidation"
	};
	public static final String[] PERFORMANCE = new String[]{
			"performance"
	};
	public static final String[] PERSUASION = new String[]{
			"persuasion"
	};
}
