package com.ufrn.paulofh.cg.core;

/**
 * A flat (unlit) material that always returns a constant color.
 */
public class FlatMaterial extends Material {
    private final RGBColor flatColor;

    public FlatMaterial(RGBColor color) {
        this.flatColor = color;
    }

    @Override
    public RGBColor color() {
        return flatColor;
    }
}
