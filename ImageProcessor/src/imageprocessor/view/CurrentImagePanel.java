package imageprocessor.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;


/**
 * Class for the image panel. This class will create a scrollPane that will contain the image
 * that the user wants to be manipulated. This scroll pane will be added to the main panel
 * in the JView class. The scroll bars will appear on the pane until the image size is less than
 * that of the scroll pane.
 */
public class CurrentImagePanel extends JPanel {
  JLabel imageLabel;

  /**
   * Construct a CurrentImagePanel. The constructor will add scroll bars to the
   * pane, if the image is larger than the pane.
   * The scroll bars will appear on the pane until the image size is less than
   *  * that of the scroll pane.
   */
  public CurrentImagePanel() {
    super();
    imageLabel = new JLabel();
    setPreferredSize(new Dimension(800, 600));
    this.setBorder(BorderFactory.createTitledBorder("Current image"));
    this.setLayout(new FlowLayout());
    JScrollPane scrollPane = new JScrollPane(imageLabel);
    scrollPane.setPreferredSize(new Dimension(950, 550));
    this.add(scrollPane);
  }

  public void update(Image image) {
    imageLabel.removeAll();
    imageLabel.setIcon(new ImageIcon(image));
  }
}
