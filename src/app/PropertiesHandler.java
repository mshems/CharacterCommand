package app;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesHandler{
    private Path dataDir;
    private Path exportDir;
    private boolean viewAlways;
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
        } else {
            readProperties();
        }
    }

    private void readProperties(){
        InputStream in = null;
        try{
            in = new FileInputStream("config.properties");
            properties.load(in);

            this.dataDir = Paths.get(properties.getProperty("dataDir", "./data"));
            this.exportDir = Paths.get(properties.getProperty("exportDir", "./data"));
            this.viewAlways = Boolean.parseBoolean(properties.getProperty("viewAlways", "false"));

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

    public Path getDataDir(){
        return Paths.get(properties.getProperty("dataDir", "./data"));
    }
    public void setDataDir(Path dataDir){
        this.dataDir = dataDir;
    }

    public Path getExportDir(){
        return Paths.get(properties.getProperty("exportDir", "./data"));
    }
    public void setExportDir(Path exportDir){
        this.exportDir = exportDir;
    }

    public boolean getViewAlways(){
        return Boolean.parseBoolean(properties.getProperty("viewAlways", "false"));
    }
    public void setViewAlways(boolean viewAlways){
        this.viewAlways = viewAlways;
    }
}
