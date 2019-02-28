/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MNIST;

import GUI.Component.GUIEventHandler;
import GUI.LogIFrame;
import Resource.Resource;
import Tools.ArrayListPlus;
import Tools.Timer;
import java.io.IOException;

/**
 *
 * @author Majd Malaeb
 */
public class MNIST {

    public static final int TRAINING_ITEMS_COUNT = 60000;
    public static final int TESTING_ITEMS_COUNT = 10000;
    private static final int TRAINING_IMAGES_MAGIC_NUMBER = 2051;
    private static final int TESTING_IMAGES_MAGIC_NUMBER = 2051;
    private static final int TRAINING_LABELS_MAGIC_NUMBER = 2049;
    private static final int TESTING_LABELS_MAGIC_NUMBER = 2049;
    public static final int TRAINING_IMAGE_WIDTH = 28;
    public static final int TESTING_IMAGE_WIDTH = 28;
    public static final int TRAINING_IMAGE_HEIGHT = 28;
    public static final int TESTING_IMAGE_HEIGHT = 28;

    private ArrayListPlus<Sample> trainingSamples;
    private ArrayListPlus<Sample> testingSamples;
    LogIFrame.LogMessage logMessage = new LogIFrame.LogMessage("MNIST", "", null);

    public MNIST() {
        this(TRAINING_ITEMS_COUNT, TESTING_ITEMS_COUNT);
    }

    public MNIST(int trainingCount, int testingCount) {
        trainingSamples = new ArrayListPlus<>(trainingCount);
        testingSamples = new ArrayListPlus<>(testingCount);
    }

    public synchronized long loadTrainingSamples() throws Exception {
        Timer timer = new Timer();
        if (!trainingSamples.isEmpty()) {
            return 0;
        }

        GUIEventHandler.GetInstance().log(logMessage.setMessage("Loading Training Set"));

        byte[] trainingImagesIN = Resource.getTrainingImages();
        byte[] trainingLabelsIN = Resource.getTrainingLabels();
        timer.start();
        if (!(check32Bits(trainingImagesIN, 0, TRAINING_IMAGES_MAGIC_NUMBER)
                && check32Bits(trainingImagesIN, 4, TRAINING_ITEMS_COUNT)
                && check32Bits(trainingImagesIN, 8, TRAINING_IMAGE_HEIGHT)
                && check32Bits(trainingImagesIN, 12, TRAINING_IMAGE_WIDTH)
                && check32Bits(trainingLabelsIN, 0, TRAINING_LABELS_MAGIC_NUMBER)
                && check32Bits(trainingLabelsIN, 4, TRAINING_ITEMS_COUNT))) {
            throw new IOException("Please check the training set file");

        }

        GUIEventHandler.GetInstance().log(logMessage.setMessage("Training images magic number: " + TRAINING_IMAGES_MAGIC_NUMBER).setStyle(LogIFrame.LogMessage.Styles.Green));
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Training images count: " + trainingSamples.getInitialCapacity()));
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Training images height: " + TRAINING_IMAGE_HEIGHT));
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Training images width: " + TRAINING_IMAGE_WIDTH));

        GUIEventHandler.GetInstance().log(logMessage.setMessage("Training labels magic number: " + TRAINING_LABELS_MAGIC_NUMBER));
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Training labels count: " + trainingSamples.getInitialCapacity()));

        int index = 16;
        for (int i = 0; i < trainingSamples.getInitialCapacity(); i++) {
            Sample sample = new Sample(TRAINING_IMAGE_HEIGHT, TRAINING_IMAGE_WIDTH);
            sample.setLabel(trainingLabelsIN[8 + i] & 0xFF);
            for (int r = 0; r < TRAINING_IMAGE_HEIGHT; r++) {
                for (int c = 0; c < TRAINING_IMAGE_WIDTH; c++) {
                    sample.setPix(r, c, trainingImagesIN[index] & 0xFF);
                    index++;
                }
            }
            trainingSamples.add(sample);
        }
        timer.stop();
        GUIEventHandler.GetInstance().setTrainingSetLoaded(true);
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Training Set Loaded").setStyle(null));
        return timer.getTime();
    }

    public synchronized long loadTestingSamples() throws Exception {
        Timer timer = new Timer();
        if (!testingSamples.isEmpty()) {
            return 0;
        }
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Loading Testing Set"));
        byte[] testingImagesIN = Resource.getTestingImages();
        byte[] testingLabelsIN = Resource.getTestingLabels();

        timer.start();
        if (!(check32Bits(testingImagesIN, 0, TESTING_IMAGES_MAGIC_NUMBER)
                && check32Bits(testingImagesIN, 4, TESTING_ITEMS_COUNT)
                && check32Bits(testingImagesIN, 8, TESTING_IMAGE_HEIGHT)
                && check32Bits(testingImagesIN, 12, TESTING_IMAGE_WIDTH)
                && check32Bits(testingLabelsIN, 0, TESTING_LABELS_MAGIC_NUMBER)
                && check32Bits(testingLabelsIN, 4, TESTING_ITEMS_COUNT))) {
            throw new IOException("Please check the testing set file");

        }
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Testing images magic number: " + TESTING_IMAGES_MAGIC_NUMBER).setStyle(LogIFrame.LogMessage.Styles.Green));
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Testing images count: " + testingSamples.getInitialCapacity()));
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Testing images height: " + TESTING_IMAGE_HEIGHT));
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Testing images width: " + TESTING_IMAGE_WIDTH));

        GUIEventHandler.GetInstance().log(logMessage.setMessage("Testing labels magic number: " + TESTING_LABELS_MAGIC_NUMBER));
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Testing labels count: " + testingSamples.getInitialCapacity()));
        int index = 16;
        for (int i = 0; i < testingSamples.getInitialCapacity(); i++) {
            Sample sample = new Sample(TESTING_IMAGE_HEIGHT, TESTING_IMAGE_WIDTH);
            sample.setLabel(testingLabelsIN[8 + i] & 0xFF);
            for (int r = 0; r < TESTING_IMAGE_HEIGHT; r++) {
                for (int c = 0; c < TESTING_IMAGE_WIDTH; c++) {
                    sample.setPix(r, c, testingImagesIN[index] & 0xFF);
                    index++;
                }
            }
            testingSamples.add(sample);
        }
        timer.stop();
        GUIEventHandler.GetInstance().setTestingSetLoaded(true);
        GUIEventHandler.GetInstance().log(logMessage.setMessage("Testing Set Loaded").setStyle(null));
        return timer.getTime();
    }

    public ArrayListPlus<Sample> getTrainingSamples() {
        return trainingSamples;
    }

    public ArrayListPlus<Sample> getTestingSamples() {
        return testingSamples;
    }

    private boolean check32Bits(byte[] in, int index, int number) throws Exception {
        return number == fromByteArray(in, index);
    }

    int fromByteArray(byte[] bytes, int start) {
        return bytes[start] << 24 | (bytes[start + 1] & 0xFF) << 16 | (bytes[start + 2] & 0xFF) << 8 | (bytes[start + 3] & 0xFF);
    }

    public void dispose() {
        trainingSamples.clear();
        testingSamples.clear();
    }

    public void dispose(int trainingCount, int testingCount) {
        dispose();
        trainingSamples = new ArrayListPlus<>(trainingCount);
        testingSamples = new ArrayListPlus<>(testingCount);
    }
}
