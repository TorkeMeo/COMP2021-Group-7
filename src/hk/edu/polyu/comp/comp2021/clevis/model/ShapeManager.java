package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.*;

public class ShapeManager {
    private Map<String, Shape> shapes;
    private List<String> commandHistory;

    public ShapeManager() {
        shapes = new HashMap<>();
        commandHistory = new ArrayList<>();
    }

    public void createRectangle(String n, double x, double y, double w, double h) {
        if (shapes.containsKey(n))
            throw new IllegalArgumentException("Shape with name '" + n + "' already exists");

        Rectangle rectangle = new Rectangle(n, x, y, w, h);
        shapes.put(n, rectangle);
    }

    public void createLine(String n, double x1, double y1, double x2, double y2) {
        if (shapes.containsKey(n))
            throw new IllegalArgumentException("Shape with name '" + n + "' already exists");

        Line line = new Line(n, x1, y1, x2, y2);
        shapes.put(n, line);
    }

    public void createCircle(String n, double x, double y, double r) {
        if (shapes.containsKey(n))
            throw new IllegalArgumentException("Shape with name '" + n + "' already exists");

        Circle circle = new Circle(n, x, y, r);
        shapes.put(n, circle);
    }

    public void createSquare(String n, double x, double y, double l) {
        if (shapes.containsKey(n))
            throw new IllegalArgumentException("Shape with name '" + n + "' already exists");

        Square square = new Square(n, x, y, l);
        shapes.put(n, square);
    }
    
    public void addShape(Shape shape) {
        if (shapes.containsKey(shape.getName()))
            throw new IllegalArgumentException("Shape with name '" + shape.getName() + "' already exists");
        shapes.put(shape.getName(), shape);
    }

    public Shape getShape(String name) {
        Shape shape = shapes.get(name);
        if (shape == null)
            throw new IllegalArgumentException("Shape '" + name + "' not found");
        return shape;
    }

    public boolean containsShape(String name) { return shapes.containsKey(name); }

    public void addGroup(String groupName, List<String> componentNames) {
        if (shapes.containsKey(groupName)) {
            throw new IllegalArgumentException("Group with name '" + groupName + "' already exists");
        }

        List<Shape> components = new ArrayList<>();
        for (String name: componentNames) {
            Shape component = getShape(name);
            components.add(component);
        }

        //It might not be necessary to check if the components is empty.

        Group group = new Group(groupName, components);
        shapes.put(groupName, group);
    }

    public void ungroup(String groupName) {
        Shape shape = getShape(groupName);

        if (!(shape instanceof Group)) {
            throw new IllegalArgumentException("Shape '" + groupName + "' is not a group");
        }

        Group group = (Group) shape;

        for(Shape component: group.getComponents()) {
            component.Grouped = false;
        }

        shapes.remove(groupName);
    }

    public void delete(String name) {
        Shape shape = getShape(name);

        if (shape instanceof Group) {
            Group group = (Group) shape;
            for (Shape component: group.getComponents()) {
                shapes.remove(component.getName());
            }
        }

        shapes.remove(name);

    }

    public List<Shape> getAllShapes() {
        List<Shape> allShapes = new ArrayList<>(shapes.values());
        allShapes.sort((a, b) -> Integer.compare(b.getZIndex(), a.getZIndex()));
        return allShapes;
    }

    public String shapeAt(double x,double y ){
        List<Shape> ShapeListAll =getAllShapes();
        for(Shape shape : ShapeListAll){
            if(shape.isGroup){
                continue;
            }
            if(shape.containsPoint(x,y)){
                return shape.getName();
            }
        }
        return null;
    }

    public boolean intersect(String n1Name,String n2Name){
        Shape n1Shape = getShape(n1Name);
        Shape n2Shape = getShape(n2Name);

        double[] n1BoundingBox = n1Shape.getBoundingBox();
        double[] n2BoundingBox = n2Shape.getBoundingBox();

        if(n1BoundingBox[0] - n2BoundingBox[2] <n2BoundingBox[0] &&
           n1BoundingBox[0] + n1BoundingBox[2] > n2BoundingBox[0] &&
           n1BoundingBox[1] - n2BoundingBox[3] < n2BoundingBox[1] &&
           n1BoundingBox[1] + n1BoundingBox[3] > n2BoundingBox[1]){
            return true;
        }
        else{
            return false;
        }
    }

    public void recordCommand(String command) { commandHistory.add(command); }

    public List<String> getCommandHistory() { return new ArrayList<>(commandHistory); }
}
