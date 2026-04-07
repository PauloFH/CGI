package com.ufrn.paulofh.cg.core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Framebuffer: stores pixel colors and saves to PNG or PPM.
 */
public class Film {

    private final int width;
    private final int height;
    private       String filename;
    private final BufferedImage buffer;

    public Film(int width, int height, String filename) {
        this.width    = width;
        this.height   = height;
        this.filename = filename;
        this.buffer   = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public int    getWidth()    { return width; }
    public int    getHeight()   { return height; }
    public String getFilename() { return filename; }
    public void   setFilename(String f) { this.filename = f; }

    public void commit(int x, int y, RGBColor c) {
        buffer.setRGB(x, y, c.toPackedRGB());
    }

    public void writeImage() throws IOException {
        File out = new File(filename);
        out.getParentFile().mkdirs();

        if (filename.endsWith(".ppm")) {
            savePPM(out);
        } else {
            String fmt = filename.substring(filename.lastIndexOf('.') + 1);
            ImageIO.write(buffer, fmt, out);
        }
        System.out.println("Saved: " + out.getAbsolutePath());
    }

    private void savePPM(File out) throws IOException {
        try (PrintStream ps = new PrintStream(out)) {
            ps.println("P3");
            ps.println(width + " " + height);
            ps.println("255");
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = buffer.getRGB(x, y);
                    ps.println(((rgb >> 16) & 0xFF) + " " + ((rgb >> 8) & 0xFF) + " " + (rgb & 0xFF));
                }
            }
        }
    }
}
