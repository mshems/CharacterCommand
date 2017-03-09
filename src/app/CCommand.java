package app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import items.Armors;
import items.Items;

public class CCommand {
	public static ArrayList<Character> chars = new ArrayList<Character>();
	public static boolean quit = false;
	public static boolean viewAlways = false; 
	public static int activeIndex;
	public static Scanner scanner;
	public static String[] input;
	
	public static void main(String[] args) throws NumberFormatException, IOException, ClassNotFoundException {
		scanner = new Scanner(System.in);
		
		if (args.length==1){
			if (args[0].equals("on")){
				viewAlways = true;
			}
		}
		
/***************************************************************************
	//add some characters and items for testing
		Character robin = new Character("Sir Robin the Brave", "Knight of the Round Table");
		chars.add(robin);
		robin.race = "Human";
		robin.caster = true;
		robin.casterType = Attribute.CHA;
		robin.addNewItem(new Weapon("Longsword"),1);
		robin.addNewItem(new Armor("Plate Armor", null, null, Armor.heavy, 18), 1);
		robin.addNewItem(new Armor("Shield of Cowardice", new ArrayList<Integer>{Attribute.CON, Attribute.CHA}, new ArrayList<Integer>{-2,-2}, Armor.shield, 2), 1);
		//robin.addNewItem(new Armor(Armors.steadfastShield), 1);
		robin.playerInventory.get(Character.gp).setItemCount(50);
		robin.playerInventory.get(Character.sp).setItemCount(0);
		robin.playerInventory.get(Character.cp).setItemCount(0);
		robin.spellbook.add(new Spell(Spell.cantrip, "Run Away"));
		robin.spellbook.add(new Spell(4, "Polymorph: Chicken"));
		robin.spellbook.add(new Spell(2, "Bravery"));

		Character frodo = new Character("Frodo Baggins", "Adventurer");
		chars.add(frodo);
		frodo.race = "Hobbit";
		frodo.addNewItem(new Equippable("Ring of Power"),1);
		frodo.addNewItem(new Weapon("Sting"),1);
		frodo.addNewItem(new Armor("Mithil Chainmail", new int[]{Attribute.CON}, new int[]{2}, Armor.light, 16), 1);
		frodo.playerInventory.get(Character.gp).setItemCount(2);
		frodo.playerInventory.get(Character.sp).setItemCount(10);
		frodo.playerInventory.get(Character.cp).setItemCount(5);
		
		Character tim = new Character("Tim", "Wizard");
		chars.add(tim);
		activeIndex = 2;
		tim.race = "Human";
		tim.caster = true;
		tim.casterType = Attribute.WIS;
		tim.spellbook.add(new Spell(Spell.cantrip, "Firebolt"));
		tim.spellbook.add(new Spell(Spell.cantrip, "Intimidate"));
	
		activeIndex = 0;
/****************************************************************************/
		
		//TODO: attacks
		//TODO: sorcery points?
		//TODO: proficiencies, EXP
		//TODO: Item editing (50%)
		//TODO: Separate export option
		
		checkDirs();
		importAll();
		//chars.get(0).addNewItem(Armors.arm1, 1);
		while (quit == false){
			
			if (viewAlways==true){
				if (charLoaded()){
					dispCharacter();
					dispInventory();
					System.out.println("[Notes]");
					System.out.println(chars.get(activeIndex).notes);
					Utils.divider();
				}
			}
			
			System.out.print("> What would you like to do? ");
			String s = scanner.nextLine().trim();
			input = s.split("\\s+");
			
			s = input[0];
			
			switch (s){
			case "viewalways":
				if (input[1].equals("on")){
					viewAlways=true;
				}
				if (input[1].equals("off")){
					viewAlways=false;
				}
				break;
			case "roll":
				System.out.println("Result: "+roll());
				break;
			case "new":
				createCharacter();
				break;
			case "delete":
				if (charLoaded()){
					deleteCharacter();
				}
				break;
			case "load":
				int index=0;
					if (input.length==1){
						index = getCharIndexFromList();
						if (index==-1){
							break;
						}
					} else {
						index = findCharByName(buildString(1));
						if (index==-1){
							break;
						}
					}
					activeIndex = index;
					System.out.println(String.format("[Loaded %s]", chars.get(activeIndex).playerName));
				break;
			case "save":
				if (charLoaded()){
					checkDirs();
					saveChar(activeIndex);
					exportChar(activeIndex);
				}
				break;
			case "savex":
				if (charLoaded()){
					checkDirs();
					saveChar(activeIndex);
					exportChar(activeIndex);
				}
				break;
			case "saveall":
				if (charLoaded()){
					for (int i = 0; i<chars.size(); i++){
						saveChar(i);
						//exportChar(i);
					}
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
				if (charLoaded()){
					checkDirs();
					exportChar(activeIndex);
				}
				break;
			case "exportall":
				if (charLoaded()){
					checkDirs();
					for (int i = 0; i<chars.size(); i++){
						exportChar(i);
					}
				}
				break;
			case "list":
				dispCharList();
				break;
			case "stat":
			case "stats":
				if (charLoaded()){
					if (input.length == 1){
						dispCharacter();
					} else {
						dispStatDetail(getAttributeByName(buildString(1)));
					}
				}
				break;
			case "skill":
			case "skills":
				if (charLoaded()){
					skills();
				}
				break;
			case "view":
				if (charLoaded()){
					dispCharacter();
					dispInventory();
				}
				break;
			case "inv":
				if (charLoaded()){
					if (input.length == 1){
						dispInventory();
						} else {
							inv(input[1]);
					}
				}
				break;
			case "item":
			case "items":
				if (charLoaded() && input.length>=2){
					item(input[1]);
				}
				break;
			case "equip":
			case "dequip":
				if (charLoaded()){
					equip();
				}
				break;
			case "heal":
				if (charLoaded()){
					if (input.length > 1){
						chars.get(activeIndex).heal(input[1]);
					} else {
						int n = getValidInt("Enter health gained: ");
						chars.get(activeIndex).heal(n);
					}
				}
				break;
			case "hurt":
				if (charLoaded()){
					if (input.length > 1){
						chars.get(activeIndex).hurt(input[1]);
					} else {
						int n = getValidInt("Enter health lost: ");
						chars.get(activeIndex).heal(n);
					}
				}
				break;
			case "set":
				if (charLoaded()){
					set();
				}
				break;
			case "level":
			case "levelup":
				if (charLoaded()){
					String lvl;
					if (input.length == 1){
						lvl = "0";
					} else { 
						lvl = input[1];
					}
					chars.get(activeIndex).levelUp(lvl);
					}
				break;
			case "magic":
			case "spell":
			case "spells":
			case "spellbook":
				if (charLoaded()){
					if (input.length > 1){
					spells(input[1]);
					} else {
					dispSpellbook();
					}
				}
				break;
			case "attack":
				System.out.println("> Coming Soon");
				break;
			case "note":
			case "notes":
				if (charLoaded()){
					notes();
				}
				break;
			case "help":
				dispHelpMenu();
				break;
			case "q":
			case "quit":
				if (charLoaded()){
					for (int i = 0; i<chars.size(); i++){
						saveChar(i);
						//exportChar(i);
					}
				}
				quit = true;
				break;
			default:
				System.out.println("> Command not found - enter \"help\" for list of commands");
				break;
			}
			Utils.divider();
		}
	}

/* DICE ROLLING **************************************************************************************************************/	

	public static Integer roll(){
		int sides=20;
		int num=1;
		int total=0;
		int mod=0;
		String result="";
		if (input.length==1){
			num = getValidInt("Enter number of dice: ");
			sides = getValidInt("Enter number of sides: ");
			mod = getValidInt("Enter any bonuses: ");
		} else if (input.length==2) {
			if (input[1].matches("(\\d+d\\d+)|(\\d+d\\d+[\\+|\\-]\\d+)")){
				String[] a = input[1].split("(d)|(?=[+|-])");
				System.out.println(Arrays.toString(a));
				
				num=Integer.parseInt(a[0]);
				sides=Integer.parseInt(a[1]);
				if (a.length==3){
					mod=Integer.parseInt(a[2]);
				}		
			} else {
				System.out.println("Invalid format");
				return 0;
			}
		} else {
			System.out.println("Invalid format");
			return 0;
		}
		Random random = new Random();
		for (int i=0; i<num; i++){
			int val = random.nextInt(sides)+1;
			result += val;
			if (i<num-1){
				result+=" + ";
			}
			total +=val;
		}
		if (mod!=0){
			System.out.println(String.format("Rolling %dd%d%+d", num,sides,mod));
			System.out.println(String.format("%s (%+d)", result,mod));
		} else {
			System.out.println(String.format("Rolling %dd%d", num,sides));
			if (num>1){System.out.println(result);}
		}
		return total+mod;
	}
	
	public static Integer roll(int num, int sides, int mod){
		DiceRoll roll = new DiceRoll(num, sides);
		int total = roll.roll();
		return total+mod;
	}
	
/* NEW CHARACTER METHODS *****************************************************************************************************/	

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
			a.setBaseVal(getValidInt("Enter "+a.getName()+" score: "));
		}
		System.out.print("Spellcaster? [y/n]: ");
		
