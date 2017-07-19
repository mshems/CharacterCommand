package app;

import character.*;
import items.*;
import magic.*;
import utils.*;
import java.util.*;

//@SuppressWarnings("unused")
public class App {
	/**
	 * version 0.2.4
	 */
	public static final long VERSION = 203L;
	private static final String version = "CharacterCommand v0.2.4";
	//private static String newLine = System.lineSeparator();
	static boolean QUIT_ALL = false;
    static PlayerCharacter activeChar;
	static LinkedHashMap<String, PlayerCharacter> characterList;
	static Scanner scanner;
	static String[] input;
    static LinkedList<String> tokens;
	static PropertiesHandler propertiesHandler;
    static IOHandler ioHandler;
    static CommandHandler commandHandler;

	public static void main(String[] args){
	    initApp();

	    if(args.length>0){
			switch(args[0]){
				case "--test":
					makeTestCharacter();
					break;
				default:
					break;
			}
		}

        String prompt;

		while(!QUIT_ALL){
		    if (activeChar != null){
				prompt = "CharacterCommand @ "+activeChar.getName()+"> ";

				if(propertiesHandler.isViewAlways()){
					System.out.println(activeChar);
				}
			} else {
                prompt = "CharacterCommand> ";
            }

            getCommand(prompt);
			commandHandler.doCommand(tokens.peek(), activeChar);

		}

		if(activeChar==null){
			propertiesHandler.setLast("");
		}else{
			propertiesHandler.setLast(activeChar.getName().toLowerCase());
		}
		propertiesHandler.writeProperties();
	}

/**GETTING COMMAND*****************************************************************************************************/
	private static void getCommand(String prompt){
        System.out.print(prompt);
        input = scanner.nextLine()
                .trim()
                .split("\\s+");
        Collections.addAll(tokens, input);
    }

/**INITIALIZATION******************************************************************************************************/
    private static void initApp() {
		System.out.println("---"+version+"---");
		propertiesHandler = new PropertiesHandler();
        commandHandler = new CommandHandler();
		ioHandler = new IOHandler();
        IOHandler.checkDirs();
        tokens = new LinkedList<>();
        scanner = new Scanner(System.in);
        characterList = new LinkedHashMap<>();
        ioHandler.importAll(false);
        if(propertiesHandler.isResume()){
        	String key = propertiesHandler.getLast();
			if(characterList.containsKey(key)){
        		activeChar = characterList.get(key);
			}
		}
	}

/**CREATE CHARACTER****************************************************************************************************/
	static void createCharacter(){
        String name;
	    while(true) {
            System.out.print("Character name: ");
            name = scanner.nextLine().trim();
            if(isValidName(name)){
                break;
            }
	    }
		System.out.print("Race: ");
        String raceName = scanner.nextLine();
		System.out.print("Class: ");
		String className = scanner.nextLine();
		PlayerCharacter c = new PlayerCharacter(name, raceName, className);
		for(String key:c.getAbilities().keySet()){
			Ability a = c.getAbilities().get(key);
			a.setBaseVal(getValidInt("Enter "+a.getName()+" score: "));
		}
		if(getYN("Spellcaster? ")){
			Ability spellAbility = null;
			while (spellAbility == null) {
				System.out.print("Spellcasting ability: ");
				String abilityName = scanner.nextLine();
				spellAbility = c.getAbilities().get(abilityName);
				if (spellAbility == null){
					System.out.println("ERROR: Ability not found");
				} else {
					c.setSpellcaster(true);
					c.initMagicStats(spellAbility);
				}
			}
		} else {
			c.setSpellcaster(false);
		}
		c.updateStats();
		characterList.put(c.getName().toLowerCase(), c);
		System.out.println("Created "+c.getName());
		activeChar = c;
	}

/**STATS***************************************************************************************************************/
	static void stats(){
		tokens.pop();
		if(!tokens.isEmpty()){
			statsParser();
		} else {
			System.out.println("view | edit | cancel");
			boolean exit = false;
			while(!exit) {
                System.out.print("Action: ");
                String action = scanner.nextLine().toLowerCase().trim();
				switch (action){
					case "v":
					case "view":
						Stat stat = getStatByName();
						if (stat != null){
							System.out.println(stat.detailString());
						}
						exit = true;
						break;
					case "e":
					case "edit":
						edit();
						exit = true;
						break;
					case "cancel":
						exit = true;
						break;
				}
			}
		}
	}

	private static void statsParser(){
		StringBuilder nameBuilder = new StringBuilder();
		boolean help = false;
		boolean view = true;
		while(!tokens.isEmpty()){
			switch(tokens.peek()){
				case "-e":
				case "--edit":
					edit();
					view = false;
					//tokens.pop();
					break;
				case "--help":
					tokens.pop();
					help = true;
					break;
				default:
					if (tokens.peek().startsWith("-")){
						System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
					} else {
						nameBuilder.append(tokens.pop());
						nameBuilder.append(" ");
					}
					break;
			}
		}
		if(help){
			System.out.println(Help.STATS);
		} else {
			String statName = nameBuilder.toString().trim();
			Stat stat = activeChar.getStat(statName);
			if (stat != null && view){
				System.out.println(stat.detailString());
			}
		}
	}

/**EDIT***************************************************************************************************************/
	static void edit(){
		tokens.pop();
		if (!tokens.isEmpty()){
			editParser();
		} else {
			Stat stat = getStatByName();
			if(stat!=null){
				int val = getValidInt(stat.getName() + " value: ");
				stat.setBaseVal(val);
				activeChar.updateStats();
				System.out.println("Updated "+stat.getName());
			}
		}
	}

	private static void editParser(){
		StringBuilder nameBuilder = new StringBuilder();
		Integer bonus = null;
		Integer value = null;
		boolean help = false;
		while (!tokens.isEmpty()){
			switch (tokens.peek()){
				case "-v":
				case "--value":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": stat value");
					} else {
						value = getIntToken();
					}
					break;
				case "-b":
				case "--bonus":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": bonus");
					} else {
						bonus = getIntToken();
					}
					break;
				case "--help":
					help = true;
					break;
				default:
					if (tokens.peek().startsWith("-")){
						System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
					} else {
						nameBuilder.append(tokens.pop());
						nameBuilder.append(" ");
					}
					break;
			}
		}
		if(help){
			System.out.println(Help.EDIT);
		} else {
			String statName = nameBuilder.toString().trim();
			Stat stat = activeChar.getStat(statName);
			if(value!=null){
				if(stat!=null){
					if(bonus!=null){
						stat.setBonusVal(bonus);
						System.out.println("Updated "+stat.getName());
					}
						stat.setBaseVal(value);
						activeChar.updateStats();
						System.out.println("Updated "+stat.getName());
				} else {
					System.out.println(Message.MSG_NO_STAT);
				}
			} else {
				System.out.println(Message.ERROR_INPUT);
			}
		}
	}

