package app.io;

import app.CharacterCommand;
import core.character.PlayerCharacter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Reader {
    public static PlayerCharacter readCharacter(CharacterCommand cc, String characterName, boolean verbose) {
        Path charPath = Paths.get(cc.dataDir() + "/" + IOUtils.toFileName(characterName) + ".data");
        return readCharacter(charPath);
    }

    public static PlayerCharacter readCharacter(CharacterCommand cc, String fileName){
        Path charPath = Paths.get(cc.dataDir() + "/" + fileName);
        return readCharacter(charPath);
    }

    private static PlayerCharacter readCharacter(Path charPath){
        PlayerCharacter pc = null;
        if (Files.exists(charPath)) {
            File charFile = charPath.toFile();
            try {
                ObjectInputStream inStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(charFile)));
                pc = (PlayerCharacter) inStream.readObject();
                inStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pc;
    }
}
