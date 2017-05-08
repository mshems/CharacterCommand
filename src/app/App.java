package app;

import character.*;
import magic.*;
import items.*;
import utils.*;

import java.util.*;

@SuppressWarnings("unused")
public class App {

	private static String newLine = System.lineSeparator();
	static boolean QUIT_ALL = false;
    static PlayerCharacter activeChar;
	static LinkedHashMap<String, PlayerCharacter> characterList;
	static Scanner scanner;
	static String[] input;
    static LinkedList<String> tokens;

	static PropertiesHandler propertiesHandler;
	private static CommandHandler commandHandler;
    static IOHandler ioHandler;

	public static void main(String[] args){
		String prompt;
	    initApp();
		makeTestCharacter();

		while(!QUIT_ALL){
		    if (activeChar != null){
				prompt = "CharacterCommand @ "+activeChar.getName()+"> ";
				if(propertiesHandler.readViewAlways()){
					System.out.println(activeChar);
				}
			} else {
                prompt = "CharacterCommand> ";
            }
            getCommand(prompt);
			commandHandler.doCommand(tokens.peek(), activeChar);
		}
	}

/**GET COMMAND*********************************************************************************************************/
	private static void getCommand(String prompt){
        System.out.print(prompt);
        input = scanner.nextLine()
                .trim()
                .split("\\s+");
        Collections.addAll(tokens, input);
    }

/**INITIALIZATION******************************************************************************************************/
    private static void initApp() {
    	propertiesHandler = new PropertiesHandler();
        commandHandler = new CommandHandler();
		ioHandler= new IOHandler();
        IOHandler.checkDirs();
        tokens = new LinkedList<>();
        scanner = new Scanner(System.in);
        characterList = new LinkedHashMap<>();
        ioHandler.importAll(false);
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
		characterList.put(c.getName().toLowerCase(), c);
		System.out.println("Created "+c.getName());
		activeChar = c;
	}
	
/**SKILLS**************************************************************************************************************/
    static void skills(){
		String command = tokens.pop();
		String action;
		if (!tokens.isEmpty()){
			skills(command);
		} else {
			Skill skill;
			boolean exit = false;
			while(!exit){
				System.out.print("View | Train | Forget | Expertise | View All : ");
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
				case "expertise":
					skill = getSkillByName();
					if(skill!=null){
						skill.expert(activeChar);
						System.out.println("Gained expertise in "+skill.getName());
					}
					exit=true;
					break;
				case "va":
				case "view all":
                    System.out.println(activeChar.skillsToString());
                    exit=true;
					break;
				case "quit":
					exit=true;
					break;
				default:
					System.out.println(Message.ERROR_SYNTAX);
					System.out.println("Enter 'quit' to quit");
					exit = false;
					break;
				}
			}
		}
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
					System.out.println("Enter 'skill --help' for help");
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
			System.out.println(activeChar.getName()+" is now level "+activeChar.getLevel());
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
					activeChar.level(level);
					System.out.println(activeChar.getName()+" is now level "+activeChar.getLevel());
				} else {
					System.out.println("ERROR: Invalid input");
				}
			} else {
                System.out.println(Help.LEVELUP);
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
			//TODO: learn
			System.out.println("manual spell learning placeholder -- use command arguments for now");
		}
    }

	private static void learn(String command){
		tokens.pop();
		StringBuilder nameBuilder = new StringBuilder();
		Integer spellLevel = null;
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
			}
		}
		if(spellLevel == null){
			spellLevel = Spell.CANTRIP;	//default level
		}

		if(!help){
			Spell spell = new Spell(nameBuilder.toString().trim(), spellLevel);
			learnSpell(spell);
		} else {
			System.out.println(Help.LEARN);
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
			//TODO: forget
			System.out.println("manual spell forgetting placeholder -- use command arguments for now");
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
			}
		}
		if(!help){
			Spell spell = activeChar.getSpell(nameBuilder.toString().trim());
			if(spell!=null){
				forgetSpell(spell);
			} else {
				System.out.println(Message.MSG_NO_SPELL);
			}
		} else {
			System.out.println(Help.FORGET);
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
		} else if(amount!=null){
			if (healAll){
				healAll(command);
			} else {
				heal(command, amount);
			}
		} else {
			System.out.println(Message.ERROR_SYNTAX+newLine+"Enter '"+command+" --help' for help");
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
			System.out.println("manual use placeholder -- use command arguments for now");
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
					if(item.getCount()<=0){
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
			System.out.println("manual equip/dequip placeholder -- use command arguments for now");
		}
	}


	private static void equip(String equipDequip){
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
					if (equipDequip.equalsIgnoreCase("equip")){
						activeChar.equip(item);
						System.out.println(item.getName()+" equipped");
					} else {
						activeChar.dequip(item);
						System.out.println(item.getName()+" dequipped");
					}
				} else {
					System.out.println(Message.ERROR_NOT_EQUIP);
				}
			} else {
				System.out.println(Message.MSG_NO_ITEM);
			}
		} else {
			if(equipDequip.equalsIgnoreCase("equip")){
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

			System.out.println("Item | Equippable | Weapon | Armor | Consumable");
			while(true){
				System.out.print("Item type: ");
				String s = scanner.nextLine().trim();
				if(s.equalsIgnoreCase("quit")){
					quit_get = true;
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
                    System.out.println("Light | Medium | Heavy | Shield | Other");
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

				System.out.print("Item name: ");
                itemName = scanner.nextLine().trim();
                itemCount = getValidInt("Count: ");

                if((itemType.equals("weapon"))||(itemType.equals("armor"))||(itemType.equals("equippable"))){
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
                switch(itemType){
                    case "item":
                        Item item = new Item(itemName, itemCount);
                        activeChar.addNewItem(item);
                        break;
                    case "consumable":
                        Consumable consumable = new Consumable(itemName, itemCount);
                        activeChar.addNewItem(consumable);
                        break;
                    case "equippable":
                        Equippable equippable = new Equippable(itemName, itemCount);
                        equippable.setEffects(fxList);
                        activeChar.addNewItem(equippable);
                        break;
                    case "weapon":
                        Weapon weapon = new Weapon(itemName, itemCount);
                        weapon.setEffects(fxList);
                        activeChar.addNewItem(weapon);
                        break;
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

	private static void get(String command){
		StringBuilder nameBuilder = new StringBuilder();
		String itemType = null;
		Integer itemCount = null;
		Integer ac=null;
		Armor.ArmorType at=null;
		ArrayList<ItemEffect> fxList=null;
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
		/*DEFAULT VALUES****************/

		if (help){
            System.out.println(Help.GET);
        } else {
        	String itemName = nameBuilder.toString().trim();
            if (itemName.isEmpty()){
                quit = true;
                System.out.println(Message.ERROR_NO_ARG + ": item_name");
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
                        addDropCoin(Inventory.indexPL, "Platinum", itemCount, false, addDrop);
                        break;
                    case "gp":
                    case "gold":
                        addDropCoin(Inventory.indexGP, "Gold", itemCount, false, addDrop);
                        break;
                    case "sp":
                    case "silver":
                        addDropCoin(Inventory.indexSP, "Silver", itemCount, false, addDrop);
                        break;
                    case "cp":
                    case "copper":
                        addDropCoin(Inventory.indexCP, "Copper", itemCount, false, addDrop);
                        break;
                    default:
                        addDropItem(item, itemCount, false, addDrop);
                        break;
				}
			}
		}
	}

	private static void addDrop(String addDrop){
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
		/*DEFAULT VALUES****************/
		if(!help){
			String itemName = nameBuilder.toString().trim();
			//if (itemCount != null || dropAll){
				switch (itemName.toLowerCase()){
                    case "pp":
                    case "platinum":
                        addDropCoin(Inventory.indexPL, "Platinum", itemCount, dropAll, addDrop);
                        break;
                    case "gp":
                    case "gold":
                        addDropCoin(Inventory.indexGP, "Gold", itemCount, dropAll, addDrop);
                        break;
                    case "sp":
                    case "silver":
                        addDropCoin(Inventory.indexSP, "Silver", itemCount, dropAll, addDrop);
                        break;
                    case "cp":
                    case "copper":
                        addDropCoin(Inventory.indexCP, "Copper", itemCount, dropAll, addDrop);
                        break;
                    default:
                        item = activeChar.getItem(itemName);
                        break;
				}
				if (item!=null){
					addDropItem(item, itemCount, dropAll, addDrop);
				}
			//}
		} else {
		    if(addDrop.equals("add")){
                System.out.println(Help.ADD);
            }
            if(addDrop.equals("drop")){
                System.out.println(Help.DROP);
            }
		}
	}

	private static void addDropCoin(int coinType, String itemName, Integer itemCount, boolean dropAll, String addDrop){
		if (addDrop.equals("drop") && !dropAll){
			itemCount = -itemCount;
			System.out.println(String.format("Dropped %dx %s", -itemCount, itemName));
		}
		if (dropAll){
			activeChar.getCurrency(coinType).setCount(0);
			System.out.println(String.format("Dropped all %s", itemName));
		} else {
			activeChar.addCurrency(coinType, itemCount);
			if (addDrop.equals("add")){
				System.out.println(String.format("Added %dx %s", itemCount, itemName));
			}
		}
	}

	private static void addDropItem(Item item, Integer itemCount, boolean dropAll, String addDrop){
		if (addDrop.equals("drop") && !dropAll){
			itemCount = -itemCount;
			System.out.println(String.format("Dropped %dx \"%s\"", -itemCount, item.getName()));
		}
		if (dropAll){
			activeChar.removeItem(item);
			System.out.println(String.format("Dropped all \"%s\"", item.getName()));
		} else {
			activeChar.addDropItem(item, itemCount);
			if (addDrop.equals("add")){
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
			System.out.println("Skill name:");
			String skillName = scanner.nextLine().trim();
			if(skillName.equalsIgnoreCase("quit")){
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

	private static Spell getSpellByName(){
		Spell spell;
		while (true){
			System.out.print("Name: ");
			String spellName = scanner.nextLine().trim();
			if(spellName.equalsIgnoreCase("quit")){
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
			System.out.print("Name: ");
			String itemName = scanner.nextLine().trim();
			if(itemName.equalsIgnoreCase("quit")){
				return null;
			} else {
				item = activeChar.getItem(itemName);
				if (item==null){
					for (Item coin:activeChar.getCurrency()){
						if (coin.getName().equalsIgnoreCase(itemName)){
							return coin;
						}
					}
					System.out.println(Message.MSG_NO_ITEM);
				} else {
					return item;
				}
			}
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
            if (yn.equalsIgnoreCase("y")){
                return true;
            }
            if (yn.equalsIgnoreCase("n")){
                return false;
            }
        }
    }

    static String toFileName(String s){
	    return s.replaceAll(".*[^a-zA-Z0-9)(].*", "_");
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

	/*Add starting currency*/
    frodo.addCurrency(Inventory.indexGP, 10);
    frodo.addCurrency(Inventory.indexSP, 35);
    frodo.addCurrency(Inventory.indexCP, 4);

    characterList.put("frodo baggins", frodo);
}
}
