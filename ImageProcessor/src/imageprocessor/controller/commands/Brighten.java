package imageprocessor.controller.commands;

import imageprocessor.model.EnhancedModel;

/**
 * Brighten Command, implements the ImageCommand interface.
 * This command will brighten/darken an image based on the given increment.
 */
public class Brighten extends AbstractCommand implements ImageCommand {
  private final int increment;

  /**
   * Constructs a Brighten Command, with given increment, fileName (imageName) and the dest name.
   * This constructor will throw no exceptions, it accepts any input given by the user with
   * compatibles types.
   * @param increment int represents how much the user want to brighten/darken an image
   * @param imageName String represents the name of the image that the user loaded/created.
   * @param destName String represents the desired name of the image to be brightened.
   */
  public Brighten(int increment, String imageName, String destName) {
    super(imageName, destName);
    this.increment = increment;
  }

  // This method will call the model's brightenOrDarkenBy and will pass in the fields of this class.
  @Override
  public void execute(EnhancedModel m) throws IllegalArgumentException {
    m.brightenOrDarkenBy(this.increment, this.imageName,
            this.destName);
  }
}
