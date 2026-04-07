package com.ufrn.paulofh.cg.core;

/**
 * Represents a camera. Currently only holds type and Film.
 * Will be extended in future projects with projection, position, etc.
 */
public class Camera {

    private final String type; // e.g. "orthographic"
    private final Film   film;

    public Camera(String type, Film film) {
        this.type = type;
        this.film = film;
    }

    public String getType() { return type; }
    public Film   getFilm() { return film; }
}
