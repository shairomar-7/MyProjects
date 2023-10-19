package imageprocessor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imageprocessor.Pixel;

/**
 * Image Processor's model component, responsible for all image operations, and for storing data.
 * The operations involved are used to manipulate images: flip (horizontal/vertical),
 * brighten/darken by a certain increment, or visualize component (luma/value/intensity/r/g/b).
 * This model also gives access to the IModelState interface, which is extended by the IModel
 * interface. Thus, clients of this class can access the state of the model through
 * getPixelsFromHistory which returns a 2D array of Pixels representing the image the user has
 * created/stored. Upon instantiation, this model class will either contain an empty history, and
 * is waiting for the client to start creating and storing images using the addToHistory method,
 * OR the model was given a 2D array of pixels and a width and height of an image, and adds the
 * given array to the history map, with the key being "initialModel".
 * Assumptions: we assume that the maxDepth (the max values of r/g/b) is 255, which
 * is the convention for PPM files. We also assume that the inner lists of the 2d array of pixels,
 * which represents the columns, are all of the same size (all same size as first). These 2
 * assumptions are justified given that this Model is somewhat specific to ppm files. If we were
 * to support other file formats, we would simply create an Abstract class, and overide methods as
 * needed.
 * Note: this model does not support a width/height less than or equal to zero, we decided that
 * the UX of allowing a client to manipulate an empty image is pretty bad. this model also does not
 * handle pixels with rgb values greater than the image specification's max depth, this is the
 * controller's job. Since the controller will never allow such an image file to be read, we
 * can assume this model will never reach such a scenario.
 * Invariants of this class:
 * maximum value of r/g/b components is maxDepth, because the brightenOrDarkenBy will
 * ensure that the r/g/b values are never greater than the maxDepth, if it is then it is set to
 * the maxDepth. On top of that, we will reject an image file that does not follow this constraint,
 * through the Controller. Thus, the Model should never receive such invalid data.
 * width/height of the image will stay the same, simply because the outer array represents
 * the rows (height), and the inner array represents the columns (width). This array is constant
 * in length, so is the inner array, and thus, the height and width will stay the same.
 * 2D Array of Pixels will always be a none null.
 */
public class Model implements IModel {
  private final Map<String, List<List<Pixel>>> history;
  private final int maxDepth;

  /**
   * Constructs a Model, with no arguments whatsover.
   * This constructor initializes the history hashmap.
   * This constructor is used for running the program, since we allow the user to run this
   * application even if no image was loaded. However, if the user does not load an image, and tries
   * to manipulate some image that is not stored in our database, then the controller must inform
   * the user that he/she has not loaded an image. This is done through the view component.
   * This constructor may also be used for testing.
   */
  public Model() {
    this.history = new HashMap<>();
    this.maxDepth = 255;
  }

  /**
   * Constructs a Model with the given 2D array of pixels, the width, the height of some image.
   * This constructor will not ensure that the pixels in the given 2D array of pixels are not
   * greater than the maxDepth. This is because our Model is not responsible for ensuring
   * this condition. The controller on the other hand will only transmit data to the model if and
   * only if the image file is not corrupt.
   *
   * @param pixels 2d list of pixels represents the pixels of some image.
   * @param width  int represents the width of some image.
   * @param height int represents the height of some image.
   * @throws IllegalArgumentException if the given height is not equal to the length
   *                  of the 2d array of pixels representing the rows or the width
   *                  is not equal to the length of the inner list representing the columns.
   *                  This is because the image would be broken, and this program
   *                  does not support broken image ppm files, or if the given 2d array of pixels
   *                  is null, or if the width or height is less than or equal to zero.
   */
  public Model(List<List<Pixel>> pixels, int width, int height)
          throws IllegalArgumentException {
    if (this.checkConstructorValidity(pixels, width, height)) {
      throw new IllegalArgumentException("invalid 2d array of pixels given!");
    }
    this.history = new HashMap<>();
    this.maxDepth = 255;
    this.history.put("initialModel", new ArrayList(pixels));
  }

  /**
   * Checks if the arguments passed to the constructor are valid.
   *
   *     @param pixels 2d list of pixels represents the given 2D array of pixels.
   *     @param width  int width represents the width of the image.
   *     @param height int height represents the width of the image.
   *     @return a boolean telling us whether the constructor is invalid or not.
   *     true -> invalid
   *     false -> valid
   */
  private boolean checkConstructorValidity(List<List<Pixel>> pixels, int width, int height) {
    return pixels == null ||
            pixels.size() != height ||
            pixels.get(0).size() != width ||
            width <= 0 || height <= 0;
  }

