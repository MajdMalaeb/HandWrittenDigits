/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MNIST;

/**
 *
 * @author Majd Malaeb
 */
public class DistanceSample implements Comparable<DistanceSample> {

    private final double distance;
    private final Sample sample;

    public DistanceSample(double distance, Sample sample) {
        this.distance = distance;
        this.sample = sample;
    }

    public double getDistance() {
        return distance;
    }

    public Sample getSample() {
        return sample;
    }

    @Override
    public int compareTo(DistanceSample o) {
        return Double.compare(this.distance, o.getDistance());
    }

}