/**AP******************************************************************************************************************/
	static void abilityPoints(){
		tokens.pop();
		if (!tokens.isEmpty()){
			abilityPointsParser();
		} else {
			System.out.println("use | get | set");
			System.out.print("Action: ");
			String action = scanner.nextLine().toLowerCase().trim();
			boolean exit = false;
			int amount;
			while(!exit) {
				switch (action){
					case "u":
					case "use":
						amount = getValidInt("Ability Points to use: ");
						((CounterStat) activeChar.getStat("ap")).countDown(amount);
						System.out.println("Used "+amount+" ability points");
						exit = true;
						break;
					case "g":
					case "get":
						amount = getValidInt("Ability Points gained: ");
						((CounterStat) activeChar.getStat("ap")).countUp(amount);
						System.out.println("Gained "+amount+" ability points");
						exit = true;
						break;
					case "s":
					case "set":
						amount = getValidInt("Ability Points maximum: ");
						((CounterStat) activeChar.getStat("ap")).setMaxVal(amount);
						System.out.println("Ability Point maximum now "+amount);
						exit = true;
						break;
					case "cancel":
						exit = true;
						break;
				}
			}
		}
	}

	private static void abilityPointsParser(){
		boolean use = false;
		boolean get = false;
		boolean set = false;
		boolean help = false;
		boolean all = false;
		Integer count = 1;
		while(!tokens.isEmpty()){
			switch (tokens.peek()){
				case "-u":
				case "--use":
					tokens.pop();
					use = true;
					get = false;
					set = false;
					break;
				case "-g":
				case "--get":
					tokens.pop();
					get = true;
					use = false;
					set = false;
					break;
				case "-s":
				case "--set":
					tokens.pop();
					set = true;
					get = false;
					use = false;
					break;
				case "-c":
				case "--count":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": level");
					} else {
						count = getIntToken();
					}
					break;
				case "--all":
					tokens.pop();
					all = true;
					break;
				case "--help":
					tokens.pop();
					help = true;
					break;
				default:
					if (tokens.peek().startsWith("-")){
						System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
					}
					break;
			}
		}
		if(help){
			System.out.println(Help.AP);
		} else {
			if (count != null){
				CounterStat ap  = ((CounterStat) activeChar.getStat("ap"));
				if (use){
					if(all){
						ap.setCurrVal(0);
						System.out.println("Used all ability points");
					} else {
						ap.countDown(count);
						System.out.println("Used " + count + " ability points");
					}
				} else if (get){
					if(all){
						ap.setCurrVal(ap.getMaxVal());
						System.out.println("Gained all ability points");
					} else {
						ap.countUp(count);
						System.out.println("Gained " + count + " ability points");
					}
				} else if (set){
					ap.setMaxVal(count);
					System.out.println("Ability Point maximum now "+count);
				} else {
					System.out.println(Message.ERROR_SYNTAX);
				}
			}
		}
	}

/**SKILLS**************************************************************************************************************/
    static void skills(){
		tokens.pop();
		String action;
		if (!tokens.isEmpty()){
			skillsParser();
		} else {
			Skill skill;
			boolean exit = false;
			while(!exit){
				System.out.println("view | train | forget | expert | view all");
				System.out.print("Action: ");
				action = scanner.nextLine().trim().toLowerCase();
				switch(action){
					case "v":
					case "view":
						skill = getSkillByName();
						if(skill!=null){
							System.out.println(skill);
						}
						exit=true;
						break;
					case "t":
					case "train":
						skill = getSkillByName();
						if(skill!=null){
							skill.train(activeChar);
							System.out.println("Gained proficiency in "+skill.getName());
						}
						exit=true;
						break;
					case "f":
					case "forget":
						skill = getSkillByName();
						if(skill!=null){
							skill.untrain(activeChar);
							System.out.println("Lost proficiency in "+skill.getName());
						}
						exit=true;
						break;
					case "e":
					case "expert":
						skill = getSkillByName();
						if(skill!=null){
							skill.expert(activeChar);
							System.out.println("Gained expertise in "+skill.getName());
						}
						exit=true;
						break;
					case "va":
					case "viewall":
					case "view all":
						System.out.println(activeChar.skillsToString());
						exit=true;
						break;
					case "cancel":
						exit=true;
						break;
					default:
						System.out.println(Message.ERROR_SYNTAX);
						System.out.println("Enter 'cancel' to exit");
						exit = false;
						break;
					}
			}
		}
	}

	private static void skillsParser(){
		StringBuilder nameBuilder = new StringBuilder();
    	Skill skill;
		boolean expert = false;
		boolean forget = false;
		boolean train = false;
		boolean view = false;
		boolean viewAll = false;
		boolean help = false;
		
		while(!tokens.isEmpty()){
			switch(tokens.peek()){
			case "-e":
			case "--expert":
				expert = true;
				tokens.pop();
				break;
			case "-t":
			case "--train":
				train = true;
				tokens.pop();
				break;
			case "-f":
			case "--forget":
				forget = true;
				tokens.pop();
				break;
			case "-v":
			case "--view":
				view = true;
				tokens.pop();
				break;
			case "-va":
			case "--viewall":
				tokens.pop();
				viewAll =true;
				break;
			case "--help":
				tokens.pop();
				help = true;
				break;
			default:
				if (tokens.peek().startsWith("-")){
					System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
				} else {
					nameBuilder.append(tokens.pop());
					nameBuilder.append(" ");
				}
				break;
			}
		}
		if(help){
            System.out.println(Help.SKILL);
		} else {
			String skillName = nameBuilder.toString().trim();
			skill = activeChar.getSkill(skillName);
			if (viewAll){
                System.out.println(activeChar.skillsToString());
            }
			if(skill!=null){
				if (!forget){
					if (expert){
						skill.expert(activeChar);
						System.out.println("Gained expertise in "+skill.getName());
					} else if (train){
						skill.train(activeChar);
						System.out.println("Gained proficiency in "+skill.getName());
					}
				} else {
					skill.untrain(activeChar);
					System.out.println("Lost proficiency in "+skill.getName());
				}
				if (view){
					System.out.println(skill);
				}
			} else {
				if (skillName.equals("") && !viewAll){
					System.out.println("ERROR: Missing argument: skill name");
				} else {
					if (!viewAll){
						System.out.println(Message.ERROR_SYNTAX);
					}
				}
			}
		}
	}
	
