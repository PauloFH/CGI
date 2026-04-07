package com.ufrn.paulofh.cg.core;

import java.io.IOException;

/**
 * Static API that manages scene objects and runs the render loop.
 */
public class API {

    private static Film       film;
    private static Background background;

    public static void setFilm(Film f)           { film       = f; }
    public static void setBackground(Background b) { background = b; }

    public static void render() throws IOException {
        int w = film.getWidth();
        int h = film.getHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float u = (float) x / (w - 1);
                float v = (float) (h - 1 - y) / (h - 1); // v=0 at bottom

                RGBColor color = background.sampleUV(u, v);
                film.commit(x, y, color);
            }
        }

        film.save();
    }
}
