package imageprocessor.model.matrixoperations;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that extends filter, and represents a Sharpen function object.
 * This classes only unique purpose it initMatrix(), which sets the
 * matrix List to contain the kernel that is used for sharpening images.
 */
public class Sharpen extends Filter implements ImageMatrixOperations {

  /**
   * Calls super to initialize the imageName and destName through
   * the Filter constructor.
   * @param imageName the name of the image that is to be sharpened
   * @param destName the name that the new image will be stored under
   */
  public Sharpen(String imageName, String destName) {
    super(imageName, destName);
  }

  @Override
  protected void initMatrix() {
    this.matrix = new ArrayList<>();
    this.matrix.add(Arrays.asList((double) -1 / 8, (double) -1 / 8, (double) -1 / 8,
            (double) -1 / 8, (double) -1 / 8));
    this.matrix.add(Arrays.asList((double) -1 / 8, (double) 1 / 4, (double) 1 / 4,
            (double) 1 / 4, (double) -1 / 8));
    this.matrix.add(Arrays.asList((double) -1 / 8, (double) 1 / 4, (double) 1,
            (double) 1 / 4, (double) -1 / 8));
    this.matrix.add(Arrays.asList((double) -1 / 8, (double) 1 / 4, (double) 1 / 4,
            (double) 1 / 4, (double) -1 / 8));
    this.matrix.add(Arrays.asList((double) -1 / 8, (double) -1 / 8, (double) -1 / 8,
            (double) -1 / 8, (double) -1 / 8));
  }
}
