package imageprocessor.controller;

import java.awt.image.WritableRenderedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import javax.imageio.ImageIO;

import imageprocessor.Pixel;
import imageprocessor.controller.commands.Brighten;
import imageprocessor.controller.commands.Filter;
import imageprocessor.controller.commands.Flip;
import imageprocessor.controller.commands.ImageCommand;
import imageprocessor.controller.commands.LinearTranform;
import imageprocessor.controller.commands.Visualize;
import imageprocessor.controller.io.ImageIOHelper;
import imageprocessor.controller.io.ImagePNGJPGBMP;
import imageprocessor.controller.io.ImagePPM;
import imageprocessor.model.EnhancedModel;
import imageprocessor.view.IView;

/**
 * Controller component of the Image Processor application.
 * This controller is-A IController, and thus, inherits the following methods:
 * go, to instantiate and run the program as long as the user has not quit/exited.
 * This controller is not specific to PPM/PNG/JPEG/JPG/BMP, however, with our current
 * implementation, this class will support only the formats. To add support to more files,
 * we will add another entry in the knownImageFormats Map, and would just create a new
 * ImageIOHelper subclass to deal with the read/save of that specific image format. This controller
 * was changed from the previous assignment in the following ways:
 * This controller now accepts an EnhancedModel instead of an IModel, we thought this would be
 * easier instead of creating a new Controller that extends the old controller. Another reason
 * we did that is to avoid casting the new Controller's model to an enhanced model. Also, if we
 * had changed the Controller to now accept an EnhancedModel, we might as well just enhance some
 * of the private methods, which is what we did. This new controller works exactly the same as the
 * old controller did when it comes to ppm files and all commands except blur/sharpen/sepia/luma,
 * which are newly added in the EnhancedModel interface. So what we did was basically move the
 * Pthprivate readPPMImage to the ImagePPM's read function, and all of its helper methods too. We
 * did the same for the saveImageAs and its helper methods. Thus, the ImagePPM has the exact same
 * logic as we did in our previous controller. We then added a Map to store the different image
 * formats as the key, and the different ImageIOHelper subclasses corresponding to the format.
 * Now, the readFile and saveFile would simply get the corresponding value of the Map based on
 * the file path (ends with ...) and would perform either the save or read method. This made our
 * code more clear, and more flexible for other future image formats to be incorporated.
 * Another change we made was simple adding the new commands to our Map of commands. Finally,
 * we added the new commands as instructions in the printMenu which will print the menu of this
 * image processor to the user.
 * Invariants:
 * IModel model and IView view and Appendable destination will never be null.
 */
public class Controller implements IController {
  private Map<String, ImageIOHelper> knownImageFormats;
  private Map<String, Function<Scanner, ImageCommand>> knownCommands;
  private final Readable inputs;
  private final EnhancedModel model;
  private final IView view;

