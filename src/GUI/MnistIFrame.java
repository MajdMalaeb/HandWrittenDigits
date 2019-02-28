/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.Component.IntegerSpinner;
import GUI.Component.GraphJLabel;
import GUI.Component.GUIEventHandler;
import KNN.KNN;
import MNIST.MNIST;
import MNIST.Sample;
import KNN.Distance;
import MNIST.DistanceSample;
import Tools.ImageTools;
import Tools.Timer;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.SwingWorker;

/**
 *
 * @author Majd Malaeb
 */
public class MnistIFrame extends JInternalFrame {

    private GraphJLabel testingImage;
    private JLabel resultLabel;
    private GraphJLabel[] neighborImages;
    private JButton trainingBtn, oneByOneBtn, autoBtn, stopBtn, pauseBtn;
    private MNIST loader;
    private IntegerSpinner trainingSpin, testingSpin, kSpin, startIndexSpin, endIndexSpin;
    private JRadioButton euclideanRBtn, euclideanPSNRRBtn, manhattanPSNRRBtn, manhattanRBtn;
    private JCheckBoxMenuItem showImageChb;
    private SwingWorker sw;
    private JProgressBar progressBar;
    private int nextIndex = 0;
    private double testCount = 0, recognitionCount = 0;
    private boolean paused = false, stop = false;
    private final KNN knn = new KNN();
    private final int mainWidth = MNIST.TRAINING_IMAGE_WIDTH * 8;

    public MnistIFrame() {
        super("MNSIT", false, true, false, true);
        buildGUI();
        addListener();
        addActions();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        showImageChb = new JCheckBoxMenuItem("Show Images", true);
        menuBar.add(showImageChb);
        return menuBar;
    }

