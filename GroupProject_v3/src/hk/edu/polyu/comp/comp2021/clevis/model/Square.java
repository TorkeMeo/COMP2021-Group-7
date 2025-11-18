package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.Arrays;
import java.util.List;

public class Square extends Shape {
    private double x, y, l;

    public Square(String n, double x, double y, double l) {
        super(n);
        this.x = x;
        this.y = y;
        this.l = l;
    }

    @Override
    public double[] getBoundingBox() {
        return new double[]{x, y, l, l};
    }

    @Override
    public boolean containsPoint(double pointX, double pointY) {
        double minDistance = Math.min(
                Math.min(Math.abs(pointX - x), Math.abs(pointX - (x + l))),
                Math.min(Math.abs(pointY - y), Math.abs(pointY - (y + l)))
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
                "Type: Square",
                "Name: " + getName(),
                "Top-left: (" + String.format("%.2f", x) + ", " + String.format("%.2f", y) + ")",
                "Side length: " + String.format("%.2f", l)
        );
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getSideLength() { return l; }
}