/**LEVELUP*************************************************************************************************************/
	static void levelUp(){
		tokens.pop();
		boolean help = false;
		if (tokens.isEmpty()){
			activeChar.levelUp();
			System.out.println(String.format("%s is now level %.0f", activeChar.getName(), activeChar.getLevel().getBaseVal()));
		} else {
			Integer level = null;
			
			while(!tokens.isEmpty()){
				switch(tokens.peek()){
				case "-l":
				case "--level":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": level");
					} else {
						level = getIntToken();
					}
					break;
				case "--help":
					tokens.pop();
					help = true;
					break;
				default:
					if (tokens.peek().startsWith("-")){
						System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
					} else {
						tokens.pop();
					}
					break;
				}
			}
			if (!help){
				if (level!=null){
					activeChar.levelUp(level);
					System.out.println(String.format("%s is now level %.0f", activeChar.getName(), activeChar.getLevel().getBaseVal()));
				} else {
					System.out.println("ERROR: Invalid input");
				}
			} else {
                System.out.println(Help.LEVELUP);
			}
		}
	}

/**SPELL SLOTS*********************************************************************************************************/
	static void spellSlots(){
		tokens.pop();
		if(!tokens.isEmpty()){
			switch (tokens.peek()){
				case "--charge":
					charge();
					break;
				case "--get":
				case "--set":
					setSlots();
					break;
				case "--help":
					System.out.println(Help.SPELLSLOTS);
					break;
				default:
					System.out.println(Message.ERROR_INPUT);
					break;
			}
		} else {
			System.out.println(activeChar.spellSlotsToString());
		}
	}

	private static void setSlots(){
        tokens.pop();
        if(!tokens.isEmpty()){
			setSlotsParser();
		} else {
			int level = getValidInt("Enter spell slot level: ");
			int max = getValidInt("Enter new spell slot maximum: ");
			activeChar.getSpellSlots()[level].setMaxVal(max);
			System.out.println("Maximum level "+level+" spell slots set to "+max);
		}
	}

	private static void setSlotsParser(){
		Integer level = null;
		Integer max = null;
		boolean help = false;
		while (!tokens.isEmpty()) {
			switch (tokens.peek()){
				case "-l":
				case "--level":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": level");
					} else {
						level = getIntToken();
						if(level > Spell.MAX_LEVEL){
							level =  Spell.MAX_LEVEL;
						}
						if(level <  Spell.CANTRIP){
							level =  Spell.CANTRIP;
						}
					}
					break;
				case "-m":
				case "--max":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": count");
					} else {
						max = getIntToken();
					}
					break;
				case "--help":
					tokens.pop();
					help=true;
					break;
				default:
					if (tokens.peek().startsWith("-")){
						System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
					} else {
						tokens.pop();
					}
					break;
			}
		}
		if(help){
			System.out.println(Help.SETSLOTS);
		} else {
			if (level != null && max != null){
				activeChar.getSpellSlots()[level].setMaxVal(max);
				System.out.println("Maximum level "+level+" spell slots set to "+max);
			}
		}
	}


	static void charge(){
		tokens.pop();
		if(!tokens.isEmpty()){
			chargeParser();
		} else {
			int level = getValidInt("Enter spell slot level: ");
			int count = getValidInt("Enter number of slots to recharge: ");
			System.out.println("Recharged "+count+" level "+level+" spell slots");
		}
	}

/**CHARGE**************************************************************************************************************/
	private static void chargeParser(){
		boolean all=false;
		boolean help=false;
		Integer level=null;
		Integer count=null;
		while (!tokens.isEmpty()){
			switch(tokens.peek()){
				case "-l":
				case "--level":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": level");
					} else {
						level = getIntToken();
						if(level > Spell.MAX_LEVEL){
							level =  Spell.MAX_LEVEL;
						}
						if(level <  Spell.CANTRIP){
							level =  Spell.CANTRIP;
						}
					}
					break;
				case "-c":
				case "--count":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": count");
					} else {
						count = getIntToken();
					}
					break;
				case "--all":
					tokens.pop();
					all=true;
					break;
				case "--help":
					tokens.pop();
					help=true;
					break;
				default:
					if (tokens.peek().startsWith("-")){
						System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
					} else {
						tokens.pop();
					}
					break;
			}
		}
		if(help){
			System.out.println(Help.CHARGE);
		} else {
			if(level==null && count==null){
				if(all){
					for(SpellSlot s:activeChar.getSpellSlots()){
						if(s.getMaxVal()>0){
							s.fullCharge();
						}
					}
					System.out.println("All spell slots recharged");
				} else {
					System.out.println(Message.ERROR_NO_VALUE);
				}
			} else
			if (level!=null && count==null){
				if(all){
					activeChar.getSpellSlots()[level].fullCharge();
					System.out.println("Level "+level+" spell slots fully recharged");
				} else {
					activeChar.getSpellSlots()[level].charge();
					System.out.println("Level "+level+" spell slot recharged");
				}
			}
			if(level==null && count!=null){
				if(all){
					for(SpellSlot s:activeChar.getSpellSlots()){
						if(s.getMaxVal()>0){
							s.charge(count);
						}
					}
					System.out.println("Recharged "+count+" spell slots of each level known");
				} else {
					System.out.println(Message.ERROR_NO_ARG+": level");
				}
			} else
			if(level!=null && count!=null){
				activeChar.getSpellSlots()[level].charge(count);
				System.out.println("Recharged "+count+" level "+level+" spell slots");
			}
		}
	}


