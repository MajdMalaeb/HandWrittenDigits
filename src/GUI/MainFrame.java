/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Resource.Resource;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyVetoException;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author Majd Malaeb
 */
public class MainFrame extends JFrame {

    private JDesktopPane desktopPane;
    private final int w = 1280, h = 750;
    private boolean mnistIsOpen = false, sourceIsOpen = false;
    private static JFileChooser fileChooser;
    private final static FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());

    public MainFrame() {
        buildGUI();
        createSourceFrame();
        createMNISTIFrame();
        createLogIFrame(w / 2, 180);
    }

    /**
     * Build the Main Interface
     */
    private void buildGUI() {
        desktopPane = new JDesktopPane() {
            final Image image = Resource.getBackground();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //drawing the background 
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        //Prevent the Internal frame from moving if the property is fixed
//        DefaultDesktopManager defaultDesktopManager = new DefaultDesktopManager() {
//            @Override
//            public void beginDraggingFrame(JComponent f) {
//                if (!"fixed".equals(f.getClientProperty("dragMode"))) {
//                    super.beginDraggingFrame(f);
//                }
//            }
//
//            @Override
//            public void dragFrame(JComponent f, int newX, int newY) {
//                if (!"fixed".equals(f.getClientProperty("dragMode"))) {
//                    super.dragFrame(f, newX, newY);
//                }
//            }
//
//            @Override
//            public void endDraggingFrame(JComponent f) {
//                if (!"fixed".equals(f.getClientProperty("dragMode"))) {
//                    super.endDraggingFrame(f);
//                }
//            }
//        };
//        desktopPane.setDesktopManager(defaultDesktopManager);

        desktopPane.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
        setContentPane(desktopPane);
        createMenuBar();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(w, h);
        desktopPane.setSize(w, h);
        setTitle("HWML");
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Creating the Menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem mnistMenuItem = new JMenuItem("Open MNIST Frame");
        JMenuItem sourceMenuItem = new JMenuItem("Open Source Frame");
        mnistMenuItem.setMaximumSize(new Dimension(125, Short.MAX_VALUE));
        sourceMenuItem.setMaximumSize(new Dimension(125, Short.MAX_VALUE));

        // Selecting or creating a new internal frame
        mnistMenuItem.addActionListener((e) -> {
            if (!mnistIsOpen) {
                createMNISTIFrame();
            } else {
                selectIFrame(MnistIFrame.class);
            }
        });
        sourceMenuItem.addActionListener((e) -> {
            if (!sourceIsOpen) {
                createSourceFrame();
            } else {
                selectIFrame(SourceFrame.class);
            }
        });
        menuBar.add(sourceMenuItem);
        menuBar.add(mnistMenuItem);
        setJMenuBar(menuBar);
    }

    /**
     * Creating the MNIST Internal Interface
     */
    private void createMNISTIFrame() {
        MnistIFrame mnistIFrame = new MnistIFrame();
        mnistIFrame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                mnistIsOpen = true;
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                mnistIsOpen = false;
            }
        });
//        centerJIF(readDBPanel);
//        fixIFrame(mnistIFrame);
        mnistIFrame.setLocation(desktopPane.getWidth() - mnistIFrame.getWidth() - 30, 20);
        mnistIFrame.setVisible(true);
        desktopPane.add(mnistIFrame);
        desktopPane.setSelectedFrame(mnistIFrame);

    }

    /**
     * Creating the Source Internal Interface
     */
    private void createSourceFrame() {
        SourceFrame sourceFrame = new SourceFrame(this);
        sourceFrame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                sourceIsOpen = true;
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                sourceIsOpen = false;
            }
        });
        sourceFrame.setLocation(30, 20);
//        centerJIF(editDBIFrame);
//        fixIFrame(sourceFrame);
        sourceFrame.setVisible(true);
        desktopPane.add(sourceFrame);
        desktopPane.setSelectedFrame(sourceFrame);
    }

    /**
     * Creating the Log Internal Interface
     */
    private void createLogIFrame(int width, int height) {
        LogIFrame logIFrame = new LogIFrame(width, height);
        logIFrame.setLocation(w - width - 15, h - height - 60);
//        centerJIF(editDBIFrame);
//        fixIFrame(logIFrame);
        logIFrame.setVisible(true);
        desktopPane.add(logIFrame);
    }

    /**
     * Center the Internal Interface
     */
    private void centerJIF(JInternalFrame iFrame) {
        iFrame.setLocation((desktopPane.getWidth() - iFrame.getWidth()) / 2,
                (desktopPane.getHeight() - iFrame.getHeight()) / 2);
    }

    /**
     * Add "fixed" as a property for dragMode
     */
    private void fixIFrame(JInternalFrame iFrame) {
        iFrame.putClientProperty("dragMode", "fixed");
    }

    /**
     * Select the Internal Interface by looping and finding it from all the
     * frames
     */
    private void selectIFrame(Class<?> cls) {
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (cls.isInstance(frame)) {
                try {
                    frame.setSelected(true);
                    break;
                } catch (PropertyVetoException ex) {
                }
            }
        }
    }

    /**
     * @param multi enable or disable MultiSelection
     * @return JFilechooser with image filter
     */
    public static JFileChooser getImageFileChooser(boolean multi) {
        fileChooser.setMultiSelectionEnabled(multi);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(imageFilter);
        return fileChooser;
    }

    /**
     * @return JFilechooser with no filters
     */
    public static JFileChooser getMNISTFileChooser() {
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.removeChoosableFileFilter(imageFilter);
        return fileChooser;
    }

    /**
     * Main Function
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            fileChooser = new JFileChooser();
            UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
            updateFileChooserUI(fileChooser, false);
            fileChooser.setCurrentDirectory(new File("."));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    /**
     * Updating UI of a component with different UI
     *
     * @param c Component to update
     * @param includeParent Include Parent or no
     */
    private static void updateFileChooserUI(JComponent c, boolean includeParent) {
        if (includeParent) {
            c.updateUI();
        }
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component child = c.getComponent(i);
            if (child instanceof JComponent) {
                updateFileChooserUI((JComponent) child, true);
            }
        }
    }
}
