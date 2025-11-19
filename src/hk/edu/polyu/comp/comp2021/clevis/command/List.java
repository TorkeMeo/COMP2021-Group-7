package hk.edu.polyu.comp.comp2021.clevis.command;

import hk.edu.polyu.comp.comp2021.clevis.model.Shape;
import hk.edu.polyu.comp.comp2021.clevis.model.Group;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeManager;

/**
 * REQ13: List
 * Command: list n
 * Effect: Lists the basic information about the shape named n
 */

public class List {
    private ShapeManager shapeManager;

    public List(ShapeManager shapeManager) {
        this.shapeManager = shapeManager;
    }

    // command list
    public void execute(String[] tokens) {
        // check token
        if (tokens.length != 2) throw new IllegalArgumentException("Invalid list command format. Expected: list name");

        String name = tokens[1];
        Shape shape = shapeManager.getShape(name);      // get shape
        printShapeInfo(shape);
    }

    // print info
    private void printShapeInfo(Shape shape) {
        if (shape instanceof Group) {       // for group
            Group group = (Group) shape;
            System.out.println("Group: " + shape.getName());
            System.out.println("Components:");
            for (Shape component : group.getComponents()) System.out.println("  - " + component.getName());     // list names
        } else {       // for simple shape
            java.util.List<String> info = shape.getInfo();
            for (String line : info) System.out.println(line);
        }
    }
}