package imageprocessor.controller;

/**
 * IController interface. This interface represents the Controller component of this application.
 * The IController interface provides a method to instantiate the image processor and to keep
 * it running as long as the user has not intentionally quit the app.
 * Different Image formats should be supported by this interface, it should not be specific to PPM.
 */
public interface IController {

  /**
   * Instantiates the image processor and keeps it running as long as the user has no quit/exited
   * the image processor.
   * @throws IllegalStateException if the controller has no more inputs to read, yet the user
   *        has not quit the processor.
   */
  void goImageProcessor() throws IllegalStateException;
}
