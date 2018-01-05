package jterminal.optional.properties;

import jterminal.core.CommandAction;
import jterminal.core.IllegalTokenException;
import jterminal.core.JTerminal;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesManager{
    private static final String COMMAND_CONFIG = "terminal-config";
    private static final String FILENAME = "jterminal.properties";
    private static final String PATH = "./"+FILENAME;
    private static Properties properties;
    private static PropertiesConfigCommand propertiesConfigCommand = new PropertiesConfigCommand();

    public static void addPropertiesManager(JTerminal terminal){
        properties = new Properties();
        terminal.putCommand(COMMAND_CONFIG, ()->propertiesConfigCommand.config(terminal, properties));
        addProperty("font-size", "16", () -> {
            if (!terminal.hasTokens()) return;
            try {
                int fontSize = terminal.nextIntToken();
                properties.setProperty("font-size", "" + fontSize);
                terminal.setFontSize(fontSize);
            } catch (IllegalTokenException e) {
                terminal.out.println("ERROR: Font size must be an integer");
            }
        });
    }

    public static String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }

    public static void setProperty(String propertyName, String propertyValue){
        properties.setProperty(propertyName,propertyValue);
    }

    public static void addProperty(String propertyName, String defaultValue){
        properties.setProperty(propertyName, defaultValue);
    }

    public static void addProperty(String propertyName, String defaultValue, CommandAction customBehavior){
        properties.setProperty(propertyName, defaultValue);
        propertiesConfigCommand.addCustomBehavior(propertyName, customBehavior);
    }

    public static void removeProperty(String propertyName){
        properties.remove(propertyName);
        propertiesConfigCommand.removeCustomBehavior(propertyName);
    }

     public static void readProperties(JTerminal terminal){
        OutputStream out = null;
        Path configPath = Paths.get(PATH);
        if(!Files.exists(configPath)){
            try{
                out= new FileOutputStream(FILENAME);
                properties.store(out, null);
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if(out != null){
                    try{
                        out.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        loadProperties(terminal);
    }

    private static void loadProperties(JTerminal terminal){
        InputStream in = null;
        try{
            in = new FileInputStream(FILENAME);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try{
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeProperties(JTerminal terminal){
        OutputStream out = null;
        try {
            out = new FileOutputStream(FILENAME);
            properties.store(out, null);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
