package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.Arrays;
import java.util.List;

public class Circle extends Shape {
    private double x, y, r;

    public Circle(String n, double x, double y, double r) {
        super(n);
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public double[] getBoundingBox() {
        return new double[]{x - r, y - r, r * 2, r * 2};
    }

    @Override
    public boolean containsPoint(double pointX, double pointY) {
        double distanceToCenter = Math.sqrt(Math.pow(pointX - x, 2) + Math.pow(pointY - y, 2));
        return Math.abs(distanceToCenter - r) < 0.05;
    }

    @Override
    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public List<String> getInfo() {
        return Arrays.asList(
                "Type: Circle",
                "Name: " + getName(),
                "Center: (" + String.format("%.2f", x) + ", " + String.format("%.2f", y) + ")",
                "Radius: " + String.format("%.2f", r)
        );
    }

    public double getCenterX() { return x; }
    public double getCenterY() { return y; }
    public double getRadius() { return r; }
}
