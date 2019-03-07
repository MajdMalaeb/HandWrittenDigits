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

    /**
     * Read a resource as InputStream
     *
     * @param name name of the resource
     * @return
     */
    public static InputStream getResource(String name) {
        return Resource.class.getResourceAsStream(name);
    }

    /**
     * Read and return the Training Image File as byte array
     *
     * @return array of all the bytes
     * @throws IOException
     */
    public static byte[] getTrainingImages() throws IOException {
        return getFile("train-images.idx3-ubyte");
    }

    /**
     * Read and return the Training label File as byte array
     *
     * @return array of all the bytes
     * @throws IOException
     */
    public static byte[] getTrainingLabels() throws IOException {
        return getFile("train-labels.idx1-ubyte");
    }

    /**
     * Read and return the Testing Image File as byte array
     *
     * @return array of all the bytes
     * @throws IOException
     */
    public static byte[] getTestingImages() throws IOException {
        return getFile("t10k-images.idx3-ubyte");
    }

    /**
     * Read and return the Testing label File as byte array
     *
     * @return array of all the bytes
     * @throws IOException
     */
    public static byte[] getTestingLabels() throws IOException {
        return getFile("t10k-labels.idx1-ubyte");
    }

    /**
     * Get the background Image from resource
     *
     * @return Image
     */
    public static Image getBackground() {
        try {
            return ImageIO.read(getResource("bg.png"));
        } catch (IllegalArgumentException | java.io.IOException | NullPointerException ex) {
            return null;
        }
    }

    /**
     * Check and Read a specific file as a byte array
     *
     * @param name name of the file
     * @return array of all the bytes
     * @throws IOException
     */
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
