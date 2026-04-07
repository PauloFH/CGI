package com.ufrn.paulofh.cg.core;

import java.awt.image.BufferedImage;

public class ImageUtils {

    public static void writePPM(BufferedImage img) {
        int width  = img.getWidth();
        int height = img.getHeight();

        System.out.println("P3");
        System.out.println(width + " " + height);
        System.out.println("255");

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8)  & 0xFF;
                int b =  rgb        & 0xFF;
                System.out.println(r + " " + g + " " + b);
            }
        }
    }
}
