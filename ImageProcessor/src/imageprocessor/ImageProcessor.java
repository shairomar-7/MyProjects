package imageprocessor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import imageprocessor.controller.Controller;
import imageprocessor.controller.ControllerGUI;
import imageprocessor.controller.IController;
import imageprocessor.model.EnhancedModel;
import imageprocessor.model.EnhancedModelImpl;
import imageprocessor.model.MaskedImageModel;
import imageprocessor.view.IView;
import imageprocessor.view.JView;
import imageprocessor.view.View;

/**
 * Image Processor, class that holds the main program for our ImageProcessor application.
 */
public class ImageProcessor {

  /**
   * Runs the Image Processor application, by creating a Model, a View, and a Controller.
   * This function will call the go() method of the controller, thus running the program until
   * the user has intentionally quit the game.
   *
   * @param args represents the arguments to be passed into the main through System.in.
   */
  public static void main(String[] args) {
    EnhancedModel model = new EnhancedModelImpl();
    IView view = new View();
    if (args.length == 0) {
      ControllerGUI controller = new ControllerGUI(model);
      controller.setView(new JView("Image Processor"));
    } else if (acceptScriptFile(args) != null) {
      IController controller = new Controller(model, view, acceptScriptFile(args));
      controller.goImageProcessor();
    }
    else if (args.length == 1 && args[0].equals("-text")) {
      IController controller = new Controller(model, view);
      controller.goImageProcessor();
    }
    else if (args.length == 1 && args[0].equals("-masked")) {
      model = new MaskedImageModel();
      IController controller = new Controller(model, view);
      controller.goImageProcessor();
    }
    else {
      System.out.println("Invalid command-line argument given!");
    }
  }

  private static Readable acceptScriptFile(String[] args) {
    if (args.length >= 1 && args[0].endsWith(".txt")) {
      if (args.length == 1) {
        try {
          File file = new File(args[0]);
          return new FileReader(file);
        } catch (IOException e) {
          System.out.println("Invalid file given!");
        }
      }
      else if (args.length == 2 && args[1].equals("-masked")) {
        try {
          File file = new File(args[0]);
          return new FileReader(file);
        } catch (IOException e) {
          System.out.println("Invalid file given!");
        }
      }
    }
    return null;
  }
}
