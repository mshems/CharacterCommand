package utils;

public class Help{
    private static final String newLine = System.lineSeparator();

    public static final String PREFS="Usage:" +
            newLine+"  prefs [options]                            manage application preferences" +
            newLine+"Options:" +
            newLine+"  -e, --toTextFile <export_directory>        set directory for exported .txt files" +
            newLine+"  -d, --data <data_directory>                set directory for character files" +
            newLine+"  -va, --viewalways <true/false>             display character after each command" +
            newLine+"  -r, --resume <true/false>                  reopen last character on startup " +
            newLine+"  --help";
    public static final String IMPORT="Usage:" +
            newLine+"  import [character_name][options]           import from .data file" +
            newLine+"Options:" +
            newLine+"  --all                                      import all .data files" +
            newLine+"  --help";
    public static final String EXPORT="Usage:" +
            newLine+"  toTextFile [character_name][options]       export to .txt file" +
            newLine+"Options:" +
            newLine+"  --all                                      export all characters" +
            newLine+"  --help";
    public static final String SAVE="Usage:" +
            newLine+"  import [options]                           save to .data file" +
            newLine+"Options:" +
            newLine+"  --all                                      save all characters" +
            newLine+"  --help";
    public static final String STATS="Usage:" +
            newLine+"  stats [options]                            show stats" +
            newLine+"Options:" +
            newLine+"  --help";
    public static final String NEW="Usage:" +
            newLine+"  new [options]                              create a character" +
            newLine+"Options:" +
            newLine+"  --help";
    public static final String LOAD="Usage:" +
            newLine+"  load [options]                             load a character" +
            newLine+"Options:" +
            newLine+"  --help";
    public static final String LIST="Usage:" +
            newLine+"  list [options]                             show imported characters" +
            newLine+"Options:" +
            newLine+"  --help";
    public static final String INV="Usage:" +
            newLine+"  inv [options]                              show inventory" +
            newLine+"Options:" +
            newLine+"  --help";
    public static final String VIEW="Usage:" +
            newLine+"  view [options]                             show overview" +
            newLine+"Options:" +
            newLine+"  --help";
    public static final String GET="Usage:" +
            newLine+"  get [options, <item_name>]                 get a new item" +
            newLine+"Options:" +
            newLine+"  -c, --count <item_count>                   " +
            newLine+"  -t, --type <item_type>                     " +
            newLine+"     item_type:" +
            newLine+"         i ,item" +
            newLine+"         c, consumable" +
            newLine+"         e, equippable" +
            newLine+"         a, armor" +
            newLine+"         w, weapon" +
            newLine+"  -e, --effect <fx_tgt> <fx_bonus>           " +
            newLine+"     fx_tgt:   name of stat affected         " +
            newLine+"     fx_bonus: bonus applied to fx_target    " +
            newLine+"  -at, --armortype <armor_type>              type of armor" +
            newLine+"     armor_type:" +
            newLine+"         l, light" +
            newLine+"         m, medium" +
            newLine+"         h, heavy" +
            newLine+"         s, shield" +
            newLine+"         o, other" +
            newLine+"  -ac, --armorclass <armor_class>            base AC of the armor" +
            newLine+"  --help";
    public static final String ADD="Usage:" +
            newLine+"  add [options]                              add more of an existing item" +
            newLine+"Options:" +
            newLine+"  -c, --count <item_count>                   " +
            newLine+"  --help";
    public static final String DROP="Usage:" +
            newLine+"  drop [options]                             drop items from inventory" +
            newLine+"Options:" +
            newLine+"  -c, --count <item_count>                   " +
            newLine+"  --all                                      toggle dropping all of specified item " +
            newLine+"  --help";
    public static final String SKILL="Usage:" +
            newLine+"  skill [options, <skill_name>]              view and train skills" +
            newLine+"Options:" +
            newLine+"  -t, --train                                gain proficiency" +
            newLine+"  -f, --forget                               lose proficiency" +
            newLine+"  -e, --expert                               gain expertise" +
            newLine+"  -v  --view                                 view the specified skill" +
            newLine+"  -va, --viewall                             view all skills" +
            newLine+"  --help";
    public static final String LEVELUP="Usage:" +
            newLine+"  levelup [options]                          level up character" +
            newLine+"Options:" +
            newLine+"  -l, --level <level_number>                 " +
            newLine+"  --help";
    public static final String HEAL="Usage:" +
            newLine+"  heal [options]                             gain hp" +
            newLine+"Options:" +
            newLine+"  -hp, --health <hp_healed>                  " +
            newLine+"  --all                                      toggles healing to max hp" +
            newLine+"  --help";
    public static final String HURT="Usage:" +
            newLine+"  hurt [options]                             lose hp" +
            newLine+"Options:" +
            newLine+"  -hp, --health <hp_lost>                    " +
            newLine+"  --all                                      toggle dropping to 0 hp" +
            newLine+"  --help";
    public static final String EQUIP="Usage:" +
            newLine+"  equip [item_name]                          equip items" +
            newLine+"Options:" +
            newLine+"  --help";
    public static final String DEQUIP="Usage:" +
            newLine+"  dequip [item_name]                         dequip items" +
            newLine+"Options:" +
            newLine+"  --help";
    public static final String USE="Usage:" +
            newLine+"  use [options, <item_name>]                 use items" +
            newLine+"Options:" +
            newLine+"  -c, --count <amount>" +
            newLine+"  --help";
    public static final String ROLL="Usage:" +
            newLine+"  roll <num_dice>d<num_sides>[modifier] [options]" +
            newLine+"     num_dice:  # of dice to roll" +
            newLine+"     num_sides: # of sides" +
            newLine+"     modifier:  optional modifier to result of roll (eg: +5, -2, etc...)" +
            newLine+"Options:" +
            newLine+"  --help";
    public static final String SPELLSLOTS="Usage:" +
            newLine+"  spellslots [options]" +
            newLine+"Options:" +
            newLine+"  --charge" +
            newLine+"  --set [options]" +
            newLine+"    -l, --level <level_num>" +
            newLine+"    -m, --max <max_slots>" +
            newLine+"  --help";
    public static final String SETSLOTS="Usage:" +
            newLine+"  spellslots --set [options]" +
            newLine+"Options:" +
            newLine+"  -l, --level <level_num>" +
            newLine+"  -m, --max <max_slots>" +
            newLine+"  --help";
    public static final String CHARGE="Usage:"+
            newLine+"  cast [options]" +
            newLine+"Options:" +
            newLine+"  -l, --level <level_num>" +
            newLine+"  -c, --count <max_slots>" +
            newLine+"  --all" +
            newLine+"  --help";
    public static final String SPELLS="Usage:" +
            newLine+"  spell [options]" +
            newLine+"Options:" +
            newLine+"  --slots" +
            newLine+"  --learn" +
            newLine+"  --forget" +
            newLine+"  --cast" +
            newLine+"  --help";
    public static final String CAST="Usage:" +
            newLine+"  cast [options, <spell_name>]               cast a spell" +
            newLine+"  spell --cast [options, <spell_name>]       " +
            newLine+"Options:" +
            newLine+"  -l, --level <level_num>                    " +
            newLine+"  --help";
    public static final String LEARN="Usage:" +
            newLine+"  learn [options, <spell_name>]" +
            newLine+"  spell --learn [options, <spell_name>]" +
            newLine+"Options:" +
            newLine+"  -l, --level" +
            newLine+"  --help";
    public static final String FORGET="Usage:" +
            newLine+"  forget [options, <spell_name>]" +
            newLine+"  spell --forget [options, <spell_name>]" +
            newLine+"Options:" +
            newLine+"  --help";




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
            case "toTextFile":
                System.out.println(EXPORT);
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
            case "spellslots":
                System.out.println(SPELLSLOTS);
                break;
            case "charge":
                System.out.println(CHARGE);
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
            default:
                break;
        }
    }

    public static final String COMMANDS_LIST ="Commands List:" +
            newLine+"  import.............import a character from .data file" +
            newLine+"  export.............export a character to a .txt file" +
            newLine+"  load...............load a character to modify" +
            newLine+"  list...............show list of all imported characters" +
            newLine+"  save...............save character to a .data file" +
            newLine+"  new................create a new character" +
            newLine+"  view...............show overview" +
            newLine+"  stats..............show stats" +
            newLine+"  skills.............edit/view skills" +
            newLine+"  inv................show inventory" +
            newLine+"  get................get new items" +
            newLine+"  add/drop...........add/drop items from inventory" +
            newLine+"  equip/dequip.......equip/dequip items in inventory" +
            newLine+"  heal/hurt..........heal/hurt HP" +
            newLine+"  use................use items in inventory" +
            newLine+"  spell..............edit/view spells" +
            newLine+"  spellslots.........edit/view spell slots" +
            newLine+"  charge.............recharge spell slots" +
            newLine+"  cast...............cast a spell" +
            newLine+"  learn..............learn a spell" +
            newLine+"  forget.............forget a spell" +
            newLine+"  levelup............increase character level" +
            newLine+"  roll...............roll dice" +
            newLine+"  prefs..............edit program preferences" +
            newLine+"  help...............help menu" +
            newLine+"  quit...............quit the program" +
            newLine+"Enter 'help <command_name>' for details";

}
