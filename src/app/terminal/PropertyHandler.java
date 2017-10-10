package app.terminal;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyHandler {
    private static final String PATH = "terminal-config.properties";

    public static void initProperties(Terminal terminal){

        Properties properties = terminal.getProperties();
        OutputStream out = null;
        Path configPath = Paths.get("./"+PATH);
        if(!Files.exists(configPath)){
            try{
                out= new FileOutputStream(PATH);

                properties.setProperty("font-size", "16");

                properties.store(out, null);
            } catch (IOException e){
                //e.printStackTrace();
            } finally {
                if(out != null){
                    try{
                        out.close();
                    } catch (IOException e){
                        //e.printStackTrace();
                    }
                }
            }
        }
        readProperties(terminal);
    }

    public static void readProperties(Terminal terminal){
        Properties properties = terminal.getProperties();
        InputStream in = null;
        try{
            in = new FileInputStream(PATH);
            properties.load(in);

            try{
                int fontSize = Integer.parseInt(properties.getProperty("font-size"));
                terminal.setFontSize(fontSize);
            } catch (NumberFormatException e){
                //e.printStackTrace();
            }



        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            if (in != null){
                try{
                    in.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

    public static void writeProperties(Terminal terminal){
        Properties properties = terminal.getProperties();
        OutputStream out = null;
        try {
            out = new FileOutputStream(PATH);

            properties.setProperty("font-size", Integer.toString(terminal.getFontSize()));

            properties.store(out, null);
        } catch (IOException e){
            //e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