		if (scanner.nextLine().equalsIgnoreCase("y")){
			while(true){
				c.caster = true;
				System.out.print("Spell Ability?: ");
				String str = scanner.nextLine().toLowerCase().trim().substring(0, 3);
				
				if (str.equals("wis")){
					c.casterType = Attribute.WIS;
					break;
				} else 
				if (str.equals("int")){
					c.casterType = Attribute.INT;
					break;	
				} else
				if (str.equals("cha")){
					c.casterType = Attribute.CHA;
					break;	
				} else {
					System.out.println("Attribute not found");
				} 
			}
		}
		c.updateSkills();
		c.updateStats();
		chars.add(c);	
		System.out.println("[Created "+c.playerName+"]");
		activeIndex = chars.size()-1;
		return c;
	}
	
/* IO METHODS **************************************************************************************************************/	
	
	public static boolean charLoaded(){
		if (chars.size()>0 && activeIndex >= 0){
			return true;
		} else {
			System.out.println("[Error: No characters to load]");
			return false;
		}
	}
	
	public static void checkDirs() throws IOException, FileNotFoundException{
		Path dataPath = Paths.get("./data/");
		if (!Files.exists(dataPath)){
			Files.createDirectories(dataPath);
		}
	}
	
	public static void importAll() throws ClassNotFoundException, IOException, StreamCorruptedException {
		try {
			File path = new File("./data");

			File [] files = path.listFiles();
			for (int i = 0; i < files.length; i++){
			    File file = files[i];
				if (file.isFile()&&file.getName().endsWith(".data")){
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
	
	public static void importChar() throws ClassNotFoundException, IOException {
		
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
					character = (Character) inStream.readObject();
					inStream.close();
					chars.add(character);
					System.out.println("["+name+" imported]");
					break;
				} catch (FileNotFoundException e) {
					System.out.println("[\""+name+"\""+" not found]");
					break;
				}
			}
	}
	
	public static void saveChar(int i) throws IOException {
			try {
				String dataFile = "./data/"+chars.get(i).playerName+".data";
				ObjectOutputStream out = null;
				out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dataFile)));
				out.writeObject(chars.get(i));
				out.close();
				System.out.println("["+chars.get(i).playerName+" saved]");
			} catch (FileNotFoundException e) {
			}
	}
	
	public static void exportChar(int i) throws IOException {
		
		String text = "";
		if (input.length == 1){
			text = chars.get(i).toExport();
		} else {
			i = findCharByName(buildString(1));
		}
			if (i != -1){
				File file = new File("./data/"+chars.get(i).playerName+".txt");
				try (
					BufferedReader reader = new BufferedReader(new StringReader(text));
					PrintWriter writer = new PrintWriter(new FileWriter(file));
						){
						reader.lines().forEach(line -> writer.println(line));
				}
				System.out.println("[Exported character to "+chars.get(i).playerName+".txt]");
			}
	}
	
	public static void deleteCharacter(){
		System.out.println("[Select character to delete]");
		int del = getCharIndexFromList();
		System.out.print("Are you sure? [Y/N]: ");
		if (scanner.nextLine().equalsIgnoreCase("y")){
			System.out.println(String.format("[Deleted %s]", chars.get(del).playerName));
			chars.remove(del);
			activeIndex=-1;
		}
	}
	
