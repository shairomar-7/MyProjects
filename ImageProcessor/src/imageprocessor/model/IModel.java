package imageprocessor.model;

import java.util.List;

import imageprocessor.Pixel;

/**
 * IModel interface, represents the model component of the Image Processor.
 * This interface is responsible for all operations involving the manipulation of images.
 * IModel also extend the IModelState, and thus, has access to the provided interface methods.
 */
public interface IModel extends IModelState {

  /**
   * Adds the given 2d array of pixels to this model's history of images that the client created.
   * The 2D array of pixels will preferably be stored in a hashmap with the given image name as key.
   * If given a destName that is already in the model's history, then the newly given 2d array of
   * pixels will override the existing one.
   *
   * @param pixels   2d list of pixels represents the 2D array of pixels of some image.
   * @param destName String represents the desired name of the image to be added to history.
   */
  void addToHistory(List<List<Pixel>> pixels, String destName);

  /**
   * Void method will manipulate the 2D array of pixels representing the given imageName created.
   * Specifically, flip will either flip the image horizontally or vertically by getting the
   * array of pixels from the given imageName that should be stored in this model's history, and
   * manipulating that array, then stored the newly created image in this model's history with the
   * given destName as the key.
   * If given a destName that is already in the model's history, then the newly created 2d array of
   * pixels will override the existing one.
   *
   * @param horizontalHuh boolean represents whether we are flipping horizontally or vertically.
   * @param imageName String represents the name of the image to be flipped.
   * @param destName String represents the desired name of the created image to be added to history.
   * @throws IllegalArgumentException if given an imageName that is not in this model's history
   *                                  of images.
   */
  void flip(boolean horizontalHuh, String imageName, String destName)
          throws IllegalArgumentException;

  /**
   * Void method manipulates the 2D array of pixels representing the given imageName created.
   * Specifically, visualizeComponent will visualize the components of an image, then store the
   * newly created image in this model's history with the given destName as the key.
   * If given a destName that is already in the model's history, then the newly create 2d array of
   * pixels will override the existing one.
   * Valid components are the following: luma, value, intensity, r, g, b.
   * For ex: 120 150 200 -> visualize("r") -> Pixel(120, 120, 120)
   *
   * @param component String represents the component of the image to be visualized.
   * @param imageName String represents the image that the user created/loaded and would like
   *                  to manipulate.
   * @param destName String represents the desired name of the created image to be added to history.
   * @throws IllegalArgumentException if given an imageName that is not in this model's history
   *                                  of images, or if the given component is invalid.
   */
  void visualizeComponent(String component, String imageName, String destName)
          throws IllegalArgumentException;

  /**
   * Void method manipulates the 2D array of pixels representing the given imageName created.
   * Specifically, brightenOrDarkenBy will brighten or darken the pixels of an image,
   * then store the newly created image in this model's history with the given destName as the key.
   * If given a destName that is already in the model's history, then the newly create 2d array of
   * pixels will override the existing one.
   * If given a negative increment, this method will darken the pixels, if positive, brighten.
   * If given an increment that leads to more than the max depth supported by the image format,
   * then set the pixel's component to the max depth. If given an increment that leads to
   * less than 0, then set the pixel's component to 0.
   *
   * @param increment int represents the increment of the image to be brightened/darkened.
   * @param imageName String represents the image that the user created/loaded and would like
   *                  to manipulate.
   * @param destName String represents the desired name of the created image to be added to history.
   * @throws IllegalArgumentException if given an imageName that is not in this model's history
   *                                  of images.
   */
  void brightenOrDarkenBy(int increment, String imageName, String destName)
          throws IllegalArgumentException;
}
