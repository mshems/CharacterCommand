package app.io;

import app.CharacterCommand;
import core.character.PlayerCharacter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Writer {
    public static void writeCharacter(CharacterCommand cc, PlayerCharacter pc, boolean verbose){
        IOUtils.checkDirs(cc);
        try{
            Path path = Paths.get(cc.dataDir()+"/" + pc.getName() + ".data");
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path.toString())));
            out.writeObject(pc);
            out.close();
            if(verbose) {
                cc.terminal.out.println("Saved " + pc.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
