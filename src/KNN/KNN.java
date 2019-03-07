/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KNN;

import MNIST.Sample;
import Tools.ArrayListPlus;
import MNIST.DistanceSample;
import MNIST.MNIST;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Majd Malaeb
 */
public class KNN {

    private int K = 5;
    private Distance.Method method = Distance.Method.EuclideanPSNR; //Default Method

    /**
     * Set K Value
     *
     * @param K
     */
    public void setK(int K) {
        this.K = K;
    }

    /**
     * Get K Value
     *
     * @return K
     */
    public int getK() {
        return K;
    }

    /**
     * Set the Method
     *
     * @param method
     */
    public void setMethode(Distance.Method method) {
        this.method = method;
    }

    /**
     * Get the Method
     *
     * @return
     */
    public Distance.Method getMethod() {
        return method;
    }

    /**
     * Search for Neighbors using the default Training set samples
     *
     * @param sample
     * @param mnist
     * @return List of DistanceSample
     */
    public ArrayList<DistanceSample> findKNNs(Sample sample, MNIST mnist) {
        return findKNNs(sample, mnist.getTrainingSamples());
    }

    /**
     * Search for Neighbors using a specific Training set samples
     *
     * @param sample
     * @param samples
     * @return List of DistanceSample
     */
    public ArrayList<DistanceSample> findKNNs(Sample sample, ArrayListPlus<Sample> samples) {

        ArrayList<DistanceSample> list = new ArrayList<>(K);
        for (Sample spl : samples) {
            //calculate the distance between 2 samples using a specific method
            double dist = Distance.getDistance(sample, spl, method);
            if (list.size() < K) {
                //Create and add a new DistanceSample to the list without comparing the distance
                list.add(new DistanceSample(dist, spl));
            } else {
                //Compare and replace one of the neighbors if we find a new closest one
                for (DistanceSample key : list) {
                    if (Distance.compare(key.getDistance(), dist, method)) {
                        list.remove(key);
                        list.add(new DistanceSample(dist, spl));
                        break;
                    }
                }
            }
        }
        //Sort the Neighbors by distance
        Collections.sort(list);
        return list;
    }

    /**
     * Count the labels of the neighbors and return the result
     *
     * @param samples
     * @return
     */
    public int result(ArrayList<DistanceSample> samples) {
        int[] countLabel = new int[10];
        int res = 0;
        for (int i = 0; i < K; i++) {
            int j = samples.get(i).getSample().getLabel();
            countLabel[j]++;
            if (countLabel[j] > countLabel[res]) {
                res = j;
            }
        }
        return res;
    }
}
