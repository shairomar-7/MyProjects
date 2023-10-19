package imageprocessor.model;

/**
 * The interface for EnhancedModel. This is an extension of the
 * IModel interface. It contains all the same methods of IModel interface,
 * except it offers two new methods: colorTransform(String, String, String),
 * and filter(String, String, String.
 */
public interface EnhancedModel extends IModel {

  /**
   * Performs a color transformation on an image. This
   * method is similar to the visualizeComponent() method in the IModel
   * interface, except it expresses the color transformation in matrix form.
   * This is useful for doing transformations such as sepia, where the r, g, and
   * b values are changed using different equations. This method also makes sure to
   * clamp pixel values: If a new pixel value is above 255, it will be set at 255,
   * a pixel value cannot be below 0, because none of the matrices used for transforming
   * multiply a value component by a negative value.
   *
   * @param transformMethod the type of transformation that is to be done
   *                        on the image, this program offers the ability
   *                        to do a sepia or luma color transformation
   * @param imageName       the name of the image that is to be transformed
   * @param destName        the name that the new image will be stored under
   * @throws IllegalArgumentException if the transformMethod is not identified by the model, or
   *                                  the given image name is not in the history of images.
   */
  void colorTransform(String transformMethod, String imageName, String destName)
          throws IllegalArgumentException;

  /**
   * Applies a filter to an image. Filtering changes each pixel of an image
   * by applying a kernel to all the surrounding pixels within a certain range,
   * and calculating a new value for the pixel. This program offers the user
   * the ability to blur and sharpen an image. This method also makes sure to
   * clamp pixel values: If a new value component is below 0, it will be set at 0.
   * If a new value component is above 255, it will be set at 255.
   *
   * @param filterMethod the type of filter (blur, sharpen) that is to be performed on a
   *                     certain image
   * @param imageName    the name of the image that is to be filtered
   * @param destName     the name that the new image will be stored under
   * @throws IllegalArgumentException if the filter method is unidentified by the model or the
   *                                  given image name is not in the history of images.
   */
  void filter(String filterMethod, String imageName, String destName)
          throws IllegalArgumentException;


  /**
   * Downscales an image from its original dimensions to the newly given dimension.
   * This function will downscale the image in the history of images corresponding to the given
   * imageName and will add the downscaled image to the model's history with the given destName.
   * Note: this function only downscales, no upscale here. If the size is the same, we allow that
   * even though the user is simply wasting time and money.
   *
   * @param imageName String represents the name of the image to be downscaled.
   * @param destName  String represents the desired name of the downscaled image.
   * @param height    int represents the new height of the image
   * @param width     int represents the new width of the image.
   * @throws IllegalArgumentException if the given width or height are greater than the original
   *                                  image, or if the given imageName is not found in the history
   *                                  of images of this model.
   */
  void downscaleImage(String imageName, String destName, int height, int width)
          throws IllegalArgumentException;
}
