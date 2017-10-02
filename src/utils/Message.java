package utils;

import terminal.Terminal;

public class Message{
	public static final String ERROR_NO_VALUE = 	"ERROR: No value given";
	public static final String ERROR_ITEM_TYPE = 	"ERROR: Not a valid item type";
	//public static final String ERROR_NO_LOAD = 		"\033[1;31mERROR: No active character\033[0m";
	public static final String ERROR_NO_LOAD = 		"ERROR: No active character";
	public static final String ERROR_SYNTAX =  		"ERROR: Syntax not recognized";
	public static final String ERROR_NAN = 			"ERROR: Not a number";
	public static final String ERROR_NOT_INT = 		"ERROR: Not an integer value";
	public static final String ERROR_INPUT = 		"ERROR: Invalid input";
	public static final String ERROR_NO_COMMAND =	"ERROR: Command not found";
	public static final String ERROR_NO_CHAR =		"ERROR: Character not found";
	public static final String ERROR_NO_ARG =       "ERROR: Missing argument(s)";
	public static final String ERROR_NOT_EQUIP =    "ERROR: Item not equippable";
	public static final String ERROR_NOT_CON =    "ERROR: Item not consumable";


	public static final String MSG_NOT_CAST = "Not a spellcaster";
	public static final String MSG_NO_ITEM = "No item by that name";
    public static final String MSG_NO_SPELL = "No spell by that name";
    public static final String MSG_NO_SKILL = "No skill by that name";
	public static final String MSG_NO_STAT = "No stat by that name";

	public static void errorMessage(Terminal terminal, String message){
		terminal.println(message);
	}
}
