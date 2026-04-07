package com.ufrn.paulofh.cg.core;

public class Background {

    private RGBColor bl, br, tl, tr;

    /**
     * @param bl bottom-left corner color
     * @param br bottom-right corner color
     * @param tl top-left corner color
     * @param tr top-right corner color
     */
    public Background(RGBColor bl, RGBColor br, RGBColor tl, RGBColor tr) {
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
     * Returns interpolated color at (u, v).
     * u in [0,1]: 0 = left, 1 = right
     * v in [0,1]: 0 = bottom, 1 = top
     */
    public RGBColor sampleUV(float u, float v) {
        RGBColor xb = lerp(bl, br, u); // interpolate bottom edge
        RGBColor xt = lerp(tl, tr, u); // interpolate top edge
        return lerp(xb, xt, v);        // interpolate vertically
    }
}
