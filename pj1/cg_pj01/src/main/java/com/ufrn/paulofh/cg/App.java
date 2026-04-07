package com.ufrn.paulofh.cg;

import com.ufrn.paulofh.cg.core.API;
import com.ufrn.paulofh.cg.parser.SceneParser;

/**
 * Entry point. Usage:
 *   App <scene.xml> [--outfile <filename>]
 */
public class App {

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || args[0].equals("--help")) {
            printHelp();
            return;
        }

        String sceneFile = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--outfile") && i + 1 < args.length) {
                i++; // skip next (override not yet implemented)
            } else if (!args[i].startsWith("--")) {
                sceneFile = args[i];
            }
        }

        if (sceneFile == null) {
            System.err.println("Error: no scene file specified.");
            printHelp();
            System.exit(1);
        }

        SceneParser.parse(sceneFile);
        API.render();
    }

    private static void printHelp() {
        System.out.println("Usage: App <scene.xml> [options]");
        System.out.println("  --help              Print this help");
        System.out.println("  --outfile <file>    Override output filename");
    }
}
