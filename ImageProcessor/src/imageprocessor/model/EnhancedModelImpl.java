package imageprocessor.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import imageprocessor.Pixel;
import imageprocessor.model.matrixoperations.Blur;
import imageprocessor.model.matrixoperations.Downsize;
import imageprocessor.model.matrixoperations.ImageMatrixOperations;
import imageprocessor.model.matrixoperations.Luma;
import imageprocessor.model.matrixoperations.Sepia;
import imageprocessor.model.matrixoperations.Sharpen;

/**
 * The EnhancedModelImpl class. This is an extension of the Model class,
 * and also an implementation of the new EnhancedModel interface. This method
 * serves all the same functionalities as the previous Model class, and for the
 * most part their constructors are the same. Each constructor calls the super()
 * method, since the fields history, and maxDepth should still be initialized the same.
 * The main difference is that this class stores a second hashmap that maps strings
 * (representing each new functionality: "blur", "sharpen", "sepia", "luma"), to a function that
 * takes in a String[], containing the name of an image and its destination name,
 * and produces the appropriate ImageMatrixOperation object.
 * This hashmap is initialized using the private method initOperations(). The filter and
 * colorTransform methods work by taking in a String that represents the operation that the user
 * wants to execute, and passing it to a helper method that retrieves the appropriate
 * ImageMatrixOperation object from the hashmap, and using it to execute the operation.
 */
public class EnhancedModelImpl extends Model implements EnhancedModel {
  Map<String, Function<String[], ImageMatrixOperations>> matrixOperations;

  /**
   * Default constructor for EnhancedModelImpl. This uses super() to initialize
   * the map of images, and the MaxDepth, which represents the maximum depth of
   * each value component in a pixel, to 255.
   * This constructor then calls initOperations() to initialize the matrixOperations hashmap
   * to contain the four new operations that the EnhancedModel interface is meant to support.
   */
  public EnhancedModelImpl() {
    super();
    initOperations();
  }

  /**
   * calls super() to construct an EnhancedModelImpl from the given 2D List of Pixels, and
   * width/height of an image. This constructor enforces all the same constraints
   * as the corresponding constructor in the Model class. This constructor will also call
   * initConditions() to initialize hte matrixOperations hashmap in the same way as the
   * default constructor.
   *
   * @param pixels the list of pixels that represents the image.
   * @param width  the width of image
   * @param height the height of the image.
   * @throws IllegalArgumentException if the given height is not equal to the length
   *                        of the 2d array of pixels representing the rows or the width
   *                        is not equal to the length of the inner list representing the columns.
   *                        This is because the image would be broken, and this program
   *                        does not support broken image files, or if the given 2d array of pixels
   *                        is null, or if the width or height is less than or equal to zero.
   */
  public EnhancedModelImpl(List<List<Pixel>> pixels, int width, int height)
          throws IllegalArgumentException {
    super(pixels, width, height);
    initOperations();
  }

  /**
   * Creates a new ImageMatrixOperations object that gets
   * mapped to a string representing what it does. s[0]
   * and s[1] represent the name and destination name of
   * an image.
   */
  private void initOperations() {
    this.matrixOperations = new HashMap<>();
    this.matrixOperations.put("blur", s -> (new Blur(s[0], s[1])));
    this.matrixOperations.put("sharpen", s -> (new Sharpen(s[0], s[1])));
    this.matrixOperations.put("sepia", s -> (new Sepia(s[0], s[1])));
    this.matrixOperations.put("luma", s -> (new Luma(s[0], s[1])));
  }

  @Override
  public void colorTransform(String transformMethod, String imageName, String destName)
          throws IllegalArgumentException {
    this.helpMatrixOperations(transformMethod, imageName, destName);
  }

  @Override
  public void filter(String filterMethod, String imageName, String destName)
          throws IllegalArgumentException {
    this.helpMatrixOperations(filterMethod, imageName, destName);
  }

  @Override
  public void downscaleImage(String imageName, String destName, int height, int width)
          throws IllegalArgumentException {
    List<List<Pixel>> pixels = getPixelsFromHistory(imageName);
    if (height > pixels.size() || width > pixels.get(0).size()) {
      throw new IllegalArgumentException("Given height and width must be" +
              " less than or equal to the image size!");
    }
    ImageMatrixOperations down = new Downsize(imageName, destName, height, width);
    down.execute(this);
  }

  /**
   * Helps the filter and colorTransform to get the corresponding ImageMatrixOperations and to
   * execute the operation on this model.
   *
   * @param method    String represents the filter/transform method
   * @param imageName String represents the name of the image to be filtered/transformed.
   * @param destName  String represents the desired name of the result of filtering/transforming.
   * @throws IllegalArgumentException if the given method for filter/transform is invalid,
   *                                  or if the given image name is not in the history!
   */
  private void helpMatrixOperations(String method, String imageName, String destName)
          throws IllegalArgumentException {
    Function<String[], ImageMatrixOperations> func = this.matrixOperations.get(method);
    if (func == null) {
      throw new IllegalArgumentException("invalid filter method!");
    }
    ImageMatrixOperations op = func.apply(new String[]{imageName, destName});
    op.execute(this);
  }
}
