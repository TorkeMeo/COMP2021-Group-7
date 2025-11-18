package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.List;

public abstract class Shape {
    private static int nextZIndex = 0;

    protected String name;
    protected int zIndex;
    protected boolean Grouped = false;

    public Shape(String name) {
        this.name = name;
        this.zIndex = nextZIndex++;
    }

    public String getName() { return name; }
    public int getZIndex() { return zIndex; }
    public boolean isGrouped() {return Grouped;}


    public abstract double[] getBoundingBox();
    public abstract boolean containsPoint(double x, double y);
    public abstract void move(double dx, double dy);
    public abstract List<String> getInfo();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Shape other = (Shape) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() { return name.hashCode(); }
}
