package com.ufrn.paulofh.cg.core.camera;

import com.ufrn.paulofh.cg.core.Film;
import com.ufrn.paulofh.cg.core.Ray;
import org.joml.Vector3f;

/**
 * Abstract camera.
 *
 * Uses a LEFT-HAND coordinate system for the camera frame:
 *   w = normalize(look_at - look_from)  → forward (into the scene, +Z in LH)
 *   u = normalize(cross(up, w))         → right (+X)
 *   v = cross(w, u)                     → true up (+Y)
 *
 * Subclasses implement generateRay(i, j) to produce a ray for pixel (i, j).
 */
public abstract class Camera {

    protected final Vector3f eye;  // camera position
    protected final Vector3f u;    // right axis
    protected final Vector3f v;    // up axis
    protected final Vector3f w;    // forward axis (into scene — LH)
    protected final Film film;

    protected Camera(Vector3f lookFrom, Vector3f lookAt, Vector3f up, Film film) {
        this.eye  = new Vector3f(lookFrom);
        this.film = film;

        // Left-hand rule: w points FORWARD into the scene
        this.w = new Vector3f(lookAt).sub(lookFrom).normalize();

        // u = cross(up, w) → right
        this.u = new Vector3f(up).cross(w).normalize();

        // v = cross(w, u) → true up
        this.v = new Vector3f(w).cross(u);
    }

    /** Generates a ray for pixel column i, row j (j=0 is top of image). */
    public abstract Ray generateRay(int i, int j);

    public Film getFilm() { return film; }
}
