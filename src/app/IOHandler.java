package app;

import character.PlayerCharacter;
import utils.Help;
import utils.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IOHandler{
    public static void checkDirs(){
        if (!Files.exists(CharacterCommand.propertiesHandler.getDataDir())){
            try {
                Files.createDirectories(CharacterCommand.propertiesHandler.getDataDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(CharacterCommand.propertiesHandler.getExportDir())){
            try {
                Files.createDirectories(CharacterCommand.propertiesHandler.getExportDir());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**IMPORT**************************************************************************************************************/
    public void importCharacter(){
        CharacterCommand.tokens.pop();
        String characterName=null;
        if(!CharacterCommand.tokens.isEmpty()){
            if (CharacterCommand.tokens.contains("--help")){
                System.out.println(Help.IMPORT);
            } else if (CharacterCommand.tokens.contains("--all")){
                importAll(true);
            } else {
                StringBuilder nameBuilder = new StringBuilder();
                while (!CharacterCommand.tokens.isEmpty()) {
                    nameBuilder.append(CharacterCommand.tokens.pop());
                    nameBuilder.append(" ");
                }
                characterName = nameBuilder.toString().trim();
            }
        } else {
            System.out.println("manual import placeholder -- use command arguments for now");
        }
        if(characterName!=null){
            Path charPath = Paths.get(CharacterCommand.propertiesHandler.getDataDir() + "/" + characterName + ".data");
            if (Files.exists(charPath)){
                File charFile = charPath.toFile();
                try{
                    ObjectInputStream inStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(charFile)));
                    PlayerCharacter playerCharacter = (PlayerCharacter) inStream.readObject();
                    inStream.close();
                    if (!CharacterCommand.characterList.containsKey(playerCharacter.getName().toLowerCase())){
                        CharacterCommand.characterList.put(playerCharacter.getName().toLowerCase(), playerCharacter);
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

    public void importAll(boolean verbose) {
        for (File file : CharacterCommand.propertiesHandler.getDataDir().toFile().listFiles()){
            if (file.getName().endsWith(".data")){
                try{
                    ObjectInputStream inStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                    PlayerCharacter playerCharacter = (PlayerCharacter) inStream.readObject();
                    inStream.close();
                    if (!CharacterCommand.characterList.containsKey(playerCharacter.getName().toLowerCase())){
                        CharacterCommand.characterList.put(playerCharacter.getName().toLowerCase(), playerCharacter);
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
    public boolean export(){
        CharacterCommand.tokens.pop();
        PlayerCharacter pc=null;
        String characterName=null;
        if(!CharacterCommand.tokens.isEmpty()){
            if(CharacterCommand.tokens.contains("--help")){
                System.out.println(Help.EXPORT);
                return true;
            } else if (CharacterCommand.tokens.contains("--all")){
                exportAll();
                return true;
            } else {
                StringBuilder nameBuilder = new StringBuilder();
                while (!CharacterCommand.tokens.isEmpty()) {
                    nameBuilder.append(CharacterCommand.tokens.pop());
                    nameBuilder.append(" ");
                }
                characterName = nameBuilder.toString().trim();
                pc = CharacterCommand.characterList.get(characterName.toLowerCase());

            }
        } else {
            pc = CharacterCommand.activeChar;
        }
        if(pc!=null && CharacterCommand.characterList.containsKey(pc.getName().toLowerCase())){
            Path path = Paths.get(CharacterCommand.propertiesHandler.getExportDir()+"/" + pc.getName() + ".txt");
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

            for (PlayerCharacter pc: CharacterCommand.characterList.values()){
                path = Paths.get(CharacterCommand.propertiesHandler.getExportDir() + "/" + pc.getName() + ".txt");
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
    public void saveChar() {
        PlayerCharacter pc=null;
        String characterName;
        CharacterCommand.tokens.pop();
        if(!CharacterCommand.tokens.isEmpty()){
            if(CharacterCommand.tokens.contains("--help")){
                System.out.println(Help.EXPORT);
            } else if (CharacterCommand.tokens.contains("--all")){
                saveAll();
            } else {
                StringBuilder nameBuilder = new StringBuilder();
                while (!CharacterCommand.tokens.isEmpty()) {
                    nameBuilder.append(CharacterCommand.tokens.pop());
                    nameBuilder.append(" ");
                }
                characterName = nameBuilder.toString().trim();
                pc = CharacterCommand.characterList.get(characterName.toLowerCase());
            }
        }else{
            pc = CharacterCommand.activeChar;
        }
        if(pc!=null && CharacterCommand.characterList.containsKey(pc.getName().toLowerCase())){
            try{
                Path path = Paths.get(CharacterCommand.propertiesHandler.getDataDir() +"/" + pc.getName() + ".data");
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
            for(PlayerCharacter pc: CharacterCommand.characterList.values()){
                path = Paths.get(CharacterCommand.propertiesHandler.getDataDir()  + "/" + pc.getName() + ".data");
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
    public void loadChar(){
        String command = CharacterCommand.tokens.pop();
        if (!CharacterCommand.tokens.isEmpty()){
            loadChar(command);
        } else {
            System.out.print("Enter name of character to load, or enter 'new' to create a new character: ");
            String characterName = CharacterCommand.scanner.nextLine().toLowerCase();
            if (characterName.equalsIgnoreCase("new")) {
                PlayerCharacter.createCharacter();
            } else if (!characterName.equalsIgnoreCase("quit")) {
                if (CharacterCommand.characterList.get(characterName) != null) {
                    CharacterCommand.activeChar = CharacterCommand.characterList.get(characterName);
                    System.out.println(characterName + " loaded");
                } else {
                    System.out.println("ERROR: Character not found");
                }
            }
        }
    }

    private void loadChar(String command){
        String characterName = "";
        while (!CharacterCommand.tokens.isEmpty()){
            characterName += CharacterCommand.tokens.pop()+" ";
        }
        characterName = characterName.trim().toLowerCase();
        if (CharacterCommand.characterList.get(characterName) != null) {
            CharacterCommand.activeChar = CharacterCommand.characterList.get(characterName);
            System.out.println(CharacterCommand.activeChar.getName() + " loaded");
        } else {
            System.out.println("ERROR: Character not found");
        }
    }
}