/**SPELLS**************************************************************************************************************/
	static void spells(){
		tokens.pop();
		if(!tokens.isEmpty()){
			switch(tokens.peek()){
				case "--learn":
					learn();
					break;
				case "--forget":
					forget();
					break;
				case "--cast":
					cast();
					break;
				case "--stats":
					System.out.println(activeChar.spellStatsToString());
					break;
				case "--slots":
					spellSlots();
					break;
				case "--help":
					System.out.println(Help.SPELLS);
					break;
				default:
					System.out.println("Error: command syntax");
					break;
			}
		} else {
			System.out.println(activeChar.getSpellBook());
		}
	}

/**LEARN***************************************************************************************************************/
    static void learn(){
        tokens.pop();
        if(!tokens.isEmpty()){
			learnParser();
		} else {
			String spellName;
			int spellLevel;
			System.out.print("Spell name: ");
			spellName = scanner.nextLine().trim();
			spellLevel = getValidInt("Spell level: ");
			learnSpell(new Spell(spellName, spellLevel));
		}
    }

	private static void learnParser(){
		StringBuilder nameBuilder = new StringBuilder();
		Integer spellLevel = null;
		boolean learnmagic=false;
		boolean help=false;
		while(!tokens.isEmpty()){
			switch(tokens.peek()){
				case "-l":
				case "--level":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": level");
						spellLevel = null;
					} else {
						spellLevel = getIntToken();
					}
					break;
				case "-m":
				case "--magic":
					tokens.pop();
					learnmagic = true;
					break;
				case "--help":
					tokens.pop();
					help = true;
					break;
				default:
					if (tokens.peek().startsWith("-")){
						System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
					}else {
						nameBuilder.append(tokens.pop());
						nameBuilder.append(" ");
					}
					break;
			}
		}
		if(learnmagic){
			learnMagic();
		}
		if(activeChar.isSpellcaster()){
			if (spellLevel == null){
				spellLevel = Spell.CANTRIP;    //default level
			}
			if (help){
				System.out.println(Help.LEARN);
			} else {
				String spellName = nameBuilder.toString().trim();
				if (!spellName.isEmpty()){
					Spell spell = new Spell(spellName, spellLevel);
					learnSpell(spell);
				}
			}
		} else {
			System.out.println(Message.MSG_NOT_CAST);
		}
	}

	private static void learnMagic(){
		Ability spellAbility = null;
		while (spellAbility == null) {
			System.out.print("Spellcasting ability: ");
			String abilityName = scanner.nextLine();
			if (abilityName.equalsIgnoreCase("cancel")){
				break;
			} else {
				spellAbility = activeChar.getAbilities().get(abilityName);
				if (spellAbility == null){
					System.out.println("ERROR: Ability not found");
				} else {
					activeChar.setSpellcaster(true);
					activeChar.initMagicStats(spellAbility);
				}
			}
		}
	}

	private static void learnSpell(Spell spell){
		activeChar.getSpellBook().learn(spell);
		if(spell.isCantrip()){
			System.out.println("Learned cantrip "+spell.getSpellName());
		} else {
			System.out.println("Learned level "+spell.getSpellLevel()+" spell "+spell.getSpellName());
		}
	}

/**FORGET**************************************************************************************************************/
	static void forget(){
        tokens.pop();
        if(!tokens.isEmpty()){
			forgetParser();
		} else {
			Spell spell = getSpellByName();
			if(spell!=null){
				forgetSpell(spell);
			}
		}
	}

	private static void forgetParser(){
		StringBuilder nameBuilder = new StringBuilder();
		boolean help=false;
		while(!tokens.isEmpty()){
			switch(tokens.peek()){
				case "--help":
					tokens.pop();
					help = true;
					break;
				default:
					if (tokens.peek().startsWith("-")){
						System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
					}else {
						nameBuilder.append(tokens.pop());
						nameBuilder.append(" ");
					}
					break;
			}
		}
		if(help){
			System.out.println(Help.FORGET);
		} else {
			Spell spell = activeChar.getSpell(nameBuilder.toString().trim());
			if(spell!=null){
				forgetSpell(spell);
			} else {
				System.out.println(Message.MSG_NO_SPELL);
			}
		}
	}

	private static void forgetSpell(Spell spell){
		activeChar.getSpellBook().forget(spell);
		if(spell.isCantrip()){
			System.out.println("Forgot cantrip "+spell.getSpellName());
		} else {
			System.out.println("Forgot level "+spell.getSpellLevel()+" spell "+spell.getSpellName());
		}
	}

/**CAST****************************************************************************************************************/
	static void cast(){
        tokens.pop();
        if (!tokens.isEmpty()){
			castParser();
		} else {
			Integer castLevel = -1;
			Spell spell = getSpellByName();
			if(spell!=null){
				if (!spell.isCantrip()){
					castLevel = getValidInt("Cast at level: ");
				}
				castSpell(spell, "spellName", castLevel);
			}
		}
	}

	private static void castParser(){
		Spell spell;
		StringBuilder nameBuilder = new StringBuilder();
		Integer castLevel = -1;
		boolean help = false;
		while (!tokens.isEmpty()){
			switch(tokens.peek()){
			case "-l":
			case "--level":
				tokens.pop();
				if (tokens.isEmpty()){
					System.out.println(Message.ERROR_NO_ARG+": level");
					castLevel = null;
				} else {
					castLevel = getIntToken();
				}
				break;
			case "--help":
				help = true;
				tokens.pop();
				break;
			default:
				if (tokens.peek().startsWith("-")){
					System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
				}else {
					nameBuilder.append(tokens.pop());
					nameBuilder.append(" ");
				}
			}
		}
		if(help){
            System.out.println(Help.CAST);
		} else {
			String spellName = nameBuilder.toString().trim();
			spell = activeChar.getSpell(spellName);
			castSpell(spell, spellName, castLevel);
		}
	}

	private static void castSpell(Spell spell, String spellName, Integer castLevel){
		if(spell!=null && castLevel!=null){
			if (castLevel == -1){
				castLevel = spell.getSpellLevel();
			}
			castLevel = activeChar.cast(spell, castLevel);
			if (spell.isCantrip()){
				System.out.println("Cast '"+spell.getSpellName()+"' as a cantrip");
			} else {
				if (castLevel < 0){
					System.out.println("No level "+(-castLevel)+" spell slots remaining");
				} else {
					System.out.println("Cast '"+spell.getSpellName()+"' at level "+castLevel);
				}
			}
		} else {
			if (spellName.equals("")){
				System.out.println("ERROR: Missing argument: spell name");
			} else if (spell == null){
				System.out.println("No spell by that name");
			}
		}
	}
	
