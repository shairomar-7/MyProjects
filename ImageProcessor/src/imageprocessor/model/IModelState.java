package imageprocessor.model;

import java.util.List;

import imageprocessor.Pixel;

/**
 * IModelState, represents the state of this Image Processor's model component.
 * This interface provides methods to get insight on the current IModel's state.
 * This interface provides two methods: getDepth which returns the max depth of the model,
 * and getPixelsFromHistory which gets a copy of the 2d array of pixels stored in this model's
 * history of images that the client created/loaded,
 */
public interface IModelState {

  /**
   * Gets the max depth of this IModelState.
   * The max depth represents the max value of the components of a pixel, it depends on the image
   * file format.
   * A getter is used because the maxDepth field of a model must be private final.
   *
   * @return an int representing the model's max depth.
   */
  int getDepth();

  /**
   * Returns a COPY of the 2D array of Pixels that the user has loaded/created in the model history.
   * We return a shallow copy because the Pixel class does not offer any mutation methods,
   * and its fields are all private final.
   *
   * @param imageName represents the name of the image that is stored in this model's history of
   *                  images that a client has created/loaded.
   * @return a 2D Array of Pixels representing the pixels of an image the client has created/loaded.
   */
  List<List<Pixel>> getPixelsFromHistory(String imageName);
}
