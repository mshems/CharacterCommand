package jterminal.optional.properties;

import jterminal.core.CommandAction;
import jterminal.core.CommandMap;
import jterminal.core.JTerminal;

import java.util.Properties;

public class PropertiesConfigCommand {
    private CommandMap commandMap;

    PropertiesConfigCommand(){
        commandMap = new CommandMap();
    }

    void addCustomBehavior(String propertyName, CommandAction commandAction){
        commandMap.put(propertyName, commandAction);
    }

    void removeCustomBehavior(String propertyName){
        commandMap.remove(propertyName);
    }

    void config(JTerminal terminal, Properties properties) {
        if (terminal.hasTokens()) {
            String propertyName = terminal.nextToken();
            if (commandMap.get(propertyName) != null) {
                commandMap.get(propertyName).executeCommand();
            } else {
                if (properties.getProperty(propertyName) != null && terminal.hasTokens()) {
                    properties.setProperty(propertyName, terminal.nextToken());
                }
            }
            PropertiesManager.writeProperties(terminal);
        } else {
            terminal.out.println("No property specified");
        }
    }
}
