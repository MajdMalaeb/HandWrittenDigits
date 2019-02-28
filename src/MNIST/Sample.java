/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MNIST;

import Tools.ImageTools;
import java.awt.image.BufferedImage;

/**
 *
 * @author Majd Malaeb
 */
public class Sample {

    private final int[] data;
    private final int r, c;
    private int label = -1;
    private int firstIndex = Integer.MAX_VALUE, lastIndex = 0;

    public Sample(int r, int c) {
        this.r = r;
        this.c = c;
        this.data = new int[r * c];
    }

    public int getPix(int row, int col) {
        return data[(r * row) + col];
    }

    public void setPix(int row, int col, int value) {
        int index = (r * row) + col;
        data[index] = value;
        if (value != 0) {
            lastIndex = Math.max(index, lastIndex);
            firstIndex = Math.min(index, firstIndex);
        }
    }

    public int getFirstIndex() {
        return Math.max(firstIndex, 0);
    }

    public int getLastIndex() {
        return Math.min(lastIndex, lastIndex - 1);
    }

    public int[] getData() {
        return data;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getRowsNumb() {
        return r;
    }

    public int getColNumb() {
        return c;
    }

    public static Sample GetInstance(BufferedImage image) {
        Sample sample = new Sample(MNIST.TRAINING_IMAGE_WIDTH, MNIST.TESTING_IMAGE_HEIGHT);
        image = ImageTools.resizeImage(image, sample.getRowsNumb(), sample.getColNumb());
        for (int r = 0; r < image.getWidth(); r++) {
            for (int c = 0; c < image.getHeight(); c++) {
                sample.setPix(c, r, (image.getRGB(r, c) >> 24) & 0xff);
            }
        }
        return sample;
    }
}