    private void buildGUI() {

        euclideanRBtn = new JRadioButton("Euclidean");
        euclideanRBtn.setName(Distance.Methode.Euclidean.name());
        euclideanPSNRRBtn = new JRadioButton("Euclidean PSNR");
        euclideanPSNRRBtn.setName(Distance.Methode.EuclideanPSNR.name());
        manhattanRBtn = new JRadioButton("Manhattan");
        manhattanRBtn.setName(Distance.Methode.Manhattan.name());
        manhattanPSNRRBtn = new JRadioButton("Manhattan PSNR");
        manhattanPSNRRBtn.setName(Distance.Methode.ManhattanPSNR.name());

        ButtonGroup radioGroup1 = new ButtonGroup();
        radioGroup1.add(euclideanRBtn);
        radioGroup1.add(euclideanPSNRRBtn);
        radioGroup1.add(manhattanRBtn);
        radioGroup1.add(manhattanPSNRRBtn);

        euclideanPSNRRBtn.setSelected(true);

        testingImage = new GraphJLabel(mainWidth);
        neighborImages = new GraphJLabel[5];
        for (int i = 0; i < 5; i++) {
            neighborImages[i] = new GraphJLabel(mainWidth / 2);
        }
        resultLabel = new JLabel(" ", JLabel.CENTER);
        resultLabel.setFont(new Font(resultLabel.getName(), Font.PLAIN, 25));

        JLabel trainingLabel = new JLabel("Training Samples");
        JLabel testingLabel = new JLabel("Testing Samples");
        JLabel neighborsLabel = new JLabel("Neighbors Number (K)");
        JLabel startIndexLabel = new JLabel("Sample Start Index");
        JLabel endIndexLabel = new JLabel("Sample End Index");

        trainingSpin = new IntegerSpinner(MNIST.TRAINING_ITEMS_COUNT, 1000, MNIST.TRAINING_ITEMS_COUNT, 1000);
        testingSpin = new IntegerSpinner(MNIST.TESTING_ITEMS_COUNT, 10, MNIST.TESTING_ITEMS_COUNT, 1000);
        kSpin = new IntegerSpinner(knn.getK(), 3, MNIST.TRAINING_ITEMS_COUNT / 2, 1);

        startIndexSpin = new IntegerSpinner(0, 0, MNIST.TESTING_ITEMS_COUNT, 100);
        startIndexSpin.setEnabled(false);
        endIndexSpin = new IntegerSpinner(500, 1, MNIST.TESTING_ITEMS_COUNT, 100);
        endIndexSpin.setEnabled(false);

        progressBar = new JProgressBar();
        trainingBtn = new JButton("Load MNIST");
        oneByOneBtn = new JButton("Check One");
        oneByOneBtn.setEnabled(false);
        autoBtn = new JButton("Check All");
        autoBtn.setEnabled(false);
        stopBtn = new JButton("Stop");
        stopBtn.setEnabled(false);
        pauseBtn = new JButton("Pause");
        pauseBtn.setEnabled(false);

        JPanel mnistConfigPanel = new JPanel();
        mnistConfigPanel.setBorder(BorderFactory.createTitledBorder("MNIST Config"));

        GroupLayout mnistConfigLayout = new GroupLayout(mnistConfigPanel);
        mnistConfigPanel.setLayout(mnistConfigLayout);
        mnistConfigLayout.setAutoCreateGaps(true);
        int one = Math.min(mainWidth, 186);
        int two = (one / 2) - 3;
        mnistConfigLayout.setHorizontalGroup(
                mnistConfigLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(trainingLabel)
                .addComponent(trainingSpin, one, one, one)
                .addComponent(testingLabel)
                .addComponent(testingSpin, one, one, one)
                .addComponent(neighborsLabel)
                .addComponent(kSpin, one, one, one)
                .addComponent(startIndexLabel)
                .addComponent(startIndexSpin, one, one, one)
                .addComponent(endIndexLabel)
                .addComponent(endIndexSpin, one, one, one)
                .addComponent(euclideanPSNRRBtn, one, one, one)
                .addComponent(manhattanPSNRRBtn, one, one, one)
                .addGroup(mnistConfigLayout.createSequentialGroup()
                        .addComponent(euclideanRBtn, two, two, two)
                        .addComponent(manhattanRBtn, two, two, two)
                )
                .addComponent(trainingBtn, one, one, one));

        mnistConfigLayout.setVerticalGroup(
                mnistConfigLayout.createSequentialGroup()
                .addComponent(trainingLabel)
                .addComponent(trainingSpin, 28, 28, 28)
                .addComponent(testingLabel)
                .addComponent(testingSpin, 28, 28, 28)
                .addComponent(neighborsLabel)
                .addComponent(kSpin, 28, 28, 28)
                .addComponent(startIndexLabel)
                .addComponent(startIndexSpin, 28, 28, 28)
                .addComponent(endIndexLabel)
                .addComponent(endIndexSpin, 28, 28, 28)
                .addComponent(euclideanPSNRRBtn)
                .addComponent(manhattanPSNRRBtn)
                .addGroup(mnistConfigLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(euclideanRBtn)
                        .addComponent(manhattanRBtn)
                )
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(trainingBtn));

        GroupLayout mainLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(mainLayout);
        mainLayout.setAutoCreateGaps(true);
        mainLayout.setAutoCreateContainerGaps(true);

        mainLayout.setHorizontalGroup(mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addGroup(mainLayout.createSequentialGroup()
                                .addComponent(testingImage)
                                .addComponent(resultLabel, 0, 0, Short.MAX_VALUE)
                        )
                        .addGroup(mainLayout.createSequentialGroup()
                                .addComponent(neighborImages[0])
                                .addComponent(neighborImages[1])
                                .addComponent(neighborImages[2])
                                .addComponent(neighborImages[3])
                                .addComponent(neighborImages[4])
                        )
                        .addGroup(mainLayout.createSequentialGroup()
                                .addComponent(oneByOneBtn, two, two, Short.MAX_VALUE)
                                .addComponent(autoBtn, two, two, Short.MAX_VALUE)
                                .addComponent(stopBtn, two, two, Short.MAX_VALUE)
                                .addComponent(pauseBtn, two, two, Short.MAX_VALUE)
                        )
                        .addComponent(progressBar)
                )
                .addComponent(mnistConfigPanel));

