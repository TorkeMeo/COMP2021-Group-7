package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.Arrays;
import java.util.List;

public class Rectangle extends Shape {
    private double x, y, w, h;

    public Rectangle(String n, double x, double y, double w, double h) {
        super(n);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public double[] getBoundingBox() {
        return new double[]{x, y, w, h};
    }

    @Override
    public boolean containsPoint(double pointX, double pointY) {
        double minDistance = Math.min(
                Math.min(Math.abs(pointX - x), Math.abs(pointX - (x + w))),
                Math.min(Math.abs(pointY - y), Math.abs(pointY - (y + h)))
        );
        return minDistance < 0.05;
    }

    @Override
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public List<String> getInfo() {
        return Arrays.asList(
                "Type: Rectangle",
                "Name: " + getName(),
                "Top-left: (" + String.format("%.2f", x) + ", " + String.format("%.2f", y) + ")",
                "Width: " + String.format("%.2f", w),
                "Height: " + String.format("%.2f", h)
        );
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return w; }
    public double getHeight() { return h; }
}