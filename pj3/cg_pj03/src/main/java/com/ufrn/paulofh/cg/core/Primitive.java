package com.ufrn.paulofh.cg.core;

/**
 * Abstract base for all scene primitives.
 */
public abstract class Primitive {
    protected Material material;

    /** Full intersection: fills sf with hit info, updates ray.tMax. Returns true on hit. */
    public abstract boolean intersect(Ray r, Surfel sf);

    /** Shadow/occlusion test: returns true if ray hits, without filling Surfel. */
    public abstract boolean intersectP(Ray r);

    public Material getMaterial() { return material; }
}
