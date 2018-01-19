package app.io;

import app.CharacterCommand;
import jterminal.optional.properties.PropertiesManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOUtils {
    public static void checkDirs(CharacterCommand cc){
        if (!Files.exists(Paths.get(cc.exportDir()))){
            try {
                Files.createDirectories(Paths.get(cc.exportDir()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(Paths.get(cc.dataDir()))){
            try {
                Files.createDirectories(Paths.get(cc.dataDir()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String toFileName(String str){
        return str.replaceAll(("[\"*|/<>?\\\\]"), "").replaceAll("\\s+", "_");
    }
}
