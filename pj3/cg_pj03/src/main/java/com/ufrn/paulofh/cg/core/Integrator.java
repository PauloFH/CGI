package com.ufrn.paulofh.cg.core;

/**
 * Abstract base for all integrators (light transport algorithms).
 */
public abstract class Integrator {
    /** Estimates the radiance arriving along the given ray. */
    public abstract RGBColor li(Ray ray, float u, float v, Scene scene);
}
