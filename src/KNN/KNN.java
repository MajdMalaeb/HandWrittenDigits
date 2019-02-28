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

/**
 *
 * @author Majd Malaeb
 */
public class KNN {

    private int K = 5;
    private Distance.Methode methode = Distance.Methode.EuclideanPSNR;

    public KNN() {
    }

    public void setK(int K) {
        this.K = K;
    }

    public int getK() {
        return K;
    }

    public void setMethode(Distance.Methode methode) {
        this.methode = methode;
    }

    public Distance.Methode getMethode() {
        return methode;
    }

    public ArrayList<DistanceSample> findKNNs(Sample sample, MNIST mnist) {
        return findKNNs(sample, mnist.getTrainingSamples());
    }

    public ArrayList<DistanceSample> findKNNs(Sample sample, ArrayListPlus<Sample> samples) {

        ArrayList<DistanceSample> list = new ArrayList<>(K);
        for (Sample spl : samples) {
            double dist = Distance.getDistance(sample, spl, methode);
            if (list.size() < K) {
                list.add(new DistanceSample(dist, spl));
            } else {
                for (DistanceSample key : list) {
                    if (Distance.compare(key.getDistance(), dist, methode)) {
                        list.remove(key);
                        list.add(new DistanceSample(dist, spl));
                        break;
                    }
                }
            }
        }
        return list;
    }

    public int result(ArrayList<DistanceSample> samples) {
        //Collections.sort(samples);
        int[] countLabel = new int[10];
        for (int i = 0; i < K; i++) {
            int j = samples.get(i).getSample().getLabel();
            countLabel[j]++;
        }
        int res = 0;
        for (int i = 1; i < 10; i++) {
            if (countLabel[i] > countLabel[res]) {
                res = i;
            }
        }
        return res;
    }
}
