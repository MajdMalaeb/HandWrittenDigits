/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Component;

import MNIST.MNIST;
import Tools.ImageTools;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Majd Malaeb
 */
public class GraphJLabel extends JLabel {

    private final Color color = Color.BLACK;
    private final int strokeSize = 20;
    private final Stroke stroke = new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
    private final ArrayList<Point> tempPoints = new ArrayList<>(), toCenter = new ArrayList<>();
    private BufferedImage tempImage;
    private boolean drawable = true;

    public GraphJLabel(int w, int h, boolean drawable) {
        setIcon(new ImageIcon(
                ImageTools.createGraphImage(
                        MNIST.TRAINING_IMAGE_HEIGHT,
                        MNIST.TRAINING_IMAGE_WIDTH,
                        w, h)));
        limitSize(new Dimension(w, h));
        clear();
        if (drawable) {
            addListener();
        }
    }

    public GraphJLabel(int w) {
        this(w, w);
    }

    public GraphJLabel(int w, int h) {
        this(w, h, false);
    }

    /**
     * Limit the size to one Dimension
     *
     * @param d
     */
    private void limitSize(Dimension d) {
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
        setSize(d);
    }

    /**
     * add listener to mouse events if it's a drawable Label
     */
    private void addListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                //!drawable is here so we can prevent the user from drawing if he's reading a displaying an external image on the same Jlabel
                if (!drawable || !SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                addPoint(e.getPoint());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!drawable || !SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                Graphics2D g2d = tempImage.createGraphics();
                drawPoints(g2d);
                g2d.dispose();
                toCenter.addAll(tempPoints);
                toCenter.add(null);
                tempPoints.clear();
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!drawable || !SwingUtilities.isLeftMouseButton(e)) {
                    return;
                }
                addPoint(e.getPoint());
                repaint();
            }
        });
    }

    /**
     * Draw the image and the new points if exists
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(tempImage, 0, 0, null);
        if (tempPoints.size() < 2) {
            return;
        }
        drawPoints(g);
    }

    /**
     * add a point to the point list and checking if it's inside the JLabel
     *
     * @param p
     */
    private void addPoint(Point p) {
        p.x = Math.min(Math.max(0, p.x), getWidth());
        p.y = Math.min(Math.max(0, p.y), getHeight());
        tempPoints.add(p);
    }

    private void drawPoints(Graphics g) {
        drawPoints(g, 0, 0);
    }

    private void drawPoints(Graphics g, int offsetX, int offsetY) {
        drawPoints(g, tempPoints, offsetX, offsetY);
    }

    /**
     * draw the points on a specific graphics using offset
     *
     * @param g graphics
     * @param points point list
     * @param offsetX x offset
     * @param offsetY y offset
     */
    private void drawPoints(Graphics g, ArrayList<Point> points, int offsetX, int offsetY) {
        if (points.size() < 2) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(stroke);

        for (int i = 1; i < points.size(); i++) {
            if (points.get(i) == null) {
                i++;
                continue;
            }
            g2d.drawLine(points.get(i - 1).x - offsetX, points.get(i - 1).y - offsetY, points.get(i).x - offsetX, points.get(i).y - offsetY);
        }
    }

    /**
     * Center the Image Using the List of points
     */
    public void centerImage() {
        if (toCenter.isEmpty()) {
            return;
        }
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Point point : toCenter) {
            if (point != null) {
                minX = Math.min(minX, point.x);
                maxX = Math.max(maxX, point.x);
                minY = Math.min(minY, point.y);
                maxY = Math.max(maxY, point.y);
            }
        }
        int w = maxX - minX;
        int h = maxY - minY;
        int size = w > h ? w : h;
        size += strokeSize;
        tempImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        //drawPoints(tempImage.createGraphics(), toCenter, (maxX + minX - getWidth()) / 2, (maxY + minY - getHeight()) / 2);
        drawPoints(tempImage.createGraphics(), toCenter, (maxX + minX - size) / 2, (maxY + minY - size) / 2);
        tempImage = ImageTools.resizeImage(tempImage, getWidth(), getHeight(), (int) (getWidth() * 0.75), (int) (getHeight() * 0.75));
        repaint();
    }

    /**
     * clear the points and the image
     */
    public final void clear() {
        toCenter.clear();
        tempImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        drawable = true;
        repaint();
    }

    /**
     * Get the tempImage
     *
     * @return tempImage
     */
    public BufferedImage getImage() {
        return tempImage;
    }

    /**
     * Set an external image
     *
     * @param img
     */
    public void setImage(BufferedImage img) {
        clear();
        Graphics2D g2 = tempImage.createGraphics();
        g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        g2.dispose();
        drawable = false;
        repaint();
    }
}
