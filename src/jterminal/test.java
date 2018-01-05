package jterminal;

import jterminal.core.CommandMap;
import jterminal.core.JTerminal;
import jterminal.core.JTerminalPrinter;
import jterminal.core.theme.Theme;
import jterminal.optional.theme.ThemeManager;
import jterminal.optional.menu.ListMenu;
import jterminal.optional.menu.MenuFactory;
import jterminal.optional.properties.PropertiesManager;
import jterminal.optional.theme.ThemeXMLHandler;

import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class test{
    /**
     * Creates and runs a JTerminal for demonstration
     * @param args command-line arguments
     */
    public static void main(String[] args){
        //ThemeXMLHandler.writeTheme(Theme.DEFAULT_THEME());
        //ThemeXMLHandler.writeTheme(Theme.DEFAULT_LIGHT_THEME());
        //ThemeXMLHandler.writeTheme(Theme.DEFAULT_DARK_THEME());

        //Create new JTerminal instance
        JTerminal terminal = new JTerminal("JTerminal");
        //Set new app icon
        terminal.setAppIcon("./res/jterminal-icon.png");
        //Add properties manager
        PropertiesManager.addPropertiesManager(terminal);

        //Add theme property and config command action
        PropertiesManager.addProperty("theme","default", ()-> {
            //Theme selection menu
            String themeName = ListMenu.queryMenu(new MenuFactory()
                    .setDirection(ListMenu.HORIZONTAL)
                    .buildObjectMenu(terminal, ThemeManager.themeList, (str) -> str));
            if (themeName == null) return;
            //Set theme property and load selected theme
            PropertiesManager.setProperty("theme", themeName);
            ThemeManager.setTheme(terminal, themeName);
        });

        //Define startup behavior
        terminal.setStartBehavior((t)->{
            //Read properties from file
            PropertiesManager.readProperties(terminal);
            //Get list of themes
            ThemeManager.makeThemesList();
            //Load theme
            ThemeManager.setTheme(terminal, PropertiesManager.getProperty("theme"));

            try{
                //Load font size
                t.setFontSize(Integer.parseInt(PropertiesManager.getProperty("font-size")));
            } catch (NumberFormatException e){
                //Default to font size in theme
                t.setFontSize(terminal.getTheme().font.getSize());
            }

            //Splash screen displaying version number
            t.out.println("JTerminal v0.1.2", JTerminalPrinter.CENTERED);
            t.out.println("------------------------", JTerminalPrinter.CENTERED);
        });

        //Define close behavior
        terminal.setCloseBehavior(PropertiesManager::writeProperties);

//Add commands:
        //Display basic theme properties
        terminal.putCommand("theme", ()->{
            Theme theme = terminal.getTheme();
            terminal.out.println("Name: "+theme.themeName);
            terminal.out.println(String.format("Background: #%02x%02x%02x", theme.backgroundColor.getRed(),
                    theme.backgroundColor.getGreen(), theme.backgroundColor.getBlue()));
            terminal.out.println(String.format("Foreground: #%02x%02x%02x", theme.foregroundColor.getRed(),
                    theme.foregroundColor.getGreen(), theme.foregroundColor.getBlue()));
        });

        //Input then display a string
        terminal.putCommand("print", ()->{
            String s = terminal.queryString("Enter a string: ");
            terminal.out.print(s);
        });

        //Show a menu of all top-level commands
        terminal.putCommand("command-menu", ()->{
            CommandMap map = (CommandMap)terminal.getCommandMap().clone();
            map.remove("command-menu");
            ListMenu.queryMenu(new MenuFactory()
                    .setDirection(ListMenu.VERTICAL)
                    .buildActionMenu(terminal, map))
                .executeCommand();

        });

        //Show a basic example of a menu
        terminal.putCommand("menu", ()->{
            List<String> ll = Arrays.asList("1","2","3","4","5");
            String s = ListMenu.queryMenu(new MenuFactory()
                    .setDirection(ListMenu.HORIZONTAL)
                    .buildObjectMenu(terminal, ll, (str)->"Label #"+str));
            if(s!=null) terminal.out.println("You selected: "+s);
        },"m");

        //Quit command with options
        terminal.putCommand("quit", ()->{
            if(terminal.hasTokens()){
                String token = terminal.nextToken();
                if(token.equals("-f") || token.equals("--force")){
                    terminal.close();
                    System.exit(0);
                }
            } else {
                if(terminal.queryYN("Are you sure? [Y/N] : ")){
                    terminal.close();
                    System.exit(0);
                }
            }
        },"q");

        /*jterminal.putCommand("restart", ()->{
            jterminal.close();
            jterminal.start();
        });*/

        //Start the jterminal
        terminal.start();
    }
}
