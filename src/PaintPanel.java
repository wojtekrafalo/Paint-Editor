import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.awt.geom.Point2D.distance;
import static java.lang.StrictMath.abs;

/**
 * Class of a Panel provided for drawing shapes.
 * Additionally class implements MouseListener, MouseMotionListener and MouseWheelListener
 * to respond to events like moving shapes, changing color, resizing by mouse wheel.
 * @author wojtekrafalo
 * @version 1.0
 * @since 1.0
 */
public class PaintPanel extends JPanel implements MouseListener, MouseMotionListener , MouseWheelListener {
    private ArrayList<Shape> shapes = new ArrayList<>();

    private Shape move = null;
    private Shape createdShape = null;
    private Shape activeShape = null;

    private boolean boolCreated = false;
    private boolean boolHit = false;
    private boolean boolActive = false;

    private Point movingPoint = null;

    private int distance;
    private double wheelConst;
    int chosenKind= 0;                                                                                                  //chosen kind of a shape.
    Color chosenColor = Color.RED;

    private int x,y, s1, s2;
    private int kind;

    /**
     * Constructor of a panel. Sets basic properties, like name, visibility, size, layout, background.
     * @param WIDTH width of window.
     * @param positionY vertical position of the Panel.
     * @param panelHEIGHT height of the Panel.
     * @param distance maximal distance used to finish a polygon. It is finished,
     *                 when last point was chosen in this distance from a first vertex.
     * @param wheelConst scale of resizing shapes using wheel of a mouse.
     */
    PaintPanel(int WIDTH, int positionY, int panelHEIGHT, int distance, double wheelConst){
        setLayout(null);
        setBackground(Color.GRAY);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        setBounds(0, positionY, WIDTH, panelHEIGHT);

        this.distance = distance;
        this.wheelConst = wheelConst;
    }


    /**
     * Method implemented from a MouseListener.
     * Provides actions like pressing a mouse at a canvas or shape,
     * to create new one or set an activity of existing one.
     * @param event object of an event of a pressed mouse button.
     */
    @Override
    public void mousePressed (MouseEvent event){
        if (event.getButton() == MouseEvent.BUTTON1 && !boolCreated)                                                    //case of an event, when user clicked at canvas
            for (Shape p : shapes) {
                if (p.fieldCondition(event.getPoint())) {move = null; boolHit = false;}
                else {
                    move = p;
                    movingPoint = event.getPoint();
                    boolHit = true;
                    break;
                }
            }

        activeShape = null;
        boolActive = false;
        for (Shape p : shapes)
            if (!(p.fieldCondition(event.getPoint()))) {                                                                //case of an event, when user clicked at shape
                if (boolCreated && p != createdShape) {                                                                 //condition, when user creates a polygon. Different shapes shouldn't interfere.
                    continue;
                }
                if (!boolActive) {                                                                                      //setting an activity
                    activeShape = p;
                    boolActive = true;
                    p.setActive(true);
                }
                if (p.getActive() && event.getButton() == MouseEvent.BUTTON3) {                                         //displaying a window for typing a color
                    Color temp = p.getColor();
                    p.setColor(JColorChooser.showDialog(null, "Chose color", temp));
                    repaint();
                    break;
                }
            }
            else p.setActive(false);
    }


    /**
     * Method implemented from a MouseListener.
     * Provides actions like releasing a mouse,
     * when a shape was moving to different position.
     * @param event object of an event of a released mouse button.
     */
    @Override
    public void mouseReleased (MouseEvent event){
        if (boolHit) {                                                                                                  //ending the motion
            if (move.getKind() == 2) {
                int minX=10000, minY=10000, maxX=-1, maxY=-1;
                for (int i = 0; i < move.polygon.npoints ; i ++) {
                    if (move.polygon.xpoints[i] < minX) minX = (int) move.polygon.xpoints[i];
                    if (move.polygon.xpoints[i] > maxX) maxX = (int) move.polygon.xpoints[i];
                    if (move.polygon.ypoints[i] < minY) minY = (int) move.polygon.ypoints[i];
                    if (move.polygon.ypoints[i] > maxY) maxY = (int) move.polygon.ypoints[i];
                }
                createdShape.center = new Point((maxX + minX)/2 , (maxY + minY)/2);
            }

            boolHit = false;
            move = null;
        }
        repaint();
    }


