package hk.edu.polyu.comp.comp2021.clevis.model;

import hk.edu.polyu.comp.comp2021.clevis.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class Clevis {
    private ShapeManager shapeManager;
    private Logger logger;
    private String htmlLogFile;
    private String txtLogFile;
    private boolean isRunning;

    public Clevis() {
        this.shapeManager = new ShapeManager();
        this.isRunning = true;
    }

    public void initializeLogging(String htmlLogFile, String txtLogFile) {
        this.htmlLogFile = htmlLogFile;
        this.txtLogFile = txtLogFile;
        this.logger = new Logger(htmlLogFile, txtLogFile);
    }

    public void processCommand(String command) {
        if (logger != null)
            logger.logCommand(command);

        shapeManager.recordCommand(command);
        String[] tokens = command.trim().split("\\s+");

        if (tokens.length == 0) return;

        try {
            switch (tokens[0].toLowerCase()) {
                case "rectangle":
                    processRectangle(tokens);
                    break;
                case "line":
                    processLine(tokens);
                    break;
                case "circle":
                    processCircle(tokens);
                    break;
                case "square":
                    processSquare(tokens);
                    break;
                case "group":
                    processGroup(tokens);
                    break;
                case "ungroup":
                    processUngroup(tokens);
                    break;
                case "delete":
                    processDelete(tokens);
                    break;
                case "boundingbox":
                    processBoundingBox(tokens);
                    break;
                case "shapeat":
                    processShapeAt(tokens);
                    break;
                case "intersect":
                    processIntersect(tokens);
                    break;
                case "quit":
                    processQuit();
                    break;
            }
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    public void processRectangle(String[] tokens) {
        if (tokens.length != 6)
            throw new IllegalArgumentException("Invalid rectangle command format. Please enter: rectangle name x y width height");

        String n = tokens[1];
        double x = Double.parseDouble(tokens[2]);
        double y = Double.parseDouble(tokens[3]);
        double w = Double.parseDouble(tokens[4]);
        double h = Double.parseDouble(tokens[5]);

        shapeManager.createRectangle(n, x, y, w, h);
        System.out.println("Rectangle '" + n + "' created successfully");
    }

    public void processLine(String[] tokens) {
        if (tokens.length != 6)
            throw new IllegalArgumentException("Invalid line command format. Please enter: line name x1 y1 x2 y2");

        String n = tokens[1];
        double x1 = Double.parseDouble(tokens[2]);
        double y1 = Double.parseDouble(tokens[3]);
        double x2 = Double.parseDouble(tokens[4]);
        double y2 = Double.parseDouble(tokens[5]);

        shapeManager.createLine(n, x1, y1, x2, y2);
        System.out.println("Line '" + n + "' created successfully");
    }

    public void processCircle(String[] tokens) {
        if (tokens.length != 5)
            throw new IllegalArgumentException("Invalid circle command format. Please enter: circle name centerX centerY radius");

        String n = tokens[1];
        double x = Double.parseDouble(tokens[2]);
        double y = Double.parseDouble(tokens[3]);
        double r = Double.parseDouble(tokens[4]);

        shapeManager.createCircle(n, x, y, r);
        System.out.println("Circle '" + n + "' created successfully");
    }

    public void processSquare(String[] tokens) {
        if (tokens.length != 5)
            throw new IllegalArgumentException("Invalid square command format. Please enter: square name x y sideLength");

        String n = tokens[1];
        double x = Double.parseDouble(tokens[2]);
        double y = Double.parseDouble(tokens[3]);
        double l = Double.parseDouble(tokens[4]);

        shapeManager.createSquare(n, x, y, l);
        System.out.println("Square '" + n + "' created successfully");
    }

    private void processGroup(String[] tokens) {
        if (tokens.length < 3) {
            throw new IllegalArgumentException("Invalid group command format. Please enter: group groupName shape1 shape2 ...");
        }

        String groupName = tokens[1];
        List<String> componentNames = Arrays.asList(tokens).subList(2, tokens.length);

        shapeManager.addGroup(groupName, componentNames);
        System.out.println("Group '" + groupName + "' created successfully");
    }

    private void processUngroup(String[] tokens) {
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid ungroup command format. Please enter: ungroup name");
        }

        String groupName = tokens[1];
        shapeManager.ungroup(groupName);
        System.out.println("Group '" + groupName + "' ungrouped");
    }

    private void processDelete(String[] tokens) {
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid delete command format. Please enter: delete name");
        }

        String name = tokens[1];
        isGrouped(name);
        shapeManager.delete(name);
    }

    private void processBoundingBox(String[] tokens) {
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Invalid boundingbox command format. Please enter: boundingbox name");
        }

        String name = tokens[1];

        Shape shape = shapeManager.getShape(name);
        double[] boundingBox = shape.getBoundingBox();

        System.out.printf("%.2f %.2f %.2f %.2f%n",
                boundingBox[0], boundingBox[1], boundingBox[2], boundingBox[3]);
    }

    private void processShapeAt(String[] tokens){
        if(tokens.length !=3){
            throw new IllegalArgumentException("Invalid shapeat command format. Please enter: shapeAt x y");
        }
        double[] PointXY = {Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2])};

        String targetShapeName = shapeManager.shapeAt(PointXY[0],PointXY[1]);

        if (targetShapeName != null){
            System.out.println("The shape at point (" + PointXY[0] + "," + PointXY[1] + ") is:" + targetShapeName + ".");
        }
        else{
            System.out.println("There is no shape found at point (" + PointXY[0] + "," + PointXY[1] + ").");
        }
        
    }

    private void processIntersect(String[] tokens){
        if(tokens.length != 3){
            throw new IllegalArgumentException("Invalid intersect command format. Please enter: intersect n1 n2");
        }
        String[] shapeNames = {tokens[1],tokens[2]};
        isGrouped(shapeNames[0]);
        isGrouped(shapeNames[1]);
        if(shapeManager.intersect(shapeNames[0],shapeNames[1])){
            System.out.println("Shape " + shapeNames[0] + " and " + shapeNames[1] + " intersect.");
        }
        else{
            System.out.println("Shape " + shapeNames[0] + " and " + shapeNames[1] + " not intersect.");
        }
    }

    private void isGrouped(String name) {
        Shape shape = shapeManager.getShape(name);
        if (shape.isGrouped()) {
            throw new IllegalArgumentException(
                    "Shape '" + name + "' is grouped. Please operate on the group instead.");
        }
    }

    public void closeLogging() {
        if (logger != null)
            logger.close();
    }
    
    private void processQuit() {
        System.out.println("Goodbye!");
        isRunning = false;
        closeLogging();
    }

    public boolean isRunning() { return isRunning; }

    public ShapeManager getShapeManager() { return shapeManager; }
}
