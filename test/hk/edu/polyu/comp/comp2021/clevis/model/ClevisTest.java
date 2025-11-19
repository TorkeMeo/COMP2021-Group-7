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
    public void testEmptyCommand() {
        clevis.processCommand("");
        clevis.processCommand("   ");
        clevis.processCommand("\t");
        assertTrue(true);
    }

    @Test
    public void testUnknownCommand() {
        clevis.processCommand("invalidcommand param1 param2");
        assertTrue(true);
    }

    @Test
    public void testInvalidNumberFormat() {
        clevis.processCommand("rectangle invalid x y 10 10");
        clevis.processCommand("circle invalid 5 abc 10");
        assertTrue(true);
    }

    @Test
    public void testWithoutLoggingInitialized() {
        Clevis freshClevis = new Clevis();
        freshClevis.processCommand("rectangle test 0 0 10 10");
        assertTrue(freshClevis.getShapeManager().containsShape("test"));
    }

    @Test
    public void testCloseLogging() {
        clevis.closeLogging();
        assertTrue(true);
    }

    @Test
    public void testQuitCommand() {
        assertTrue(clevis.isRunning());
        clevis.processCommand("quit");
        assertFalse(clevis.isRunning());
    }

    //Fenggang add. Needing select and improve.
    @Test
    public void testProcessIntersectOverlappingShapes() {
        clevis.processCommand("rectangle r1 0 0 20 20");
        clevis.processCommand("rectangle r2 10 10 20 20");
        clevis.processCommand("intersect r1 r2");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Shape r1 and r2 intersect"));
    }
    
    @Test
    public void testProcessIntersectNonOverlappingShapes() {
        clevis.processCommand("rectangle r1 0 0 10 10");
        clevis.processCommand("rectangle r2 50 50 10 10");
        clevis.processCommand("intersect r1 r2");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Shape r1 and r2 do not intersect"));
    }
    
    @Test
    public void testProcessIntersectSameShape() {
        clevis.processCommand("rectangle r1 0 0 10 10");
        clevis.processCommand("intersect r1 r1");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Shape r1 and r1 intersect"));
    }
    
    @Test
    public void testProcessIntersectDifferentShapeTypes() {
        clevis.processCommand("circle c1 10 10 5");
        clevis.processCommand("rectangle r1 12 12 10 10");
        clevis.processCommand("intersect c1 r1");
        
        String output = outputStream.toString();
        assertTrue(output.contains("intersect"));
    }
    
    @Test
    public void testProcessIntersectWithLine() {
        clevis.processCommand("line l1 0 0 20 20");
        clevis.processCommand("rectangle r1 5 5 10 10");
        clevis.processCommand("intersect l1 r1");
        
        String output = outputStream.toString();
        assertTrue(output.contains("intersect"));
    }
    
    @Test
    public void testProcessIntersectNonExistentFirstShape() {
        clevis.processCommand("rectangle r1 0 0 10 10");
        clevis.processCommand("intersect nonexistent r1");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Error:"));
        assertTrue(output.contains("not found"));
    }
    
    @Test
    public void testProcessIntersectNonExistentSecondShape() {
        clevis.processCommand("rectangle r1 0 0 10 10");
        clevis.processCommand("intersect r1 nonexistent");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Error:"));
        assertTrue(output.contains("not found"));
    }
    
    @Test
    public void testProcessIntersectGroupedShape() {
        clevis.processCommand("rectangle r1 0 0 10 10");
        clevis.processCommand("circle c1 5 5 3");
        clevis.processCommand("group g1 r1 c1");
        
        clevis.processCommand("intersect r1 c1");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Error:"));
        assertTrue(output.contains("is grouped"));
    }
    
    @Test
    public void testProcessIntersectInvalidCommandFormat() {
        clevis.processCommand("intersect");
        clevis.processCommand("intersect r1");
        clevis.processCommand("intersect r1 r2 r3");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Error:"));
        assertTrue(output.contains("Invalid intersect command format"));
    }
    //Fenggang add. Needing select and improve.
    @Test
    public void testProcessShapeAtPointInsideShape() {
        clevis.processCommand("rectangle r1 10 10 30 30");
        clevis.processCommand("shapeat 15 15");
        
        String output = outputStream.toString();
        assertTrue(output.contains("The shape at point (15.0, 15.0) is: r1"));
    }
    
    @Test
    public void testProcessShapeAtPointOnBoundary() {
        clevis.processCommand("rectangle r1 10 10 30 30");
        clevis.processCommand("shapeat 10 10");
        
        String output = outputStream.toString();
        assertTrue(output.contains("The shape at point (10.0, 10.0) is: r1"));
    }
    
    @Test
    public void testProcessShapeAtPointOutsideShape() {
        clevis.processCommand("rectangle r1 10 10 30 30");
        clevis.processCommand("shapeat 5 5");
        
        String output = outputStream.toString();
        assertTrue(output.contains("There is no shape found at point (5.0, 5.0)"));
    }
    
    @Test
    public void testProcessShapeAtWithMultipleShapes() {
        clevis.processCommand("rectangle r1 0 0 20 20");
        clevis.processCommand("circle c1 10 10 5");
        
        clevis.processCommand("shapeat 10 10");
        
        String output = outputStream.toString();
        assertTrue(output.contains("The shape at point (10.0, 10.0) is: c1"));
    }
    
    @Test
    public void testProcessShapeAtWithNoShapes() {
        clevis.processCommand("shapeat 10 10");
        
        String output = outputStream.toString();
        assertTrue(output.contains("There is no shape found at point (10.0, 10.0)"));
    }
    
    @Test
    public void testProcessShapeAtDifferentShapeTypes() {
        clevis.processCommand("circle c1 25 25 10");
        clevis.processCommand("shapeat 25 25");
        
        String output = outputStream.toString();
        assertTrue(output.contains("The shape at point (25.0, 25.0) is: c1"));
    }
    
    @Test
    public void testProcessShapeAtWithLine() {
        clevis.processCommand("line l1 0 0 50 50");
        clevis.processCommand("shapeat 25 25");
        
        String output = outputStream.toString();
        assertTrue(output.contains("The shape at point (25.0, 25.0) is: l1"));
    }
    
    @Test
    public void testProcessShapeAtWithSquare() {
        clevis.processCommand("square s1 5 5 20");
        clevis.processCommand("shapeat 10 10");
        
        String output = outputStream.toString();
        assertTrue(output.contains("The shape at point (10.0, 10.0) is: s1"));
    }
    
    @Test
    public void testProcessShapeAtInvalidCommandFormat() {
        clevis.processCommand("shapeat");
        clevis.processCommand("shapeat 10");
        clevis.processCommand("shapeat 10 20 30");
        
        String output = outputStream.toString();
        assertTrue(output.contains("Error:"));
        assertTrue(output.contains("Invalid shapeat command format"));
    }
    
    @Test
    public void testProcessShapeAtWithInvalidNumbers() {
        clevis.processCommand("shapeat x y"); // 非数字参数
        
        String output = outputStream.toString();
        assertTrue(output.contains("Error:"));
    }
    
    @Test
    public void testProcessShapeAtSkipGroups() {
        clevis.processCommand("rectangle r1 0 0 10 10");
        clevis.processCommand("circle c1 5 5 3");
        clevis.processCommand("group g1 r1 c1");
        
        clevis.processCommand("shapeat 5 5");
        
        String output = outputStream.toString();
        assertTrue(output.contains("There is no shape found at point (5.0, 5.0)"));
    }
    
    @Test
    public void testProcessShapeAtComplexScenario() {
        clevis.processCommand("rectangle background 0 0 100 100");
        clevis.processCommand("circle middle 25 25 15");
        clevis.processCommand("square front 20 20 10");
        
        clevis.processCommand("shapeat 15 15");
        String output = outputStream.toString();
        
        clevis.processCommand("shapeat 25 25");
        output = outputStream.toString();
        assertTrue(output.contains("The shape at point (25.0, 25.0) is: front"));
    }
    //Fenggang add done.
}
