package imageprocessor.controller;

import java.awt.image.WritableRenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.imageio.ImageIO;
import imageprocessor.Pixel;
import imageprocessor.controller.commands.Brighten;
import imageprocessor.controller.commands.DownSize;
import imageprocessor.controller.commands.Filter;
import imageprocessor.controller.commands.Flip;
import imageprocessor.controller.commands.ImageCommand;
import imageprocessor.controller.commands.LinearTranform;
import imageprocessor.controller.commands.Visualize;
import imageprocessor.controller.io.ImageIOHelper;
import imageprocessor.controller.io.ImagePNGJPGBMP;
import imageprocessor.controller.io.ImagePPM;
import imageprocessor.model.EnhancedModel;
import imageprocessor.view.IViewGUI;

/**
 * ControllerGUI, controller component of this Image Processor that deals with all GUIs.
 * This class is-A Features, and inherits the various methods that would be "features" of the GUI.
 * This class performs basic operations for a GUI: it can exit the program, it can load an image
 * to the program, save an image, downscale an image, set a command to be executed in the future,
 * execute a command that the user chose, and can setErrorMessage to be displayed in the GUI by the
 * view component of the Image Processor. This controller accepts an EnhancedModel, and an IViewGUI.
 * The reason for that is simply because the new model is the better model, with more options for
 * image manipulations, and the reason for IViewGUI is simply because this controller controls
 * interactions between the GUI and the EnhancedModel, unlike the other Controller which is a
 * text base UI. Also, I avoided inheritance and composition because I wanted the two classes to
 * be independent and loosely coupled.
 */
public class ControllerGUI implements Features {
  private final EnhancedModel model;
  private IViewGUI view;
  private String currentCommand;
  private String currentImage;
  private Map<String, ImageIOHelper> knownImageFormats;
  private Map<String, Function<String, ImageCommand>> knownCommands;
  private Map<String, WritableRenderedImage> imageHistory;

  /**
   * Constructs a ControllerGUI, with the given EnhancedModel
   * This constructor will not only instantiate the model field, but will also initialize the map
   * of commands, image formats, and image history.
   * @param m EnhancedModel represents the model that this controller will communicate with.
   * @throws IllegalArgumentException if the given model is null, unacceptable!
   */
  public ControllerGUI(EnhancedModel m) throws IllegalArgumentException {
    if (m == null) {
      throw new IllegalArgumentException("Model must be non null!");
    }
    model = m;
    initMaps();
  }

  // initializes the map of image history, image formats, and commands.
  private void initMaps() {
    knownCommands = new HashMap<>();
    knownImageFormats = new HashMap<>();
    imageHistory = new HashMap<>();
    knownImageFormats.put("ppm", new ImagePPM());
    knownImageFormats.put("png/bmp/jpg/jpeg", new ImagePNGJPGBMP());
    knownCommands.put("flip-horizontal", s -> (new Flip(currentImage, s, true)));
    knownCommands.put("flip-vertical", s -> (new Flip(currentImage, s, false)));
    knownCommands.put("brighten", s -> (new Brighten(30, currentImage, s)));
    knownCommands.put("darken", s -> (new Brighten(-30, currentImage, s)));
    knownCommands.put("red-component", s -> (new Visualize(currentImage, s, "r")));
    knownCommands.put("green-component", s -> (new Visualize(currentImage, s, "g")));
    knownCommands.put("blue-component", s -> (new Visualize(currentImage, s, "b")));
    knownCommands.put("value-component",
        s -> (new Visualize(currentImage, s, "value")));
    knownCommands.put("intensity-component",
        s -> (new Visualize(currentImage, s, "intensity")));
    knownCommands.put("luma-component", s -> (new Visualize(currentImage, s, "luma")));
    knownCommands.put("blur", s -> (new Filter("blur", currentImage, s)));
    knownCommands.put("sharpen", s -> (new Filter("sharpen", currentImage, s)));
    knownCommands.put("sepia", s -> (new LinearTranform("sepia", currentImage, s)));
    knownCommands.put("luma", s -> (new LinearTranform("luma", currentImage, s)));
  }