/**HEAL/HURT***********************************************************************************************************/
	static void heal(){
		String command = tokens.pop();
		if (!tokens.isEmpty()){
			heal(command);
		} else {
			Integer amount;
			if(command.equals("heal")){
				amount = getValidInt("HP gained: ");
			} else {
				amount = getValidInt("HP lost: ");
			}
			heal(command, amount);
		}
	}

	private static void heal(String command){
		Integer amount=null;
		boolean healAll=false;
		boolean help = false;
		while(!tokens.isEmpty()){
			switch (tokens.peek()){
			case "-hp":
			case "--health":
				tokens.pop();
				if (tokens.isEmpty()){
					System.out.println(Message.ERROR_NO_ARG+": amount");
				} else {
					amount = getIntToken();
				}
				break;
			case "--all":
				tokens.pop();
				healAll=true;
				break;
			case "--help":
				tokens.pop();
				help = true;
				break;
			default:
				if (tokens.peek().startsWith("-")){
					System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
				} else {
					tokens.pop();
				}
				break;
			}
		}
		if (help){
			if(command.equals("heal")){
				System.out.println(Help.HEAL);
			}
			if(command.equals("hurt")){
				System.out.println(Help.HURT);
			}
		} else if (healAll){
			healAll(command);
		} else if(amount!=null){
			heal(command, amount);
		} else {
			System.out.println(Message.ERROR_SYNTAX);
		}
	}

	private static void heal(String command, int amount){
		switch (command){
		case "heal":
				activeChar.heal(amount);
				System.out.println(String.format("Gained %d HP", amount));
			break;
		case "hurt":
				activeChar.hurt(amount);
				System.out.println(String.format("Lost %d HP", amount));
			break;
		default:
			break;
		}
	}
	private static void healAll(String command){
		switch (command){
			case "heal":
					activeChar.fullHeal();
					System.out.println("HP fully restored");
				break;
			case "hurt":
					activeChar.fullHurt();
					System.out.println("No HP remaining");
				break;
			default:
				break;
		}
	}

/**USE*****************************************************************************************************************/
	static void use(){
        tokens.pop();
        if(!tokens.isEmpty()){
			useParser();
		} else {
			Item item = getItemByName();
			if(item != null){
				if (item.isConsumable()){
					int amount = getValidInt("Amount: ");
					item.use(amount);
					System.out.println("Used " + amount + "x " + item.getName());
					if (item.getCount() <= 0){
						activeChar.removeItem(item);
					}
				} else {
					System.out.println(Message.ERROR_NOT_CON);
				}
			}
		}
	}

	private static void useParser(){
		Item item;
		StringBuilder nameBuilder = new StringBuilder();
		Integer amount = 1; //default
		boolean help = false;
		while(!tokens.isEmpty()){
			switch(tokens.peek()){
				case "-c":
				case "--count":
					tokens.pop();
					if (tokens.isEmpty()){
						System.out.println(Message.ERROR_NO_ARG+": amount");
					} else {
						amount = getIntToken();
					}
					break;
				case "--help":
					help = true;
					tokens.pop();
					break;
				default:
					if (tokens.peek().startsWith("-")){
						System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
					} else {
						nameBuilder.append(tokens.pop());
						nameBuilder.append(" ");
					}
					break;
			}
		}
		if (!help){
			String itemName = nameBuilder.toString().trim();
				item = activeChar.getItem(itemName);
			if (item!=null && amount !=null){
				if (item.isConsumable()){
					item.use(amount);
					System.out.println("Used "+amount+"x "+item.getName());
					if(item.getCount() <= 0){
						activeChar.removeItem(item);
					}
				} else {
					System.out.println(Message.ERROR_NOT_CON);
				}
			} else {
				System.out.println(Message.MSG_NO_ITEM);
			}
		} else {
			System.out.println(Help.USE);
		}
	}


/**EQUIP/DEQUIP********************************************************************************************************/
	static void equip(){
		String command = tokens.pop();
		if(!tokens.isEmpty()){
			equip(command);
		}else {
			Item item = getItemByName();
			if(item!=null){
				if(item.isEquippable()){
					if (command.equalsIgnoreCase("equip")){
						if(!item.isEquipped()){
							activeChar.equip(item);
						} else {
							System.out.println("ERROR: Item already equipped");
						}
					} else {
						if(item.isEquipped()){
							activeChar.dequip(item);
						} else {
							System.out.println("ERROR: Item not equipped");
						}
					}
				} else {
					System.out.println(Message.ERROR_NOT_EQUIP);
				}
			} else {
				System.out.println(Message.MSG_NO_ITEM);
			}
		}
	}


	private static void equip(String command){
		Item item;
		StringBuilder nameBuilder = new StringBuilder();
		boolean help = false;
		
		while(!tokens.isEmpty()){
			if (tokens.peek().equals("--help")){
				help = true;
				tokens.pop();
			}
			nameBuilder.append(tokens.pop());
			nameBuilder.append(" ");
		}
		if (!help){
			String itemName = nameBuilder.toString().trim();
			item = activeChar.getItem(itemName);
			if (item!=null){
				if (item.isEquippable()){
					if (command.equalsIgnoreCase("equip")){
						if(!item.isEquipped()){
							activeChar.equip(item);
						} else {
							System.out.println("ERROR: Item already equipped");
						}
					} else {
						if(item.isEquipped()){
							activeChar.dequip(item);
						} else {
							System.out.println("ERROR: Item not equipped");
						}
					}
				} else {
					System.out.println(Message.ERROR_NOT_EQUIP);
				}
			} else {
				System.out.println(Message.MSG_NO_ITEM);
			}
		} else {
			if(command.equalsIgnoreCase("equip")){
				System.out.println(Help.EQUIP);
			} else {
				System.out.println(Help.DEQUIP);
			}
		}
	}
	
