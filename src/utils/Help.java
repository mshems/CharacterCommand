package utils;

public class Help{

    public static final String GET="Usage:" +
            "\n  get [options, <item_name>]" +
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
            "\n  add [options]" +
            "\nOptions:" +
            "\n  -c, --count <item_count>                   " +
            "\n  --help";
    public static final String DROP="Usage:" +
            "\n  drop [options]" +
            "\nOptions:" +
            "\n  -c, --count <item_count>                   " +
            "\n  --all                                      toggle dropping all of specified item " +
            "\n  --help";
    public static final String SKILL="Usage:" +
            "\n  skill [options, <skill_name>]" +
            "\nOptions:" +
            "\n  -t, --train                                gain proficiency" +
            "\n  -f, --forget                               lose proficiency" +
            "\n  -e, --expert                               gain expertise" +
            "\n  -v  --view                                 view the specified skill" +
            "\n  -va, --viewall                             view all skills" +
            "\n  --help";
    public static final String LEVELUP="Usage:" +
            "\n  levelup [options]" +
            "\nOptions:" +
            "\n  -l, --level <level_number>                 " +
            "\n  --help";
    public static final String CAST="Usage:" +
            "\n  cast [options, <spell_name>]               " +
            "\nOptions:" +
            "\n  -l, --level <level_num>                    " +
            "\n  --help";
    public static final String HEAL="Usage:" +
            "\n  heal [options]" +
            "\nOptions:" +
            "\n  -hp, --health <hp_healed>                  " +
            "\n  --all                                      toggles healing to max hp" +
            "\n  --help";
    public static final String HURT="Usage:" +
            "\n  hurt [options]" +
            "\nOptions:" +
            "\n  -hp, --health <hp_lost>                    " +
            "\n  --all                                      toggle dropping to 0 hp" +
            "\n  --help";
    public static final String EQUIP="Usage:" +
            "\n  equip [item_name]" +
            "\nOptions:" +
            "\n  --help";
    public static final String DEQUIP="Usage:" +
            "\n  dequip [item_name]" +
            "\nOptions:" +
            "\n  --help";

    public static void helpMenu(String command){
        switch(command){
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
            default:
                System.out.println("HELP :D");
        }
    }
}
