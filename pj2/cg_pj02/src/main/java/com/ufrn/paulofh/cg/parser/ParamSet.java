package com.ufrn.paulofh.cg.parser;

import com.ufrn.paulofh.cg.core.RGBColor;

import java.util.HashMap;
import java.util.Map;

/**
 * Bundles XML tag attributes as typed key-value pairs.
 */
public class ParamSet {

    private final Map<String, String> params = new HashMap<>();

    public void add(String key, String value) {
        params.put(key, value);
    }

    public String getString(String key, String def) {
        return params.getOrDefault(key, def);
    }

    public int getInt(String key, int def) {
        String v = params.get(key);
        return v != null ? Integer.parseInt(v.trim()) : def;
    }

    public float getFloat(String key, float def) {
        String v = params.get(key);
        return v != null ? Float.parseFloat(v.trim()) : def;
    }

    /**
     * Parses a color string "R G B".
     * Accepts both integer [0-255] and float [0.0-1.0] values.
     * Returns black if key is absent.
     */
    public RGBColor getColor(String key) {
        String v = params.get(key);
        if (v == null) return new RGBColor(0, 0, 0);

        String[] parts = v.trim().split("\\s+");
        float r = parseComponent(parts[0]);
        float g = parseComponent(parts[1]);
        float b = parseComponent(parts[2]);
        return new RGBColor(r, g, b);
    }

    /** Detects float vs int by presence of '.' or value ≤ 1.0. */
    private float parseComponent(String s) {
        if (s.contains(".")) {
            return Float.parseFloat(s);
        }
        int v = Integer.parseInt(s);
        return v > 1 ? v / 255f : v; // 0 or 1 treated as float
    }
}