  /**
   * Sets the given view to this controller's view, and adds the features to the view.
   * @param v IViewGUI represents the GUI of this Image Processor.
   * @throws IllegalArgumentException if given a null view.
   */
  public void setView(IViewGUI v) throws IllegalArgumentException {
    if (v == null) {
      throw new IllegalArgumentException("View must be non null!");
    }
    view = v;
    view.addFeatures(this);
  }

  @Override
  public void exitProgram() {
    System.exit(0);
  }


  /**
   * Updates the histogram visualizes in this controller's view field.
   * The way the histogram is updates is by creating a map of the different rgb values contained in
   * the image, and the count of each value as the value of the map. This is then used to determine
   * the size of the rectangles to be drawn on a Panel of some sorts, through the view's
   * showHistogram method, which takes in a Map as well.
   */
  private void updateHistogram() {
    Map[] maps = this.createHistogramMaps();
    view.showHistogram(maps);
  }

  /**
   * Figures the format out based on the given filePath.
   * @param filePath String represents the absolute or relative path to an image file.
   * @return a String representing the informal image format.
   * @throws IllegalArgumentException if given an unrecognized image format.
   */
  private String figureFormatOut(String filePath) throws IllegalArgumentException {
    if (filePath.endsWith("ppm")) {
      return "ppm";
    }
    else if (filePath.endsWith("jpeg") || filePath.endsWith("jpg") ||
            filePath.endsWith("png") || filePath.endsWith("bmp")) {
      return "png/bmp/jpg/jpeg";
    }
    throw new IllegalArgumentException("Invalid image format!");
  }

  /**
   * Helps the execute method to execute a given operation on this controller's currentImage.
   * What this does is basically get the operation from the map, calling the Function's apply
   * method to pass in any inputs to the subclasses, and finally executes it and updates the
   * current controller's image (ie the image the user can see at present).
   * @param operation String represents the commmand that will be executed on the current image.
   */
  private void commandHelper(String operation) {
    Function<String, ImageCommand> cmd = this.knownCommands.get(operation);
    ImageCommand imgCMD = cmd.apply(currentImage + operation);
    try {
      imgCMD.execute(model);
    }
    catch (IllegalArgumentException e) {
      setErrorMessage(e.getMessage());
    }
    currentImage = currentImage + operation;
  }

  /**
   * Visualizes the current image of this controller.
   */
  private void visualizeImage() {
    ImageIOHelper imageIO = knownImageFormats.get("png/bmp/jpg/jpeg");
    WritableRenderedImage image = imageIO.saveFile("DUMMY!"
                    + currentImage + ".bmp", model.getPixelsFromHistory(currentImage));
    view.setImage(image);
    imageHistory.put(currentImage, image);
  }

  @Override
  public void setErrorMessage(String message) {
    try {
      view.renderMessage(message);
    }
    catch (IOException e) {
      throw new IllegalStateException("Unexpected IO exception!");
    }
  }

  @Override
  public void loadImage(String filePath) {
    if (filePath.equals("")) {
      this.setErrorMessage("No file has been loaded yet!");
      return;
    }
    File file = new File(filePath);
    ImageIOHelper imageIO = knownImageFormats.get(figureFormatOut(filePath));
    List<List<Pixel>> pixels = imageIO.readFile(filePath);
    model.addToHistory(pixels, file.getName());
    try {
      imageHistory.put(file.getName(), ImageIO.read(file));
    } catch (IOException e) {
      setErrorMessage(e.getMessage());
    }
    currentImage = file.getName();
    this.visualizeImage();
    this.updateHistogram();
  }

