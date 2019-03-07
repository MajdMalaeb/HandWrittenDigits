/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * An ArrayList that can store the initCapacity and fire an event every time an
 * object is added to it
 *
 * @author Majd Malaeb
 * @param <T>
 */
public class ArrayListPlus<T> extends ArrayList<T> implements Serializable {

    private int initCapacity = 0, oldSize = 0;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public ArrayListPlus() {
        super();
    }

    public ArrayListPlus(Collection<? extends T> c) {
        super(c);
    }

    public ArrayListPlus(int initialCapacity) {
        super(initialCapacity);
        this.initCapacity = initialCapacity;
    }

    public int getInitialCapacity() {
        return initCapacity;
    }

    @Override
    public boolean add(T e) {
        if (super.add(e)) {
            pcs.firePropertyChange("size", oldSize, size());
            oldSize = size();
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (super.addAll(c)) {
            pcs.firePropertyChange("size", oldSize, size());
            oldSize = size();
            return true;
        }
        return false;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
}
