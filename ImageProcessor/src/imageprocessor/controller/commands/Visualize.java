package imageprocessor.controller.commands;

import imageprocessor.model.EnhancedModel;

/**
 * Visualize Command, implements the ImageCommand interface.
 * This command is responsible for visualizing component of an image (luma, intensity, value, rgb).
 * This command ultimately creates different types of greyscale images.
 */
public class Visualize extends AbstractCommand implements ImageCommand {
  private final String component;

  /**
   * Constructs a Visualize command.
   * With the given fileName (image name the user created or loaded), desired destination name of
   * the image to be "visualized" and the component (type of greyscale) of the image.
   * @param imageName String represents the image name that the user created/loaded and is the image
   *                 that this command will manipulate and store as a new image in the database.
   * @param destName String represents the desired name of the image to be "visualized".
   *                 This new image will be stored in the model's history.
   * @param component String represents the component of the image to be "visualized".
   *          (red component, blue component, green component,
   *                  value, intensity, or luma)
   */
  public Visualize(String imageName, String destName, String component) {
    super(imageName, destName);
    this.component = component;
  }


  // This method will call the given model's visualizeComponent with the given fields of this
  // command class. It will throw an exception if the component is invalid or the image name is not
  // found in the model's history. Valid args for the component are: luma,intensity,value,r,g,b.
  @Override
  public void execute(EnhancedModel m) throws IllegalArgumentException {
    m.visualizeComponent(component, imageName, destName);
  }
}