  /**
   * Constructs a controller with the given model and view.
   * This controller is not used for testing, and rather, used to actually run the program.
   * This constructor will set the input of this Controller to be System.in. This controller will
   * also initialize the Map of commands that it accepts so far.
   *
   * @param model IModel represents the model component of the ImageProcessor program.
   * @param view  IView represents the view component of the ImageProcessor program.
   * @throws IllegalArgumentException if given a null model or a null view, simply unacceptable!
   */
  public Controller(EnhancedModel model, IView view)
          throws IllegalArgumentException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and View must be non-null!");
    }
    this.model = model;
    this.view = view;
    this.inputs = new InputStreamReader(System.in);
    this.initCommands();
  }

  /**
   * Constructs a controller with the given model and view.
   * This controller is used for testing, and not used to actually run the program.
   * This constructor will set the input of this Controller to be the given readable.
   * This controller will also initialize the Map of commands that it accepts so far.
   *
   * @param model  IModel represents the model component of the ImageProcessor program.
   * @param view   IView represents the view component of the ImageProcessor program.
   * @param inputs Readable represents the given inputs to this controller.
   * @throws IllegalArgumentException if given a null model, view or input: simply unacceptable!
   */
  public Controller(EnhancedModel model, IView view, Readable inputs)
          throws IllegalArgumentException {
    if (model == null || view == null || inputs == null) {
      throw new IllegalArgumentException("Model and View and inputs must be non-null!");
    }
    this.model = model;
    this.view = view;
    this.inputs = inputs;
    this.initCommands();
  }

  @Override
  public void goImageProcessor() throws IllegalStateException {
    this.printMenuToUser();
    this.processInputs(new Scanner(this.inputs));
  }

  /**
   * Initializes the map of available commands of this image processor's controller.
   */
  protected void initCommands() {
    knownCommands = new HashMap<>();
    knownCommands.put("flip-horizontal", s -> (new Flip(s.next(), s.next(), true)));
    knownCommands.put("flip-vertical", s -> (new Flip(s.next(), s.next(), false)));
    knownCommands.put("brighten", s -> (new Brighten(s.nextInt(), s.next(), s.next())));
    knownCommands.put("red-component", s -> (new Visualize(s.next(), s.next(), "r")));
    knownCommands.put("green-component", s -> (new Visualize(s.next(), s.next(), "g")));
    knownCommands.put("blue-component", s -> (new Visualize(s.next(), s.next(), "b")));
    knownCommands.put("value-component",
        s -> (new Visualize(s.next(), s.next(), "value")));
    knownCommands.put("intensity-component",
        s -> (new Visualize(s.next(), s.next(), "intensity")));
    knownCommands.put("luma-component", s -> (new Visualize(s.next(), s.next(), "luma")));
    knownImageFormats = new HashMap<>();
    knownImageFormats.put("ppm", new ImagePPM());
    knownImageFormats.put("png/bmp/jpg/jpeg", new ImagePNGJPGBMP());
    knownCommands.put("blur", s -> (new Filter("blur", s.next(), s.next())));
    knownCommands.put("sharpen", s -> (new Filter("sharpen", s.next(), s.next())));
    knownCommands.put("sepia", s -> (new LinearTranform("sepia", s.next(), s.next())));
    knownCommands.put("luma", s -> (new LinearTranform("luma", s.next(), s.next())));
  }

  /**
   * Reads the image file with the given filePath and will store the processed data in our model.
   * This method will check what the filePath ends with and will read the file according to the
   * image format. This function can only work on ppm, bmp, png, jpg, and jpeg, for now!
   * This function will attempt to read the contents of the image and convert it into a 2d list of
   * pixels, which will be stored in the model's history with the given destName.
   *
   * @param filePath String represents the path of the image file to be read.
   * @param destName String represents the desired name of the image to be stored in the model.
   * @throws IllegalArgumentException if the image format is not one of: ppm/png/jpeg/jpg/bmp
   *                                  or if the filePath is invalid or some IO issue occurred.
   */
  private void readFile(String filePath, String destName) throws IllegalArgumentException {
    this.readFileHelper(filePath, destName);
  }

  /**
   * Helps readFile method to get the correct ImageIOHelper from this controller's map of formats.
   *
   * @param filePath String represents the file path of the image to be read.
   * @param destName String represents the desired name of the image to be loaded and stored in
   *                 our model's history of images.
   * @throws IllegalArgumentException if given an invalid filePath or an IO error occurred.
   */
  private void readFileHelper(String filePath, String destName) throws IllegalArgumentException {
    ImageIOHelper imageIO;
    if (filePath.endsWith("ppm")) {
      imageIO = knownImageFormats.get("ppm");
    } else if (this.isImageFormatPNGJPGBMP(filePath)) {
      imageIO = knownImageFormats.get("png/bmp/jpg/jpeg");
    } else {
      throw new IllegalArgumentException("Unsupported file format, for now!");
    }
    List<List<Pixel>> pixels = imageIO.readFile(filePath);
    model.addToHistory(pixels, destName);
  }

  // Prints the menu to the user, the menu represents the possible commands this controller
  // can accept.
  private void printMenuToUser() throws IllegalStateException {
    this.informUser("Supported user instructions are: ");
    this.informUser("load file-path destination-name");
    this.informUser("save file-path image-name");
    this.informUser("flip-horizontal image-name destination-name");
    this.informUser("flip-vertical image-name destination-name");
    this.informUser("brighten increment image-name destination-name");
    this.informUser("red-component image-name destination-name");
    this.informUser("blue-component image-name destination-name");
    this.informUser("green-component image-name destination-name");
    this.informUser("value-component image-name destination-name");
    this.informUser("intensity-component image-name destination-name");
    this.informUser("luma-component image-name destination-name");
    this.informUser("blur image-name destination-name");
    this.informUser("sharpen image-name destination-name");
    this.informUser("luma image-name destination-name");
    this.informUser("sepia image-name destination-name");
    this.informUser("q/Q/quit (quit the program)");
  }


  /**
   * This method will process the elements of the given scanner.
   * Based on this controller's available commands and valid arguments, this method will attempt
   * to "react" from a scanned data type.
   * Reactions:
   * "q", "quit", "Q" -> tell user he/she has quit, and end this function (and image processor).
   * "save" -> try to save an image with 2 consecutive strings, tell user he/she saved the image.
   * "load" -> try to load an image with 2 consecutive strings, tell user he/she loaded the image.
   * ANY OF THE COMMANDS IN THIS CONTROLLER -> delegate to commandHelper
   * If any one of these commands lead to an IllegalArgumentException, we catch it and inform the
   * user why that happened.
   *
   * @param s Scanner represents a text scanner that allows us to parse primitive types and strings.
   * @throws IllegalStateException if the scanner no longer has any inputs to read and the user
   *                               has not quit the program yet.
   */
  private void processInputs(Scanner s) throws IllegalStateException {
    while (s.hasNext()) {
      try {
        String next = s.next(); // ask ta if this can cause a bug
        switch (next) {
          case "q":
          case "quit":
          case "Q":
            this.informUser("ImageProcessor quit! Thank you for your time.");
            return;
          case "save":
            this.saveImageAs(s.next(), s.next());
            this.informUser("Successfully saved the image!");
            break;
          case "load":
            this.readFile(s.next(), s.next());
            this.informUser("Successfully loaded the image!");
            break;
          default:
            this.commandHelper(next, s);
            break;
        }
      } catch (IllegalArgumentException e) {
        this.informUser(e.getMessage());
      }
    }
    throw new IllegalStateException("No more inputs to be read! That's unfortunate.");
  }

  /**
   * Helps the processInputs by trying to identify the given next in this controller's commands.
   * This method will try to get the Function object from this controller's hashmap of commands,
   * and if it returns a Function, then we will call the apply, giving it the Scanner.
   * This will return an ImageCommand, and so, we can then call the execute method with the model.
   * If the execute throws an exception due to the user entering invalid arguments, we catch it and
   * inform the user.
   * If getOrDefault returns null, then we inform the user that this is an unsupported command.
   *
   * @param next String represents the next element of the processInputs's scanner.
   * @param s    Scanner represents a simple text scanner that allows us to parse the data types and
   *             execute the command if identified.
   */
  private void commandHelper(String next, Scanner s) {
    Function<Scanner, ImageCommand> cmd =
            knownCommands.getOrDefault(next, null);
    if (cmd == null) {
      this.informUser("Unsupported command given!");
    } else {
      try {
        ImageCommand c = cmd.apply(s);
        c.execute(model);
        c = null;
      } catch (IllegalArgumentException e) {
        this.informUser("Invalid image name or parameters!");
      }
    }
  }

  /**
   * This function will attempt to save a previously creates Image by passing in
   * the desired absolute path of that image. The image is identified by passing in the
   * image name that the user created or loaded onto the ImageProcessor.
   * This method will assume that the image to be saved is not corrupt, since the readFile method
   * of this Controller will ensure that only non-corrupt image files are stored in the model.
   *
   * @param filePath  String represents the path of the image file to be saved.
   * @param imageName String represents the image name that the user created/loaded and would like
   *                  to save.
   * @throws IllegalArgumentException if the given filePath is invalid or if the image name
   *                                  is not stored in the model's history, or
   *                                  if the file type is not supported (for now).
   */
  private void saveImageAs(String filePath, String imageName)
          throws IllegalArgumentException {
    if (filePath.endsWith("ppm")) {
      ImageIOHelper imageIO = this.knownImageFormats.get("ppm");
      imageIO.saveFile(filePath, model.getPixelsFromHistory(imageName));
    } else if (this.isImageFormatPNGJPGBMP(filePath)) {
      ImageIOHelper imageIO = this.knownImageFormats.get("png/bmp/jpg/jpeg");
      WritableRenderedImage image = imageIO.saveFile(filePath,
              model.getPixelsFromHistory(imageName));
      try {
        ImageIO.write(image, this.figureFormatOut(filePath), new FileOutputStream(filePath));
      } catch (IOException e) {
        throw new IllegalArgumentException("Some IO issue occurred," +
                " make sure the file path is valid!");
      }
    } else {
      throw new IllegalArgumentException("Unsupported file format!");
    }
  }

  private String figureFormatOut(String filePath) {
    if (filePath.endsWith("png")) {
      return "png";
    } else if (filePath.endsWith("jpeg") || filePath.endsWith("jpg")) {
      return "jpeg";
    } else {
      return "bmp";
    }
  }

  // Determines if the image format is one of: png/jpg/jpeg/bmp
  private boolean isImageFormatPNGJPGBMP(String filePath) {
    return filePath.endsWith("png") || filePath.endsWith("jpg") ||
            filePath.endsWith("jpeg") || filePath.endsWith("png") || filePath.endsWith("bmp");
  }

  /**
   * This method takes the given message and calls this view's renderMessage to append to output.
   * Ultimately, we are sending messages to the user.
   *
   * @param message String represents the message to be sent to the user (ie append to output)
   * @throws IllegalStateException if the view faced some IO exception.
   */
  private void informUser(String message) throws IllegalStateException {
    try {
      view.renderMessage(message + '\n');
    } catch (IOException e) {
      throw new IllegalStateException("IO error! Something went wrong" +
              " with reading inputs, or writing output");
    }
  }
}