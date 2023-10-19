package imageprocessor.controller.commands;

import imageprocessor.model.EnhancedModel;

/**
 * This interface represents given commands to the controller component of the image processor.
 * This interface's goal is to minimize code in the controller by create a command design pattern.
 * This interface has been changed from the previous submission to now accept an EnhancedModel
 * instead of an IModel. The reason we did this is to avoid having to create a new interface,
 * it just makes more sense to change the signature of the public method. This is safe because
 * the IModel implementation has remained untouched, and the EnhancedModel implementation
 * will extend the old IModel implementation, thus no errors are expected for the old commands.
 */
public interface ImageCommand {

  /**
   * Allows the user to execute a command on the enhanced model interface.
   * This method will be given a model, and will the corresponding method of the interface based
   * on the command.
   * @param m EnhancedModel represents the model that this command will be executed on.
   * @throws IllegalArgumentException if the given arguments of this interface's subclasses
   *          are invalid and the model throws the exception through the method being used.
   */
  void execute(EnhancedModel m) throws IllegalArgumentException;
}
