package jterminal.core.theme;

import java.awt.*;
import java.awt.image.ColorConvertOp;
import java.io.Serializable;
import java.util.Arrays;

public class Theme implements Serializable{
    private static final Color DEFAULT_BACKGROUND_COLOR_DARK = new Color(36, 36, 36);;
    private static final Color DEFAULT_FOREGROUND_COLOR_DARK = new Color(245, 245, 245);
    private static final Color DEFAULT_CARET_COLOR_DARK = new Color(245,245,245);
    private static final Color DEFAULT_HIGHLIGHT_COLOR_DARK = new Color(220, 220, 220);

    private static final Color DEFAULT_BACKGROUND_COLOR_LIGHT = new Color(245, 245, 245);;
    private static final Color DEFAULT_FOREGROUND_COLOR_LIGHT = new Color(33, 33, 33);
    private static final Color DEFAULT_CARET_COLOR_LIGHT = new Color(33,33,33);
    private static final Color DEFAULT_HIGHLIGHT_COLOR_LIGHT = new Color(64, 64, 64);

    private static final Font  DEFAULT_FONT_MONO = new Font(Font.MONOSPACED, Font.PLAIN, 16);

    public String themeName;
    public Color backgroundColor;
    public Color foregroundColor;
    public Color caretColor;
    public Color highlightColor;
    public Font font;

    public Theme(String name){
        themeName = name;
        font = DEFAULT_FONT_MONO;
        backgroundColor = Color.BLACK;
        foregroundColor = Color.WHITE;
        caretColor = Color.WHITE;
        highlightColor = Color.WHITE;
    }

   public static Theme DEFAULT_THEME(){
        Theme t = new Theme("default");
        t.backgroundColor = Color.BLACK;
        t.foregroundColor = Color.WHITE;
        t.caretColor = Color.WHITE;
        t.highlightColor = Color.WHITE;
        return t;
    }

    public static Theme DEFAULT_DARK_THEME(){
        Theme t = new Theme("dark");
        t.backgroundColor = DEFAULT_BACKGROUND_COLOR_DARK;
        t.foregroundColor = DEFAULT_FOREGROUND_COLOR_DARK;
        t.caretColor = DEFAULT_CARET_COLOR_DARK;
        t.highlightColor = DEFAULT_HIGHLIGHT_COLOR_DARK;
        return t;
    }

    public static Theme DEFAULT_LIGHT_THEME(){
        Theme t = new Theme("light");
        t.backgroundColor = DEFAULT_BACKGROUND_COLOR_LIGHT;
        t.foregroundColor = DEFAULT_FOREGROUND_COLOR_LIGHT;
        t.caretColor = DEFAULT_CARET_COLOR_LIGHT;
        t.highlightColor = DEFAULT_HIGHLIGHT_COLOR_LIGHT;
        return t;
    }
}