/**GET*****************************************************************************************************************/

    static void get(){
		tokens.pop();
		if (!tokens.isEmpty()){
			getParser();
		} else {
		    ItemBuilder itemBuilder = new ItemBuilder();

			System.out.println("item | equippable | weapon | armor | consumable | coin");
			while(true){
				System.out.print("Item type: ");
				String type= scanner.nextLine().toLowerCase().trim();
				if(type.equals("cancel")){
                    return;
                }
                itemBuilder.itemType = Item.parseItemType(type);
                if(itemBuilder.itemType != null){
                    break;
                }
                System.out.println(Message.ERROR_ITEM_TYPE);
			}

            switch(itemBuilder.itemType){
                case ARMOR:
                    inputItemInfo(itemBuilder);
                    System.out.println("light | medium | heavy | shield | other");
                    while (itemBuilder.armorType == null) {
                        System.out.print("Armor type: ");
                        itemBuilder.armorType = Armor.parseType(scanner.nextLine().trim());
                        if (itemBuilder.armorType == null){
                            System.out.println("ERROR: Not a valid armor type");
                        }
                        itemBuilder.armorClass = getValidInt("AC: ");
                    }
                    inputEffects(itemBuilder);
                    break;
                case COIN:
                    System.out.println("Coin type:");
                    itemBuilder.itemName = scanner.nextLine().trim();
                    itemBuilder.itemCount = getValidInt("Count: ");
                    getCoins(itemBuilder);
                    return;
                case CONSUMABLE:
                case ITEM:
                    inputItemInfo(itemBuilder);
                    break;
                case WEAPON:
                    inputItemInfo(itemBuilder);
                    System.out.println("Weapon damage: ");
                    itemBuilder.damage = getDiceRoll();
                    inputEffects(itemBuilder);
                    break;
                case EQUIPPABLE:
                    inputItemInfo(itemBuilder);
                    inputEffects(itemBuilder);
                    break;
                default:
                    break;
            }
            getItem(itemBuilder);
        }
    }

    private static void inputItemInfo(ItemBuilder itemBuilder){
        System.out.print("Item name: ");
        itemBuilder.itemName = scanner.nextLine().trim();
        itemBuilder.itemCount = getValidInt("Count: ");
    }

    private static void inputEffects(ItemBuilder itemBuilder){
        while(getYN("Add effect? ")){
            System.out.print("Effect target: ");
            String statName = scanner.nextLine();
            if (!statName.equalsIgnoreCase("cancel")){
                Stat target = activeChar.getStat(statName);
                if (target == null){
                    System.out.println("ERROR: Effect target not found");
                } else {
                    itemBuilder.addEffect(target, getValidInt("Stat bonus: "));
                }
            }
        }
    }

	private static void getParser(){
	    StringBuilder nameBuilder = new StringBuilder();
	    ItemBuilder itemBuilder = new ItemBuilder();

	    while(!tokens.isEmpty()){
	        switch(tokens.peek()){
                case "-c":
                case "--count":
                    tokens.pop();
                    if (tokens.isEmpty()){
                        System.out.println(Message.ERROR_NO_ARG + ": count");
                        return;
                    }
                    itemBuilder.itemCount = getIntToken();
                    break;
                case "--armor":
                    tokens.pop();
                    itemBuilder.itemType = Item.ItemType.ARMOR;
                    break;
                case "--consumable":
                    tokens.pop();
                    itemBuilder.itemType = Item.ItemType.CONSUMABLE;
                    break;
                case "--equippable":
                    tokens.pop();
                    itemBuilder.itemType = Item.ItemType.EQUIPPABLE;
                    break;
                case "--item":
                    tokens.pop();
                    itemBuilder.itemType = Item.ItemType.ITEM;
                    break;
                case "--weapon":
                    tokens.pop();
                    itemBuilder.itemType = Item.ItemType.WEAPON;
                    break;
                case "-t":
                case "--type":
                    tokens.pop();
                    if (tokens.isEmpty()){
                        System.out.println(Message.ERROR_NO_ARG+": type");
                        return;
                    }
                    itemBuilder.itemType = Item.parseItemType(tokens.pop());
                    if(itemBuilder.itemType == null){
                        System.out.println(Message.ERROR_ITEM_TYPE);
                        return;
                    }
                    break;
                case "-d":
                case "--damage":
                    tokens.pop();
                    if (tokens.isEmpty()){
                        System.out.println(Message.ERROR_NO_ARG+": damage");
                        return;
                    }
                    itemBuilder.damage = getDiceRoll(tokens.pop());
                    break;
                case "-e":
                case "--enchant":
                case "--effect":
                    tokens.pop();
                    Stat target;
                    if(!tokens.isEmpty()){
                        target = activeChar.getStat(tokens.pop());
                    } else {
                        System.out.println(Message.ERROR_NO_ARG+": effect target");
                        return;
                    }
                    if(target!=null){
                        Integer bonus = getIntToken();
                        if(bonus!=null){
                            itemBuilder.addEffect(target,bonus);
                        } else {
                            System.out.println(Message.ERROR_NO_ARG + ": effect bonus");
                            return;
                        }
                    } else {
                        System.out.println("ERROR: Effect target not found");
                        return;
                    }
                    break;
                case "-ac":
                case "--armorclass":
                    tokens.pop();
                    itemBuilder.armorClass = getIntToken();
                    break;
                case "-at":
                case "--armortype":
                    tokens.pop();
                    if (tokens.isEmpty()){
                        System.out.println("ERROR: No armor type specified");
                        return;
                    }
                    itemBuilder.armorType = Armor.parseType(tokens.peek().toLowerCase());
                    if(itemBuilder.armorType == null){
                        System.out.println("ERROR: Not a valid armor type");
                        return;
                    }
                    break;
                case "--help":
                    tokens.pop();
                    System.out.println(Help.GET);
                    return;
                default:
                    if (tokens.peek().startsWith("-")){
                        System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
                    } else {
                        nameBuilder.append(tokens.pop());
                        nameBuilder.append(" ");
                    }
                    break;
            }
        }
        itemBuilder.itemName = nameBuilder.toString().trim();
        if (itemBuilder.itemName.isEmpty()){
            System.out.println(Message.ERROR_NO_ARG + ": item_name");
            return;
        }
        if (Inventory.isCurrency(itemBuilder.itemName)){
            getCoins(itemBuilder);
            return;
        }
        getItem(itemBuilder);
    }

    private static void getItem(ItemBuilder itemBuilder){
        switch (itemBuilder.itemType){
            case ITEM:
                Item item = itemBuilder.toItem();
                activeChar.addNewItem(item);
                break;
            case CONSUMABLE:
                Consumable consumable = itemBuilder.toConsumable();
                activeChar.addNewItem(consumable);
                break;
            case EQUIPPABLE:
                Equippable equippable = itemBuilder.toEquippable();
                activeChar.addNewItem(equippable);
                break;
            case WEAPON:
                Weapon weapon = itemBuilder.toWeapon();
                activeChar.addNewItem(weapon);
                break;
            case ARMOR:
                Armor armor = itemBuilder.toArmor();
                activeChar.addNewItem(armor);
                break;
            default:
                return;
        }
        System.out.println(String.format("Got %dx %s", itemBuilder.itemCount, itemBuilder.itemName));
    }

    private static void getCoins(ItemBuilder itemBuilder){
        switch(itemBuilder.itemName.toLowerCase()){
            case "pp":
            case "platinum":
                addDropCoin(Inventory.indexPL, itemBuilder.itemCount, false, "add");
                break;
            case "gp":
            case "gold":
                addDropCoin(Inventory.indexGP, itemBuilder.itemCount, false, "add");
                break;
            case "sp":
            case "silver":
                addDropCoin(Inventory.indexSP, itemBuilder.itemCount, false, "add");
                break;
            case "cp":
            case "copper":
                addDropCoin(Inventory.indexCP, itemBuilder.itemCount, false, "add");
                break;
            default:
                break;
        }
    }

