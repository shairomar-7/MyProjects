package imageprocessor.controller;

/**
 * Interface represents the various operations that the Controller must support for this processor.
 * The various methods involve the basics such as: exitting the program, loading an image, and
 * saving an image. The other methods involve setting a command with the given String command,
 * downsizing an image based on the given height and width, and executing
 */
public interface Features {

  /**
   * Exits the program. What this method should do is simply call: System.exit(0).
   */
  void exitProgram();

  /**
   * Send the view the given message to display an error message to the user after some invalid
   * input. This function will assume the given string is not null. Any unsuccessful attempt should
   * lead to a respectful and elegant error message, all exceptions are caught.
   * @param message String represents the message to be displayed to the user, typically a java
   *                exception, or even one we manually wrote.
   */
  void setErrorMessage(String message);

  /**
   * Loads an image with the given filePath. This method will attempt to read the file contents,
   * if the file is either a png, jpeg, jpg, bmp, or ppm. This method should make use of a function
   * object of the ImageIOHelper interface, to avoid writing the code all over again. Once the
   * method successfully read the contents, it should send the processed data to the model, and
   * should tell the view to visualize the newly loaded image. Any unsuccessful attempt should
   * lead to a respectful and elegant error message, all exceptions are caught.
   * @param filePath String represents the absolute or relative path of an image file to be read.
   *
   */
  void loadImage(String filePath);

  /**
   * Saves an image with the given filePath. This method will attempt to write the data retrieved
   * from the model to a file with the given file path, only if the image format is one of: png,
   * jpeg,jpg,bmp, and ppm. Any unsuccessful attempt should lead to a respectful and elegant
   * error message, all exceptions are caught.
   * @param filePath String represents the path of file of the image to be saved.
   */
  void saveImage(String filePath);

  /**
   * Downscales an image with the given width and height.
   * This method will attempt to downsize the image. If an attempt to upscale is made, the model's
   * method will throw an IllegalArgumentException and the controller will catch that and inform
   * the view to display that error message.
   * @param height int represents the new height of the image
   * @param width int represents the new width of the image
   */
  void downscaleImage(int height, int width);

  /**
   * Executes the current command of the controller. By current command, we assume that the
   * controller is somehow storing which command the user has chosen, and we will be able to simply
   * execute the command that the controller is storing. This method should made use of a map
   * that stores all the commands (except downsize), and would just try to get the command (should
   * not be invalid, since you as the developer are setting the commands, and not the user), and
   * execute it. Any unsuccessful attempt should lead to a respectful and elegant
   * error message, all exceptions are caught.
   */
  void execute();

  /**
   * Sets the current controller command to be the given command. This method will be used by the
   * controller after a user chose a command from the list of options in the drop down menu. It
   * will simply set the current command to the given one. We do not expect to be given an invalid
   * command since the user is not typing anything but rather picking from his options.
   * @param command String represents the command to be set!
   */
  void setCommand(String command);
}
