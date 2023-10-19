package imageprocessor.model.matrixoperations;

import java.util.ArrayList;
import java.util.List;
import imageprocessor.Pixel;
import imageprocessor.model.EnhancedModel;

/**
 * Abstract class that represents each of the color transformation
 * operations (luma, sepia). The logic for each of these operations
 * is exactly the same, aside from the matrix that they use to
 * modify the value components of each pixel. The math for actually
 * changing the pixels is handled in the Pixel class. All the methods
 * for the color transformations are abstracted into this class, except
 * for the transformPixel method which is used to call the executeCommand
 * method in Pixel. This is left to the subclasses, as they have to pass in
 * the appropriate string.
 */
public abstract class Transform implements ImageMatrixOperations {
  private final String imageName;
  private final String destName;

  /**
   * Constructor for Transform. Takes in the name of the image that is to
   * be modified, as well as the name that the new image will be stored under.
   *
   * @param imageName the name of the image that will be transformed
   * @param destName  the name that the new image will be stored under
   */
  public Transform(String imageName, String destName) {
    this.imageName = imageName;
    this.destName = destName;
  }

  // Execute the color transformation on the given IModel according to the component in the
  // constructor. Currently the two operations the program supports  are a sepia and luma color
  // transformation. It will perform the appropriate color transformation according to which one
  // of these strings have been passed into the constructor.
  @Override
  public void execute(EnhancedModel m) throws IllegalArgumentException {
    List<List<Pixel>> pixels = m.getPixelsFromHistory(imageName);
    m.addToHistory(this.modifyPixels(pixels), destName);
  }

  // Based on the transformation being applied, transform the given pixel accordingly.
  protected abstract Pixel transformPixel(Pixel p);

  // Return a new 2D List of pixels which is a result of transforming each of the pixels in the
  // given array of pixels.
  private List<List<Pixel>> modifyPixels(List<List<Pixel>> pixels) {
    List<List<Pixel>> result = new ArrayList();
    for (int i = 0; i < pixels.size(); i++) {
      List<Pixel> inner = new ArrayList();
      for (int j = 0; j < pixels.get(i).size(); j++) {
        inner.add(this.transformPixel(pixels.get(i).get(j)));
      }
      result.add(inner);
    }
    return result;
  }
}
