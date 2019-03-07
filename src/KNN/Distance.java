/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KNN;

import MNIST.Sample;

/**
 *
 * @author Majd Malaeb
 */
public class Distance {

    /**
     * Distance Methods
     */
    public static enum Method {

        Euclidean, EuclideanPSNR, Manhattan, ManhattanPSNR;
    }

    /**
     * Calculate the Distance between 2 Sample
     *
     * @param sample1
     * @param sample2
     * @return distance in double
     */
    public static double getDistance(Sample sample1, Sample sample2) {
        return getDistance(sample1, sample2, Method.Euclidean);
    }

    /**
     * Calculate the Distance between 2 Sample using a specific method
     *
     * @param sample1
     * @param sample2
     * @param method
     * @return distance in double
     */
    public static double getDistance(Sample sample1, Sample sample2, Method method) {
        switch (method) {
            case Manhattan:
                return Manhattan(sample1, sample2);
            case Euclidean:
                return Euclidean(sample1, sample2);
            case ManhattanPSNR:
                return mseToPSNR(sample1.getData().length, Manhattan(sample1, sample2));
            case EuclideanPSNR:
            default:
                return mseToPSNR(sample1.getData().length, Euclidean(sample1, sample2));
        }
    }

    /**
     * Compare 2 distance depending on the method we're using
     *
     * @param dist1
     * @param dist2
     * @param method
     * @return boolean
     */
    public static boolean compare(double dist1, double dist2, Method method) {
        switch (method) {
            case Manhattan:
            case Euclidean:
                return dist1 > dist2;
            case ManhattanPSNR:
            case EuclideanPSNR:
            default:
                return dist1 < dist2;
        }
    }

    /**
     * Transfer Mean Square Error to Peak Signal to Noise Ratio
     *
     * @param length
     * @param dist
     * @return
     */
    private static double mseToPSNR(int length, double dist) {
        double mse = dist / length;
        return 10 * Math.log10(65025 / mse);
    }

    /**
     * Calculate distance between 2 samples using Euclidean Method
     *
     * @param sample1
     * @param sample2
     * @return
     */
    private static double Euclidean(Sample sample1, Sample sample2) {
        int[] data1 = sample1.getData();
        int[] data2 = sample2.getData();
        if (data1.length != data2.length) {
            throw new IllegalArgumentException("Samples should be the same size.");
        }
        long sum = 0;
        //*/
        //Ignore the lowest and higest empty pixels
        for (int i = Math.min(sample1.getFirstIndex(), sample2.getFirstIndex()); i <= Math.max(sample1.getLastIndex(), sample2.getLastIndex()); i++) {
            /*/ for (int i = 0; i < data1.length; i++) {
             //*/
            int diff = data1[i] - data2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    /**
     * Calculate distance between 2 samples using Manhattan Method
     *
     * @param sample1
     * @param sample2
     * @return
     */
    private static double Manhattan(Sample sample1, Sample sample2) {
        int[] data1 = sample1.getData();
        int[] data2 = sample2.getData();
        if (data1.length != data2.length) {
            throw new IllegalArgumentException("Samples should be the same size.");
        }
        int sum = 0;
        //*/
        //Ignore the lowest and higest empty pixels
        for (int i = Math.min(sample1.getFirstIndex(), sample2.getFirstIndex()); i <= Math.max(sample1.getLastIndex(), sample2.getLastIndex()); i++) {
            /*/ for (int i = 0; i < data1.length; i++) {
             //*/
            sum += Math.abs(data1[i] - data2[i]);
        }
        return Math.sqrt(sum);
    }
}
