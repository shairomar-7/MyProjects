package imageprocessor.model.matrixoperations;

import imageprocessor.model.EnhancedModel;

/**
 * An interface the represents the new operations that are supported by the EnhancedModel
 * interface. These operations are blur, sharpen, luma color transformation, and
 * sepia color transformation. Similar to the ImageCommand interface in the Controller
 * class, this interface utilizes a command design pattern in order to minimize code duplication.
 * Note: This interface is not only implemented by classes that would necessarily perform a matrix
 * operation. For example, the downsize class is perfectly able to use this interface. Apologies
 * for the bad naming.
 */
public interface ImageMatrixOperations {

  /**
   * Executes the appropriate operation on the given EnhancedModel object. The EnhancedModel
   * that is passed into the method will be used to obtain its pixels, which will then
   * be changed according to the operation. Once a new image has been created, it
   * will be added to the history hashmap in the EnhancedModel.
   * @param m the EnhancedModel that will be used to create a new image
   * @throws IllegalArgumentException if one the classes have been given invalid inputs upon
   *          construction.
   */
  void execute(EnhancedModel m) throws IllegalArgumentException;
}
