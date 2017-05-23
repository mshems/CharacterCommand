package app;

import character.PlayerCharacter;
import utils.Help;
import utils.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IOHandler{
    static void checkDirs(){
        if (!Files.exists(App.propertiesHandler.getDataDir())){
            try {
                Files.createDirectories(App.propertiesHandler.getDataDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(App.propertiesHandler.getExportDir())){
            try {
                Files.createDirectories(App.propertiesHandler.getExportDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**IMPORT**************************************************************************************************************/
    void importCharacter(){
        App.tokens.pop();
        String characterName=null;
        if(!App.tokens.isEmpty()){
            if (App.tokens.contains("--help")){
                System.out.println(Help.IMPORT);
            } else if (App.tokens.contains("--all")){
                importAll(true);
            } else {
                StringBuilder nameBuilder = new StringBuilder();
                while (!App.tokens.isEmpty()) {
                    nameBuilder.append(App.tokens.pop());
                    nameBuilder.append(" ");
                }
                characterName = nameBuilder.toString().trim();
            }
        } else {
            System.out.println("manual import placeholder -- use command arguments for now");
        }
        if(characterName!=null){
            Path charPath = Paths.get(App.propertiesHandler.getDataDir() + "/" + characterName + ".data");
            if (Files.exists(charPath)){
                File charFile = charPath.toFile();
                try{
                    ObjectInputStream inStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(charFile)));
                    PlayerCharacter playerCharacter = (PlayerCharacter) inStream.readObject();
                    inStream.close();
                    if (!App.characterList.containsKey(playerCharacter.getName().toLowerCase())){
                        App.characterList.put(playerCharacter.getName().toLowerCase(), playerCharacter);
                        System.out.println("Imported "+playerCharacter.getName());
                    } else {
                        System.out.println(playerCharacter.getName() + " already imported");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void importAll(boolean verbose) {
        for (File file : App.propertiesHandler.getDataDir().toFile().listFiles()){
            if (file.getName().endsWith(".data")){
                try{
                    ObjectInputStream inStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                    PlayerCharacter playerCharacter = (PlayerCharacter) inStream.readObject();
                    inStream.close();
                    if (!App.characterList.containsKey(playerCharacter.getName().toLowerCase())){
                        App.characterList.put(playerCharacter.getName().toLowerCase(), playerCharacter);
                    }
                    if (verbose){
                        System.out.println(playerCharacter.getName() + " already imported");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        if (verbose){
            System.out.println("All characters imported");
        }
    }

    /**IMPORT**************************************************************************************************************/
    boolean export(){
        App.tokens.pop();
        PlayerCharacter pc=null;
        String characterName=null;
        if(!App.tokens.isEmpty()){
            if(App.tokens.contains("--help")){
                System.out.println(Help.EXPORT);
                return true;
            } else if (App.tokens.contains("--all")){
                exportAll();
                return true;
            } else {
                StringBuilder nameBuilder = new StringBuilder();
                while (!App.tokens.isEmpty()) {
                    nameBuilder.append(App.tokens.pop());
                    nameBuilder.append(" ");
                }
                characterName = nameBuilder.toString().trim();
                pc = App.characterList.get(characterName.toLowerCase());

            }
        } else {
            pc = App.activeChar;
        }
        if(pc!=null && App.characterList.containsKey(pc.getName().toLowerCase())){
            Path path = Paths.get(App.propertiesHandler.getExportDir()+"/" + pc.getName() + ".txt");
            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()));
                writer.write(pc.toTextFile());
                writer.close();
                System.out.println("Exported "+pc.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Message.ERROR_NO_CHAR);
        }
        return true;
    }

    private void exportAll() {
        try{
            Path path;

            for (PlayerCharacter pc:App.characterList.values()){
                path = Paths.get(App.propertiesHandler.getExportDir() + "/" + pc.getName() + ".txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()));
                writer.write(pc.toTextFile());
                writer.close();
            }
            System.out.println("All characters exported");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**SAVE****************************************************************************************************************/
    void saveChar() {
        PlayerCharacter pc=null;
        String characterName;
        App.tokens.pop();
        if(!App.tokens.isEmpty()){
            if(App.tokens.contains("--help")){
                System.out.println(Help.EXPORT);
            } else if (App.tokens.contains("--all")){
                saveAll();
            } else {
                StringBuilder nameBuilder = new StringBuilder();
                while (!App.tokens.isEmpty()) {
                    nameBuilder.append(App.tokens.pop());
                    nameBuilder.append(" ");
                }
                characterName = nameBuilder.toString().trim();
                pc = App.characterList.get(characterName.toLowerCase());
            }
        }else{
            pc = App.activeChar;
        }
        if(pc!=null && App.characterList.containsKey(pc.getName().toLowerCase())){
            try{
                Path path = Paths.get(App.propertiesHandler.getDataDir() +"/" + pc.getName() + ".data");
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path.toString())));
                out.writeObject(pc);
                out.close();
                System.out.println("Saved " + pc.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveAll(){
        try{
            Path path;
            for(PlayerCharacter pc:App.characterList.values()){
                path = Paths.get(App.propertiesHandler.getDataDir()  + "/" + pc.getName() + ".data");
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path.toString())));
                out.writeObject(pc);
                out.close();
            }
            System.out.println("Saved all characters");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**LOAD****************************************************************************************************************/
    void loadChar(){
        String command = App.tokens.pop();
        if (!App.tokens.isEmpty()){
            loadChar(command);
        } else {
            System.out.print("Enter name of character to load, or enter 'new' to create a new character: ");
            String characterName = App.scanner.nextLine().toLowerCase();
            if (characterName.equalsIgnoreCase("new")) {
                App.createCharacter();
            } else if (!characterName.equalsIgnoreCase("quit")) {
                if (App.characterList.get(characterName) != null) {
                    App.activeChar = App.characterList.get(characterName);
                    System.out.println(characterName + " loaded");
                } else {
                    System.out.println("ERROR: Character not found");
                }
            }
        }
    }

    private void loadChar(String command){
        String characterName = "";
        while (!App.tokens.isEmpty()){
            characterName += App.tokens.pop()+" ";
        }
        characterName = characterName.trim().toLowerCase();
        if (App.characterList.get(characterName) != null) {
            App.activeChar = App.characterList.get(characterName);
            System.out.println(App.activeChar.getName() + " loaded");
        } else {
            System.out.println("ERROR: Character not found");
        }
    }
}
