/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import GUI.Component.GUIEventHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Majd Malaeb
 */
public class LogIFrame extends JInternalFrame {

    private static final JTextPane textPane = new JTextPane();
    private final StyledDocument doc = textPane.getStyledDocument();
    private static final Style blackStyle = textPane.addStyle("blackStyle", null);
    private static final Style blueStyle = textPane.addStyle("blueStyle", null);
    private static final Style redStyle = textPane.addStyle("redStyle", null);
    private static final Style greenStyle = textPane.addStyle("greenStyle", null);

    static {
        StyleConstants.setForeground(blackStyle, Color.BLACK);
        StyleConstants.setForeground(blueStyle, Color.BLUE);
        StyleConstants.setForeground(redStyle, Color.RED);
        StyleConstants.setForeground(greenStyle, Color.GREEN.darker());
    }

    public LogIFrame(int w, int h) {
        super("Logging", true, false, true, true);
        buildGUI();
        setPreferredSize(new Dimension(w, h));
        setSize(w, h);
        GUIEventHandler.GetInstance().addPropertyChangeListener((evt) -> {
            if (GUIEventHandler.event.log.name().equals(evt.getPropertyName())) {
                log((LogMessage) evt.getNewValue());
            }
        });
    }

    private void buildGUI() {
        textPane.setEditable(false);
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e) && e.getClickCount() == 2) {
                    textPane.setText("");
                }
            }
        });
        ((DefaultCaret) textPane.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        getContentPane().add(new JScrollPane(textPane));
        pack();
    }

    public final synchronized void log(LogMessage logMessage) {
        try {
            doc.insertString(doc.getLength(), logMessage.getSender() + ": ", blueStyle);
            doc.insertString(doc.getLength(), logMessage.getMessage() + System.lineSeparator(), logMessage.getStyle());
        } catch (BadLocationException e) {
        }
    }

    public static final class LogMessage {

        private String sender;
        private String message;
        private Style style;

        public enum Styles {

            Black, Blue, Red, Green
        }

        public LogMessage(String sender, String message, Styles style) {
            this.sender = sender;
            this.message = message;
            setStyle(style);
        }

        public LogMessage(String sender, Exception ex) {
            this.sender = sender;
            this.message = ex.getClass().getName() + ": " + ex.getMessage();
            setStyle(Styles.Red);
        }

        public String getSender() {
            return sender;
        }

        public String getMessage() {
            return message;
        }

        public Style getStyle() {
            return style;
        }

        public LogMessage setSender(String sender) {
            this.sender = sender;
            return this;
        }

        public LogMessage setMessage(String message) {
            this.message = message;
            return this;
        }

        public LogMessage setStyle(Styles style) {
            if (style == null) {
                style = Styles.Black;
            }
            switch (style) {
                case Blue:
                    this.style = blueStyle;
                    break;
                case Red:
                    this.style = redStyle;
                    break;
                case Green:
                    this.style = greenStyle;
                    break;
                case Black:
                default:
                    this.style = blackStyle;
            }
            return this;
        }
    }
}
