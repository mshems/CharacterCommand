package app;

import character.*;
import items.*;
import magic.*;
import utils.*;

import java.util.*;

@SuppressWarnings("unused")
public class App {
	/**
	 * version 0.2.3
	 */
	public static final long VERSION = 203L;
	private static final String version = "CharacterCommand v0.2.3";
	private static String newLine = System.lineSeparator();
	static boolean QUIT_ALL = false;
    static PlayerCharacter activeChar;
	static LinkedHashMap<String, PlayerCharacter> characterList;
	static Scanner scanner;
	static String[] input;
    static LinkedList<String> tokens;
	static PropertiesHandler propertiesHandler;
    static IOHandler ioHandler;
    static CommandHandler commandHandler;
    private static CTerm terminal;

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
            terminal.setPrompt(prompt);

            //getCommand(prompt);
			//commandHandler.doCommand(tokens.peek(), activeChar);

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
		//System.out.println("---"+version+"---");
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
		terminal = new CTerm();
        String prompt;
        if (activeChar != null){
            prompt = "CharacterCommand @ "+activeChar.getName()+"> ";

            if(propertiesHandler.isViewAlways()){
                System.out.println(activeChar);
            }
        } else {
            prompt = "CharacterCommand> ";
        }
        terminal.setPrompt(prompt);
        terminal.start();
	}

/**CREATE CHARACTER****************************************************************************************************/
	static void createCharacter(){
        /*String name;
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
		activeChar = c;*/
        terminal.println("new");
	}

/**STATS***************************************************************************************************************/
	static void stats(){
		/*String command = tokens.pop();
		if(!tokens.isEmpty()){
			stats(command);
		} else {
			System.out.println("view | edit | cancel");
			System.out.print("Action: ");
			String action = scanner.nextLine().toLowerCase().trim();
			boolean exit = false;
			while(!exit) {
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
		}*/
		terminal.print("stats");
	}

	private static void stats(String command){
		/*StringBuilder nameBuilder = new StringBuilder();
		boolean help = false;
		boolean view = true;
		while(!tokens.isEmpty()){
			switch(tokens.peek()){
				case "-e":
				case "--edit":
					edit();
					view = false;
					//tokens.pop();
					//edit = true;
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
		}*/
		terminal.print("stats-options");
	}

/**EDIT***************************************************************************************************************/
	static void edit(){
		/*String command = tokens.pop();
		if (!tokens.isEmpty()){
			edit(command);
		} else {
			Stat stat = getStatByName();
			if(stat!=null){
				int val = getValidInt(stat.getName() + " value: ");
				stat.setBaseVal(val);
				activeChar.updateStats();
				System.out.println("Updated "+stat.getName());
			}
		}*/
		terminal.print("edit");
	}

	private static void edit(String command){
		/*StringBuilder nameBuilder = new StringBuilder();
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
		}*/
		terminal.print("edit-options");
	}

/**AP******************************************************************************************************************/
	static void abilityPoints(){
		/*String command = tokens.pop();
		if (!tokens.isEmpty()){
			abilityPoints(command);
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
		}*/
		terminal.print("abilitypoints");
	}

	private static void abilityPoints(String command){
		/*boolean use = false;
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
		}*/
		terminal.print("abilitypoints-options");
	}

/**SKILLS**************************************************************************************************************/
    static void skills(){
		/*String command = tokens.pop();
		String action;
		if (!tokens.isEmpty()){
			skills(command);
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
		}*/
		terminal.print("skills");
	}

	private static void skills(String command){
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
		String command = tokens.pop();
		if(!tokens.isEmpty()){
			setSlots(command);
		} else {
			int level = getValidInt("Enter spell slot level: ");
			int max = getValidInt("Enter new spell slot maximum: ");
			activeChar.getSpellSlots()[level].setMaxVal(max);
			System.out.println("Maximum level "+level+" spell slots set to "+max);
		}
	}

	private static void setSlots(String command){
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
		String command = tokens.pop();
		if(!tokens.isEmpty()){
			charge(command);
		} else {
			int level = getValidInt("Enter spell slot level: ");
			int count = getValidInt("Enter number of slots to recharge: ");
			System.out.println("Recharged "+count+" level "+level+" spell slots");
		}
	}

