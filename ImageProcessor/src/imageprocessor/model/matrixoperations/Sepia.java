package imageprocessor.model.matrixoperations;

import imageprocessor.Pixel;

/**
 * A class that represents a Luma color transformation and
 * extends the Transform abstract class. This class's primary
 * purpose is to pass in the String "sepia" to the executeCommand()
 * method in Pixel, which will then perform the luma calculation
 * on a pixel in an image.
 */
public class Sepia extends Transform implements ImageMatrixOperations {

  /**
   * Constructor that call super() to use the constructor in Transform
   * to properly initialize the name and destination name of an image.
   * @param imageName the name of the image that is used to
   *                  do a sepia transformation.
   * @param destName the name that the new image will be stored under.
   */
  public Sepia(String imageName, String destName) {
    super(imageName, destName);
  }

  @Override
  protected Pixel transformPixel(Pixel p) {
    return p.executeCommand("sepia");
  }
}
