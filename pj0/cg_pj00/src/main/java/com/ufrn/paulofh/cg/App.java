package com.ufrn.paulofh.cg;

import com.ufrn.paulofh.cg.core.part1;
import com.ufrn.paulofh.cg.core.part3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        save(part1.generate(), "output/output_part1.png");
        save(part3.generate(), "output/output_part3.png");
    }

    private static void save(BufferedImage img, String filename) throws IOException {
        File out = new File(filename);
        out.getParentFile().mkdirs();
        ImageIO.write(img, "png", out);
        System.out.println("Saved: " + out.getAbsolutePath());
    }
}