    /**
     * Method implemented from a MouseListener.
     * It does not have any usage.
     * @param event object of an event of an entered mouse into Panel.
     */
    @Override
    public void mouseEntered (MouseEvent event){}


    /**
     * Method implemented from a MouseListener.
     * It does not have any usage.
     * @param event object of an event of an exited mouse from Panel.
     */
    @Override
    public void mouseExited (MouseEvent event){}


    /**
     * Method implemented from a MouseListener.
     * Provides actions like clicking a mouse button,
     * to create new shape, add vertex to a polygon or setting activity.
     * @param event object of an event of a clicked mouse button.
     */
    @Override
    public void mouseClicked (MouseEvent event){
        if ((boolCreated) && (event.getButton() == MouseEvent.BUTTON1)) {                                               //a condition to end a creation of shape
            if (kind == 2) {                                                                                            //actions on a polygon (ending, adding a vertex)
                x = (int) createdShape.getPoint().getX();
                y = (int) createdShape.getPoint().getY();
                if (distance(event.getX(), event.getY(), x, y) < distance) {                                            //closing a polygon
                    boolCreated = false;
                    createdShape.setClosedPolygon(true);
                }
                else createdShape.setClosedPolygon(false);
                if (!createdShape.closedPolygon) {                                                                      //adding a vertex to a polygon
                    int x2 = (int) createdShape.getLastPoint().getX();
                    int y2 = (int) createdShape.getLastPoint().getY();
                    createdShape.polygon.addPoint((int) event.getX(), (int) event.getY());
                    createdShape.setLastPoint(event.getPoint());
                }
            }
            else boolCreated = false;                                                                                   //closing a circle of a rectangle
            repaint();
        }
        else if (event.getButton() == MouseEvent.BUTTON1 && !boolCreated) {                                             //condition for closing a shape
            boolHit = false;
            for (Shape p : shapes) {
                boolHit = (!(p.fieldCondition(event.getPoint())));                                                      //checking if user clicked at a shape
                if (boolHit) break;
            }
            if (!boolHit) {

                createdShape = new Shape(chosenKind, chosenColor, event.getPoint());                                    //creating a shape
                shapes.add(createdShape);
                boolCreated = true;
                if (createdShape.kind == 2) {                                                                           //creating a generalPath
                    createdShape.polygon = new Polygon();
                    createdShape.polygon.addPoint((int) event.getX(), (int) event.getY());
                    createdShape.setLastPoint (event.getPoint());
                }
            }
        }
    }



    /**
     * Method implemented from a MouseMotionListener.
     * Provides actions like moving a mouse,
     * when a circle or rectangle was created and user chooses its size.
     * @param event object of an event of a moved mouse.
     */
    @Override
    public void mouseMoved (MouseEvent event){
        if (boolCreated) {                                                                                              //typing a size of a circle of rectangle
            x = (int) createdShape.getPoint().getX();
            y = (int) createdShape.getPoint().getY();

            int x1 = event.getX();
            int y1 = event.getY();

            s1= (int) createdShape.getSizeX();
            s2= (int) createdShape.getSizeY();

            if (chosenKind == 1) {
                createdShape.setSizeX(abs(x1 - x));
                createdShape.setSizeY(abs(y1 - y));
            }
            else if (chosenKind == 0) {
                createdShape.setSizeX(distance(x, y, x1, y1));
            }
            repaint();
        }
    }


