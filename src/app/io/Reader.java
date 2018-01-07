package app.io;

import app.CharacterCommand;
import core.character.PlayerCharacter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Reader {
    public static PlayerCharacter readCharacter(CharacterCommand cc, String characterName) {
        PlayerCharacter pc = null;
        Path charPath = Paths.get(cc.dataDir() + "/" + characterName + ".data");
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
