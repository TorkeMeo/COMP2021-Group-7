package hk.edu.polyu.comp.comp2021.clevis.command;

import hk.edu.polyu.comp.comp2021.clevis.model.Shape;
import hk.edu.polyu.comp.comp2021.clevis.model.ShapeManager;

/**
 * REQ10: Move
 * Command: move n dx dy
 * Effect: Moves the shape named n, horizontally by dx and vertically by dy. If shape
 * n is a group, all its component shapes are moved.
 */

public class Move {
    private ShapeManager shapeManager;

    public Move(ShapeManager shapeManager) {
        this.shapeManager = shapeManager;
    }

    /**
     * command move
     */
    public void execute(String[] tokens) {
        // 检查参数数量
        if (tokens.length != 4) {
            throw new IllegalArgumentException(
                    "Invalid move command format. Expected: move name dx dy");
        }

        String name = tokens[1];
        double dx = parseDouble(tokens[2], "dx");
        double dy = parseDouble(tokens[3], "dy");

        // get shape
        Shape shape = shapeManager.getShape(name);

        // check if graphs grouped
        if (shape.isGrouped()) {
            throw new IllegalArgumentException(
                    "Shape '" + name + "' is grouped. Cannot move it directly.");
        }

        // execute
        shape.move(dx, dy);

        System.out.println("Shape '" + name + "' moved by (" +
                String.format("%.2f", dx) + ", " +
                String.format("%.2f", dy) + ")");
    }

    /**
     * error message for parse Double
     */
    private double parseDouble(String value, String paramName) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid " + paramName + " value: '" + value + "'. Must be a number.");
        }
    }
}
