import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class of Project. Represents a window with two panels:
 * Menu and Paint Panel. Additionally class implements
 * ActionListener to respond to manipulating buttons.
 * @author wojtekrafalo
 * @version 1.0
 * @since 1.0
 */
public class MainWindow extends JFrame implements ActionListener{
    private PaintPanel paintPanel;

    /**
     * Constructor of a window. Sets basic properties of a window, such as
     * name, visibility, size. Creates an object of a MenuPanel
     * with all buttons and default PaintPanel.
     * @param WIDTH width of window.
     * @param menuHEIGHT height of a menu Panel.
     * @param panelHEIGHT height of a paint Panel.
     * @param distance maximal distance used to finish a polygon.
     * @param wheelConst scale of resizing shapes using wheel of a mouse.
     */
    private MainWindow(int WIDTH, int menuHEIGHT, int panelHEIGHT, int distance, double wheelConst) {
        super("Paint Editor");

        this.setSize(new Dimension(WIDTH,menuHEIGHT + panelHEIGHT));
        setVisible(true);
        JLayeredPane main = new JLayeredPane();
        main.setPreferredSize(new Dimension(WIDTH, menuHEIGHT + panelHEIGHT));
        this.add(main);

        addWindowListener(new MenuDemoWindowAdapter ());
        JPanel menuPanel = new JPanel();
        paintPanel = new PaintPanel (WIDTH, menuHEIGHT, panelHEIGHT, distance, wheelConst);


        menuPanel.setBackground(Color.WHITE);
        menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuPanel.setBounds(0,0, WIDTH, menuHEIGHT);

        Button circle = new Button("CIRCLE");
        Button rectangle = new Button("RECTANGLE");
        Button polygon = new Button("POLYGON");
        Button color = new Button("COLOR");
        Button info = new Button("INFO");
        Button save = new Button ("SAVE");
        Button load = new Button ("LOAD");
        Button reset = new Button ("RESET");
        Button delete = new Button("DELETE");

        circle.addActionListener(this);
        rectangle.addActionListener(this);
        polygon.addActionListener(this);
        color.addActionListener(this);
        info.addActionListener(this);
        save.addActionListener(this);
        load.addActionListener(this);
        reset.addActionListener(this);
        delete.addActionListener(this);

        menuPanel.add(circle);
        menuPanel.add(rectangle);
        menuPanel.add(polygon);
        menuPanel.add(color);
        menuPanel.add(info);
        menuPanel.add(save);
        menuPanel.add(load);
        menuPanel.add(reset);
        menuPanel.add(delete);

        main.add(menuPanel);
        main.add(paintPanel);

        main.setBackground(Color.GRAY);
        main.setLayout (null);
    }


    /**
     * Method implemented with ActionListener.
     * Provides actions such as clicking buttons at menu Panel.
     * @param e object of an event captured by ActionListener.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        String information = "Paint_Editor\n" +
                "Hello in program to drawing geometric shapes. \n" +
                "Choose a shape which you want to draw. Then choose a color. \n" +
                "Draw a shape by choosing a start point and then a close point. \n" +
                "If you want to draw the polygon, every clicked point means one vertex of your polygon. \n" +
                "You can move your created shape to other place by holding left mouse button and resize shape by your scroll wheel. \n" +
                "You can change shape's color by second mouse button and choose a color. \n" +
                "Author: Wojciech-Karol Rafalowski";

        switch (e.getActionCommand()) {
            case "CIRCLE":
                paintPanel.chosenKind = 0;
                break;
            case "RECTANGLE":
                paintPanel.chosenKind = 1;
                break;
            case "POLYGON":
                paintPanel.chosenKind = 2;
                break;
            case "COLOR":
                paintPanel.chosenColor = JColorChooser.showDialog(this, "Choose a color", paintPanel.chosenColor);
                break;
            case "INFO":
                JOptionPane.showMessageDialog(this, information);
                break;
            case "RESET":
                paintPanel.getShapes().clear();
                break;
            case "SAVE":
                String name = JOptionPane.showInputDialog("Please type a name of file. Your file would be saved at main Project folder.");

                try {
                    PrintWriter saving = new PrintWriter(name + ".txt");

                    for (Shape f : paintPanel.getShapes()) {
                        saving.println(f.getKind() + " ");
                        saving.println(f.getColor().getRed() + " " + f.getColor().getGreen() + " " + f.getColor().getBlue() + " ");
                        saving.println((int) f.getPoint().getX() + " " + (int) f.getPoint().getY());

                        if (f.getKind() == 2) {
                            saving.println(f.polygon.npoints);
                            for (int i = 0; i < f.polygon.npoints; i++) {
                                saving.print(f.polygon.xpoints[i] + " " + f.polygon.ypoints[i] + " ");
                            }
                            saving.println();
                        } else {
                            saving.print((int) f.getSizeX() + " ");
                            if (f.getKind() == 1)
                                saving.println((int) f.getSizeY() + " ");
                            else saving.println();
                        }
                    }
                    saving.close();

                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                break;
            case "LOAD":
                try {
                    String info = JOptionPane.showInputDialog("Please type a name of file, what you want to load. Your file should be at main Project folder.");
                    File file = new File(info + ".txt");
                    Scanner loading = new Scanner(file);

                    paintPanel.getShapes().clear();

                    while (loading.hasNext()) {
                        int kind, colorR, colorG, colorB, x1, y1;
                        double x, y, s1, s2;

                        kind = loading.nextInt();
                        colorR = loading.nextInt();
                        colorG = loading.nextInt();
                        colorB = loading.nextInt();
                        x = loading.nextDouble();
                        y = loading.nextDouble();

                        if (kind == 0 || kind == 1) {
                            s1 = loading.nextDouble();
                            paintPanel.getShapes().add(new Shape(kind, new Color(colorR, colorG, colorB), new Point((int) x, (int) y), s1));
                            if (kind == 1) {
                                s2 = loading.nextDouble();
                                paintPanel.getShapes().add(new Shape(kind, new Color(colorR, colorG, colorB), new Point((int) x, (int) y), s1, s2));
                            }

                        } else if (kind == 2) {
                            Polygon polygon1 = new Polygon();
                            int temp = loading.nextInt();
                            int i = 0;
                            while (i < temp * 2) {
                                x1 = loading.nextInt();
                                y1 = loading.nextInt();
                                System.out.print(x1 + " " + y1 + "\n");
                                polygon1.addPoint(x1, y1);
                                i += 2;
                            }
                            paintPanel.getShapes().add(new Shape(kind, new Color(colorR, colorG, colorB), new Point((int) x, (int) y), polygon1));
                        }
                    }
                } catch (FileNotFoundException err) {
                    JOptionPane.showMessageDialog(null, "Sorry, file not found");
                } catch (InputMismatchException er) {
                    JOptionPane.showMessageDialog(null, "Sorry, error in reading file");
                }
                break;
        }
    }


    /**
     * Main method used to run Project Window.
     * @param args arguments provided by default main method.
     */
    public static void main(String[] args) {
        int WIDTH = 1000;
        int panelHEIGHT = 700;
        int menuHEIGHT = 60;

        int distance = 20;
        double wheelConst = 10.0;


        new MainWindow(WIDTH, menuHEIGHT, panelHEIGHT, distance, wheelConst);
    }
}
