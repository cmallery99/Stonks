package main.java.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {
    private Vector2f start;
    private Vector2f end;
    private Vector3f color;
    private int lifetime;

    public Line2D(Vector2f start, Vector2f end, Vector3f color, int lifetime) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifetime = lifetime;
    }

    public int beginFrame() {
        this.lifetime--;
        return this.lifetime;
    }

    public void setStart(Vector2f start) {
        this.start = start;
    }

    public void setEnd(Vector2f end) {
        this.end = end;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public Vector2f getStart() {
        return start;
    }

    public Vector2f getEnd() {
        return end;
    }

    public Vector3f getColor() {
        return color;
    }

    public int getLifetime() {
        return lifetime;
    }

}
