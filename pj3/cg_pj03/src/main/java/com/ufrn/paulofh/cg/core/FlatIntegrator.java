package com.ufrn.paulofh.cg.core;

/**
 * Flat shading integrator: returns the material's constant color on hit,
 * or the background color on miss.
 */
public class FlatIntegrator extends Integrator {

    @Override
    public RGBColor li(Ray ray, float u, float v, Scene scene) {
        RGBColor color = scene.background.sampleUV(u, v);
        Surfel sf = new Surfel();
        for (Primitive p : scene.primitives) {
            if (p.intersect(ray, sf)) {
                // ray.tMax is updated inside intersect — ensures closest hit
                color = p.getMaterial().color();
            }
        }
        return color;
    }
}
