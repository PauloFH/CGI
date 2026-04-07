package com.ufrn.paulofh.cg.core;

public class RGBColor {
    public float r, g, b;

    public RGBColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static RGBColor fromInt(int r, int g, int b) {
        return new RGBColor(r / 255f, g / 255f, b / 255f);
    }

    public int toPackedRGB() {
        int ri = Math.min(255, (int)(r * 255));
        int gi = Math.min(255, (int)(g * 255));
        int bi = Math.min(255, (int)(b * 255));
        return (ri << 16) | (gi << 8) | bi;
    }
}