  @Override
  public void saveImage(String filePath) {
    if (filePath.equals("")) {
      this.setErrorMessage("No file has been chosen yet!");
      return;
    }
    File file = new File(filePath);
    if (!figureFormatOut(filePath).equals("ppm")) {
      WritableRenderedImage image = imageHistory.get(currentImage);
      try {
        ImageIO.write(image, format(filePath), new FileOutputStream(file));
      }
      catch (IOException e) {
        this.setErrorMessage(e.getMessage());
        return;
      }
    }
    else {
      ImageIOHelper imageiO = knownImageFormats.get("ppm");
      imageiO.saveFile(filePath, model.getPixelsFromHistory(currentImage));
    }
  }

  @Override
  public void downscaleImage(int height, int width) {
    if (height <= 0 || width <= 0) {
      this.setErrorMessage("Invalid height or width given, try again!");
      return;
    }
    else {
      ImageCommand cmd = new DownSize(currentImage, currentImage + "downsize",
              height, width);
      try {
        cmd.execute(model);
      }
      catch (IllegalArgumentException e) {
        this.setErrorMessage(e.getMessage());
        return;
      }
      currentImage = currentImage + "downsize";
      this.visualizeImage();
      updateHistogram();
    }
  }

  @Override
  public void execute() {
    if (currentCommand == null) {
      this.setErrorMessage("You have not chosen a command!");
      return;
    }
    if (imageHistory.size() == 0) {
      this.setErrorMessage("You have not yet loaded an image!");
      return;
    }
    this.commandHelper(currentCommand);
    this.visualizeImage();
    updateHistogram();
    currentCommand = null;
  }

  @Override
  public void setCommand(String command) {
    currentCommand = command;
  }


  /**
   * This method is responsible for retrieving data from the model regarding the current image.
   * This data is then used to create a map of each rgb value from 0-255 with their corresponding
   * count as the value in the map. This is done by iterating over the 2d array of marbles
   * and just adding to the map each new rgb value seen. If an rgb value is seen and already in
   * the map, we just add to its count so far. We do this for red, green, blue, and intensity.
   * @return an array of map representing the different rgb values containing in the image and
   *         their count.
   */
  private Map<Integer, Integer>[] createHistogramMaps() {
    Map<Integer, Integer> countR = new HashMap<>();
    Map<Integer, Integer> countG = new HashMap<>();
    Map<Integer, Integer> countB = new HashMap<>();
    Map<Integer, Integer> countI = new HashMap<>();
    List<List<Pixel>> pixels = model.getPixelsFromHistory(currentImage);
    for (int i = 0; i < pixels.size(); i++) {
      for (int j = 0; j < pixels.get(i).size();j++) {
        Pixel p = pixels.get(i).get(j);
        int red = p.getComponent(0);
        int green = p.getComponent(1);
        int blue = p.getComponent(2);
        int intensity = p.executeCommand("intensity").getComponent(0);
        this.assignValues(red, countR);
        this.assignValues(blue, countB);
        this.assignValues(green, countG);
        this.assignValues(intensity, countI);
      }
    }
    return new Map[]{countR, countG,countB, countI};
  }

  /**
   * Returns the format of the given file path of an image.
   * @param filePath String represents the absolute/relative path of some image file.
   * @return a String representing the format of the image with the given file path.
   */
  private String format(String filePath) {
    String extension = figureFormatOut(filePath);
    if (extension.equals("ppm")) {
      return "ppm";
    }
    else {
      if (filePath.endsWith("jpg")) {
        return "jpg";
      }
      else if (filePath.endsWith("jpeg")) {
        return "jpeg";
      }
      else if (filePath.endsWith("png")) {
        return "png";
      }
      else {
        return "bmp";
      }
    }
  }

  /**
   * Either adds to the map a new rgb value, or adds to the count so far for that value (component).
   * @param component int represents the value of rgb or intensity.
   * @param map of int and int represents the map of rgb values of some image and the count of each.
   */
  private void assignValues(int component, Map<Integer, Integer> map) {
    if (map.containsKey(component)) {
      map.put(component, map.get(component) + 1);
    }
    else {
      map.put(component, 1);
    }
  }
}
