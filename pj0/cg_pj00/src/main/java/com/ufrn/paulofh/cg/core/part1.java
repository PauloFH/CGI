package com.ufrn.paulofh.cg.core;

import java.awt.image.BufferedImage;

/**
 * Part 1 - Simple image generation.
 * Red increases left→right, Green increases bottom→top, Blue fixed at 20%.
 */
public class part1 {

    static final int WIDTH  = 400;
    static final int HEIGHT = 200;

    public static BufferedImage generate() {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int r = (x * 255) / (WIDTH - 1);
                int g = ((HEIGHT - 1 - y) * 255) / (HEIGHT - 1); // inverted: top = 255
                int b = 51; // 20% of 255

                img.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }

        return img;
    }

    public static void main(String[] args) {
        BufferedImage img = generate();
        ImageUtils.writePPM(img);
    }
}
