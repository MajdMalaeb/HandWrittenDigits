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

    public static enum Methode {

        Euclidean, EuclideanPSNR, Manhattan, ManhattanPSNR;
    }

    public static double getDistance(Sample sample1, Sample sample2) {
        return getDistance(sample1, sample2, Methode.Euclidean);
    }

    public static boolean compare(double dist1, double dist2, Methode methode) {
        switch (methode) {
            case Manhattan:
            case Euclidean:
                return dist1 > dist2;
            case ManhattanPSNR:
            case EuclideanPSNR:
            default:
                return dist1 < dist2;
        }
    }

    public static double getDistance(Sample sample1, Sample sample2, Methode methode) {
        switch (methode) {
            case Manhattan:
                return sqrt(Manhattan(sample1, sample2));
            case Euclidean:
                return sqrt(Euclidean(sample1, sample2));
            case ManhattanPSNR:
                return mseToPSNR(sample1.getData().length, Manhattan(sample1, sample2));
            case EuclideanPSNR:
            default:
                return mseToPSNR(sample1.getData().length, Euclidean(sample1, sample2));
        }
    }

    private static double mseToPSNR(int length, double dist) {
        double mse = dist / length;
        return 10 * Math.log10(65025 / mse);
    }

    private static double sqrt(double dist) {
        return Math.sqrt(dist);
    }

    private static double Euclidean(Sample sample1, Sample sample2) {
        int[] data1 = sample1.getData();
        int[] data2 = sample2.getData();
        if (data1.length != data2.length) {
            throw new IllegalArgumentException("Samples should be the same size.");
        }
        long sum = 0;
        //*/
        for (int i = Math.min(sample1.getFirstIndex(), sample2.getFirstIndex()); i <= Math.max(sample1.getLastIndex(), sample2.getLastIndex()); i++) {
            /*/ for (int i = 0; i < data1.length; i++) {
             //*/
            int diff = data1[i] - data2[i];
            sum += diff * diff;
        }
        return sum;
    }

    private static double Manhattan(Sample sample1, Sample sample2) {
        int[] data1 = sample1.getData();
        int[] data2 = sample2.getData();
        if (data1.length != data2.length) {
            throw new IllegalArgumentException("Samples should be the same size.");
        }
        int sum = 0;
        //*/
        for (int i = Math.min(sample1.getFirstIndex(), sample2.getFirstIndex()); i <= Math.max(sample1.getLastIndex(), sample2.getLastIndex()); i++) {
            /*/ for (int i = 0; i < data1.length; i++) {
             //*/
            sum += Math.abs(data1[i] - data2[i]);
        }
        return sum;
    }
}