        mainLayout.setVerticalGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                .addGroup(mainLayout.createSequentialGroup()
                        .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(testingImage)
                                .addComponent(resultLabel, 0, 0, Short.MAX_VALUE)
                        )
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(neighborImages[0])
                                .addComponent(neighborImages[1])
                                .addComponent(neighborImages[2])
                                .addComponent(neighborImages[3])
                                .addComponent(neighborImages[4])
                        )
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(oneByOneBtn)
                                .addComponent(autoBtn)
                                .addComponent(stopBtn)
                                .addComponent(pauseBtn)
                        )
                        .addComponent(progressBar, 28, 28, 28)
                )
                .addComponent(mnistConfigPanel));
        setJMenuBar(createMenuBar());
        pack();
    }

    private void addListener() {
        trainingSpin.addChangeListener((e) -> {
            kSpin.setMaximum(trainingSpin.getIntegerValue() / 2);
        });

        testingSpin.addChangeListener((e) -> {
            startIndexSpin.setMaximum(testingSpin.getIntegerValue());
            endIndexSpin.setMaximum(testingSpin.getIntegerValue());
        });

        kSpin.addChangeListener((e) -> {
            knn.setK(kSpin.getIntegerValue());
            clearNeighbor(knn.getK());
        });

        startIndexSpin.addChangeListener((e) -> {
            endIndexSpin.setMinimum(startIndexSpin.getIntegerValue() + 1);
        });
        showImageChb.addActionListener((e) -> {
            if (!showImageChb.isSelected()) {
                clearImages();
            }
        });
        GUIEventHandler.GetInstance().addPropertyChangeListener((evt) -> {
            if (GUIEventHandler.event.SearchOne.name().equals(evt.getPropertyName())) {
                searchOne((Sample) evt.getNewValue());
            }
        });
    }

    private void clearImages() {
        testingImage.clear();
        clearNeighbor(0);
    }

    private void clearNeighbor(int K) {

        for (int i = K; i < 5; i++) {
            neighborImages[i].clear();
        }
    }

    private void addActions() {
        trainingBtn.addActionListener((e) -> {
            loadMNISTDataSet();
        });
        oneByOneBtn.addActionListener((e) -> {
            searchKNN(true);
        });
        autoBtn.addActionListener((e) -> {
            searchKNN(false);
        });

        stopBtn.addActionListener((e) -> {
            stop = true;
        });

        pauseBtn.addActionListener((e) -> {
            paused = !paused;
            pauseBtn.setText(paused ? "Continue" : "Pause");
        });

        euclideanRBtn.addActionListener((e) -> {
            knn.setMethode(Distance.Methode.valueOf(euclideanRBtn.getName()));
        });
        manhattanRBtn.addActionListener((e) -> {
            knn.setMethode(Distance.Methode.valueOf(manhattanRBtn.getName()));
        });
        euclideanPSNRRBtn.addActionListener((e) -> {
            knn.setMethode(Distance.Methode.valueOf(euclideanPSNRRBtn.getName()));
        });
        manhattanPSNRRBtn.addActionListener((e) -> {
            knn.setMethode(Distance.Methode.valueOf(manhattanPSNRRBtn.getName()));
        });
    }

    private void loadMNISTDataSet() {
        if (sw != null) {
            sw.cancel(true);
        }
        sw = new SwingWorker() {
            private boolean loaded = true;
            private String text = "Loading Training set: ";

            @Override
            public String doInBackground() {
                try {
                    int trainingCount = (int) trainingSpin.getValue();
                    int testingCount = (int) testingSpin.getValue();
                    loader = new MNIST(trainingCount, testingCount);

                    trainingSpin.setEnabled(false);
                    testingSpin.setEnabled(false);
                    trainingBtn.setEnabled(false);

                    PropertyChangeListener l = (evt) -> {
                        int value = (int) evt.getNewValue();
                        progressBar.setValue(value);
                        progressBar.setString(text + Math.round(progressBar.getPercentComplete() * 100) + "% (" + value + "/" + progressBar.getMaximum() + ")");
                    };

                    progressBar.setStringPainted(true);
                    progressBar.setModel(new DefaultBoundedRangeModel(0, 1, 0, trainingCount));

                    loader.getTrainingSamples().addPropertyChangeListener(l);
                    loader.getTestingSamples().addPropertyChangeListener(l);
                    long time = loader.loadTrainingSamples();
                    loader.getTrainingSamples().removePropertyChangeListener(l);

                    text = "Loading Testing set: ";
                    progressBar.setModel(new DefaultBoundedRangeModel(0, 1, 0, testingCount));
                    time += loader.loadTestingSamples();
                    loader.getTestingSamples().removePropertyChangeListener(l);

                    GUIEventHandler.GetInstance().log(new LogIFrame.LogMessage("Loading", "Done Loading " + (trainingCount + testingCount) + " Samples in: "
                            + Timer.toString(time), LogIFrame.LogMessage.Styles.Green));
                } catch (Exception ex) {
                    loaded = false;
                    GUIEventHandler.GetInstance().log(new LogIFrame.LogMessage("Loading Set", ex));
                }
                return "";
            }

            @Override
            protected void done() {
                oneByOneBtn.setEnabled(loaded);
                autoBtn.setEnabled(loaded);
                startIndexSpin.setEnabled(loaded);
                endIndexSpin.setEnabled(loaded);

                trainingSpin.setEnabled(!loaded);
                testingSpin.setEnabled(!loaded);
                trainingBtn.setEnabled(!loaded);
            }
        };
        sw.execute();
    }

    private void searchKNN(boolean next) {
        if (sw != null) {
            sw.cancel(true);
        }
        sw = new SwingWorker() {
            @Override
            public String doInBackground() throws InterruptedException {
                try {
                    disableWhileLoading(false);
                    progressBar.setValue(0);
                    if (next) {
                        if (nextIndex > MNIST.TESTING_ITEMS_COUNT) {
                            nextIndex = 0;
                        }
                        progressBar.setStringPainted(false);
                        progressBar.setIndeterminate(true);
                        searchOne(loader.getTestingSamples().get(nextIndex));
                        nextIndex++;
                        return "";
                    }

                    testCount = 0.0;
                    recognitionCount = 0.0;

                    int start = startIndexSpin.getIntegerValue();
                    int end = endIndexSpin.getIntegerValue();
                    progressBar.setStringPainted(true);
                    progressBar.setModel(new DefaultBoundedRangeModel(start, 1, start, end));

                    long nano = 0;
                    for (int i = start; i < end && !stop; i++) {
                        nano += searchOne(loader.getTestingSamples().get(i));
                        progressBar.setValue(i + 1);
                        progressBar.setString(Math.round(progressBar.getPercentComplete() * 100) + "% (" + (i + 1) + "/" + progressBar.getMaximum() + ")");
                        while (paused && !stop) {
                            Thread.sleep(100);
                        }
                    }
                    setFormattedText(Timer.toString(nano));
                    GUIEventHandler.GetInstance().log(new LogIFrame.LogMessage("Testing Samples", getFormattedText(Timer.toString(nano)), LogIFrame.LogMessage.Styles.Green));

                    testCount = 0.0;
                    recognitionCount = 0.0;

                } catch (Exception x) {
                }
                return "";
            }

            @Override
            protected void done() {
                progressBar.setStringPainted(false);
                progressBar.setIndeterminate(false);
                disableWhileLoading(true);
                progressBar.setValue(0);
                paused = false;
                stop = false;
                //System.gc();
            }
        };
        sw.execute();
    }

    private void setFormattedText(String time) {
        setFormattedText(null, null, time);
    }

    private void setFormattedText(String result, String expected, String time) {
        resultLabel.setText("<html><center>" + getFormattedText(result, expected, time, "<br>") + "</center><html>");
    }

    private String getFormattedText(String time) {
        return getFormattedText(null, null, time, ", ");
    }

    private String getFormattedText(String result, String expected, String time, String separator) {
        return (result == null ? "" : ("Result: " + result + "<br>"))
                + (expected == null ? "" : ("Expected: " + expected + "<br>"))
                + "Samples Tested: " + Math.max(testCount, 1)
                + separator
                + "Recognition Rate:" + String.format("%.2f%%", recognitionCount / Math.max(1, testCount) * 100)
                + separator
                + "K: " + knn.getK()
                + separator
                + "Methode: " + knn.getMethode().name()
                + separator
                + "Time: " + time;
    }

    public long searchOne(Sample sample) {
        if (loader == null) {
            return 0;
        }
        Timer timer = new Timer();
        timer.start();
        ArrayList<DistanceSample> samples = knn.findKNNs(sample, loader);
        int result = knn.result(samples);
        int expected = sample.getLabel();

        if (expected >= 0) {
            if (result == expected) {
                recognitionCount += 1.0;
            }
            testCount += 1.0;
        }
        timer.stop();

        resultLabel.setForeground(result == expected ? Color.GREEN.darker() : expected == -1 ? Color.BLUE : Color.RED);
        setFormattedText(result + "", expected == -1 ? "?" : expected + "", timer.toString());
        if (showImageChb.isSelected()) {
            testingImage.setImage(ImageTools.resizePixels(testingImage.getWidth(), testingImage.getHeight(), sample, false));
            for (int i = 0; i < knn.getK() && i < 5; i++) {
                neighborImages[i].setImage(ImageTools.resizePixels(neighborImages[i].getWidth(),
                        neighborImages[i].getHeight(),
                        samples.get(i).getSample(),
                        true));
            }
        }
        return timer.getTime();
    }

    private void disableWhileLoading(boolean enable) {

        euclideanPSNRRBtn.setEnabled(enable);
        euclideanRBtn.setEnabled(enable);
        manhattanPSNRRBtn.setEnabled(enable);
        manhattanRBtn.setEnabled(enable);

        oneByOneBtn.setEnabled(enable);
        autoBtn.setEnabled(enable);
        stopBtn.setEnabled(!enable);
        pauseBtn.setEnabled(!enable);
        kSpin.setEnabled(enable);

        startIndexSpin.setEnabled(enable);
        endIndexSpin.setEnabled(enable);

        GUIEventHandler.GetInstance().setSearching(!enable);
    }
}
