/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resource;

import GUI.MainFrame;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Majd Malaeb
 */
public class Resource {

    private static String currentPath = ".";

    public static InputStream getResource(String name) {
        return Resource.class.getResourceAsStream(name);
    }

    public static byte[] getTrainingImages() throws IOException {
        return getFile("train-images.idx3-ubyte");
    }

    public static byte[] getTrainingLabels() throws IOException {
        return getFile("train-labels.idx1-ubyte");
    }

    public static byte[] getTestingImages() throws IOException {
        return getFile("t10k-images.idx3-ubyte");
    }

    public static byte[] getTestingLabels() throws IOException {
        return getFile("t10k-labels.idx1-ubyte");
    }

    public static Image getBackground() {
        try {
            return ImageIO.read(getResource("bg.png"));
        } catch (IllegalArgumentException | java.io.IOException | NullPointerException ex) {
            return null;
        }
    }

    private static byte[] getFile(String name) throws IOException {
        File file = new File(currentPath + "/" + name);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null,
                    "Unable to find: " + name,
                    "Error", JOptionPane.ERROR_MESSAGE);
            JFileChooser fileChooser = MainFrame.getMNISTFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                currentPath = file.getParent();
            }
        }
        return Files.readAllBytes(file.toPath());
    }
}
