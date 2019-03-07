/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import MNIST.Sample;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Majd Malaeb
 */
public class ImageTools {

    private static final Font font = new Font("TimesRoman", Font.BOLD, 28);
    private static final FontMetrics fm = new Canvas().getFontMetrics(font);

    public static BufferedImage createGraphImage(int row, int col, int w, int h) {
        return createGraphImage(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB), row, col, w, h);
    }

    public static BufferedImage createGraphImage(BufferedImage image, int row, int col, int w, int h) {
        Graphics2D gd = image.createGraphics();
        createGraphImage(gd, row, col, w, h);
        return image;
    }

    /**
     * Draw lines on a specific graphics2d
     *
     * @param gd
     * @param row row count
     * @param col column count
     * @param w width
     * @param h heigh
     */
    public static void createGraphImage(Graphics2D gd, int row, int col, int w, int h) {
        int newPixSizeW = w / col;
        int newPixSizeH = h / row;
        gd.setBackground(Color.WHITE);
        gd.clearRect(0, 0, w, h);
        gd.setColor(Color.GRAY);
        for (int c = 0; c < col; c++) {
            for (int r = 0; r < row; r++) {
                gd.drawRect(newPixSizeH * r, c * newPixSizeW, newPixSizeW, newPixSizeH);
            }
        }
        gd.dispose();
    }

    public static BufferedImage resizeImage(Image img, int w, int h) {
        return resizeImage(img, w, h, w, h);
    }

    /**
     * Resize and center a buffer image inside a new image
     *
     * @param img Image to resize
     * @param w Width of the resized image
     * @param h Height of the resized image
     * @param imgW Width of the old Image inside the resized image
     * @param imgH Height of the old Image inside the resized image
     * @return
     */
    public static BufferedImage resizeImage(Image img, int w, int h, int imgW, int imgH) {
        BufferedImage resizedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        Map<RenderingHints.Key, Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(hints);
        //center the image
        g2d.drawImage(img, (w - imgW) / 2, (h - imgH) / 2, imgW, imgH, null);
        g2d.dispose();
        return resizedImage;
    }

    /**
     * Resize Image by expanding each pixel
     *
     * @param w Width of the resized image
     * @param h Height of the resized image
     * @param sample sample to resize
     * @param drawString
     * @return
     */
    public static BufferedImage resizePixels(int w, int h, Sample sample, boolean drawString) {
        int rowNumb = sample.getRowsNumb();
        int colNumb = sample.getColNumb();
        int newPixSizeW = w / colNumb;
        int newPixSizeH = h / rowNumb;

        BufferedImage resizedImage = new BufferedImage(newPixSizeW * rowNumb, newPixSizeH * rowNumb, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gd = resizedImage.createGraphics();

        //draw each pixel as a rectangle and add a Gray border
        for (int c = 0; c < colNumb; c++) {
            for (int r = 0; r < rowNumb; r++) {
                int pic = 255 - sample.getPix(c, r);
                gd.setColor(new Color(pic, pic, pic));
                gd.fillRect(newPixSizeH * r, c * newPixSizeW, newPixSizeW, newPixSizeH);
                gd.setColor(Color.GRAY);
                gd.drawRect(newPixSizeH * r, c * newPixSizeW, newPixSizeW, newPixSizeH);
            }
        }
        //Draw the label
        if (drawString) {
            String txt = sample.getLabel() + "";
            gd.setColor(Color.BLUE);
            gd.setFont(font);
            gd.drawString(txt, fm.stringWidth(txt) / 2, fm.getAscent());
        }
        gd.dispose();
        return resizedImage;
    }

    /**
     * Remove all white pixels and invert the Image if the first pixel is not
     * white
     *
     * @param img
     * @return
     */
    public static BufferedImage removeWhite(BufferedImage img) {
        return removeWhiteAndInvert(img, img.getRGB(0, 0) < -12000000);
    }

    /**
     * Remove all white pixels and invert the Image
     *
     * @param img
     * @param invert
     * @return
     */
    private static BufferedImage removeWhiteAndInvert(BufferedImage img, boolean invert) {

        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int rgba = img.getRGB(x, y);
                Color col = new Color(rgba, true);
                if (invert) {
                    col = new Color(255 - col.getRed(),
                            255 - col.getGreen(),
                            255 - col.getBlue());
                }
                if (col.getRGB() < -12000000) {//Store the pixel if it's not white
                    result.setRGB(x, y, col.getRGB());
                }
            }
        }
        return result;
    }
}
