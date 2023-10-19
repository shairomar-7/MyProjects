package imageprocessor.controller.commands;

import imageprocessor.model.EnhancedModel;

/**
 * Filter command, which applies either the blur or sharpen filtration on an image.
 * This class has been abstracted for clarity and the reduction of code duplication.
 */
public class Filter extends AbstractCommand implements ImageCommand {
  private final String method;

  /**
   * Constructs a Filter command, with the given filter method, the image name, and the dest name.
   * @param method String represents the filtration method to be applied on the image, for now:
   *               "blur" and "sharpen" are supported.
   * @param imageName String represents the name of the image that this command is applied on.
   * @param destName String represents the desired name of the result of applying this command on
   *                 the given image.
   */
  public Filter(String method, String imageName, String destName) {
    super(imageName, destName);
    this.method = method;
  }

  @Override
  public void execute(EnhancedModel m) throws IllegalArgumentException {
    m.filter(method, imageName, destName);
  }
}
