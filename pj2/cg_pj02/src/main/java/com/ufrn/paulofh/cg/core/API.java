package com.ufrn.paulofh.cg.core;

import com.ufrn.paulofh.cg.parser.SceneParser;
import com.ufrn.paulofh.cg.rt3.RunningOptions;

import java.io.IOException;

/**
 * Static API that manages scene objects and the render lifecycle.
 */
public class API {

    private static RunningOptions options;
    private static Camera         camera;
    private static Background     background;

    public static void initEngine(RunningOptions opt) {
        options    = opt;
        camera     = null;
        background = null;
    }

    public static void setCamera(Camera c)       { camera     = c; }
    public static void setBackground(Background b) { background = b; }

    /** Parses the scene file and triggers rendering at world_end. */
    public static void run() throws Exception {
        SceneParser.parse(options.sceneFile, options);
    }

    /** Main render loop: samples background for every pixel. */
    public static void render() throws IOException {
        Film film = camera.getFilm();
        int  w    = film.getWidth();
        int  h    = film.getHeight();

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                float u = (float) i / (w - 1);
                float v = (float) (h - 1 - j) / (h - 1); // v=0 at bottom
                film.commit(i, j, background.sampleUV(u, v));
            }
        }

        film.writeImage();
    }

    public static void cleanUp() {
        camera     = null;
        background = null;
        options    = null;
    }
}
