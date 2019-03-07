/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.Component.GraphJLabel;
import GUI.Component.GUIEventHandler;
import MNIST.Sample;
import Tools.ImageTools;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 *
 * @author Majd Malaeb
 */
public class SourceFrame extends JInternalFrame {

    private GraphJLabel drawLabel;
    private JButton clearBtn, testBtn, saveBtn;
    private File[] files;
    private int index = 0;

    public SourceFrame(MainFrame mainFrame) {
        super("Source", false, true, false, true);
        buildGUI();
        addComponentListener();

    }

    /**
     * Building the interface
     */
    private void buildGUI() {
        drawLabel = new GraphJLabel(280, 280, true);
        drawLabel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

        clearBtn = new JButton("Clear");
        testBtn = new JButton("Test");
        saveBtn = new JButton("Save");
        clearBtn.setEnabled(false);
        testBtn.setEnabled(false);
        saveBtn.setEnabled(false);

        JPanel imagePanel = new JPanel(new FlowLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JPanel mainPanel = new JPanel();

        drawLabel.setPreferredSize(new Dimension(280, 280));
        createMenuBar();
        imagePanel.add(drawLabel);

        buttonPanel.add(clearBtn);
        buttonPanel.add(testBtn);
        buttonPanel.add(saveBtn);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(imagePanel);
        mainPanel.add(buttonPanel);
        setContentPane(new JScrollPane(mainPanel));
        pack();
        setSize(getSize().width + 20, getSize().height + 20);
        setFocusable(true);

    }

    /**
     * create the menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem openImageItem = new JMenuItem("Open Image");
        openImageItem.setIcon(UIManager.getIcon("FileView.fileIcon"));
        openImageItem.addActionListener((e) -> {
            JFileChooser fileChooser = MainFrame.getImageFileChooser(true);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                files = fileChooser.getSelectedFiles();
                index = 0;
                openNextImage();
            }
        });
        menuBar.add(openImageItem);
        setJMenuBar(menuBar);
    }

    /**
     * Add listeners
     */
    private void addComponentListener() {
        clearBtn.addActionListener((e) -> {
            resetImageLabel();
            files = null;
            setTestEnable(testBtn.isEnabled());
        });

        testBtn.addActionListener((e) -> {
            if (files == null) {
                drawLabel.centerImage();
            } else {
                testNextImage();
            }
            Sample test = Sample.GetInstance(drawLabel.getImage());
            GUIEventHandler.GetInstance().SearchOne(test);
        });

        saveBtn.addActionListener((e) -> {
            drawLabel.centerImage();
            JFileChooser fileChooser = MainFrame.getImageFileChooser(false);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    String path = fileChooser.getSelectedFiles()[0].getAbsolutePath();
                    ImageIO.write(drawLabel.getImage(), "png", new File(path + (path.endsWith(".png") ? "" : ".png")));
                } catch (IOException ex) {
                }
            }

        });
        GUIEventHandler.GetInstance().addPropertyChangeListener((evt) -> {
            if (GUIEventHandler.event.TestingSetLoaded.name().equals(evt.getPropertyName())) {
                setEnable((boolean) evt.getNewValue());
            } else if (GUIEventHandler.event.KNNRunning.name().equals(evt.getPropertyName())) {
                setEnable(!(boolean) evt.getNewValue());
            }
        });
    }

    /**
     * Clear the Image
     */
    private void resetImageLabel() {
        drawLabel.clear();
    }

    /**
     * Enable or Disable the buttons
     *
     * @param enable
     */
    private void setEnable(boolean enable) {
        saveBtn.setEnabled(enable);
        clearBtn.setEnabled(enable);
        setTestEnable(enable);
    }

    /**
     * Enable or Disable the test button
     *
     * @param enable
     */
    private void setTestEnable(boolean enable) {
        testBtn.setEnabled(enable && (files == null || (index < files.length)));
        testBtn.setText("Test (" + (files == null ? "1/1)" : (index + 1) + "/" + files.length + ")"));
    }

    /**
     * display the next image and increase the Index
     */
    private void testNextImage() {
        if (files != null) {
            openNextImage();
            incIndex();
        }
    }

    /**
     * increase or reset the index for the next image
     */
    private void incIndex() {
        index++;
        if (index >= files.length) {
            index = 0;
        }
    }

    /**
     * Read and display the next image
     */
    private void openNextImage() {
        if (files != null) {
            try {
                String path = files[index].getAbsolutePath();
                drawLabel.setImage(ImageTools.removeWhite(ImageIO.read(new File(path))));
                setTestEnable(testBtn.isEnabled());
            } catch (IOException ex) {
                GUIEventHandler.GetInstance().log(new LogIFrame.LogMessage("Source Frame", ex));
            }
        }
    }
}
