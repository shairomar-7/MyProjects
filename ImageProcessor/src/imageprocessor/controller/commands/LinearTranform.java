package imageprocessor.controller.commands;

import imageprocessor.model.EnhancedModel;

/**
 * LinearTransform command, which applies either the sepia or luma color transformation on an image.
 * This class has been abstracted for clarity and the reduction of code duplication.
 */
public class LinearTranform extends AbstractCommand implements ImageCommand {
  private final String method;

  /**
   * Constructs a LinearTranform, with the given filter method, the image name, and the dest name.
   * @param method String represents the transformation method to be applied on the image, for now:
   *               "sepia" and "luma" are supported.
   * @param imageName String represents the name of the image that this command is applied on.
   * @param destName String represents the desired name of the result of applying this command on
   *                 the given image.
   */
  public LinearTranform(String method, String imageName, String destName) {
    super(imageName, destName);
    this.method = method;
  }

  @Override
  public void execute(EnhancedModel m) throws IllegalArgumentException {
    m.colorTransform(method, imageName, destName);
  }
}
