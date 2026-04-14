package com.ufrn.paulofh.cg.core.camera;

import com.ufrn.paulofh.cg.core.Film;
import com.ufrn.paulofh.cg.core.Ray;
import org.joml.Vector3f;

/**
 * Perspective camera.
 *
 * fovy: vertical field of view in degrees.
 *
 * Ray generation (LH — w points forward into the scene):
 *   halfH = tan(toRadians(fovy / 2))
 *   halfW = halfH * (W / H)
 *   dx    = (2*(i+0.5)/W - 1) * halfW
 *   dy    = (1 - 2*(j+0.5)/H) * halfH    (Y inverted: j=0 → top → +dy)
 *   direction = normalize(dx*u + dy*v + w)
 *   origin    = eye
 */
public class PerspectiveCamera extends Camera {

    private final float halfH;
    private final float halfW;

    public PerspectiveCamera(Vector3f lookFrom, Vector3f lookAt, Vector3f up,
                              float fovy, Film film) {
        super(lookFrom, lookAt, up, film);
        float W = film.getWidth();
        float H = film.getHeight();
        this.halfH = (float) Math.tan(Math.toRadians(fovy / 2.0));
        this.halfW = halfH * (W / H);
    }

    @Override
    public Ray generateRay(int i, int j) {
        float W = film.getWidth();
        float H = film.getHeight();

        float dx = (2f * (i + 0.5f) / W - 1f) * halfW;
        float dy = (1f - 2f * (j + 0.5f) / H) * halfH;

        // LH: direction uses +w (forward into scene)
        Vector3f dir = new Vector3f(u).mul(dx)
                            .add(new Vector3f(v).mul(dy))
                            .add(w)
                            .normalize();

        return new Ray(new Vector3f(eye), dir);
    }
}
