package com.ufrn.paulofh.cg.parser;

import com.ufrn.paulofh.cg.core.API;
import com.ufrn.paulofh.cg.rt3.RunningOptions;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Parses an RT3 XML scene file and drives the API.
 *
 * Supported tags (in order inside <RT3>):
 *   <lookat>    look_from, look_at, up
 *   <camera>    type, fovy | screen_window
 *   <integrator> type
 *   <film>      w_res/h_res (or x_res/y_res), filename, img_type
 *   <world_begin/>
 *   <background> type, color | bl/br/tl/tr
 *   <material>  type, color
 *   <object>    type, radius, center
 *   <world_end/>
 */
public class SceneParser {

    public static void parse(String xmlPath, RunningOptions options) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new File(xmlPath));
        doc.getDocumentElement().normalize();

        // Iterate children of root <RT3> in document order
        NodeList children = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            Element el  = (Element) node;
            ParamSet ps = toParamSet(el);

            switch (el.getTagName()) {
                case "lookat":
                    API.setLookAt(ps);
                    break;
                case "camera":
                    API.setCamera(ps);
                    break;
                case "integrator":
                    API.setIntegratorType(ps);
                    break;
                case "film":
                    API.setFilm(ps);
                    break;
                case "world_begin":
                    // no-op: marker only
                    break;
                case "background":
                    API.setBackground(ps);
                    break;
                case "material":
                    API.setMaterial(ps);
                    break;
                case "object":
                    API.addObject(ps);
                    break;
                case "world_end":
                    API.worldEnd();
                    break;
                default:
                    System.err.println("SceneParser: unknown tag <" + el.getTagName() + ">, skipping.");
            }
        }
    }

    /** Converts all attributes of an XML element into a ParamSet. */
    private static ParamSet toParamSet(Element el) {
        ParamSet ps    = new ParamSet();
        NamedNodeMap attrs = el.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node a = attrs.item(i);
            ps.add(a.getNodeName(), a.getNodeValue());
        }
        return ps;
    }
}
