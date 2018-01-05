package jterminal.optional.theme;

import jterminal.core.JTerminal;
import jterminal.core.theme.Theme;

import java.io.File;
import java.util.ArrayList;

public class ThemeManager {
    public static ArrayList<String> themeList;

    public static void makeThemesList(){
        if(themeList==null) themeList = new ArrayList<>();
        ThemeXMLHandler.checkDirs();
        File themeDir = new File(ThemeXMLHandler.THEME_DIR);
        if(themeDir.isDirectory()) {
            for (File f : themeDir.listFiles()) {
                if(f != null) {
                    if(f.getName().endsWith(".xml")){
                       themeList.add(f.getName().replace(".xml", ""));
                    }
                }
            }
        }
    }

    public static void setTheme(JTerminal terminal, String themeName){
        terminal.setTheme(ThemeXMLHandler.readTheme(themeName));
    }

    public static void saveTheme(Theme theme){
        ThemeXMLHandler.writeTheme(theme);
    }
}
