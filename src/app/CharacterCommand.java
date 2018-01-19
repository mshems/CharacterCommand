package app;

import app.io.IOUtils;
import app.io.Reader;
import app.io.Writer;
import app.ui.useraction.*;
import core.character.AbstractCharacter;
import core.magic.SpellSlot;
import jterminal.core.*;
import jterminal.core.behavior.*;
import core.character.PlayerCharacter;
import jterminal.optional.menu.ListMenu;
import jterminal.optional.menu.MenuFactory;
import jterminal.optional.properties.*;
import jterminal.optional.theme.ThemeManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class CharacterCommand {
    private static final String defaultExportDir = "./data";
    private static final String defaultDataDir = "./data";

    public JTerminal terminal;
    private PlayerCharacter activeCharacter;
    private HashMap<String, PlayerCharacter> loadedCharacters = new HashMap<>();

    public CharacterCommand(){
        initTerminal();
        addCommands();
    }

    public synchronized void start(){
        terminal.start();
    }

    private void initTerminal(){
        this.terminal = new JTerminal("CharacterCommand");
        terminal.setDefaultPrompt("CharacterCommand ~ ");
        terminal.setAppIcon("./res/jterminal-icon.png");
        PropertiesManager.addPropertiesManager(terminal);
        PropertiesManager.addProperty("export", defaultExportDir);
        PropertiesManager.addProperty("data", defaultDataDir);
        PropertiesManager.addProperty("theme","default", ()-> {
            String themeName = ListMenu.queryMenu(new MenuFactory()
                    .setDirection(ListMenu.HORIZONTAL)
                    .buildObjectMenu(terminal, ThemeManager.themeList, (str) -> str));
            if (themeName == null) return;
            PropertiesManager.setProperty("theme", themeName);
            ThemeManager.setTheme(terminal, themeName);
        });



        terminal.setStartBehavior((t)->{
            PropertiesManager.readProperties(terminal);
            ThemeManager.makeThemesList();
            ThemeManager.setTheme(terminal, PropertiesManager.getProperty("theme"));
            IOUtils.checkDirs(this);

            for(File f: Paths.get(this.dataDir()).toFile().listFiles()){
                if(f.getName().endsWith(".data")){
                    this.loadCharacter(Reader.readCharacter(this, f.getName()));
                }
            }

            if(activeCharacter!=null){
                terminal.setPrompt(activeCharacter.getName()+" @ CharacterCommand ~ ");
            } else {
                terminal.resetPrompt();
            }

            try{
                t.setFontSize(Integer.parseInt(PropertiesManager.getProperty("font-size")));
            } catch (NumberFormatException e){
                t.setFontSize(terminal.getTheme().font.getSize());
            }
        });

        terminal.setCommandExecutor(new CommandExecutor(terminal){
            @Override
            public void doCommand(String token) throws UnknownCommandException{
                if (activeCharacter != null){
                    super.doCommand(token);
                }
                if(activeCharacter!=null){
                    terminal.setPrompt(activeCharacter.getName()+" @ CharacterCommand ~ ");
                } else {
                    terminal.resetPrompt();
                }
            }
        });

        //Define close behavior
        terminal.setCloseBehavior((terminal)->{
            PropertiesManager.writeProperties(terminal);
            Writer.writeCharacter(this, activeCharacter, false);
            //TODO: saving
        });
    }

    private void addCommands(){
        terminal.putCommand("load", this::load, "l");

        terminal.putCommand("heal",     ()-> HealthAction.heal(this));
        terminal.putCommand("hurt",     ()-> HealthAction.hurt(this));
        terminal.putCommand("add",      ()-> InventoryAction.add(this));
        terminal.putCommand("get",      ()-> InventoryAction.add(this));
        terminal.putCommand("view",     ()-> terminal.out.println(activeCharacter),"v");
        terminal.putCommand("inv",      ()-> InventoryAction.viewInventory(this), "i");
        terminal.putCommand("I",        ()-> InventoryAction.viewInventoryMenu(this));
        terminal.putCommand("cast",     ()-> SpellAction.cast(this));
        terminal.putCommand("spells",   ()-> terminal.out.println(getActiveCharacter().getSpellBook().toString()));
        terminal.putCommand("equip",    ()-> EquipAction.equip(this));
        terminal.putCommand("dequip",    ()-> EquipAction.dequip(this));

        terminal.putCommand("new",  ()->{
            PlayerCharacter pc = NewCharacterAction.newCharacter(this);
            activeCharacter = loadedCharacters.get(pc.getName().toLowerCase());
        });

        terminal.putCommand("slots",    ()->{
            for(SpellSlot s:activeCharacter.getSpellSlots()){
                if(s.getMaxValue()>0) terminal.out.println(s.toString());
            }
        });

        terminal.putCommand("list", ()->{
            for(PlayerCharacter p:loadedCharacters.values()){
                terminal.out.println(p.getName());
            }
        });

        terminal.putCommand("save",     ()-> {
                Writer.writeCharacter(this, activeCharacter, true);
            }, "s");


        terminal.putCommand("quit", ()->{
                if(terminal.hasTokens()){
                    String token = terminal.nextToken();
                    if(token.equalsIgnoreCase("-f") || token.equalsIgnoreCase("--force")){
                        terminal.close();
                        System.exit(0);
                    }
                } else if(terminal.queryYN("Are you sure? [Y/N]: ")) {
                    terminal.close();
                    System.exit(0);
                }
            }, "q");
    }


    private void load(){
        if(!terminal.hasTokens()){
            PlayerCharacter pc = ListMenu.queryMenu(
                    new MenuFactory()
                        .setDirection(ListMenu.VERTICAL)
                        .buildObjectMenu(terminal, loadedCharacters.values(), AbstractCharacter::getName));
            if(pc!=null) activeCharacter = pc;
        }
    }

    public void loadCharacter(PlayerCharacter pc){
        loadedCharacters.put(pc.getName().toLowerCase(), pc);
    }

    public PlayerCharacter getActiveCharacter() {
        return activeCharacter;
    }

    public void setActiveCharacter(PlayerCharacter activeCharacter) {
        this.activeCharacter = activeCharacter;
    }

    public String dataDir(){
        return PropertiesManager.getProperty("data");
    }

    public String exportDir(){
        return PropertiesManager.getProperty("export");
    }
}

