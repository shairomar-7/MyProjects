package imageprocessor.controller.commands;

/**
 * AbstractCommand, represents the commonality between the different commands of image processor.
 * What's common is the fact that all commands take in an imageName, and a destName, thus, the
 * fields are the same, and the constructor is the same too.
 */
public abstract class AbstractCommand implements ImageCommand {
  protected final String imageName;
  protected final String destName;

  /**
   * Constructs an AbstractCommand, which represents the abstraction of all commands.
   * @param imageName String represents the name of the image to apply the command on.
   * @param destName String represents the desired name of result of applying this command on the
   *                 given image.
   */
  protected AbstractCommand(String imageName, String destName) {
    this.imageName = imageName;
    this.destName = destName;
  }
}
