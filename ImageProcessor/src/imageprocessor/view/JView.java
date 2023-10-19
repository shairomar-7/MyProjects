package imageprocessor.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.WritableRenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import imageprocessor.controller.Features;



/**
 * The class for the view that is used for the GUI. This class is responsible for generating
 * each panel that is necessary for the gui (image panel, histogram panels, each command button),
 * as well as storing each action listener that will update the view according the input from
 * the controller. This class is also responsible for displaying error messages to the user
 * when they try to do something illegal.
 */
public class JView extends JFrame implements IViewGUI {


  private final JButton exit;
  private final JButton load;
  private final JButton save;
  private final JButton execute;
  private final JButton downScale;
  private final JComboBox combobox;
  private final CurrentImagePanel imagePanel;
  private final HistogramPanel histogramPanelR;
  private final HistogramPanel histogramPanelG;
  private final HistogramPanel histogramPanelB;
  private final HistogramPanel histogramPanelI;

  /**
   * The constructor for JView. This constructor initializes each panel for the gui. It creates
   * a main panel that contains all  the necessary sub-panels. It initializes each histogram,
   * as well as creating buttons for the commands. Additionally, it creates a combobox, to
   * store all the operations.
   * @param caption The title of the gui
   */
  public JView(String caption) {
    super(caption);
    JPanel mainPanel = new JPanel();
    setSize(new Dimension(800, 200));
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    JPanel comboBoxPanel = new JPanel();
    imagePanel = new CurrentImagePanel();
    JPanel histogramPanel = new JPanel();
    histogramPanel.setLayout(new FlowLayout());
    histogramPanel.setBorder(BorderFactory.createTitledBorder("Histogram"));
    histogramPanelR = new HistogramPanel(new HashMap<>());
    histogramPanelR.setBorder(BorderFactory.createTitledBorder("Red"));
    histogramPanelG = new HistogramPanel(new HashMap<>());
    histogramPanelG.setBorder(BorderFactory.createTitledBorder("Green"));
    histogramPanelB = new HistogramPanel(new HashMap<>());
    histogramPanelB.setBorder(BorderFactory.createTitledBorder("Blue"));
    histogramPanelI = new HistogramPanel(new HashMap<>());
    histogramPanelI.setBorder(BorderFactory.createTitledBorder("Intensity"));
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new FlowLayout());
    exit = new JButton("Exit");
    load = new JButton("load");
    save = new JButton("save");
    execute = new JButton("Execute");
    downScale = new JButton("Down Scale");
    combobox = new JComboBox<String>();
    comboBoxPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
    comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.PAGE_AXIS));
    String[] options = {"luma", "sepia", "flip-horizontal",
      "flip-vertical", "brighten", "darken", "blur", "sharpen",
      "red-component", "green-component", "blue-component",
      "intensity-component", "value-component", "luma-component"};
    combobox.setActionCommand("operation options");
    for (int i = 0; i < options.length; i++) {
      combobox.addItem(options[i]);
    }
    comboBoxPanel.add(combobox);
    histogramPanel.add(histogramPanelR);
    histogramPanel.add(histogramPanelG);
    histogramPanel.add(histogramPanelB);
    histogramPanel.add(histogramPanelI);
    buttonPanel.add(load);
    buttonPanel.add(save);
    buttonPanel.add(exit);
    buttonPanel.add(downScale);
    mainPanel.add(buttonPanel);
    mainPanel.add(comboBoxPanel);
    mainPanel.add(execute);
    mainPanel.add(imagePanel);
    mainPanel.add(histogramPanel);
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);
    mainScrollPane.setPreferredSize(new Dimension(1500, 799));
    add(mainScrollPane);
    pack();
    setVisible(true);
  }

  @Override
  public void setImage(WritableRenderedImage image) {
    imagePanel.update((Image) image);
    imagePanel.repaint();
    imagePanel.revalidate();
  }

  @Override
  public void addFeatures(Features features) {
    combobox.addActionListener(evt -> features.setCommand((String) combobox.getSelectedItem()));
    save.addActionListener(evt -> features.saveImage(saveButtonHelper()));
    load.addActionListener(evt -> features.loadImage(loadButtonHelper()));
    execute.addActionListener(evt -> features.execute());
    exit.addActionListener(evt -> features.exitProgram());
    downScale.addActionListener(evt -> features.downscaleImage(downSizeButtonHeightHelper(),
            downSizeButtonWidthHelper()));
  }

  // Helps the saveImage method of features by getting the directory chosen and the name of the
  // file that was typed.
  private String saveButtonHelper() {
    JFileChooser fchooser = new JFileChooser(".");
    fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int retvalue = fchooser.showOpenDialog(this);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      String fileName = JOptionPane.showInputDialog("Enter the name of the file");
      File f = fchooser.getSelectedFile();
      return f.getAbsolutePath() + "/" + fileName;
    }
    return "";
  }

  // Helps the loadImage method of features by getting the chosen absolute path
  private String loadButtonHelper() {
    JFileChooser fchooser = new JFileChooser(".");
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Bunch of Formats", "jpg", "jpeg", "png", "ppm", "bmp");
    fchooser.setFileFilter(filter);
    int retvalue = fchooser.showOpenDialog(this);
    if (retvalue == JFileChooser.APPROVE_OPTION) {
      File f = fchooser.getSelectedFile();
      return f.getAbsolutePath();
    }
    return "";
  }

  // helps the downScaleImage of features by passing in the height that the user inputted.
  private int downSizeButtonHeightHelper() {
    String input = JOptionPane.showInputDialog("Enter the desired height of the image:");
    int height = 0;
    try {
      height = Integer.parseInt(input);
    }
    catch (NumberFormatException n) {
      try {
        this.renderMessage("Must be an integer!");
        return height;
      }
      catch (IOException e) {
        System.out.println("unreachable statement");
      }
    }
    return height;
  }

  // helps the downScaleImage of features by passing in the width that the user inputted.
  private int downSizeButtonWidthHelper() {
    String input2 = JOptionPane.showInputDialog("Enter the desired width of the image:");
    int width = 0;
    try {
      width =  Integer.parseInt(input2);
    }
    catch (NumberFormatException n) {
      try {
        this.renderMessage("Must be an integer!");
        return width;
      }
      catch (IOException e) {
        System.out.println("unreachable statement");
      }
    }
    return width;
  }


  @Override
  public void renderMessage(String message) throws IOException {
    JOptionPane.showMessageDialog(this, message, "Inane error",
            JOptionPane.ERROR_MESSAGE);
  }

  /** shows the histogram that is based on the given map of integer, integer, where the key
   /* represents the rgb values between 0-255 and the value represents the count for each value.
   /* Heights of rectangles to be drawn are scaled down and are according to the count. The
   /* x position of the rectangle is simply based on the value contained from 0-255.
   @param map A map containing all the values for the histograms.
   */
  public void showHistogram(Map<Integer, Integer>[] map) {
    histogramPanelR.setMap(map[0]);
    histogramPanelG.setMap(map[1]);
    histogramPanelB.setMap(map[2]);
    histogramPanelI.setMap(map[3]);
    histogramPanelR.revalidate();
    histogramPanelR.repaint();
    histogramPanelG.revalidate();
    histogramPanelG.repaint();
    histogramPanelB.revalidate();
    histogramPanelB.repaint();
    histogramPanelI.revalidate();
    histogramPanelI.repaint();
  }
}
