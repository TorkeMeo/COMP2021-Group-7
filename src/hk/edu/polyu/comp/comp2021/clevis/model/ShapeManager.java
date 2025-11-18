package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.*;

public class ShapeManager {
    private Map<String, Shape> shapes;
    private List<String> commandHistory;

    public ShapeManager() {
        shapes = new HashMap<>();
        commandHistory = new ArrayList<>();
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
            if(shape.contianspoint(x,y){
                return shape.getName();
            }
        }
        return null;
    }

    public void recordCommand(String command) { commandHistory.add(command); }

    public List<String> getCommandHistory() { return new ArrayList<>(commandHistory); }
}
