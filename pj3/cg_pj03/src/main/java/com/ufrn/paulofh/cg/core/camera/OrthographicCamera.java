package com.ufrn.paulofh.cg.core.camera;

import com.ufrn.paulofh.cg.core.Film;
import com.ufrn.paulofh.cg.core.Ray;
import org.joml.Vector3f;

/**
 * Orthographic camera (parallel projection).
 *
 * screen_window = [left, right, bottom, top] defines the view rectangle in camera space.
 * Default: [-1, 1, -1, 1].
 *
 * Ray generation (LH — direction is +w, i.e. forward into the scene):
 *   ox = left  + (right - left) * (i + 0.5) / W
 *   oy = bottom + (top - bottom) * (H - 1 - j) / (H - 1)    (j=0 = top of image)
 *   origin    = eye + ox*u + oy*v
 *   direction = w
 */
public class OrthographicCamera extends Camera {

    private final float left, right, bottom, top;

    public OrthographicCamera(Vector3f lookFrom, Vector3f lookAt, Vector3f up,
                               float[] screenWindow, Film film) {
        super(lookFrom, lookAt, up, film);
        this.left   = screenWindow[0];
        this.right  = screenWindow[1];
        this.bottom = screenWindow[2];
        this.top    = screenWindow[3];
    }

    @Override
    public Ray generateRay(int i, int j) {
        int W = film.getWidth();
        int H = film.getHeight();

        float ox = left + (right - left) * (i + 0.5f) / W;
        float oy = bottom + (top - bottom) * (H - 1 - j) / (float)(H - 1);

        Vector3f origin = new Vector3f(eye)
            .add(new Vector3f(u).mul(ox))
            .add(new Vector3f(v).mul(oy));

        // LH: direction is forward (w)
        return new Ray(origin, new Vector3f(w));
    }
}