/**ADD/DROP************************************************************************************************************/
	static void addDrop(){
		Item item;
		String addDrop = tokens.pop();
		if (!tokens.isEmpty()){
			addDrop(addDrop);
		} else {
			Integer itemCount;
			item = getItemByName();
			itemCount = getValidInt("Amount: ");
			if (item!=null){
				switch (item.getName().toLowerCase()){
				    case "pp":
				    case "platinum":
                        addDropCoin(Inventory.indexPL, itemCount, false, addDrop);
                        break;
                    case "gp":
                    case "gold":
                        addDropCoin(Inventory.indexGP, itemCount, false, addDrop);
                        break;
                    case "sp":
                    case "silver":
                        addDropCoin(Inventory.indexSP, itemCount, false, addDrop);
                        break;
                    case "cp":
                    case "copper":
                        addDropCoin(Inventory.indexCP, itemCount, false, addDrop);
                        break;
                    default:
                        addDropItem(item, itemCount, false, addDrop);
                        break;
				}
			}
		}
	}

	private static void addDrop(String command){
		Item item = null;
		StringBuilder nameBuilder = new StringBuilder();
		Integer itemCount = null;
		boolean dropAll = false;
		boolean help = false;
		while(!tokens.isEmpty()){
			switch (tokens.peek()){
			case "-c":
			case "-count":
				tokens.pop();
				if (tokens.isEmpty()){
					System.out.println(Message.ERROR_NO_ARG+": count");
				} else {
					itemCount = getIntToken();
				}
				break;
			case "--all":
				tokens.pop();
				dropAll = true;
				break;
			case "--help":
				tokens.pop();
				help = true;
				break;
			default:
				if (tokens.peek().startsWith("-")){
					System.out.println("ERROR: Invalid flag '"+tokens.pop()+"'");
				} else {
					nameBuilder.append(tokens.pop());
					nameBuilder.append(" ");
				}
				break;
			}
		}

		/*DEFAULT VALUES****************/
		if (itemCount == null){
			itemCount = 1;
		}

		if(!help){
			String itemName = nameBuilder.toString().trim();
				switch (itemName.toLowerCase()){
                    case "pp":
                    case "platinum":
                        addDropCoin(Inventory.indexPL, itemCount, dropAll, command);
                        break;
                    case "gp":
                    case "gold":
                        addDropCoin(Inventory.indexGP, itemCount, dropAll, command);
                        break;
                    case "sp":
                    case "silver":
                        addDropCoin(Inventory.indexSP, itemCount, dropAll, command);
                        break;
                    case "cp":
                    case "copper":
                        addDropCoin(Inventory.indexCP, itemCount, dropAll, command);
                        break;
                    default:
                        item = activeChar.getItem(itemName);
                        break;
				}
				if (item!=null){
					addDropItem(item, itemCount, dropAll, command);
				}
		} else {
		    if(command.equals("add")){
                System.out.println(Help.ADD);
            }
            if(command.equals("drop")){
                System.out.println(Help.DROP);
            }
		}
	}

	private static void addDropCoin(int coinType, Integer itemCount, boolean dropAll, String command){
		String itemName="";
		switch (coinType){
			case Inventory.indexPL:
				itemName = "Platinum";
				break;
			case Inventory.indexGP:
				itemName = "Gold";
				break;
			case Inventory.indexSP:
				itemName = "Silver";
				break;
			case Inventory.indexCP:
				itemName = "Copper";
				break;
		}
		if (command.equals("drop") && !dropAll){
			itemCount = -itemCount;
			System.out.println(String.format("Dropped %dx %s", -itemCount, itemName));
		}
		if (dropAll){
			activeChar.getCurrency(coinType).setCount(0);
			System.out.println(String.format("Dropped all %s", itemName));
		} else {
			activeChar.addCurrency(coinType, itemCount);
			if (command.equals("add")){
				System.out.println(String.format("Added %dx %s", itemCount, itemName));
			}
		}
	}

	private static void addDropItem(Item item, Integer itemCount, boolean dropAll, String command){
		if (command.equals("drop") && !dropAll){
			itemCount = -itemCount;
			System.out.println(String.format("Dropped %dx \"%s\"", -itemCount, item.getName()));
		}
		if (dropAll){
			activeChar.removeItem(item);
			System.out.println(String.format("Dropped all \"%s\"", item.getName()));
		} else {
			activeChar.addDropItem(item, itemCount);
			if (command.equals("add")){
				System.out.println(String.format("Added %dx \"%s\"", itemCount, item.getName()));
			}
		}
	}
	
