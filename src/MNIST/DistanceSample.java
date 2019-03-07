/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MNIST;

/**
 * A Comparable Class Containing a Sample and Distance
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

    /**
     * Get the Distance
     *
     * @return
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Get the Sample
     *
     * @return
     */
    public Sample getSample() {
        return sample;
    }

    /**
     * Comparing using the distance
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(DistanceSample o) {
        return Double.compare(this.distance, o.getDistance());
    }

}
