package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;
import java.util.List;

public class Group extends Shape{
    private List<Shape> components;

    Group(String name, List<Shape> components) {
        super(name);
        this.components = new ArrayList<>(components);

        for(Shape component: components) {
            component.Grouped = true;
        }
    }

    public List<Shape> getComponents() {
        return new ArrayList<>(components);
    }

    @Override
    public double[] getBoundingBox() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Shape component: components) {
            double[] boundingBox = component.getBoundingBox();
            minX = Math.min(minX, boundingBox[0]);
            maxY = Math.max(maxY, boundingBox[1]);
            minY = Math.min(minY, boundingBox[1] + boundingBox[3]);
            maxX = Math.max(maxX, boundingBox[0] + boundingBox[2]);
        }

        return new double[]{minX, minY, maxX - minX, maxY - minY};
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return false;
    }

    @Override
    public void move(double dx, double dy) {

    }

    @Override
    public List<String> getInfo() {
        return List.of();
    }


}
