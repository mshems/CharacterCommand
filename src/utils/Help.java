package utils;

public class Help{
    public static final String PREFS="Usage:" +
            "\n  prefs [options]                            manage application preferences" +
            "\nOptions:" +
            "\n  -e, --export <export_directory>            set directory for exported .txt files" +
            "\n  -d, --data <data_directory>                set directory for character files" +
            "\n  -va, --viewalways <true/false>             display character after each command" +
            "\n  --help";
    public static final String IMPORT="Usage:" +
            "\n  import [options]                           import from .data file" +
            "\nOptions:" +
            "\n  --all                                      import all .data files" +
            "\n  --help";
    public static final String SAVE="Usage:" +
            "\n  import [options]                           save to .data file" +
            "\nOptions:" +
            "\n  --all                                      save all characters" +
            "\n  --help";
    public static final String STATS="Usage:" +
            "\n  stats [options]                            show stats" +
            "\nOptions:" +
            "\n  --help";
    public static final String NEW="Usage:" +
            "\n  new [options]                              create a character" +
            "\nOptions:" +
            "\n  --help";
    public static final String LOAD="Usage:" +
            "\n  load [options]                             load a character" +
            "\nOptions:" +
            "\n  --help";
    public static final String LIST="Usage:" +
            "\n  list [options]                             show imported characters" +
            "\nOptions:" +
            "\n  --help";
    public static final String INV="Usage:" +
            "\n  inv [options]                              show inventory" +
            "\nOptions:" +
            "\n  --help";
    public static final String VIEW="Usage:" +
            "\n  view [options]                             show overview" +
            "\nOptions:" +
            "\n  --help";
    public static final String GET="Usage:" +
            "\n  get [options, <item_name>]                 get a new item" +
            "\nOptions:" +
            "\n  -c, --count <item_count>                   " +
            "\n  -t, --type <item_type>                     " +
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
    public static final String USE="Usage:" +
            "\n  use [options, <item_name>]                 use items" +
            "\nOptions:" +
            "\n  -c, --count <amount>" +
            "\n  --help";
    public static final String ROLL="Usage:" +
            "\n  roll <num_dice>d<num_sides>[modifier] [options]" +
            "\n     num_dice:  # of dice to roll" +
            "\n     num_sides: # of sides" +
            "\n     modifier:  optional modifier to result of roll (eg: +5, -2, etc...)" +
            "\nOptions:" +
            "\n  --help";
    public static final String SPELLS="Usage:" +
            "\n  spell [options]" +
            "\nOptions:" +
            "\n  --learn" +
            "\n  --forget" +
            "\n  --cast" +
            "\n  --help";
    public static final String CAST="Usage:" +
            "\n  cast [options, <spell_name>]               cast a spell" +
            "\n  spell --cast [options, <spell_name>]       " +
            "\nOptions:" +
            "\n  -l, --level <level_num>                    " +
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
                System.out.println(LOAD);
                break;
            case "list":
                System.out.println(LIST);
                break;
            case "save":
                System.out.println(SAVE);
                break;
            case "import":
                System.out.println(IMPORT);
                break;
            case "n":
            case "new":
                System.out.println(NEW);
            break;
            case "v":
            case "view":
                System.out.println(VIEW);
                break;
            case "i":
            case "inv":
                System.out.println(INV);
                break;
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
            case "skills":
                System.out.println(SKILL);
                break;
            case "spell":
            case "spells":
                System.out.println(SPELLS);
                break;
            case "cast":
                System.out.println(CAST);
                break;
            case "learn":
                System.out.println(LEARN);
                break;
            case "forget":
                System.out.println(FORGET);
                break;
            case "s":
            case "stats":
                System.out.println(STATS);
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
            case "use":
                System.out.println(USE);
            case "levelup":
                System.out.println(LEVELUP);
                break;
            /*case "--list":
                System.out.println(COMMANDS_LIST);
                break;*/
            default:
                break;
        }
    }

    public static final String COMMANDS_LIST ="Commands List:" +
            "\n  import.............import a character from .data file" +
            "\n  load...............load a character to modify" +
            "\n  list...............show list of all imported characters" +
            "\n  save...............save character to a .data file" +
            "\n  new................create a new character" +
            "\n  view...............show overview" +
            "\n  stats..............show stats" +
            "\n  skills.............edit/view skills" +
            "\n  inv................show inventory" +
            "\n  get................get new items" +
            "\n  add/drop...........add/drop items from inventory" +
            "\n  equip/dequip.......equip/dequip items in inventory" +
            "\n  heal/hurt..........heal/hurt HP" +
            "\n  use................use items in inventory" +
            "\n  spell..............edit/view spells" +
            "\n  cast...............cast a spell" +
            "\n  learn..............learn a spell" +
            "\n  forget.............forget a spell" +
            "\n  levelup............increase character level" +
            "\n  roll...............roll dice" +
            "\n  prefs..............edit program preferences" +
            "\n  help...............help menu" +
            "\n  quit...............quit the program" +
            "\nEnter 'help command' for details";

}
