package app;

import utils.Help;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesHandler{
    private Path dataDir;
    private Path exportDir;
    private boolean viewAlways;
    private boolean resume;
    private String last;
    private Properties properties;


    public PropertiesHandler(){
        this.properties = new Properties();
        initProperties();
    }

    private void initProperties() {
        OutputStream out = null;
        Path configPath = Paths.get("./config.properties");
        if (!Files.exists(configPath)) {
            try {
                out = new FileOutputStream("config.properties");

                properties.setProperty("dataDir", "./data");
                properties.setProperty("exportDir", "./data");
                properties.setProperty("viewAlways", "false");
                properties.setProperty("resume", "false");
                properties.setProperty("last", "");

                properties.store(out, null);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        readProperties();
    }

    private void readProperties(){
        InputStream in = null;
        try{
            in = new FileInputStream("config.properties");
            properties.load(in);

            this.dataDir = Paths.get(properties.getProperty("dataDir", "./data"));
            this.exportDir = Paths.get(properties.getProperty("exportDir", "./data"));
            this.viewAlways = Boolean.parseBoolean(properties.getProperty("viewAlways", "false"));
            this.resume = Boolean.parseBoolean(properties.getProperty("resume", "false"));
            this.last = properties.getProperty("last", "");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try{
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeProperties(){
        OutputStream out = null;
        try {
            out = new FileOutputStream("config.properties");

            properties.setProperty("dataDir", dataDir.toString());
            properties.setProperty("exportDir", exportDir.toString());
            properties.setProperty("viewAlways", Boolean.toString(viewAlways));
            properties.setProperty("resume", Boolean.toString(resume));
            properties.setProperty("last", last);

            properties.store(out, null);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void prefs(){
        String command = CharacterCommand.tokens.pop();
        if(!CharacterCommand.tokens.isEmpty()){
            prefs(command);
        } else {
            //TODO: prefs
            System.out.println("manual prefs editing placeholder -- use command arguments for now");
        }
    }

    private void prefs(String command){
        while(!CharacterCommand.tokens.isEmpty()) {
            switch (CharacterCommand.tokens.peek()) {
                case "-e":
                case "--export":
                    CharacterCommand.tokens.pop();
                    if(!CharacterCommand.tokens.isEmpty()){
                        File exportFile = Paths.get(CharacterCommand.tokens.pop()).toFile();
                        if (exportFile.isDirectory()){
                            this.exportDir = exportFile.toPath();
                        }
                        System.out.println("Set toTextFile directory to " + exportFile.toString());
                    }
                    break;
                case "-d":
                case "--data":
                    CharacterCommand.tokens.pop();
                    if(!CharacterCommand.tokens.isEmpty()){
                        File dataFile = Paths.get(CharacterCommand.tokens.pop()).toFile();
                        if (dataFile.isDirectory()){
                            this.dataDir = dataFile.toPath();
                        }
                        System.out.println("Set data directory to " + dataFile.toString());
                    }
                    break;
                case "-v":
                case "-va":
                case "--viewAlways":
                    CharacterCommand.tokens.pop();
                    if(!CharacterCommand.tokens.isEmpty()){
                        String token = CharacterCommand.tokens.pop();
                        if (token.equalsIgnoreCase("true") || token.equalsIgnoreCase("false")){
                            this.viewAlways = Boolean.parseBoolean(token);
                            System.out.println("Set 'viewAlways' to " + token);
                        } else {
                            System.out.println("ERROR: Argument must be 'true' or 'false'");
                        }
                    }
                    break;
                case "-r":
                case "--resume":
                    CharacterCommand.tokens.pop();
                    if(!CharacterCommand.tokens.isEmpty()){
                        String token = CharacterCommand.tokens.pop();
                        if (token.equalsIgnoreCase("true") || token.equalsIgnoreCase("false")){
                            this.resume = Boolean.parseBoolean(token);
                            System.out.println("Set 'resume' to " + token);
                        } else {
                            System.out.println("ERROR: Argument must be 'true' or 'false'");
                        }
                    }
                    break;
                case "--help":
                    CharacterCommand.tokens.pop();
                    System.out.println(Help.PREFS);
                    break;
                default:
                    if (CharacterCommand.tokens.peek().startsWith("-")) {
                        System.out.println("ERROR: Invalid flag '" + CharacterCommand.tokens.pop() + "'");
                    } else {
                        CharacterCommand.tokens.pop();
                    }
                    break;
            }
        }
        this.writeProperties();
    }
    public Path getDataDir(){
        return dataDir;
    }
    public Path getExportDir(){
        return exportDir;
    }
    public boolean isViewAlways(){
        this.viewAlways = Boolean.parseBoolean(properties.getProperty("viewAlways", "false"));
        return viewAlways;
    }
    public boolean isResume(){
        return resume;
    }
    public String getLast(){
        return last;
    }
    public void setLast(String last){
        this.last = last;
    }
}