/* FUNCTION METHODS **************************************************************************************************************/
	
	public static void item(String command){
		if (input.length==1){
			
		} else if (input.length>=2){
			switch(command){
				case "view":
					Item viewItem=null;
					String viewName="";
					if (input.length==2){
						viewItem = getItemFromInv();
					} else {
						viewName = buildString(2);
						viewItem=getItemByName(viewName);
					}
					
					System.out.println(viewItem.details());
					break;
				case "edit":
					Item editItem=null;
					String itemName="";
					
					if (input.length==2){
						editItem = getItemFromInv();
					} else {
						itemName = buildString(2);
						editItem=getItemByName(itemName);
					}
					
					if (editItem==null){
						break;
					}
					
					System.out.println("[ITEM EDITOR]"+"\n"+editItem.details());
					
					String opt = "[Edit: Name | Value | Description";
						if (editItem.getClass()==Armor.class){
							opt+=" | Type | AC | Effects]";
						} else if (editItem.getClass()==Weapon.class){
							opt+=" | Ability | Damage | Effects]";
						} else if (editItem.getClass()==Equippable.class){
							opt+=" | Effects]";
						} else {
							opt+="]";
						}
						System.out.println(opt);
						opt=scanner.nextLine().trim();
						if (opt.equalsIgnoreCase("name")){
							System.out.print("Enter new name: ");
							editItem.setItemName(scanner.nextLine().trim());
						}
						if (opt.equalsIgnoreCase("description")|| opt.equalsIgnoreCase("desc")){
							System.out.print("Enter new description: ");
							editItem.description = (scanner.nextLine().trim());
						}
						if (opt.equalsIgnoreCase("value")){
							editItem.setItemCount(getValidInt("Enter new value: "));
						}
						if (opt.equalsIgnoreCase("type") && editItem.getClass()==Armor.class){
							System.out.println("[None | Light | Medium | Heavy | Shield | Other]");
							Armor editArmor = (Armor) editItem;
							String choice = scanner.nextLine().trim().toLowerCase();
							switch(choice){
							case "none":
								editArmor.armorWeight = Armor.none;
								break;
							case "l":
							case "light":
								editArmor.armorWeight = Armor.light;
								break;
							case "m":
							case "medium":
								editArmor.armorWeight = Armor.medium;
								break;
							case "h":
							case "heavy":
								editArmor.armorWeight = Armor.heavy; 
								break;
							case "shield":
								editArmor.armorWeight = Armor.shield;
								break;
							case "other":
								editArmor.armorWeight = Armor.other; 
								break;
							case "cancel":
								break;
							}
						}
						if (opt.equalsIgnoreCase("ac")&& editItem.getClass()==Armor.class){
							Armor editArmor = (Armor) editItem;
							editArmor.acBase = getValidInt("Enter new base AC: ");
						}
						if (opt.equalsIgnoreCase("ability")&& editItem.getClass()==Weapon.class){
							Weapon editWeapon = (Weapon) editItem;
							System.out.print("Weapon Ability?: ");
							opt = scanner.nextLine().toLowerCase().trim().substring(0, 3);
							if (opt.equals("str")){
								editWeapon.attackAbility = Attribute.STR;
								break;
							} else 
							if (opt.equals("dex")){
								editWeapon.attackAbility = Attribute.DEX;
								break;	
							}  else {
								System.out.println("Attribute not found");
							} 
						}
						if (opt.equalsIgnoreCase("damage")&& editItem.getClass()==Weapon.class){
							Weapon editWeapon = (Weapon) editItem;
							int num = getValidInt("Enter number of dice: ");
							int sides = getValidInt("Enter number of sides: ");
							editWeapon.damageRoll = new DiceRoll(num, sides);
						}

						if (opt.equalsIgnoreCase("effects")&& (editItem.getClass()==Armor.class || editItem.getClass()==Weapon.class || editItem.getClass()==Equippable.class)){

							System.out.println("[Add | Remove | Edit]");
							opt = scanner.nextLine().trim().toLowerCase();

							if (opt.equals("add")){
								System.out.println("Enter ability: ");
								int abil = Utils.getAbilIndexByName(scanner.nextLine());
								int mod = getValidInt("Enter bonus: ");
								if (editItem.getClass()==Armor.class){
									Armor edit = (Armor) editItem; 
									edit.attributeTargets.add(abil);
									edit.attributeBonuses.add(mod);
								} else if (editItem.getClass()==Weapon.class){
									Weapon edit = (Weapon) editItem; 
									edit.attributeTargets.add(abil);
									edit.attributeBonuses.add(mod);
								} else if (editItem.getClass()==Equippable.class){
									Equippable edit = (Equippable) editItem;
									edit.attributeTargets.add(abil);
									edit.attributeBonuses.add(mod);
								}
								
							}if (opt.equals("remove")){
								int effect = getValidInt("Enter Number of effect to remove: ")-1;
								if (editItem.getClass()==Armor.class){
									Armor edit = (Armor) editItem; 
									edit.attributeBonuses.remove(effect);
									edit.attributeTargets.remove(effect);
								} else if (editItem.getClass()==Weapon.class){
									Weapon edit = (Weapon) editItem; 
									edit.attributeBonuses.remove(effect);
									edit.attributeTargets.remove(effect);
								} else if (editItem.getClass()==Equippable.class){
									Equippable edit = (Equippable) editItem; 
									edit.attributeBonuses.remove(effect);
									edit.attributeTargets.remove(effect);
								}
								
							}if (opt.equals("edit")){
								int effect = getValidInt("Enter Number of effect to edit: ")-1;
								System.out.println("Enter new ability: ");
								int abil = Utils.getAbilIndexByName(scanner.nextLine());
								int mod = getValidInt("Enter new bonus: ");
								if (editItem.getClass()==Armor.class){
									Armor edit = (Armor) editItem; 
									edit.attributeTargets.set(effect, abil);
									edit.attributeBonuses.set(effect, mod);
								} else if (editItem.getClass()==Weapon.class){
									Weapon edit = (Weapon) editItem; 
									edit.attributeTargets.set(effect, abil);
									edit.attributeBonuses.set(effect, mod);
								} else if (editItem.getClass()==Equippable.class){
									Equippable edit = (Equippable) editItem; 
									edit.attributeTargets.set(effect, abil);
									edit.attributeBonuses.set(effect, mod);
								}
							}
						}
						System.out.println("[Done]");
					break;
				default:
					break;
			}
		} else {
			
		}
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
					s.proficient = true;
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
		String spellName="";
		int spellLevel = -1;
		int lvl;
		int num;
		
		
		switch (command){
		case "learn":
			if (input.length==2){
				System.out.print("Enter spell name: ");
				spellName = scanner.nextLine();
				spellLevel = getValidInt("Enter spell level: ");
				chars.get(activeIndex).learnSpell(new Spell(spellLevel, spellName));
				break;
			} else {
				spellLevel = getSpellLevel(input[input.length-1]);
				if (spellLevel == -1 || spellLevel == -2){
					System.out.println("Missing argument: Spell Level");
					break;
				}
				spellName = buildString(2);			
				chars.get(activeIndex).learnSpell(new Spell(spellLevel, spellName));
				break;
			}
		case "forget":
			Spell forgetSpell = null;
			if (input.length==2){
				dispSpellbook();
				System.out.print("Enter spell to forget: ");
				spellName = scanner.nextLine();
				forgetSpell = getSpellByName(spellName);
				chars.get(activeIndex).forgetSpell(forgetSpell);
				break;
			} else {
				forgetSpell = getSpellByName(buildString(2));
				chars.get(activeIndex).forgetSpell(forgetSpell);
				break;
			}
		case "cast":
			Spell castSpell=null;
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
				System.out.print("Enter spell to cast: ");
				spellName = scanner.nextLine();
				spellLevel = getValidInt("Enter spell level: ");
				castSpell = getSpellByName(spellName);
				chars.get(activeIndex).castSpell(castSpell, spellLevel);
				break;
			}
			break;
		case "charge":
			
			if (input.length>=4){
				if (!validIntInput(2) || !validIntInput(3)){break;}
				lvl = Integer.parseInt(input[2]);
				num = Integer.parseInt(input[3]);
				chars.get(activeIndex).chargeSpell(lvl, num);
			} else if (input.length==2){
				lvl = getValidInt("Enter spell slot level: ");
				num = getValidInt("Enter number of slots to recharge: ");
			} else {
				System.out.println("[Invalid command format]");
			}
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
			if (input.length == 5 && input[2].equalsIgnoreCase("get")){
				if (!validIntInput(3) || !validIntInput(4)){
					System.out.println("[Invalid command format]");
					break;
				}
				lvl = Integer.parseInt(input[4]);
				num = Integer.parseInt(input[3]);
				chars.get(activeIndex).spellSlots[lvl-1].total+=num;
				System.out.println("[Gained "+num+" level "+lvl+" spell slots]");
			} else if (input[2].equalsIgnoreCase("get") && input.length==3){
				lvl = getValidInt("Enter spell slot level: ");
				num = getValidInt("Enter number of slots to gain: ");
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
					a.setBaseVal(val);
					System.out.println("["+a.getName()+" set to "+val+"]");
				} catch (NumberFormatException e) {
					System.out.println("Invalid Command Format");
				}
			}
		} else if (input.length == 2) {
					
			//special case toggles --- temporary
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
			a.setBaseVal(value);
			System.out.println("["+a.getName()+" set to "+value+"]");
		
		}
		chars.get(activeIndex).updateSkills();
		chars.get(activeIndex).updateStats();
	}
	
	public static void inv(String command){
		int count=0;
		String name="";
		String type="";
		
		switch(command){
			case "get":
				if (input.length==2){
					System.out.println("[Get new items]");
					System.out.print("Item | Weapon | Armor | Equippable: ");
					type = scanner.nextLine();
					
					if (type.equalsIgnoreCase("item") || type.equalsIgnoreCase("weapon") || type.equalsIgnoreCase("armor")|| type.equalsIgnoreCase("equip") || type.equalsIgnoreCase("equippable")){
						System.out.print("Enter item name: ");
						name = scanner.nextLine();
						count = getValidInt("Enter count: ");
					} else {
						break;
					}
				} 
				if (input.length==3){

					break;
				} 
				if (input.length>=4){
					type = input[2].toLowerCase();
					name = buildString(3);
					count = getSpecialInt(input[input.length-1]);
					if (count==0){break;}
				}
				switch (type){
					case "item":
						chars.get(activeIndex).addNewItem(new Item(name), count);
						break;
					case "weapon":
						chars.get(activeIndex).addNewItem(new Weapon(name), count);
						break;
					case "armor":
						chars.get(activeIndex).addNewItem(new Armor(name), count);
						break;
					case "equip":
					case "equippable":
						chars.get(activeIndex).addNewItem(new Equippable(name), count);
						break;
					default:
						System.out.println("> Command not recognized. Enter \"inv help\" for help");
						break;
				}
				break;
			case "add":
			case "drop":
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
				if (index!=-1){
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
				}
				break;
			case "sort":
				Collections.sort(chars.get(activeIndex).playerInventory.subList(3,chars.get(activeIndex).playerInventory.size()));
				System.out.println("[Inventory  sorted]");
				break;
			case "help":
				System.out.println("help");
				break;
			default:
				System.out.println("> Command not recognized. Enter \"inv help\" for help");
				break;
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
	
/* UTILITY METHODS **************************************************************************************************************/
	
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
		if(charLoaded()){
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
		return n-1;
		} 
		return -1;
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
	
/* DISPLAY METHODS **************************************************************************************************************/
	
	public static void dispCharList(){
		System.out.println("[Characters]");
		int i=1;
		for (Character c : chars){
			System.out.println(String.format("[%d] - %s", i, c.playerName));
			i++;
		}
	}
	
	public static void dispStatDetail(Attribute a){
		System.out.println(a.detail());
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
		System.out.println("[heal <#>][hurt <#>][level <#>][levelup][skills <arguments>][notes <arguments>][roll <#d#>]");
		System.out.println("View COMMANDS.txt for details");
	}
}
