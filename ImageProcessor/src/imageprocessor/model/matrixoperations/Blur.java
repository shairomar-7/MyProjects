package imageprocessor.model.matrixoperations;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that extends filter, and represents a Blur function object.
 * This classes only unique purpose it initMatrix(), which sets the
 * matrix List to contain the kernel that is used for blurring images.
 */
public class Blur extends Filter implements ImageMatrixOperations {

  /**
   * Calls super to initialize the imageName and destName through
   * the Filter constructor.
   *
   * @param imageName the name of the image that is to be blurred
   * @param destName  the name that the new image will be stored under
   */
  public Blur(String imageName, String destName) {
    super(imageName, destName);
  }

  @Override
  protected void initMatrix() {
    this.matrix = new ArrayList<>();
    this.matrix.add(Arrays.asList((double) 1 / 16, (double) 1 / 8, (double) 1 / 16));
    this.matrix.add(Arrays.asList((double) 1 / 8, (double) 1 / 4, (double) 1 / 8));
    this.matrix.add(Arrays.asList((double) 1 / 16, (double) 1 / 8, (double) 1 / 16));
  }
}
