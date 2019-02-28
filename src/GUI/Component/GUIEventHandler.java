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

    public enum event {

        TrainingSetLoaded, TestingSetLoaded, KNNRunning, SearchOne, log
    }

    public void setTrainingSetLoaded(boolean trainingSetLoaded) {
        boolean oldValue = this.trainingSetLoaded;
        this.trainingSetLoaded = trainingSetLoaded;
        pcs.firePropertyChange(event.TrainingSetLoaded.name(), oldValue, trainingSetLoaded);
    }

    public void setTestingSetLoaded(boolean testingSetLoaded) {
        boolean oldValue = this.testingSetLoaded;
        this.testingSetLoaded = testingSetLoaded;
        pcs.firePropertyChange(event.TestingSetLoaded.name(), oldValue, testingSetLoaded);
    }

    public void setSearching(boolean searching) {
        boolean oldValue = this.searching;
        this.searching = searching;
        pcs.firePropertyChange(event.KNNRunning.name(), oldValue, searching);
    }

    public void SearchOne(Sample sample) {
        Sample oldValue = this.sample;
        this.sample = sample;
        pcs.firePropertyChange(event.SearchOne.name(), oldValue, sample);
    }

    public void log(LogIFrame.LogMessage logMessage) {
        pcs.firePropertyChange(event.log.name(), null, logMessage);
    }

    public boolean isTrainingSetLoaded() {
        return trainingSetLoaded;
    }

    public boolean isTestingSetLoaded() {
        return testingSetLoaded;
    }

    public boolean isSearching() {
        return searching;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    public synchronized static GUIEventHandler GetInstance() {
        if (instance == null) {
            instance = new GUIEventHandler();
        }
        return instance;
    }

}
