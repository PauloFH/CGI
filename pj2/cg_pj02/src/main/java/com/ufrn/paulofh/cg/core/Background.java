package com.ufrn.paulofh.cg.core;

/**
 * Returns a background color given a UV coordinate.
 * Supports single_color and 4_colors (bilinear interpolation) types.
 * Supports screen mapping (default); spherical reserved for future projects.
 */
public class Background {

    public enum Type    { SINGLE_COLOR, FOUR_COLORS }
    public enum Mapping { SCREEN, SPHERICAL }

    private final Type    type;
    private final Mapping mapping;
    private final RGBColor bl, br, tl, tr;

    public Background(RGBColor color, Mapping mapping) {
        this.type    = Type.SINGLE_COLOR;
        this.mapping = mapping;
        this.bl = this.br = this.tl = this.tr = color;
    }

    public Background(RGBColor bl, RGBColor br, RGBColor tl, RGBColor tr, Mapping mapping) {
        this.type    = Type.FOUR_COLORS;
        this.mapping = mapping;
        this.bl = bl;
        this.br = br;
        this.tl = tl;
        this.tr = tr;
    }

    private RGBColor lerp(RGBColor A, RGBColor B, float t) {
        return new RGBColor(
            (1 - t) * A.r + t * B.r,
            (1 - t) * A.g + t * B.g,
            (1 - t) * A.b + t * B.b
        );
    }

    /**
     * Returns the interpolated color at (u, v).
     * u in [0,1]: 0=left, 1=right
     * v in [0,1]: 0=bottom, 1=top
     */
    public RGBColor sampleUV(float u, float v) {
        if (mapping == Mapping.SPHERICAL)
            throw new UnsupportedOperationException("Spherical mapping not yet implemented.");
        if (type == Type.SINGLE_COLOR) return bl;
        RGBColor xb = lerp(bl, br, u);
        RGBColor xt = lerp(tl, tr, u);
        return lerp(xb, xt, v);
    }
}
