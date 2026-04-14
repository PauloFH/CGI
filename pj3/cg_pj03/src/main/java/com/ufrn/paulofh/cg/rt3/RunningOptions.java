package com.ufrn.paulofh.cg.rt3;

/**
 * Holds command-line arguments parsed by App.
 */
public class RunningOptions {
    public String  sceneFile;
    public String  outfile;      // null if not specified
    public boolean quick;        // --quick flag: render at 1/4 resolution
    public int[]   cropwindow;   // [x0,x1,y0,y1] or null
}
