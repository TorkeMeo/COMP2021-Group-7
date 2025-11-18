package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;

public class ClevisTest {
    private Clevis clevis;
    private ShapeManager shapeManager;

    @Before
    public void setUp() {
        clevis = new Clevis();
        clevis.initializeLogging("test_log.html", "test_log.txt");
        shapeManager = clevis.getShapeManager();
    }

    @Test
    public void testClevisConstructor(){
        Clevis clevis = new Clevis();
        assert true;
    }

    @Test
    public void testRectangleCreation() {
        clevis.processCommand("rectangle r1 10 20 30 40");
        assertTrue(shapeManager.containsShape("r1"));

        Shape shape = shapeManager.getShape("r1");
        assertTrue(shape instanceof Rectangle);
        Rectangle rect = (Rectangle) shape;
        assertEquals(10, rect.getX(), 0.001);
        assertEquals(20, rect.getY(), 0.001);
        assertEquals(30, rect.getWidth(), 0.001);
        assertEquals(40, rect.getHeight(), 0.001);
    }

    @Test
    public void testLineCreation() {
        clevis.processCommand("line l1 0 0 50 50");
        assertTrue(shapeManager.containsShape("l1"));

        Shape shape = shapeManager.getShape("l1");
        assertTrue(shape instanceof Line);
        Line line = (Line) shape;
        assertEquals(0, line.getX1(), 0.001);
        assertEquals(0, line.getY1(), 0.001);
        assertEquals(50, line.getX2(), 0.001);
        assertEquals(50, line.getY2(), 0.001);
    }

    @Test
    public void testCircleCreation() {
        clevis.processCommand("circle c1 25 25 10");
        assertTrue(shapeManager.containsShape("c1"));

        Shape shape = shapeManager.getShape("c1");
        assertTrue(shape instanceof Circle);
        Circle circle = (Circle) shape;
        assertEquals(25, circle.getCenterX(), 0.001);
        assertEquals(25, circle.getCenterY(), 0.001);
        assertEquals(10, circle.getRadius(), 0.001);
    }

    @Test
    public void testSquareCreation() {
        clevis.processCommand("square s1 5 5 20");
        assertTrue(shapeManager.containsShape("s1"));

        Shape shape = shapeManager.getShape("s1");
        assertTrue(shape instanceof Square);
        Square square = (Square) shape;
        assertEquals(5, square.getX(), 0.001);
        assertEquals(5, square.getY(), 0.001);
        assertEquals(20, square.getSideLength(), 0.001);
    }

    @Test
    public void testDuplicateNameError() {
        clevis.processCommand("rectangle shape1 0 0 10 10");
        clevis.processCommand("circle shape1 5 5 2");

        assertTrue(shapeManager.containsShape("shape1"));
        assertTrue(shapeManager.getShape("shape1") instanceof Rectangle);
    }

    @Test
    public void testCommandHistory() {
        clevis.processCommand("rectangle r1 0 0 10 10");
        clevis.processCommand("circle c1 5 5 3");
        clevis.processCommand("line l1 1 1 2 2");
        clevis.processCommand("square s1 2 2 5");

        List<String> history = shapeManager.getCommandHistory();
        assertEquals(4, history.size());
        assertEquals("rectangle r1 0 0 10 10", history.get(0));
        assertEquals("circle c1 5 5 3", history.get(1));
        assertEquals("line l1 1 1 2 2", history.get(2));
        assertEquals("square s1 2 2 5", history.get(3));
    }

    @Test
    public void testShapeZIndexOrder() {
        clevis.processCommand("rectangle first 0 0 10 10");
        clevis.processCommand("circle second 5 5 3");
        clevis.processCommand("square third 2 2 5");

        List<Shape> shapes = shapeManager.getAllShapes();
        assertEquals(3, shapes.size());
        assertEquals("third", shapes.get(0).getName());
        assertEquals("second", shapes.get(1).getName());
        assertEquals("first", shapes.get(2).getName());

        assertTrue(shapes.get(0).getZIndex() > shapes.get(1).getZIndex());
        assertTrue(shapes.get(1).getZIndex() > shapes.get(2).getZIndex());
    }

    @Test
    public void testInvalidCommands() {
        clevis.processCommand("rectangle");
        clevis.processCommand("line line1");
        clevis.processCommand("circle");
        clevis.processCommand("square");

        assertTrue(true);
    }

    @Test
    public void testQuitCommand() {
        assertTrue(clevis.isRunning());
        clevis.processCommand("quit");
        assertFalse(clevis.isRunning());
    }
}