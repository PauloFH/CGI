package com.ufrn.paulofh.cg.parser;

import com.ufrn.paulofh.cg.core.API;
import com.ufrn.paulofh.cg.core.Background;
import com.ufrn.paulofh.cg.core.Film;
import com.ufrn.paulofh.cg.core.RGBColor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Parses an XML scene file and populates the API.
 *
 * Expected format:
 * <RT3>
 *   <film type="image" x_res="800" y_res="400" filename="out.png" img_type="png"/>
 *   <world_begin/>
 *     <background type="single_color" color="R G B"/>
 *     <background type="4_colors" bl="R G B" br="R G B" tl="R G B" tr="R G B"/>
 *   <world_end/>
 * </RT3>
 */
public class SceneParser {

    public static void parse(String xmlPath) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(xmlPath));
        doc.getDocumentElement().normalize();

        parseFilm(doc);
        parseBackground(doc);
    }

    private static void parseFilm(Document doc) {
        NodeList nodes = doc.getElementsByTagName("film");
        if (nodes.getLength() == 0) throw new RuntimeException("<film> tag not found in scene file.");

        Element el       = (Element) nodes.item(0);
        int     width    = Integer.parseInt(el.getAttribute("x_res"));
        int     height   = Integer.parseInt(el.getAttribute("y_res"));
        String  filename = el.getAttribute("filename");

        API.setFilm(new Film(width, height, filename));
    }

    private static void parseBackground(Document doc) {
        NodeList nodes = doc.getElementsByTagName("background");
        if (nodes.getLength() == 0) throw new RuntimeException("<background> tag not found in scene file.");

        Element el   = (Element) nodes.item(0);
        String  type = el.getAttribute("type");

        if (type.equals("single_color")) {
            RGBColor color = parseColor(el.getAttribute("color"));
            API.setBackground(new Background(color));

        } else if (type.equals("4_colors")) {
            RGBColor bl = parseColor(el.getAttribute("bl"));
            RGBColor br = parseColor(el.getAttribute("br"));
            RGBColor tl = parseColor(el.getAttribute("tl"));
            RGBColor tr = parseColor(el.getAttribute("tr"));
            API.setBackground(new Background(bl, br, tl, tr));

        } else {
            throw new RuntimeException("Unknown background type: " + type);
        }
    }

    /** Parses a color string "R G B" (0-255 integers) into RGBColor. */
    private static RGBColor parseColor(String s) {
        String[] parts = s.trim().split("\\s+");
        return RGBColor.fromInt(
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            Integer.parseInt(parts[2])
        );
    }
}
