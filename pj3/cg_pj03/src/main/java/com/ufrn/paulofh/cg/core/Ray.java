package com.ufrn.paulofh.cg.core;

import org.joml.Vector3f;

/**
 * A ray defined by origin and direction, with [tMin, tMax] interval.
 */
public class Ray {
    public Vector3f origin;
    public Vector3f direction;
    public float tMin = 0.001f;
    public float tMax = Float.MAX_VALUE;

    public Ray(Vector3f origin, Vector3f direction) {
        this.origin    = new Vector3f(origin);
        this.direction = new Vector3f(direction);
    }

    /** Returns the point at parameter t: origin + t * direction. */
    public Vector3f at(float t) {
        return new Vector3f(direction).mul(t).add(origin);
    }
}
