package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.Arrays;
import java.util.List;

public class Line extends Shape {
    private double x1, y1, x2, y2;

    public Line(String n, double x1, double y1, double x2, double y2) {
        super(n);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public double[] getBoundingBox() {
        double minX = Math.min(x1, x2);
        double minY = Math.min(y1, y2);
        double maxX = Math.max(x1, x2);
        double maxY = Math.max(y1, y2);
        return new double[]{minX, minY, maxX - minX, maxY - minY};
    }

    @Override
    public boolean containsPoint(double pointX, double pointY) {
        double lineLength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        if (lineLength == 0) {
            return Math.sqrt(Math.pow(pointX - x1, 2) + Math.pow(pointY - y1, 2)) < 0.05;
        }

        double t = Math.max(0, Math.min(1, ((pointX - x1) * (x2 - x1) + (pointY - y1) * (y2 - y1)) / (lineLength * lineLength)));
        double projX = x1 + t * (x2 - x1);
        double projY = y1 + t * (y2 - y1);
        double distance = Math.sqrt(Math.pow(pointX - projX, 2) + Math.pow(pointY - projY, 2));

        return distance < 0.05;
    }

    @Override
    public void move(double dx, double dy) {
        this.x1 += dx;
        this.y1 += dy;
        this.x2 += dx;
        this.y2 += dy;
    }

    @Override
    public List<String> getInfo() {
        return Arrays.asList(
                "Type: Line",
                "Name: " + getName(),
                "Point 1: (" + String.format("%.2f", x1) + ", " + String.format("%.2f", y1) + ")",
                "Point 2: (" + String.format("%.2f", x2) + ", " + String.format("%.2f", y2) + ")"
        );
    }

    public double getX1() { return x1; }
    public double getY1() { return y1; }
    public double getX2() { return x2; }
    public double getY2() { return y2; }
}
