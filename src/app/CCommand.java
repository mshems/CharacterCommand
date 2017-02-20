package app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CCommand {
	public static ArrayList<Character> chars = new ArrayList<Character>();
	public static boolean quit = false;
	public static int activeIndex;
	public static Scanner scanner;
	public static String[] input;
	
	public static void main(String[] args) throws NumberFormatException, IOException, ClassNotFoundException {
		scanner = new Scanner(System.in);
		
/****************************************************************************/
	//add some characters and items for testing
		Character robin = new Character("Sir Robin the Brave", "Knight of the Round Table");
		robin.race = "Human";
		chars.add(robin);
		
		robin.addNewItem(new Weapon("Longsword"),1);
		robin.addNewItem(new Armor("Plate Armor", null, null, Armor.heavy, 18), 1);
		robin.addNewItem(new Armor("Shield", null, null, Armor.shield, 2), 1);

		robin.playerInventory.get(Character.gp).setItemCount(50);
		robin.playerInventory.get(Character.sp).setItemCount(0);
		robin.playerInventory.get(Character.cp).setItemCount(0);
		robin.spellbook.add(new Spell(Spell.cantrip, "Run Away"));
		robin.spellbook.add(new Spell(4, "Polymorph: Chicken"));
		robin.spellbook.add(new Spell(2, "Bravery"));

		Character frodo = new Character("Frodo Baggins", "Adventurer");
		frodo.race = "Hobbit";
		chars.add(frodo);
		
		frodo.addNewItem(new Equippable("Ring of Power"),1);
		frodo.addNewItem(new Weapon("Sting"),1);
		frodo.playerInventory.get(Character.gp).setItemCount(0);
		frodo.playerInventory.get(Character.sp).setItemCount(0);
		frodo.playerInventory.get(Character.cp).setItemCount(0);
	
		activeIndex = 0;
/****************************************************************************/
		checkDirs();
		
		while (quit == false){
			System.out.print("> What would you like to do? ");
			String s = scanner.nextLine();
			input = s.split("\\s+");
			
			s = input[0];
			
			switch (s){
			case "new":
				createCharacter();
				break;
			case "delete":
				deleteCharacter();
				break;
			case "load":
				if (input.length==1){
				activeIndex = getCharIndexFromList()-1;
				} else {
					activeIndex = findCharByName(buildString(1)); 
				}
				System.out.println(String.format("[Loaded %s]", chars.get(activeIndex).playerName));
				break;
			case "save":
				checkDirs();
				saveChar(activeIndex);
				break;
			case "saveall":
				for (int i = 0; i<chars.size(); i++){
					saveChar(i);
				}
				break;
			case "import":
				checkDirs();
				importChar();
				break;
			case "importall":
				checkDirs();
				importAll();
				break;
			case "export":
				checkDirs();
				exportChar(activeIndex);
				break;
			case "exportall":
				checkDirs();
				for (int i = 0; i<chars.size(); i++){
					exportChar(i);
				}
				break;
			case "list":
				dispCharList();
				break;
			case "stats":
				dispCharacter();
				break;
			case "skill":
			case "skills":
				skills();
				break;
			case "view":
				dispCharacter();
				dispInventory();
				break;
			case "inv":
				if (input.length == 1){
				dispInventory();
				} else {
				inv(input[1]);
				}
				break;
			case "equip":
			case "dequip":
				equip();
				break;
			case "heal":
				if (input.length > 1){
					chars.get(activeIndex).heal(input[1]);
				} else {
					System.out.println("[Missing argument: amount]");
				}
				break;
			case "hurt":
				if (input.length > 1){
					chars.get(activeIndex).hurt(input[1]);
				} else {
					System.out.println("[Missing argument: amount]");
				}
				break;
			case "set":
				set();
				break;
			case "level":
			case "levelup":
				String lvl;
				if (input.length == 1){
					lvl = "0";
				} else { 
					lvl = input[1];
				}
				chars.get(activeIndex).levelUp(lvl);
				break;
			case "magic":
			case "spell":
			case "spells":
			case "spellbook":
				if (input.length > 1){
				spells(input[1]);
				} else {
				dispSpellbook();
				}
				break;
			case "attack":
				
				break;
			case "note":
			case "notes":
				notes();
				break;
			case "help":
				dispHelpMenu();
				break;
			case "quit":
				quit = true;
				break;
			default:
				System.out.println("> Command not found - enter \"help\" for list of commands");
				break;
			}
		}
	}
	
	public static Character createCharacter(){ 
		System.out.print("[New Character]\nName: ");
		String name = scanner.nextLine();
		System.out.print("Race: ");
		String race = scanner.nextLine();
		System.out.print("Class: ");
		String classname = scanner.nextLine();
		Character c = new Character(name, classname);
		c.race = race;
		for (Attribute a : c.abilityScores){
			a.setValue(getValidInt("Enter "+a.getName()+" score: "));
		}
		System.out.print("Spellcaster? [y/n]: ");
		if (scanner.nextLine().equalsIgnoreCase("y")){
			c.caster = true;
			System.out.print("Spell Ability?: ");
			c.casterType = (int) getAttributeByName(scanner.nextLine()).getValue();
		} 
		c.updateSkills();
		c.updateStats();
		chars.add(c);	
		System.out.println("[Created "+c.playerName+"]");
		activeIndex = chars.size()-1;
		return c;
	}
	
	//TODO: attacks
	//TODO: edit items
	//TODO: fix & update export text formatting
	//TODO: spellcasting ability, spell save DC, spell attack bonus
	//TODO: sorcery points
	//TODO: proficiencies, EXP
	
	public static void checkDirs() throws IOException, FileNotFoundException{
		Path dataPath = Paths.get("./data/");
		if (!Files.exists(dataPath)){
			Files.createDirectories(dataPath);
		}
	}
	
	public static void importAll() throws ClassNotFoundException, IOException, StreamCorruptedException {
		try {
			File path = new File("./data");
			//File path = new File("../data");

			File [] files = path.listFiles();
			for (int i = 0; i < files.length; i++){
			    File file = files[i];
				if (file.isFile()){
			    	ObjectInputStream inStream = null;
					Character character = new Character();
						inStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));		
						character = (Character) inStream.readObject();
						inStream.close();
						chars.add(character);			    
						}
			}
			System.out.println("[All found characters imported]");
		} catch (StreamCorruptedException e) {
			System.out.println("[Error: No importable files found]");
		}	
		
	}
	
	public static Character importChar() throws ClassNotFoundException, IOException {
		
		ObjectInputStream inStream = null;
		String name = "";
		Character character = new Character();
			while(true){
				try {
					if (input.length == 1){
						System.out.print("[Import Character]\nEnter name of character to import: ");
						name = scanner.nextLine();
					} else {
						name = buildString(1);
					}
					inStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream("./data/"+name+".data")));
					//inStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream("../data/"+name+".data")));
					break;
				} catch (FileNotFoundException e) {
					System.out.println("[\""+name+"\""+" not found]");
					if (input.length != 1){return null;}
					scanner.nextLine();
				}
			}
			character = (Character) inStream.readObject();
			inStream.close();
			System.out.println("["+name+" imported]");
			chars.add(character);
			return character;
	}
	
	public static void saveChar(int i) throws IOException {
			try {
				String dataFile = "./data/"+chars.get(i).playerName+".data";
				//String dataFile = "../data/"+chars.get(i).playerName+".data";
				ObjectOutputStream out = null;
				out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)));
				out.writeObject(chars.get(i));
				out.close();
				System.out.println("["+chars.get(i).playerName+" saved]");
			} catch (FileNotFoundException e) {
				//e.printStackTrace();
			}
	}
	
	public static void exportChar(int i) throws IOException {
		String text = "";
		if (input.length == 1){
			text = chars.get(i).toString()+"\n"+chars.get(i).playerInventory.toString();
		} else {
			i = findCharByName(buildString(1));
		}
			if (i != -1){
				text = chars.get(i).toString()+"\n"+chars.get(i).playerInventory.toString();
				Files.write(Paths.get("./data/"+chars.get(i).playerName+".txt"), text.getBytes());
				//Files.write(Paths.get("../data/"+chars.get(i).playerName+".txt"), text.getBytes());
				System.out.println("[Exported character to "+chars.get(i).playerName+".txt]");
			}
		
	}
	
	public static void deleteCharacter(){
		int del;
		System.out.println("[Select character to delete]");
		dispCharList();
		del = getCharIndexFromList();
		System.out.println(String.format("[Deleted %s]\n", chars.get(del).playerName));
		chars.remove(del);
	}
	
	public static void notes(){
		String command;
		if (input.length > 1){
			command = input[1];
		} else {
			command = "help";
		}
		switch (command){
		case "add":
			System.out.print("Enter text to add: ");
			if (!chars.get(activeIndex).notes.equals("")){
				chars.get(activeIndex).notes+="\n";
			}
			chars.get(activeIndex).notes+="- "+scanner.nextLine();
			System.out.println("[Note added]");
			break;
		case "clear":
			chars.get(activeIndex).notes = "";
			System.out.println("[Notes cleared]");
			break;
		case "view":
			System.out.println("[Notes]");
			System.out.println(chars.get(activeIndex).notes);
			break;
		case "help":
			System.out.println("[notes add/view/clear]");
			break;
		default:
			System.out.println("> Command not recognized. Enter \"notes help\" for help");
			break;
		}
	}

	public static void skills(){
		if (input.length >= 3){
			String command = input[1];
			Skill s = getSkillByName(buildString(2));

			switch (command){
			case "train":
				if (s!=null){
					s.proficient = true;
					System.out.println("[Gained proficiency with "+s.skillName+"]");
				}
				break;
			case "forget":
				if (s!=null){
					s.proficient = false;
					s.expertise = false;
					System.out.println("[Lost proficiency with "+s.skillName+"]");
				}
				break;
			case "expert":
			case "expertise":
				if (s!=null){
					s.expertise = true;
					System.out.println("[Gained expertise with "+s.skillName+"]");
				}
				break;
			case "view":
				System.out.println(s);
				break;
			case "help":
				System.out.println("[skills train/forget/expert/view <skillname>]");
				break;
			default:
				System.out.println("[Invalid command]");
				break;
			}
			chars.get(activeIndex).updateSkills();
		} else if (input.length == 2) {
			System.out.println("[Invalid command]");
		} else {
			dispSkills();
		}
	}
	
	public static void spells(String command){
		String spellName;
		int spellLevel = -1;
		Spell castSpell;
		int lvl;
		int num;
		
		switch (command){
		case "learn":
			spellLevel = getSpellLevel(input[input.length-1]);
			if (spellLevel == -1){break;}
			spellName = buildString(2);			
			chars.get(activeIndex).learnSpell(new Spell(spellLevel, spellName));
			break;
		case "forget":
			Spell forgetSpell = getSpellByName(buildString(2));
			chars.get(activeIndex).forgetSpell(forgetSpell);
			break;
		case "cast":
			if (input.length>2){
					spellName = buildString(2);
					castSpell = getSpellByName(spellName);
					if (castSpell == null){
						break;
					}
					spellLevel = getSpellLevel(input[input.length-1]);
					if (spellLevel == -1){break;}
				chars.get(activeIndex).castSpell(castSpell, spellLevel);
			} else { 
				//guided casting interface
				System.out.println("[Guided casting - placeholder]");
				break;
			}
			break;
		case "charge":
			if (!validIntInput(2) || !validIntInput(3)){break;}
			lvl = Integer.parseInt(input[2]);
			num = Integer.parseInt(input[3]);
			chars.get(activeIndex).chargeSpell(lvl, num);

			break;
		case "chargeall":
			chars.get(activeIndex).chargeAll();
			break;
		case "prep":
		case "unprep":
			spellName = buildString(2);
			Spell prepSpell = getSpellByName(spellName); 
			if (prepSpell == null){break;}
			if (command.equals("unprep")){
				prepSpell.spellPrepared = false;
				System.out.println("[Spell \""+prepSpell.spellName+"\" no longer prepared]");
			} else {
				prepSpell.spellPrepared = true;
				System.out.println("[Prepared spell \""+prepSpell.spellName+"\"]");
			}
			break;	
		case "prepall":
		case "unprepall":
			for (Spell spell : chars.get(activeIndex).spellbook){
				if (command.equals("prepall")){
					spell.spellPrepared = true;
				} else {
					spell.spellPrepared = false;
				}
			}
			break;
		case "slots":
			if (input.length == 5 && input[2].equals("get")){
				if (!validIntInput(3) || !validIntInput(4)){
					System.out.println("[Invalid command format]");
					break;
				}
				lvl = Integer.parseInt(input[4]);
				num = Integer.parseInt(input[3]);
				chars.get(activeIndex).spellSlots[lvl-1].total+=num;
				System.out.println("[Gained "+num+" level "+lvl+" spell slots]");
			} else if (input.length == 2){
				dispSpellSlots();
			} else {
				System.out.println("[Invalid command format]");
			}
			break;
		default:
			break;
		}
	}
	
	public static void set(){
		String tgt;
		Attribute a = null;
		if (input.length >= 3){
			tgt = buildString(1);
			
			a = getAttributeByName(tgt);
			if (a != null){
				try {
					int val = getSpecialInt(input[input.length-1]);
					a.setValue(val);
					//a.setValue(Double.parseDouble(input[1]));
					System.out.println("["+a.getName()+" set to "+val+"]");
				} catch (NumberFormatException e) {
					System.out.println("Invalid Command Format");
				}
			}
		} else if (input.length == 2) {
					
			//special case toggle --- temporary
			tgt = input[1];
			if (tgt.equalsIgnoreCase("unprepOnCast")){
				chars.get(activeIndex).unprepOnCast = !chars.get(activeIndex).unprepOnCast;
			} else if (tgt.equalsIgnoreCase("requirePrep")){
				chars.get(activeIndex).requirePrep = !chars.get(activeIndex).requirePrep;
			} else {
				System.out.println("[Invalid command: missing arguments]");
			}
			
		} else {
			System.out.println("[Edit Attributes]");
			while(a == null){
				System.out.print("Enter attribute to edit: ");
				tgt = scanner.nextLine();
				a = getAttributeByName(tgt);
			}
			int value = getValidInt("Enter new value: ");
			a.setValue(value);
			System.out.println("["+a.getName()+" set to "+value+"]");
		
		}
		chars.get(activeIndex).updateSkills();
		chars.get(activeIndex).updateStats();
	}
	
	public static void inv(String command){
		int count;
		String name;
		
		switch (command){
		case "get":
			if (input.length>=4){
				String type = input[2];
				
				//if (!validIntInput(3)){break;}
				//count = Integer.parseInt(input[3]);
				count = getSpecialInt(input[input.length-1]);
				name = buildString(3);

				switch (type){
				case "item":
					chars.get(activeIndex).addNewItem(new Item(name), count);
					System.out.println(String.format("[Added %dx %s]", count, name));
					break;
				case "weapon":
					chars.get(activeIndex).addNewItem(new Weapon(name), count);
					System.out.println(String.format("[Added %dx %s]", count, name));
					break;
				case "armor":
					chars.get(activeIndex).addNewItem(new Armor(name), count);
					System.out.println(String.format("[Added %dx %s]", count, name));
					break;
				case "equip":
				case "equippable":
					chars.get(activeIndex).addNewItem(new Equippable(name), count);
					System.out.println(String.format("[Added %dx %s]", count, name));
					break;
				default:
					System.out.println("> Command not recognized. Enter \"inv help\" for help");
				break;
				}
			} else if (input.length >=3){
				System.out.println("[Invalid command: missing arguments]");
			} else {
				System.out.println("[Get new items]");
				System.out.print("Item/Weapon/Armor/Equippable: ");
				String type = scanner.nextLine();
				if (type.equalsIgnoreCase("item") || type.equalsIgnoreCase("weapon") || type.equalsIgnoreCase("armor")|| type.equalsIgnoreCase("equip") || type.equalsIgnoreCase("equippable")){
					System.out.print("Enter item name: ");
					name = scanner.nextLine();
					count = getValidInt("Enter count: ");
					switch (type){
					case "item":
						chars.get(activeIndex).addNewItem(new Item(name), count);
						System.out.println(String.format("[Added %dx %s]", count, name));
						break;
					case "weapon":
						chars.get(activeIndex).addNewItem(new Weapon(name), count);
						System.out.println(String.format("[Added %dx %s]", count, name));
						break;
					case "armor":
						chars.get(activeIndex).addNewItem(new Armor(name), count);
						System.out.println(String.format("[Added %dx %s]", count, name));
						break;
					case "equip":
					case "equippable":
						chars.get(activeIndex).addNewItem(new Equippable(name), count);
						System.out.println(String.format("[Added %dx %s]", count, name));
						break;
					default:
						System.out.println("> Command not recognized. Enter \"inv help\" for help");
					break;
					}
				}
			}
			break;
		case "drop":
		case "add":
			if (input.length>=4){
				if (!validIntInput(2)){break;}
				count = Integer.parseInt(input[2]);
				
				name = buildString(3);
				if (command.equals("drop")){ count = -count; }
				
				switch (name){
				case "gp":
				case "gold":
					chars.get(activeIndex).addCoin(0, count);
					break;
				case "sp":
				case "silver":
					chars.get(activeIndex).addCoin(1, count);
					break;
				case "cp":
				case "copper":
					chars.get(activeIndex).addCoin(2, count);
					break;
				default:
					int index = getItemIndexByName(name);
					if (index >= 0){
						chars.get(activeIndex).addMoreItem(index, count);
					}
					break;	
				}
					if (command.equals("drop")){
						System.out.println(String.format("[Lost %dx %s]", -count, name));
					} else {
						System.out.println(String.format("[Added %dx %s]", count, name));
					}
				break;
			} else if (input.length == 3){
				System.out.println("[Invalid command: missing arguments]");
			} else {
				System.out.println("[Add/Drop items]");
				dispInventory();
				
				System.out.print("Enter item name: ");
				name = scanner.nextLine();
				int index;
					switch (name){
					case "gp":
					case "gold":
						index = 0;
						break;
					case "sp":
					case "silver":
						index = 1;
						break;
					case "cp":
					case "copper":
						index = 2;
						break;
					default:
						index = getItemIndexByName(name);
						break;
					}
				Item item = chars.get(activeIndex).playerInventory.get(index);
				String str;
				if (command.equals("drop")){
					str = "lost";
				} else {
					str = "gained";
				}
				count = getValidInt("Enter amount "+str+": ");
				
				if (command.equals("drop")){ count = -count; }
				chars.get(activeIndex).addMoreItem(index, count);
				if (command.equals("drop")){
					System.out.println(String.format("[Lost %dx %s]", -count, item.getItemName() ));
				} else {
					System.out.println(String.format("[Added %dx %s]", count, item.getItemName()));
				}
			}
			break;
		case "help":
			System.out.println("[inv get/add/drop # name]");
			break;
		case "sort":
			Collections.sort(chars.get(activeIndex).playerInventory.subList(3,chars.get(activeIndex).playerInventory.size()));
			break;
		default:
			System.out.println("> Command not recognized. Enter \"inv help\" for help");
		}
	}
	
	public static void equip(){
		Item item;
		if (input.length > 1){
			item = getItemByName(buildString(1));
		} else {
			item = getItemFromInv();
		}
		if (item != null && item.equippable == true){
			if (input[0].equals("dequip")){
				item.dequip(chars.get(activeIndex));
				System.out.print("["+item.getItemName()+" unequipped]\n");
			} else {
				item.equip(chars.get(activeIndex));
				System.out.print("["+item.getItemName()+" equipped]\n");
			}
		} else {
			System.out.print("[Item not valid]\n");
		}
	}
	
	public static int getValidInt(String message){
		int n = 0;
		while (true) {
			System.out.print(message);
			if (scanner.hasNextInt() == true){
				n = scanner.nextInt();
				scanner.nextLine();
				break;
			} else {
				System.out.println("[Invalid input: not an integer]");
				scanner.nextLine();
			}
		}
		return n;
	}
	
	public static Attribute getAttributeByName(String tgt){
		for (Attribute a : chars.get(activeIndex).abilityScores){
			for (String str : a.matches){
				if (str.equalsIgnoreCase(tgt)){
					return a;
				}
			}
		}
		for (Attribute a : chars.get(activeIndex).playerStats){
			for (String str : a.matches){
				if (str.equalsIgnoreCase(tgt)){
					return a;
				}
			}
		}
		System.out.println("[Attribute not fount]");
		return null;
	}
	
	public static int getCharIndexFromList(){
		dispCharList();
		int n = 0;
		boolean repeat = true;
		while (repeat == true){
			n = getValidInt("Enter character #: ");
			if (n > 0 && n <= chars.size()){
				break;
			} else {
				System.out.println("[No character at that index]");
			}
		}
		return n;
	}
	
	public static int getItemIndexByName(String name){
		for(int i = 3; i<chars.get(activeIndex).playerInventory.size(); i++){
			if (chars.get(activeIndex).playerInventory.get(i).getItemName().equalsIgnoreCase(name)){
				return i;
			}
		}
		System.out.println("[Item not found in inventory]");
		return -1;
	}
	
	public static Item getItemByName(String name){
		for(int i = 3; i<chars.get(activeIndex).playerInventory.size(); i++){
			if (chars.get(activeIndex).playerInventory.get(i).getItemName().equalsIgnoreCase(name)){
				return chars.get(activeIndex).playerInventory.get(i);
			}
		}
		System.out.println("[Item not found in inventory]");
		return null;
	}
	
	public static Item getItemFromInv(){
		boolean repeat = true;
		int n = 0;
		dispInventory();
		while (repeat == true){
			n = getValidInt("Enter item #: ")+2;
			if (n >= 0 && n < chars.get(activeIndex).playerInventory.size()){
				break;
			} else {
				System.out.println("[No item at that index]");
			}
		}
		return chars.get(activeIndex).playerInventory.get(n);
	}	
	
	public static Skill getSkillByName(String name){
		for (Skill s : chars.get(activeIndex).playerSkills){
			for ( String str : s.matches){
				if (str.equalsIgnoreCase(name)){
					return s;
				}
			}
		}
		System.out.println("[Skill not found]");
		return null;
	}
	
	public static Spell getSpellByName(String name){
		for (Spell spell : chars.get(activeIndex).spellbook){
			if (spell.spellName.equalsIgnoreCase(name)){
				return spell;
			}
		}
		System.out.println("[Spell not known]");
		return null;
	};
	
	public static int findCharByName(String name){
		int n = 0;
		for (Character c : chars){
			if (c.playerName.equalsIgnoreCase(name)){
				return n;
			}
			n++;
		}
		System.out.println("[Character not found]");
		return -1;
	}
	
	public static int getSpellLevel(String inputString){
		if (inputString.startsWith("@")){
			String s = inputString.substring(1).trim();
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				System.out.println("[Invalid command format]");
				return Spell.error;
			}
		} else {
			return Spell.defaultLevel;
		}
	}
	
	public static String getSpecialArgument(String inputString){
		if (inputString.startsWith("-")){
			String s = inputString.substring(1).trim();
			return s;
		} else {
			return null;
		}
	}
	
	public static int getSpecialInt(String inputString){
		if (inputString.startsWith("@") || inputString.startsWith("=") || inputString.startsWith("*")){
			String s = inputString.substring(1).trim();
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				System.out.println("[Invalid command format]");
				return 0;
			}
		} else {
			return 1;
		}
	}
	
	public static String buildString(int n){
		String str = "";
		for (int i = n; i<input.length; i++){
			if (!input[i].startsWith("@") && !input[i].startsWith("=") && !input[i].startsWith("*")){
				str+=input[i]+" ";
			}
		}
		return str.trim();
	}	
	
	public static boolean validIntInput(int index){
		try {
			Integer.parseInt(input[index]);
			return true;
		} catch (NumberFormatException e) {
			System.out.println("[Invalid command format]");
		}
		return false;
	}
	
	public static void dispCharList(){
		System.out.println("[Characters]");
		int i=1;
		for (Character c : chars){
			System.out.println(String.format("[%d] - %s", i, c.playerName));
			i++;
		}
	}
	
	public static void dispCharacter(){
		System.out.println(chars.get(activeIndex));
	}
	
	public static void dispSpellbook(){
		System.out.println(chars.get(activeIndex).spellBookToString());
	}
	
	public static void dispSkills(){
		System.out.println(chars.get(activeIndex).skillsToString());
	}
	
	public static void dispInventory(){
		System.out.println(chars.get(activeIndex).playerInventory);
	}
	
	public static void dispSpellSlots(){
		System.out.println(chars.get(activeIndex).spellSlotsToString());
	}

	public static void dispHelpMenu(){
		System.out.println("<> = variable field\n[new][load <name>][list][delete][save <name>][saveall][import <name>][importall][export <name>][exportall]");
		System.out.println("[stats][view][set <arguments>][inv <arguments>][spells <arguments>][equip <item>][dequip <item>]");
		System.out.println("[heal #][hurt #][level <#>][levelup][skills <arguments>][notes <arguments>]");
		System.out.println("View README.txt for details");
	}
}