/**I/O & UTILITIES*****************************************************************************************************/
	private static Integer getIntToken(){
		Integer n = null;
		try {
			if (tokens.isEmpty()){
				System.out.println(Message.ERROR_NO_VALUE);
			} else {
				n = Integer.parseInt(tokens.pop());
			}
		} catch (NumberFormatException e) {
			System.out.println(Message.ERROR_NOT_INT);
		}
		return n; 
	}

	private static Skill getSkillByName(){
		Skill skill;
		while (true){
			System.out.print("Skill name: ");
			String skillName = scanner.nextLine().trim();
			if(skillName.equalsIgnoreCase("cancel")){
				return null;
			} else {
				skill = activeChar.getSkill(skillName);
				if (skill==null){
					System.out.println(Message.MSG_NO_SKILL);
				} else {
					return skill;
				}
			}
		}
	}

	private static Stat getStatByName(){
		Stat stat;
		while (true){
			System.out.print("Stat name: ");
			String skillName = scanner.nextLine().trim();
			if(skillName.equalsIgnoreCase("cancel")){
				return null;
			} else {
				stat = activeChar.getStat(skillName);
				if (stat==null){
					System.out.println(Message.MSG_NO_SKILL);
				} else {
					return stat;
				}
			}
		}
	}

	private static Spell getSpellByName(){
		Spell spell;
		while (true){
			System.out.print("Spell name: ");
			String spellName = scanner.nextLine().trim();
			if(spellName.equalsIgnoreCase("cancel")){
				return null;
			} else {
				spell = activeChar.getSpell(spellName);
				if (spell==null){
					System.out.println(Message.MSG_NO_SPELL);
				} else {
					return spell;
				}
			}
		}
	}

	private static Item getItemByName(){
		Item item;
		while (true){
			System.out.print("Item name: ");
			String itemName = scanner.nextLine().trim();
			if(itemName.equalsIgnoreCase("cancel")){
				return null;
			} else {
				for (Item coin:activeChar.getCurrency()){
					if (coin.getName().equalsIgnoreCase(itemName)){
						return coin;
					}
				}
				item = activeChar.getItem(itemName);
				if (item==null){
					System.out.println(Message.MSG_NO_ITEM);
				} else {
					return item;
				}
			}
		}
	}

	private static DiceRoll getDiceRoll(){
		while (true){
			System.out.print("Dice roll: ");
			String s = scanner.nextLine().trim();
			if (s.matches("(\\d+d\\d+)")){
				String[] a = s.split("d");
				return new DiceRoll(Integer.parseInt(a[0]), Integer.parseInt(a[1]));
			} else {
				if(s.equalsIgnoreCase("cancel")){
					return null;
				}
			}
		}
	}

	private static DiceRoll getDiceRoll(String s){
		if (s.matches("(\\d+d\\d+)")){
			String[] a = s.split("d");
			return new DiceRoll(Integer.parseInt(a[0]), Integer.parseInt(a[1]));
		} else {
			return null;
		}
	}

	static int getValidInt(String message){
		int val;
		while (true) {
			System.out.print(message);
			if (scanner.hasNextInt()){
				val = scanner.nextInt();
				scanner.nextLine();
				break;
			} else {
				System.out.println(Message.ERROR_NOT_INT);
				scanner.nextLine();
			}
		}
		return val;
	}

	static void dispCharacterList(){
		if(!characterList.isEmpty()){
            System.out.println("Characters:");
            for (PlayerCharacter c : characterList.values()){
                System.out.println("- " + c.getName());
            }
        }else{
            System.out.println("No characters available");
        }
	}

	static boolean getYN(String message){
        while(true) {
            System.out.print(message + "[Y/N]: ");
            String yn = scanner.nextLine();
            if (yn.equalsIgnoreCase("y") || yn.equalsIgnoreCase("yes")){
                return true;
            }
            if (yn.equalsIgnoreCase("n") || yn.equalsIgnoreCase("no")){
                return false;
            }
        }
    }

    static boolean checkCaster(PlayerCharacter pc){
		if (pc.isSpellcaster()){
			return true;
		} else {
			System.out.println(Message.MSG_NOT_CAST);
			return false;
		}
	}

    private static boolean isValidName(String name){
        if(name.isEmpty() || name.matches("\\s+") || name.matches(".*[^a-zA-Z0-9)(\\s+].*")){
            System.out.println("Not a valid name");
            return false;
        } else {
            return true;
        }
    }

/**TEST CHARACTER******************************************************************************************************/
	private static void makeTestCharacter(){
		PlayerCharacter frodo;
		frodo = new PlayerCharacter("Frodo Baggins", "Halfling", "Paladin");
		frodo.addNewItem(new Consumable("Rations", 5));

		/*create some enchanted items*/
		Weapon sting = new Weapon("Sting");
		sting.addEffect(new ItemEffect(frodo.getStat(Ability.STR), 2));
		sting.setDamage(new DiceRoll(1,8));
		frodo.addNewItem(sting);
		sting.equip(frodo);
		Armor mith = new Armor("Mithril Chainmail");
		mith.addEffect(new ItemEffect(frodo.getStat(Ability.CON),2));
		mith.setArmorType(Armor.ArmorType.L_ARMOR);
		mith.setAC(14);
		frodo.addNewItem(mith);
		mith.equip(frodo);
		Equippable ring = new Equippable("Ring of Power");
		ring.addEffect(new ItemEffect(frodo.getStat(Attribute.HP), -5));
		frodo.addNewItem(ring);

		/*learn a spell*/
		frodo.getSpellBook().learn(new Spell("Invisibility",Spell.CANTRIP));
		frodo.getSpellBook().learn(new Spell("Blight",3));

		/*get spellslots*/
		SpellSlot[] spellSlots = new SpellSlot[]{
				new SpellSlot(0,0),
				new SpellSlot(1,4),
				new SpellSlot(2,3),
				new SpellSlot(3,2),
				new SpellSlot(4,0),
				new SpellSlot(5,0),
				new SpellSlot(6,0),
				new SpellSlot(7,0),
				new SpellSlot(8,0),
				new SpellSlot(9,0)
		};
		frodo.setSpellSlots(spellSlots);
		frodo.initMagicStats(frodo.getAbilities().get(Ability.WIS));
		frodo.setSpellcaster(true);

		/*Add currency*/
		frodo.addCurrency(Inventory.indexGP, 10);
		frodo.addCurrency(Inventory.indexSP, 35);
		frodo.addCurrency(Inventory.indexCP, 4);

		characterList.put("frodo baggins", frodo);
		activeChar = characterList.get("frodo baggins");
	}
}
