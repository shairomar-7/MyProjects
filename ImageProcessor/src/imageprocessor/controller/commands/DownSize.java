package imageprocessor.controller.commands;

import imageprocessor.model.EnhancedModel;

/**
 * DownSize command, class used to execute the downsize/downscale image command.
 * This class extends the AbstractCommand for code reduction and is A ImageCommand.
 */
public class DownSize extends AbstractCommand implements ImageCommand {
  private final int height;
  private final int width;

  /**
   * Constructs an AbstractCommand, which represents the abstraction of all commands.
   *
   * @param imageName String represents the name of the image to apply the command on.
   * @param destName  String represents the desired name of result of applying this command on the
   * @param height int represents the desired height of the new image to be downsized.
   * @param width int represents the desired width of the new image to be downsized.
   */
  public DownSize(String imageName, String destName, int height, int width) {
    super(imageName, destName);
    this.height = height;
    this.width = width;
  }

  @Override
  public void execute(EnhancedModel m) throws IllegalArgumentException {
    m.downscaleImage(imageName, destName, height, width);
  }
}
