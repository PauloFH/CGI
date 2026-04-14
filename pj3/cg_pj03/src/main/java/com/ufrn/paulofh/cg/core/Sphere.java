package com.ufrn.paulofh.cg.core;

import org.joml.Vector3f;

/**
 * A sphere primitive defined by center and radius.
 *
 * Intersection uses the quadratic formula:
 *   oc = ray.origin - center
 *   a  = dot(d, d)
 *   hb = dot(d, oc)          (half b)
 *   c  = dot(oc, oc) - r^2
 *   disc = hb^2 - a*c
 */
public class Sphere extends Primitive {

    private final Vector3f center;
    private final float radius;

    public Sphere(Vector3f center, float radius, Material material) {
        this.center   = new Vector3f(center);
        this.radius   = radius;
        this.material = material;
    }

    @Override
    public boolean intersect(Ray ray, Surfel sf) {
        Vector3f oc = new Vector3f(ray.origin).sub(center);
        Vector3f d  = ray.direction;

        float a    = d.dot(d);
        float hb   = d.dot(oc);
        float cCoef = oc.dot(oc) - radius * radius;
        float disc  = hb * hb - a * cCoef;

        if (disc < 0f) return false;

        float sqrtDisc = (float) Math.sqrt(disc);

        // Try closer root first
        float t = (-hb - sqrtDisc) / a;
        if (t < ray.tMin || t > ray.tMax) {
            // Try farther root
            t = (-hb + sqrtDisc) / a;
            if (t < ray.tMin || t > ray.tMax) return false;
        }

        ray.tMax = t;

        sf.p         = ray.at(t);
        sf.n         = new Vector3f(sf.p).sub(center).normalize();
        sf.wo        = new Vector3f(ray.direction).negate();
        sf.primitive = this;

        return true;
    }

    @Override
    public boolean intersectP(Ray ray) {
        Vector3f oc = new Vector3f(ray.origin).sub(center);
        Vector3f d  = ray.direction;

        float a    = d.dot(d);
        float hb   = d.dot(oc);
        float cCoef = oc.dot(oc) - radius * radius;
        float disc  = hb * hb - a * cCoef;

        if (disc < 0f) return false;

        float sqrtDisc = (float) Math.sqrt(disc);
        float t1 = (-hb - sqrtDisc) / a;
        float t2 = (-hb + sqrtDisc) / a;

        return (t1 >= ray.tMin && t1 <= ray.tMax)
            || (t2 >= ray.tMin && t2 <= ray.tMax);
    }
}
