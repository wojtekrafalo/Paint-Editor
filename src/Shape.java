import java.awt.*;

/**
 * Universal class created to store information about every shapes drawn in paint Panel.
 * Class stores information about every kind of shape, what can be confusing.
 * Contains fields like center of a shape, horizontal and vertical size, polygon of an outline
 * and several global flags. Class extends Point java class, that is why methods like distance,
 * equals, translate and basic getters and setters are inherited.
 *
 * @author wojtekrafalo
 * @version 1.0
 * @since 1.0
 */
public class Shape extends Point {
    private Point point;
    Point center;
    private Point lastPoint;
    private double sizeX = 0;
    private double sizeY = 0;
    Polygon polygon;
    private Color color;
    int kind;
    boolean active;
    boolean closedPolygon = false;

    /**
     * Method checks whether point given as parameter is located inside a Shape.
     *
     * @param pointInside testd point.
     * @return true if Shape contains pointInside.
     */
    boolean fieldCondition (Point pointInside) {
        double x1 = pointInside.getX();
        double y1 = pointInside.getY();
        double x = point.getX();
        double y = point.getY();

        switch (kind) {
            case 0 :
                return (distance(x, y, x1, y1) > sizeX);
            case 1 :
                return (!(x1 >= x && x1 <= x + sizeX && y1 >= y && y1 <= y + sizeY));
            case 2 :
                if (closedPolygon) return !polygon.contains(pointInside);
            default :
                return true;
        }
    }


    /**
     * Constructor used to create shape if known is only centre of a shape.
     *
     * @param kind kind of a shape, equals 0 if concerns circle, 1 in case of rectangle, 2 in case of polygon.
     * @param color color of a shape.
     * @param point centre of a shape.
     */
    Shape(int kind, Color color, Point point) {
        this.point = point;
        this.color = color;
        this.kind = kind;
    }


    /**
     * Constructor used to create a circle.
     *
     * @param kind kind of a shape, equals 0 if concerns circle, 1 in case of rectangle, 2 in case of polygon.
     * @param color color of a circle.
     * @param point centre of a circle.
     * @param sizeX radius of a circle.
     */
    Shape(int kind, Color color, Point point, double sizeX) {
        this.kind  = kind;
        this.color = color;
        this.point = point;
        this.sizeX = sizeX;
    }


    /**
     * Constructor used to create a rectangle.
     *
     * @param kind kind of a shape, equals 0 if concerns circle, 1 in case of rectangle, 2 in case of polygon.
     * @param color color of a rectangle.
     * @param point left top vertex of a rectangle.
     * @param sizeX horizontal size of a rectangle.
     * @param sizeY vertical size of a rectangle.
     */
    Shape(int kind, Color color, Point point, double sizeX, double sizeY) {
        this.kind  = kind;
        this.color = color;
        this.point = point;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }


    /**
     * Constructor used to create a polygon.
     *
     * @param kind kind of a shape, equals 0 if concerns circle, 1 in case of rectangle, 2 in case of polygon.
     * @param color color of a polygon.
     * @param point left top vertex of a polygon.
     * @param polygon object of class Polygon, describing all vertices of a polygon.
     */
    Shape(int kind, Color color, Point point, Polygon polygon) {
        this.kind  = kind;
        this.color = color;
        this.polygon = polygon;
        this.point = point;
        this.sizeX = 0;
        this.sizeY = 0;
        this.closedPolygon = true;
    }


    /**
     * Default setter of a horizontal size of a Shape.
     * @param sizeX horizontal size of a Shape.
     */
    void setSizeX(double sizeX) {
        this.sizeX = sizeX;
    }


    /**
     * Default setter of a vertical size of a Shape.
     * @param sizeY vertical size of a Shape.
     */
    void setSizeY(double sizeY) {
        this.sizeY = sizeY;
    }


    /**
     * Default setter of a main point of a Shape.
     * @param point main point of a Shape.
     */
    void setPoint(Point point) {
        this.point = point;
    }


    /**
     * Default setter of a horizontal position of a Shape.
     * @param x horizontal position of a Shape.
     */
    void setX(int x) {
        point.x = x;
    }


    /**
     * Default setter of a vertical position of a Shape.
     * @param y vertical position of a Shape.
     */
    void setY(int y) {
        point.y = y;
    }


    /**
     * Default setter of a last point of a polygon.
     * @param lastPoint last point of a polygon.
     */
    void setLastPoint(Point lastPoint) {
        this.lastPoint = lastPoint;
    }


    /**
     * Default setter of a color of a Shape.
     * @param color color of a Shape.
     */
    void setColor(Color color) {
        this.color = color;
    }


    /**
     * Default setter of a activity flag, which determines, whether Shape is active.
     * @param active activity of a Shape.
     */
    void setActive(boolean active) {
        this.active = active;
    }


    /**
     * Default setter of a flag, which determines,
     * whether Polygon is already closed in case if this kind of Shape was created.
     * @param closedPolygon determines, whether Polygon is already closed.
     */
    void setClosedPolygon(boolean closedPolygon) {
        this.closedPolygon = closedPolygon;
    }


    /**
     * Default getter of a horizontal size of a Shape.
     * @return horizontal size of a Shape.
     */
    double getSizeX() {
        return sizeX;
    }


    /**
     * Default getter of a vertical size of a Shape.
     * @return vertical size of a Shape.
     */
    double getSizeY() {
        return sizeY;
    }


    /**
     * Default getter of a main Point of a Shape.
     * @return main Point of a Shape.
     */
    Point getPoint() {
        return point;
    }


    /**
     * Default getter of a color of a Shape.
     * @return color of a Shape.
     */
    Color getColor() {
        return color;
    }


    /**
     * Default getter of a color of a Shape.
     * @return color of a Shape.
     */
    int getKind() {
        return kind;
    }


    /**
     * Default getter of a activity flag, which determines, whether Shape is active.
     * @return activity of a Shape.
     */
    boolean getActive() {
        return active;
    }


    /**
     * Default getter of a flag, which determines,
     * whether Polygon is already closed in case if this kind of Shape was created.
     * @return determines, whether Polygon is already closed.
     */
    Point getLastPoint() {
        return lastPoint;
    }
}