    /**
     * Method implemented from a MouseMotionListener.
     * Provides actions like dragging a mouse,
     * when a shape was already chosen to move it to different position.
     * @param event object of an event of a dragged mouse.
     */
    @Override
    public void mouseDragged (MouseEvent event){
        if (boolHit) {                                                                                                  //when shapes was already pressed
            x = event.getX();
            y = event.getY();
            int x1 = (int) movingPoint.getX();
            int y1 = (int) movingPoint.getY();
            int deltaX = x - x1;
            int deltaY = y - y1;
            if (move.getKind() == 0 || move.getKind() == 1) {
                move.setX((int) move.getPoint().getX() + deltaX);                                                       //moving
                move.setY((int) move.getPoint().getY() + deltaY);
            }
            else if (move.getKind() == 2) {
                move.polygon.translate(deltaX, deltaY);
                move.setPoint(new Point(move.polygon.xpoints[0], move.polygon.ypoints[0]));
            }

            movingPoint = event.getPoint();
            repaint();
        }
    }


    /**
     * Method implemented from a MouseWheelListener.
     * Provides actions like moving a mouse wheel,
     * when some shape is active, to resize it. Method works also at touchPad.
     * @param event object of an event of a moved mouse wheel.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        if (boolActive) {
            int source = event.getWheelRotation();
            s1 = (int) activeShape.getSizeX();
            s2 = (int) activeShape.getSizeY();
            double change = (100 + source * wheelConst) / 100;
            activeShape.setSizeX((int)(s1 * change));                                                                   //percentage change of a sizeX and sizeY
            activeShape.setSizeY((int)(s2 * change));

            if (activeShape.getKind() == 2) {                                                                           //resizing a rectangle
                for (int i = 0; i < activeShape.polygon.npoints ; i ++) {
                    activeShape.polygon.xpoints[i] = activeShape.center.x + (int)((activeShape.polygon.xpoints[i] - activeShape.center.x)*change);
                    activeShape.polygon.ypoints[i] = activeShape.center.y + (int)((activeShape.polygon.ypoints[i] - activeShape.center.y)*change);
                }
            }
        }
    }

    /**
     * A method inherited from JComponent. Paints a canvas.
     * @param g object of a canvas, on which shapes are drawing.
     */
    protected void paintComponent (Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        draw(g2d);
    }

    /**
     * A method draws all shapes at canvas with properties described at Shape class, like
     * colour, coordinates. When a shape is active, it strokes a dark outline around the shape.
     * @param g2d object of a canvas, on which shapes are drawing.
     */
    private void draw (Graphics2D g2d){

        for (Shape p : shapes) {
            x = (int) p.getPoint().getX();
            y = (int) p.getPoint().getY();
            s1= (int) p.getSizeX();
            s2= (int) p.getSizeY();
            Color color = p.getColor();
            kind = p.getKind();

            if (s1<0) {s1 = abs(s1); x = x - s1;}                                                                       //changing the sizeX for a circle
            if (s2<0) {s2 = abs(s2); y = y - s2;}

            g2d.setColor(color);
            if (kind == 0) {
                g2d.fillOval(x - s1, y - s1, 2*s1, 2*s1);
                if (p.active) {
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(x - s1, y - s1, 2*s1, 2*s1);
                    g2d.setColor(color);
                }
            }
            else if (kind == 1) {
                g2d.fillRect(x, y, s1, s2);
                if (p.active) {
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(x, y, s1, s2);
                    g2d.setColor(color);
                }
            }
            else if (kind == 2) {
                if (!p.closedPolygon) g2d.draw(p.polygon);
                else g2d.fill(p.polygon);

                if (p.active) {
                    g2d.setColor(Color.BLACK);
                    g2d.draw(p.polygon);
                    g2d.setColor(color);
                }
            }
        }
        repaint();
    }

    /**
     * Default getter of a list of shapes.
     * @return list of all shapes stored in panel.
     */
    ArrayList<Shape> getShapes () {
        return shapes;
    }
}