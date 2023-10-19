package imageprocessor.controller.commands;

import imageprocessor.model.EnhancedModel;

/**
 * Flip Command, implements the ImageCommand interface.
 * This command is used for flipping an image horizontally or vertically.
 */
public class Flip extends AbstractCommand implements ImageCommand {
  private final boolean v;

  /**
   * Constructor that allows the user to pass in a model, and flip
   * it in the specified direction. IF v is true, it does a vertical flip.
   * If not, it does a horizontal flip.
   */
  public Flip(String imageName, String destName, boolean v) {
    super(imageName, destName);
    this.v = v;
  }

  /**
   * Executes the flip(Boolean, String, String) method in the EnhancedModel interface.
   */
  public void execute(EnhancedModel m) throws IllegalArgumentException {
    m.flip(v, imageName, destName);
  }
}
