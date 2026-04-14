package com.ufrn.paulofh.cg.core;

import com.ufrn.paulofh.cg.core.camera.Camera;
import com.ufrn.paulofh.cg.core.camera.OrthographicCamera;
import com.ufrn.paulofh.cg.core.camera.PerspectiveCamera;
import com.ufrn.paulofh.cg.parser.ParamSet;
import com.ufrn.paulofh.cg.parser.SceneParser;
import com.ufrn.paulofh.cg.rt3.RunningOptions;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Static API that accumulates scene data during parsing and drives the render.
 *
 * Lifecycle:
 *   initEngine → SceneParser.parse → [setLookAt / setCamera / setIntegratorType /
 *                                      setFilm / setBackground / setMaterial / addObject /
 *                                      worldEnd] → cleanUp
 */
public class API {

    // ── Temporary state filled during parsing ──────────────────────────────

    private static RunningOptions options;

    // lookat
    private static Vector3f lookFrom;
    private static Vector3f lookAt;
    private static Vector3f up;

    // camera
    private static ParamSet cameraParams;

    // integrator
    private static String integratorType;

    // film
    private static Film filmTemp;

    // scene objects
    private static Background     background;
    private static Material       currentMaterial;
    private static List<Primitive> primitives;

    // ── Lifecycle ──────────────────────────────────────────────────────────

    public static void initEngine(RunningOptions opt) {
        options         = opt;
        lookFrom        = new Vector3f(0, 0, 0);
        lookAt          = new Vector3f(0, 0, 1);
        up              = new Vector3f(0, 1, 0);
        cameraParams    = new ParamSet();
        integratorType  = "flat";
        filmTemp        = null;
        background      = null;
        currentMaterial = null;
        primitives      = new ArrayList<>();
    }

    public static void run() throws Exception {
        SceneParser.parse(options.sceneFile, options);
    }

    public static void cleanUp() {
        options         = null;
        filmTemp        = null;
        background      = null;
        currentMaterial = null;
        primitives      = null;
    }

    // ── Parser callbacks ───────────────────────────────────────────────────

    public static void setLookAt(ParamSet ps) {
        lookFrom = ps.getVector3f("look_from", new Vector3f(0, 0, 0));
        lookAt   = ps.getVector3f("look_at",   new Vector3f(0, 0, 1));
        up       = ps.getVector3f("up",         new Vector3f(0, 1, 0));
    }

    public static void setCamera(ParamSet ps) {
        cameraParams = ps;
    }

    public static void setIntegratorType(ParamSet ps) {
        integratorType = ps.getString("type", "flat");
    }

    public static void setFilm(ParamSet ps) {
        // support both w_res/h_res and x_res/y_res
        int w = ps.getInt("w_res", ps.getInt("x_res", 400));
        int h = ps.getInt("h_res", ps.getInt("y_res", 200));
        String filename = ps.getString("filename", "output/out.png");

        if (options != null && options.outfile != null) filename = options.outfile;
        if (options != null && options.quick)           { w /= 2; h /= 2; }

        filmTemp = new Film(w, h, filename);
    }

    public static void setBackground(ParamSet ps) {
        String bgType  = ps.getString("type", "single_color");
        String mapStr  = ps.getString("mapping", "screen");
        Background.Mapping mapping = mapStr.equals("spherical")
            ? Background.Mapping.SPHERICAL
            : Background.Mapping.SCREEN;

        if (bgType.equals("single_color")) {
            background = new Background(ps.getColor("color"), mapping);
        } else if (bgType.equals("4_colors")) {
            background = new Background(
                ps.getColor("bl"), ps.getColor("br"),
                ps.getColor("tl"), ps.getColor("tr"),
                mapping
            );
        } else {
            throw new RuntimeException("Unknown background type: " + bgType);
        }
    }

    public static void setMaterial(ParamSet ps) {
        String type = ps.getString("type", "flat");
        if (type.equals("flat")) {
            currentMaterial = new FlatMaterial(ps.getColor("color"));
        } else {
            throw new RuntimeException("Unknown material type: " + type);
        }
    }

    public static void addObject(ParamSet ps) {
        String type = ps.getString("type", "sphere");
        if (type.equals("sphere")) {
            Vector3f center = ps.getVector3f("center", new Vector3f(0, 0, 0));
            float radius    = ps.getFloat("radius", 1f);
            if (currentMaterial == null)
                throw new RuntimeException("<object> before any <material> tag.");
            primitives.add(new Sphere(center, radius, currentMaterial));
        } else {
            throw new RuntimeException("Unknown object type: " + type);
        }
    }

    /** Called when <world_end/> is encountered. Builds scene and renders. */
    public static void worldEnd() throws IOException {
        if (filmTemp == null)
            throw new RuntimeException("<film> tag not found before world_end.");

        // Build camera
        String camType = cameraParams.getString("type", "perspective");
        Camera camera;
        if (camType.equals("orthographic")) {
            float[] sw = cameraParams.getFloats4("screen_window",
                new float[]{-1f, 1f, -1f, 1f});
            camera = new OrthographicCamera(lookFrom, lookAt, up, sw, filmTemp);
        } else {
            float fovy = cameraParams.getFloat("fovy", 60f);
            camera = new PerspectiveCamera(lookFrom, lookAt, up, fovy, filmTemp);
        }

        // Build integrator
        Integrator integrator;
        if (integratorType.equals("flat")) {
            integrator = new FlatIntegrator();
        } else {
            throw new RuntimeException("Unknown integrator type: " + integratorType);
        }

        // Build scene
        Scene scene = new Scene(camera, background, primitives);

        // Render
        render(scene, integrator);
    }

    // ── Render loop ────────────────────────────────────────────────────────

    private static void render(Scene scene, Integrator integrator) throws IOException {
        Film film = scene.camera.getFilm();
        int  w    = film.getWidth();
        int  h    = film.getHeight();

        for (int j = h - 1; j >= 0; j--) {          // j = h-1: bottom row; j = 0: top row
            for (int i = 0; i < w; i++) {
                float u = (float) i / w;
                float v = (float)(h - 1 - j) / (h - 1);  // v=0 bottom, v=1 top
                Ray ray = scene.camera.generateRay(i, j);
                RGBColor color = integrator.li(ray, u, v, scene);
                film.commit(i, j, color);
            }
        }

        film.writeImage();
    }
}
