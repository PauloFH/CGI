package com.ufrn.paulofh.cg.core;

import org.joml.Vector3f;

/**
 * Surface element: contact information at a ray-primitive intersection.
 */
public class Surfel {
    public Vector3f p;         // contact point in world space
    public Vector3f n;         // surface normal (unit vector)
    public Vector3f wo;        // outgoing direction = -ray.direction
    public float u, v;         // UV coordinates on the surface
    public Primitive primitive; // the intersected primitive
}
