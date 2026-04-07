package com.ufrn.paulofh.cg.parser;

import com.ufrn.paulofh.cg.core.*;
import com.ufrn.paulofh.cg.rt3.RunningOptions;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Parses an XML scene file and populates the API.
 *
 * Supported tags: RT3, camera, film, world_begin, background, world_end
 */
public class SceneParser {

    public static void parse(String xmlPath, RunningOptions options) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(xmlPath));
        doc.getDocumentElement().normalize();

        Film film = parseFilm(doc, options);
        parseCamera(doc, film);
        parseBackground(doc);
    }

    private static Film parseFilm(Document doc, RunningOptions options) {
        NodeList nodes = doc.getElementsByTagName("film");
        if (nodes.getLength() == 0) throw new RuntimeException("<film> tag not found.");

        ParamSet ps = toParamSet((Element) nodes.item(0));
        int    w    = ps.getInt("x_res", 400);
        int    h    = ps.getInt("y_res", 200);
        String file = ps.getString("filename", "output/out.png");

        if (options != null && options.outfile != null) file = options.outfile;
        if (options != null && options.quick)           { w /= 2; h /= 2; }

        return new Film(w, h, file);
    }

    private static void parseCamera(Document doc, Film film) {
        NodeList nodes = doc.getElementsByTagName("camera");
        String type = "orthographic";
        if (nodes.getLength() > 0)
            type = toParamSet((Element) nodes.item(0)).getString("type", "orthographic");
        API.setCamera(new Camera(type, film));
    }

    private static void parseBackground(Document doc) throws Exception {
        NodeList nodes = doc.getElementsByTagName("background");
        if (nodes.getLength() == 0) throw new RuntimeException("<background> tag not found.");

        ParamSet ps      = toParamSet((Element) nodes.item(0));
        String   type    = ps.getString("type", "single_color");
        String   mapStr  = ps.getString("mapping", "screen");
        Background.Mapping mapping = mapStr.equals("spherical")
                ? Background.Mapping.SPHERICAL
                : Background.Mapping.SCREEN;

        Background bg;
        if (type.equals("single_color")) {
            bg = new Background(ps.getColor("color"), mapping);
        } else if (type.equals("4_colors")) {
            bg = new Background(
                ps.getColor("bl"), ps.getColor("br"),
                ps.getColor("tl"), ps.getColor("tr"),
                mapping
            );
        } else {
            throw new RuntimeException("Unknown background type: " + type);
        }

        API.setBackground(bg);

        // world_end: trigger render
        onWorldEnd();
    }

    /** Called when world_end tag is found — triggers the render loop. */
    private static void onWorldEnd() throws Exception {
        API.render();
    }

    /** Converts all attributes of an XML element into a ParamSet. */
    private static ParamSet toParamSet(Element el) {
        ParamSet ps  = new ParamSet();
        NamedNodeMap attrs = el.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node a = attrs.item(i);
            ps.add(a.getNodeName(), a.getNodeValue());
        }
        return ps;
    }
}
