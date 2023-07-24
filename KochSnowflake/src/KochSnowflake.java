import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ComponentEvent;
import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Interactive Koch Snowflake
 * Author - Luke Piper
 */
public class KochSnowflake extends JPanel {
    static int order;

    /**
     * KochSnowflake constructor
     */
    public KochSnowflake() {
        order = 1;
    }

    /**
     * Find points of new triangle going outwards from the middle third of line
     * parameters given then recursively call itself with each new line of triangle
     * 
     * @param startLineX
     * @param startLineY
     * @param endLineX
     * @param endLineY
     * @param order
     * @param g
     */
    public void recursiveKochSnowflake(double startLineX, double startLineY, double endLineX, double endLineY,
            int order, Graphics g) {
        // Length of third(x, y) from start to end lines of triangle
        double thirdLengthX = (endLineX - startLineX) / 3;
        double thirdLengthY = (endLineY - startLineY) / 3;
        // The first point of new triangle = third length way in from startLine
        double aX = startLineX + thirdLengthX;
        double aY = startLineY + thirdLengthY;
        // Second point of new triangle = third length in from endLine
        double bX = endLineX - thirdLengthX;
        double bY = endLineY - thirdLengthY;

        // Third point of triangle using cos(60) and sin (60)
        double cX = (thirdLengthX * Math.cos(Math.toRadians(60))) - (thirdLengthY * Math.sin(Math.toRadians(60))) + aX;
        double cY = (thirdLengthX * Math.sin(Math.toRadians(60))) + (thirdLengthY * Math.cos(Math.toRadians(60))) + aY;

        // Recursively call this function if order above 1
        if (order > 1) {
            order -= 1;
            // Recursively call function with each new side of the line -^-
            recursiveKochSnowflake(startLineX, startLineY, aX, aY, order, g);
            recursiveKochSnowflake(aX, aY, cX, cY, order, g);
            recursiveKochSnowflake(cX, cY, bX, bY, order, g);
            recursiveKochSnowflake(bX, bY, endLineX, endLineY, order, g);
        } else {
            // Call drawLine
            drawKochSnowflake(startLineX, startLineY, endLineX, endLineY, g);
        }
    }

    /**
     * Draws line to form snowflake
     * 
     * @param startLineX
     * @param startLineY
     * @param endLineX
     * @param endLineY
     * @param g
     */
    public void drawKochSnowflake(double startLineX, double startLineY, double endLineX, double endLineY, Graphics g) {
        // Draw line using doubles
        Graphics2D g2 = (Graphics2D) g;
        g2.draw(new Line2D.Double(startLineX, startLineY, endLineX, endLineY));
    }

        /**
     * Repaint window based on new order value, and adjust snowflake to scale of
     * window size
     * 
     * @param g
     */
    public void paint(Graphics g) {
        // Get center point of JFrame window
        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;
        // Get the length of each side of triangle
        double sideLength = (Math.min(centerX, centerY));
        // Find bottom left of triangle
        double leftX = centerX - sideLength / 2;
        double leftY = centerY + sideLength / 2;
        // Find bottom right of triangle
        double rightX = centerX + sideLength / 2;
        double rightY = centerY + sideLength / 2;
        // Find top point of triangle
        double topX = centerX;
        double topY = centerY - sideLength / 2;

        // For each line in triangle, create koch snowflake pattern off order value
        recursiveKochSnowflake(leftX, leftY, rightX, rightY, order, g);
        recursiveKochSnowflake(topX, topY, leftX, leftY, order, g);
        recursiveKochSnowflake(rightX, rightY, topX, topY, order, g);
    }

    /**
     * Main to create Jframe, make interactive slider for user, and create new
     * KochSnowflake object
     * 
     * @param args
     */
    public static void main(String[] args) {
        // JFrame window
        JFrame frame = new JFrame("Koch Snowflake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set size
        frame.setSize(1000, 1000);
        // Make resizble
        frame.setResizable(true);

        // Slider
        JSlider slider = new JSlider(SwingConstants.HORIZONTAL, 1, 10, 1);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);

        // Add a ChangeListener to the slider
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // Change level to slider value
                order = ((JSlider) e.getSource()).getValue();
                // Repaint frame according to the new slider value
                frame.repaint();
            }
        });

        // Do not allow user to make window too small
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // If size gets too small, resize JFrame window to minimum size
                int width = frame.getWidth();
                int height = frame.getHeight();
                if (width < 500) {
                    frame.setSize(500, height);
                }
                if (height < 400) {
                    frame.setSize(width, 400);
                }
            }
        });

        // Add snowflake object and slider to Jframe
        frame.add(new KochSnowflake());
        frame.add(slider, BorderLayout.SOUTH);

        // Show JFrame
        frame.setVisible(true);
    }
}