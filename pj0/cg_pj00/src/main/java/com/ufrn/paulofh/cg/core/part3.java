package com.ufrn.paulofh.cg.core;

import java.awt.image.BufferedImage;

/**
 * Part 3 - Background class with bilinear interpolation.
 * Reproduces Part 1 result using the Background class.
 */
public class part3 {

    static final int WIDTH  = 400;
    static final int HEIGHT = 200;

    public static BufferedImage generate() {
        // Same color scheme as Part 1 mapped to 4 corners
        RGBColor bl = new RGBColor(0f, 0f, 0.2f); // bottom-left:  dark blue
        RGBColor br = new RGBColor(1f, 0f, 0.2f); // bottom-right: red + blue
        RGBColor tl = new RGBColor(0f, 1f, 0.2f); // top-left:     green + blue
        RGBColor tr = new RGBColor(1f, 1f, 0.2f); // top-right:    full + blue

        Background bkg = new Background(bl, br, tl, tr);

        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                float u = (float) x / (WIDTH - 1);
                float v = (float) (HEIGHT - 1 - y) / (HEIGHT - 1); // v=0 at bottom

                RGBColor c = bkg.sampleUV(u, v);
                img.setRGB(x, y, c.toPackedRGB());
            }
        }

        return img;
    }

    public static void main(String[] args) {
        BufferedImage img = generate();
        ImageUtils.writePPM(img);
    }
}
