package utils;

public class Help{
    public static final String PREFS="Usage:" +
            "\n  prefs [options]                            manage application preferences" +
            "\nOptions:" +
            "\n  -e, --export <export_directory>            set directory for exported .txt files" +
            "\n  -d, --data <data_directory>                set directory for character files" +
            "\n  -va, --viewalways <true/false>             display character after each command" +
            "\n  --help";
    public static final String GET="Usage:" +
            "\n  get [options, <item_name>]                 get a new item" +
            "\nOptions:" +
            "\n  -c, --count <item_count>                   " +
            "\n  -t, --type <item_type>                     type of item" +
            "\n     item_type:" +
            "\n         i ,item" +
            "\n         c, consumable" +
            "\n         e, equippable" +
            "\n         a, armor" +
            "\n         w, weapon" +
            "\n  -e, --effect <fx_tgt> <fx_bonus>           " +
            "\n     fx_tgt:   name of stat affected         " +
            "\n     fx_bonus: bonus applied to fx_target    " +
            "\n  -at, --armortype <armor_type>              type of armor" +
            "\n     armor_type:" +
            "\n         l, light" +
            "\n         m, medium" +
            "\n         h, heavy" +
            "\n         s, shield" +
            "\n         o, other" +
            "\n  -ac, --armorclass <armor_class>            base AC of the armor" +
            "\n  --help";
    public static final String ADD="Usage:" +
            "\n  add [options]                              add more of an existing item" +
            "\nOptions:" +
            "\n  -c, --count <item_count>                   " +
            "\n  --help";
    public static final String DROP="Usage:" +
            "\n  drop [options]                             drop items from inventory" +
            "\nOptions:" +
            "\n  -c, --count <item_count>                   " +
            "\n  --all                                      toggle dropping all of specified item " +
            "\n  --help";
    public static final String SKILL="Usage:" +
            "\n  skill [options, <skill_name>]              view and train skills" +
            "\nOptions:" +
            "\n  -t, --train                                gain proficiency" +
            "\n  -f, --forget                               lose proficiency" +
            "\n  -e, --expert                               gain expertise" +
            "\n  -v  --view                                 view the specified skill" +
            "\n  -va, --viewall                             view all skills" +
            "\n  --help";
    public static final String LEVELUP="Usage:" +
            "\n  levelup [options]                          level up character" +
            "\nOptions:" +
            "\n  -l, --level <level_number>                 " +
            "\n  --help";
    public static final String CAST="Usage:" +
            "\n  cast [options, <spell_name>]               cast a spell" +
            "\n  spell --cast [options, <spell_name>]       " +
            "\nOptions:" +
            "\n  -l, --level <level_num>                    " +
            "\n  --help";
    public static final String HEAL="Usage:" +
            "\n  heal [options]                             gain hp" +
            "\nOptions:" +
            "\n  -hp, --health <hp_healed>                  " +
            "\n  --all                                      toggles healing to max hp" +
            "\n  --help";
    public static final String HURT="Usage:" +
            "\n  hurt [options]                             lose hp" +
            "\nOptions:" +
            "\n  -hp, --health <hp_lost>                    " +
            "\n  --all                                      toggle dropping to 0 hp" +
            "\n  --help";
    public static final String EQUIP="Usage:" +
            "\n  equip [item_name]                          equip items" +
            "\nOptions:" +
            "\n  --help";
    public static final String DEQUIP="Usage:" +
            "\n  dequip [item_name]                         dequip items" +
            "\nOptions:" +
            "\n  --help";
    public static final String ROLL="Usage:" +
            "\n  roll <num_dice>d<num_sides>[modifier] [options]" +
            "\n     num_dice:  # of dice to roll" +
            "\n     num_sides: # of sides" +
            "\n     modifier:  optional modifier to result of roll (+5, -2, etc...)" +
            "\nOptions:" +
            "\n  --help";
    public static final String SPELLS="Usage:" +
            "\n  spell [options]" +
            "\nOptions:" +
            "\n  --learn" +
            "\n  --forget" +
            "\n  -v, --view" +
            "\n  --cast" +
            "\n  --help";
    public static final String LEARN="Usage:" +
            "\n  learn [options, <spell_name>]" +
            "\n  spell --learn [options, <spell_name>]" +
            "\nOptions:" +
            "\n  -l, --level" +
            "\n  --help";
    public static final String FORGET="Usage:" +
            "\n  forget [options, <spell_name>]" +
            "\n  spell --forget [options, <spell_name>]" +
            "\nOptions:" +
            "\n  --help";

    public static void helpMenu(String command){
        switch(command){
            case "load":
            case "list":
            case "save":
            case "import":
            case "new":
            case "roll":
                System.out.println(ROLL);
                break;
            case "prefs":
                System.out.println(PREFS);
                break;
            case "get":
                System.out.println(GET);
                break;
            case "add":
                System.out.println(ADD);
                break;
            case "drop":
                System.out.println(DROP);
                break;
            case "skill":
                System.out.println(SKILL);
                break;
            case "spell":
                System.out.println();
                break;
            case "cast":
                System.out.println(CAST);
                break;
            case "heal":
                System.out.println(HEAL);
                break;
            case "hurt":
                System.out.println(HURT);
                break;
            case "equip":
                System.out.println(EQUIP);
                break;
            case "dequip":
                System.out.println(DEQUIP);
                break;
            case "levelup":
                System.out.println(LEVELUP);
                break;
            case "--list":
                System.out.println(COMMANDS_LIST);
                break;
            default:
                System.out.println("Help Menu :D");
        }
    }

    public static final String COMMANDS_LIST ="Commands List:" +
            "\n  import" +
            "\n  load" +
            "\n  list" +
            "\n  save" +
            "\n  new" +
            "\n  view" +
            "\n  stats" +
            "\n  skills" +
            "\n  inv" +
            "\n  get" +
            "\n  add/drop" +
            "\n  equip/dequip" +
            "\n  heal/hurt" +
            "\n  cast" +
            "\n  levelup" +
            "\n  roll" +
            "\n  prefs" +
            "\n  help" +
            "\n  quit" +
            "\nEnter 'command --help' or 'help command' for details";

}
