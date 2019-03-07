/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Component;

import GUI.LogIFrame;
import MNIST.Sample;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Event Handler to fire some actions between the 3 Internal frames
 *
 * @author Majd Malaeb
 */
public class GUIEventHandler implements Serializable {

    private static GUIEventHandler instance;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean trainingSetLoaded = false;
    private boolean testingSetLoaded = false;
    private boolean searching = false;
    private Sample sample = null;

    /**
     * Type of event
     */
    public enum event {

        TrainingSetLoaded, TestingSetLoaded, KNNRunning, SearchOne, log
    }

    /**
     * Fire property change when training set is being loaded
     *
     * @param trainingSetLoaded
     */
    public void setTrainingSetLoaded(boolean trainingSetLoaded) {
        boolean oldValue = this.trainingSetLoaded;
        this.trainingSetLoaded = trainingSetLoaded;
        pcs.firePropertyChange(event.TrainingSetLoaded.name(), oldValue, trainingSetLoaded);
    }

    /**
     * Fire property change when testing set is being loaded
     *
     * @param testingSetLoaded
     */
    public void setTestingSetLoaded(boolean testingSetLoaded) {
        boolean oldValue = this.testingSetLoaded;
        this.testingSetLoaded = testingSetLoaded;
        pcs.firePropertyChange(event.TestingSetLoaded.name(), oldValue, testingSetLoaded);
    }

    /**
     * Fire property change when knn is searching for neighbors
     *
     * @param searching
     */
    public void setSearching(boolean searching) {
        boolean oldValue = this.searching;
        this.searching = searching;
        pcs.firePropertyChange(event.KNNRunning.name(), oldValue, searching);
    }

    /**
     * Fire property change to start searching for neighbors of one sample
     *
     * @param sample
     */
    public void SearchOne(Sample sample) {
        Sample oldValue = this.sample;
        this.sample = sample;
        pcs.firePropertyChange(event.SearchOne.name(), oldValue, sample);
    }

    /**
     * Fire property change to log a message
     *
     * @param logMessage
     */
    public void log(LogIFrame.LogMessage logMessage) {
        pcs.firePropertyChange(event.log.name(), null, logMessage);
    }

    /**
     * add PropertyChangeListener
     *
     * @param l
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * remove PropertyChangeListener
     *
     * @param l
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    /**
     * Get an Instance of GUIEventHandler
     *
     * @return instance
     */
    public synchronized static GUIEventHandler GetInstance() {
        if (instance == null) {
            instance = new GUIEventHandler();
        }
        return instance;
    }

}
