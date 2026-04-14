package com.ufrn.paulofh.cg;

import com.ufrn.paulofh.cg.core.API;
import com.ufrn.paulofh.cg.rt3.RunningOptions;

/**
 * Entry point.
 * Usage: App <scene.xml> [--outfile <file>] [--quick] [--cropwindow x0,x1,y0,y1]
 */
public class App {

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || args[0].equals("--help")) {
            printHelp();
            return;
        }

        RunningOptions opt = parseArgs(args);
        if (opt.sceneFile == null) {
            System.err.println("Error: no scene file specified.");
            printHelp();
            System.exit(1);
        }

        API.initEngine(opt);
        API.run();
        API.cleanUp();
    }

    private static RunningOptions parseArgs(String[] args) {
        RunningOptions opt = new RunningOptions();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--outfile":
                    if (i + 1 < args.length) opt.outfile = args[++i];
                    break;
                case "--quick":
                    opt.quick = true;
                    break;
                case "--cropwindow":
                    if (i + 1 < args.length) {
                        String[] parts = args[++i].split(",");
                        opt.cropwindow = new int[]{
                            Integer.parseInt(parts[0]), Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]), Integer.parseInt(parts[3])
                        };
                    }
                    break;
                default:
                    if (!args[i].startsWith("--")) opt.sceneFile = args[i];
            }
        }
        return opt;
    }

    private static void printHelp() {
        System.out.println("Usage: App <scene.xml> [options]");
        System.out.println("  --help                      Print this help");
        System.out.println("  --outfile <filename>        Override output filename");
        System.out.println("  --quick                     Render at half resolution");
        System.out.println("  --cropwindow x0,x1,y0,y1   Render sub-window");
    }
}