/**CHARGE**************************************************************************************************************/
	private static void charge(String command){
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
		String command = tokens.pop();
		if(!tokens.isEmpty()){
			learn(command);
		} else {
			String spellName;
			int spellLevel;
			System.out.print("Spell name: ");
			spellName = scanner.nextLine().trim();
			spellLevel = getValidInt("Spell level: ");
			learnSpell(new Spell(spellName, spellLevel));
		}
    }

	private static void learn(String command){
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
		String command = tokens.pop();
		if(!tokens.isEmpty()){
			forget(command);
		} else {
			Spell spell = getSpellByName();
			if(spell!=null){
				forgetSpell(spell);
			}
		}
	}

	private static void forget(String command){
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
		String command = tokens.pop();
		if (!tokens.isEmpty()){
			cast(command);
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

	private static void cast(String command){
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
		String command = tokens.pop();
		if(!tokens.isEmpty()){
			use(command);
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

	private static void use(String command){
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
		String command = tokens.pop();
		boolean quit_get = false;
		if (!tokens.isEmpty()){
			get(command);
		} else {
			String itemName;
			String itemType = null;
			Integer itemCount;
			Integer ac = null;
            Armor.ArmorType at=null;
			ArrayList<ItemEffect> fxList = null;
			DiceRoll dmg = null;

			System.out.println("item | equippable | weapon | armor | consumable | coin");
			while(true){
				System.out.print("Item type: ");
				String s = scanner.nextLine().toLowerCase().trim();
				if(s.equals("cancel")){
					quit_get = true;
					break;
				} else if(s.equals("coin")){
					itemType = s;
					break;
				} else if(checkStringInSet(s, Item.types)){
					itemType = s;
					break;
				} else {
					System.out.println(Message.ERROR_ITEM_TYPE);
				}
			}

			if (!quit_get){
                if(itemType.equalsIgnoreCase("armor")){
                    System.out.println("light | medium | heavy | shield | other");
                    while(at==null) {
						System.out.print("Armor type: ");
						String s = scanner.nextLine().trim();
                        switch (s){
                            case "l":
                            case "light":
                                at = Armor.ArmorType.L_ARMOR;
                                break;
                            case "m":
                            case "medium":
                                at = Armor.ArmorType.M_ARMOR;
                                break;
                            case "h":
                            case "heavy":
                                at = Armor.ArmorType.H_ARMOR;
                                break;
                            case "s":
                            case "shield":
                                at = Armor.ArmorType.SHIELD;
                                break;
                            case "o":
                            case "other":
                                at = Armor.ArmorType.OTHER;
                                break;
                            default:
                                System.out.println("ERROR: Not a valid armor type");
                                break;
                        }
                    }
                    ac = getValidInt("AC: ");
                }
				if(itemType.equalsIgnoreCase("coin")){
					System.out.println("Coin type:");
				} else {
					System.out.print("Item name: ");
				}
                itemName = scanner.nextLine().trim();
                itemCount = getValidInt("Count: ");

                if((itemType.equals("weapon"))||(itemType.equals("armor"))||(itemType.equals("equippable"))){
					System.out.println("Weapon damage: ");
					dmg = getDiceRoll();
                	fxList = new ArrayList<>();
                    Stat fxTgt=null;
                    while(getYN("Add effect? ")){
                        boolean quit_get_tgt=false;
                    	while(true){
                            System.out.println("Effect target: ");
                            String s = scanner.nextLine();
                            if(s.equalsIgnoreCase("cancel")){
                                quit_get_tgt=true;
                                break;
                            } else {
                                fxTgt = activeChar.getStat(s);
                            }
                            if(fxTgt!=null){
                                break;
                            } else {
                                System.out.println("ERROR: Effect target not found");
                            }
                        }
                        if(!quit_get_tgt){
                            int fxBon = getValidInt("Effect bonus: ");
                            ItemEffect fx = new ItemEffect(fxTgt, fxBon);
                            fxList.add(fx);
                        } else {
                            break;
                        }
                    }
                }
                itemName = itemName.trim();
                switch(itemName.toLowerCase()){
					case "pp":
					case "platinum":
						addDropCoin(Inventory.indexPL, itemCount, false, "add");
						quit_get = true;
						break;
					case "gp":
					case "gold":
						addDropCoin(Inventory.indexGP, itemCount, false, "add");
						quit_get = true;
						break;
					case "sp":
					case "silver":
						addDropCoin(Inventory.indexSP, itemCount, false, "add");
						quit_get = true;
						break;
					case "cp":
					case "copper":
						addDropCoin(Inventory.indexCP, itemCount, false, "add");
						quit_get = true;
						break;
					default:
						break;
				}
                if(!quit_get){
					switch (itemType){
						case "i":
						case "item":
							Item item = new Item(itemName, itemCount);
							activeChar.addNewItem(item);
							break;
						case "c":
						case "consumable":
							Consumable consumable = new Consumable(itemName, itemCount);
							activeChar.addNewItem(consumable);
							break;
						case "e":
						case "equippable":
							Equippable equippable = new Equippable(itemName, itemCount);
							equippable.setEffects(fxList);
							activeChar.addNewItem(equippable);
							break;
						case "w":
						case "weapon":
							Weapon weapon = new Weapon(itemName, itemCount);
							weapon.setEffects(fxList);
							if(dmg!=null){
								weapon.setDamage(dmg);
							}
							activeChar.addNewItem(weapon);
							break;
						case "a":
						case "armor":
							Armor armor = new Armor(itemName, itemCount);
							armor.setEffects(fxList);
							if (at != null){
								armor.setArmorType(at);
								if (at != Armor.ArmorType.SHIELD){
									armor.setAC(ac);
								}
								activeChar.addNewItem(armor);
							} else {
								System.out.println("ERROR: Armor type not specified. Use 'equippable' for generic equipment");
							}
							break;
					}
					System.out.println(String.format("Got %dx %s", itemCount, itemName));
				}
			}
		}
	}

	private static void get(String command){
		StringBuilder nameBuilder = new StringBuilder();
		String itemType = null;
		Integer itemCount = null;
		Integer ac=null;
		Armor.ArmorType at=null;
		ArrayList<ItemEffect> fxList=null;
		DiceRoll dmg = null;
		boolean quit = false;
		boolean help = false;
		
		while(!tokens.isEmpty()){
			switch (tokens.peek()){
			case "-c":
			case "--count":
				tokens.pop();
				if (tokens.isEmpty()){
					System.out.println(Message.ERROR_NO_ARG+": count");
					itemCount = null;
					quit = true;
				} else {
					itemCount = getIntToken();
				}
				break;
			case "-t":
			case "--type":
				tokens.pop();
				if (tokens.isEmpty()){
					System.out.println(Message.ERROR_NO_ARG+": type");
				} else {
					itemType = tokens.pop();
					if (!checkStringInSet(itemType, Item.types)){
						System.out.println(Message.ERROR_ITEM_TYPE);
						quit=true;
					}
				}
				break;
				case "-d":
				case "--damage":
					tokens.pop();
					dmg = getDiceRoll(tokens.pop());
					break;
				case "-e":
                case "--enchant":
                case "--effect":
                    if(fxList==null){
                        fxList = new ArrayList<>();
                    }
                    tokens.pop();
                    Stat fxTgt=null;
                    if(!tokens.isEmpty()){
                        fxTgt = activeChar.getStat(tokens.pop());
                    }
                    if(fxTgt!=null){
                        Integer fxBon = getIntToken();
                        if(fxBon!=null){
							fxList.add(new ItemEffect(fxTgt, fxBon));
						} else {
                        	quit = true;
						}
                    } else {
                        System.out.println("ERROR: Effect target not found");
                    }
                    break;
                case "-ac":
                case "--armorclass":
                    tokens.pop();
                    ac = getIntToken();
                    break;
                case "-at":
                case "--armortype":
                    tokens.pop();
                    String type="";
                    if (!tokens.isEmpty()){
                        type = tokens.peek().toLowerCase();
                    }
                    switch(type){
                        case"l":
                        case "light":
                            at = Armor.ArmorType.L_ARMOR;
                            tokens.pop();
                            break;
                        case"m":
                        case "medium":
                            at = Armor.ArmorType.M_ARMOR;
                            tokens.pop();
                            break;
                        case "h":
                        case "heavy":
                            at = Armor.ArmorType.H_ARMOR;
                            tokens.pop();
                            break;
                        case "s":
                        case "shield":
                            at = Armor.ArmorType.SHIELD;
                            tokens.pop();
                            break;
                        case "o":
                        case "other":
                            at = Armor.ArmorType.OTHER;
                            tokens.pop();
                            break;
                        default:
                            System.out.println("ERROR: Not a valid armor type");
                            quit = true;
                            break;
                    }
                    break;
			case "--help":
				tokens.pop();
				help = true;
				quit = true;
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
		if (itemType == null){
			itemType = "item";
		}

		if (help){
            System.out.println(Help.GET);
        } else {
        	String itemName = nameBuilder.toString().trim();
            if (itemName.isEmpty()){
                quit = true;
                System.out.println(Message.ERROR_NO_ARG + ": item_name");
            }
			switch(itemName.toLowerCase()){
				case "pp":
				case "platinum":
					addDropCoin(Inventory.indexPL, itemCount, false, "add");
					quit=true;
					break;
				case "gp":
				case "gold":
					addDropCoin(Inventory.indexGP, itemCount, false, "add");
					quit=true;
					break;
				case "sp":
				case "silver":
					addDropCoin(Inventory.indexSP, itemCount, false, "add");
					quit=true;
					break;
				case "cp":
				case "copper":
					addDropCoin(Inventory.indexCP, itemCount, false, "add");
					quit=true;
					break;
				default:
					break;
			}
            if (!quit){
            	switch (itemType){
                    case "i":
                    case "item":
                        Item item = new Item(itemName, itemCount);
                        activeChar.addNewItem(item);
                        break;
                    case "c":
                    case "consumable":
                        Consumable consumable = new Consumable(itemName, itemCount);
                        activeChar.addNewItem(consumable);
                        break;
                    case "e":
                    case "equippable":
                        Equippable equippable = new Equippable(itemName, itemCount);
                        equippable.setEffects(fxList);
                        activeChar.addNewItem(equippable);
                        break;
                    case "w":
                    case "weapon":
                        Weapon weapon = new Weapon(itemName, itemCount);
                        weapon.setEffects(fxList);
                        if(dmg!=null){
                        	weapon.setDamage(dmg);
						}
                        activeChar.addNewItem(weapon);
                        break;
                    case "a":
                    case "armor":
                        Armor armor = new Armor(itemName, itemCount);
                        armor.setEffects(fxList);
                        if (at != null){
                            armor.setArmorType(at);
                            if (ac != null && at != Armor.ArmorType.SHIELD){
                                armor.setAC(ac);
                            }
                            activeChar.addNewItem(armor);
                        } else {
                            System.out.println("ERROR: Armor type not specified. Use 'equippable' for generic equipment");
                        }
                        break;
                }
                System.out.println(String.format("Got %dx %s", itemCount, itemName));
            }
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

	private static boolean checkStringInSet(String in, String[] a){
	    for (String s: a){
			if (s.equalsIgnoreCase(in)){
				return true;
			}
		}
		return false;
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