  /**
   * Helper method for the IModel's flip method, flips the image of the given imageName in history.
   * This helper is responsible for flipping the image horizontally.
   * This helper will try to reverse the columns while keeping the rows static.
   *
   * @param imageName String represents the name of the image stored in this model's history
   *                  and to be horizontally flipped.
   * @return a 2D array of Pixels representing the horizontally flipped image.
   * @throws IllegalArgumentException if the given imageName is not found in this model's history
   *                                  of images that the user has created/laoded.
   */
  private List<List<Pixel>> flipHorizontal(String imageName) throws IllegalArgumentException {
    List<List<Pixel>> result = new ArrayList<>();
    List<List<Pixel>> pixelsOfFile = this.getPixelsFromHistory(imageName);
    for (int i = 0; i < pixelsOfFile.size(); i++) {
      List<Pixel> inner = new ArrayList<>();
      for (int j = pixelsOfFile.get(0).size() - 1; j >= 0; j--) {
        inner.add(pixelsOfFile.get(i).get(j));
      }
      result.add(inner);
    }
    return result;
  }

  /**
   * Helper method for the IModel's flip method, flips the image of the given imageName in history.
   * This helper is responsible for flipping the image vertically.
   * This helper will try to reverse the rows while keeping the column static.
   *
   * @param imageName String represents the name of the image stored in this model's history
   *                  and to be vertically flipped.
   * @return a 2D array of Pixels representing the vertically flipped image.
   * @throws IllegalArgumentException if the given imageName is not found in this model's history
   *                                  of images that the user has created/laoded.
   */
  private List<List<Pixel>> flipVertically(String imageName) throws IllegalArgumentException {
    List<List<Pixel>> result = new ArrayList<>();
    List<List<Pixel>> pixelsOfFile = this.getPixelsFromHistory(imageName);
    for (int i = pixelsOfFile.size() - 1; i >= 0; i--) {
      List<Pixel> inner = new ArrayList<>();
      for (int j = 0; j < pixelsOfFile.get(0).size(); j++) {
        inner.add(pixelsOfFile.get(i).get(j));
      }
      result.add(inner);
    }
    return result;
  }

  @Override
  public void addToHistory(List<List<Pixel>> pixels, String imageName) {
    this.history.put(imageName, pixels);
  }

  @Override
  public void flip(boolean horizontalHuh, String imageName, String destName)
          throws IllegalArgumentException {
    List<List<Pixel>> result;
    if (horizontalHuh) {
      result = this.flipHorizontal(imageName);
    } else {
      result = this.flipVertically(imageName);
    }
    this.history.put(destName, result);
  }

  @Override
  public int getDepth() {
    return this.maxDepth;
  }

  @Override
  public List<List<Pixel>> getPixelsFromHistory(String imageName) throws IllegalArgumentException {
    List<List<Pixel>> result = this.history.get(imageName);
    if (result != null) {
      return new ArrayList<>(result);
    }
    throw new IllegalArgumentException("given file name is invalid!" +
            " Not in history of images you created!");
  }

  @Override
  public void visualizeComponent(String component, String imageName, String destName)
          throws IllegalArgumentException {
    List<List<Pixel>> result = new ArrayList<>();
    List<List<Pixel>> pixelsOfFile = this.getPixelsFromHistory(imageName);
    for (int i = 0; i < pixelsOfFile.size(); i++) {
      List<Pixel> inner = new ArrayList<>();
      for (int j = 0; j < pixelsOfFile.get(i).size(); j++) {
        inner.add(pixelsOfFile.get(i).get(j).executeCommand(component));
      }
      result.add(inner);
    }
    this.history.put(destName, result);
  }

  @Override
  public void brightenOrDarkenBy(int increment, String imageName, String destName)
          throws IllegalArgumentException {
    List<List<Pixel>> result = new ArrayList<>();
    List<List<Pixel>> pixelsOfFile = this.getPixelsFromHistory(imageName);
    for (int i = 0; i < pixelsOfFile.size(); i++) {
      List<Pixel> inner = new ArrayList<>();
      for (int j = 0; j < pixelsOfFile.get(0).size(); j++) {
        inner.add(pixelsOfFile.get(i).get(j).incrementPixel(increment, maxDepth));
      }
      result.add(inner);
    }
    this.history.put(destName, result);
  }
}