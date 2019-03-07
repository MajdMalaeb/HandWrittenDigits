/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicSpinnerUI;

/**
 *
 * @author Majd Malaeb
 */
public class IntegerSpinner extends JSpinner {

    private final SpinnerNumberModel model;

    public IntegerSpinner(int value, int min, int max, int stepSize) {
        super(new SpinnerNumberModel(value, min, max, stepSize));
        model = (SpinnerNumberModel) getModel();
        ((NumberEditor) getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        updateJSpinner();
    }

    @Override
    public Object getNextValue() {
        Object o = super.getNextValue();
        return o != null ? o : model.getMaximum();
    }

    @Override
    public Object getPreviousValue() {
        Object o = super.getPreviousValue();
        return o != null ? o : model.getMinimum();
    }

    /**
     * set the Maximum Value and update the current one
     *
     * @param max
     */
    public void setMaximum(int max) {
        setValue(Math.min(max, (int) getValue()));
        model.setMaximum(max);
    }

    /**
     * set the Minimum Value and update the current one
     *
     * @param min
     */
    public void setMinimum(int min) {
        setValue(Math.max(min, (int) getValue()));
        model.setMinimum(min);
    }

    /**
     * Return the Integer value
     *
     * @return super.getValue()
     */
    public int getIntegerValue() {
        return (int) super.getValue();
    }

    /**
     * Change th UI of JSpinner
     */
    private void updateJSpinner() {
        setUI(new BasicSpinnerUI() {
            @Override
            protected Component createPreviousButton() {
                Component component = createButton("▼");
                installPreviousButtonListeners(component);
                return component;
            }

            @Override
            protected Component createNextButton() {
                Component component = createButton("▲");
                installNextButtonListeners(component);
                return component;
            }

            private Component createButton(String text) {
                JButton arrowButton = new JButton(text);
                arrowButton.setFocusable(false);
                return arrowButton;
            }
        });
        addMouseWheelListener((e) -> {
            if (isEnabled()) {
                setValue(e.getWheelRotation() == -1 ? getNextValue() : getPreviousValue());
            }
        });
    }

    @Override
    public void setModel(SpinnerModel model) {
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        super.setLayout(new BorderLayout() {
            @Override
            public void addLayoutComponent(Component comp, Object constraints) {
                if ("Editor".equals(constraints)) {
                    constraints = "Center";
                } else if ("Next".equals(constraints)) {
                    constraints = "East";
                } else if ("Previous".equals(constraints)) {
                    constraints = "West";
                }
                super.addLayoutComponent(comp, constraints);
            }
        });
    }
}
