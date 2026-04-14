package com.ufrn.paulofh.cg.core;

import com.ufrn.paulofh.cg.core.camera.Camera;

import java.util.List;

/**
 * Holds all scene data needed for rendering.
 */
public class Scene {
    public final Camera          camera;
    public final Background      background;
    public final List<Primitive> primitives;

    public Scene(Camera camera, Background background, List<Primitive> primitives) {
        this.camera     = camera;
        this.background = background;
        this.primitives = primitives;
    }
}
