package app.ui.terminal.optional.properties;

import app.ui.terminal.core.CommandAction;
import app.ui.terminal.core.CommandMap;
import app.ui.terminal.core.JTerminal;

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
        }
    }
}
