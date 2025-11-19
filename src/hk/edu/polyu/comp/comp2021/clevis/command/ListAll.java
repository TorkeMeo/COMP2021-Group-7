package hk.edu.polyu.comp.comp2021.clevis.command;

import hk.edu.polyu.comp.comp2021.clevis.model.Shape;
import hk.edu.polyu.comp.comp2021.clevis.model.Group;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeManager;

/**
 * REQ14: ListAll
 * command: listAll
 * Effect: Lists the basic information about all of the shapes that have been drawn
 * in decreasing Z-order. Use indentation to indicate the containing relation between
 * group shapes and their component shapes.
 */
public class ListAll {
    private ShapeManager shapeManager;

    public ListAll(ShapeManager shapeManager) {
        this.shapeManager = shapeManager;
    }

    // command listAll
    public void execute(String[] tokens) {
        // check token
        if (tokens.length != 1) throw new IllegalArgumentException("Invalid listAll command format. Expected: listAll");

        // get shapes
        java.util.List<Shape> allShapes = shapeManager.getAllShapes();

        if (allShapes.isEmpty()) {      // no shape
            System.out.println("No shapes have been created yet.");
            return;
        }

        System.out.println("All shapes (in Z-order):");

        for (Shape shape : allShapes) printShapeWithIndent(shape, 0);
    }

    // print shape with relation
    private void printShapeWithIndent(Shape shape, int level) {
        String indent = getIndent(level);
        if (shape instanceof Group) {       // if group
            Group group = (Group) shape;
            System.out.println(indent + "Group: " + shape.getName() + " (Z-index: " + shape.getZIndex() + ")");
            for (Shape component : group.getComponents())       // print members
                printShapeWithIndent(component, level + 1);
        } else {
            java.util.List<String> info = shape.getInfo();
            System.out.println(indent + info.get(0) + " (Z-index: " + shape.getZIndex() + ")");     // simple shape
            for (int i = 1; i < info.size(); i++)                 // more info
                System.out.println(indent + "  " + info.get(i));
        }
    }

    // indentation
    private String getIndent(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) sb.append("  ");
        return sb.toString();
    }
}