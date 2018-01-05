package jterminal.optional.theme;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import jterminal.core.theme.Theme;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ThemeXMLHandler {
    static final String THEME_DIR = "./themes";

    static void checkDirs(){
        File themeDir = new File(THEME_DIR);
        if(!themeDir.exists()){
            try{
                Files.createDirectories(Paths.get(THEME_DIR));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeTheme(Theme theme){
        checkDirs();
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document themeFile = documentBuilder.newDocument();

            Element root = themeFile.createElement("THEME");
            themeFile.appendChild(root);

            Element themeName = themeFile.createElement("theme-name");
            themeName.appendChild(themeFile.createTextNode(theme.themeName));
            root.appendChild(themeName);

            Element bgc = themeFile.createElement("background-color");
            bgc.appendChild(themeFile.createTextNode(
                    String.format("#%02x%02x%02x", theme.backgroundColor.getRed(),
                            theme.backgroundColor.getGreen(), theme.backgroundColor.getBlue())
            ));
            root.appendChild(bgc);

            Element fgc = themeFile.createElement("foreground-color");
            fgc.appendChild(themeFile.createTextNode(
                    String.format("#%02x%02x%02x", theme.foregroundColor.getRed(),
                            theme.foregroundColor.getGreen(), theme.foregroundColor.getBlue())
            ));
            root.appendChild(fgc);

            Element ctc = themeFile.createElement("caret-color");
            ctc.appendChild(themeFile.createTextNode(
                    String.format("#%02x%02x%02x", theme.caretColor.getRed(),
                            theme.caretColor.getGreen(), theme.caretColor.getBlue())
            ));
            root.appendChild(ctc);

            Element hlc = themeFile.createElement("highlight-color");
            hlc.appendChild(themeFile.createTextNode(
                    String.format("#%02x%02x%02x", theme.highlightColor.getRed(),
                            theme.highlightColor.getGreen(), theme.highlightColor.getBlue())
            ));
            root.appendChild(hlc);

            Element fontElement = themeFile.createElement("font");
                Element fontName = themeFile.createElement("font-name");
                fontName.appendChild(themeFile.createTextNode(theme.font.getName()));
                fontElement.appendChild(fontName);

                Element fontDefaultSize = themeFile.createElement("font-default-size");
                fontDefaultSize.appendChild(themeFile.createTextNode(theme.font.getSize()+""));
                fontElement.appendChild(fontDefaultSize);
            root.appendChild(fontElement);

            try{
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                tr.transform(new DOMSource(themeFile), new StreamResult(new FileOutputStream(THEME_DIR+"/"+theme.themeName+".xml")));
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Theme readTheme(String themeName){
        Theme theme = new Theme(themeName);
        String fileName = themeName+".xml";
        File themeFile = new File(THEME_DIR+"/"+fileName);
        if(themeFile.exists()){
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            try{
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document themeFileDoc = documentBuilder.parse(themeFile);
                try {
                    theme.backgroundColor = Color.decode(themeFileDoc.getElementsByTagName("background-color")
                            .item(0).getTextContent());
                    theme.foregroundColor = Color.decode(themeFileDoc.getElementsByTagName("foreground-color")
                            .item(0).getTextContent());
                    theme.caretColor = Color.decode(themeFileDoc.getElementsByTagName("caret-color")
                            .item(0).getTextContent());
                    theme.highlightColor = Color.decode(themeFileDoc.getElementsByTagName("highlight-color")
                            .item(0).getTextContent());
                    String fontName = themeFileDoc.getElementsByTagName("font-name").item(0).getTextContent();
                    int fontSize = Integer.parseInt(
                        themeFileDoc.getElementsByTagName("font-default-size")
                                .item(0).getTextContent());
                    theme.font = new Font(fontName, Font.PLAIN, fontSize);
                } catch (NumberFormatException e){
                    return Theme.DEFAULT_THEME();
                }
            } catch (Exception e){
                return Theme.DEFAULT_THEME();
            }
        }
        return theme;
    }
}
