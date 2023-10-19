package imageprocessor.view;

import java.awt.image.WritableRenderedImage;
import java.util.Map;
import imageprocessor.controller.Features;

/**
 * IViewGUI interface represents the view interface of the Image Processor.
 * As you might guess from the name, this interface makes sense to be used for a GUI.
 * This interface provides a few methods; one to set the image and to view it, another to add
 * features to the view (basically call back functions), and finally, one to show the histogram.
 * This interface is an extension of the text-based View, inheriting the renderMessage, most
 * likely to inform the user of an invalid input or
 */
public interface IViewGUI extends IView {
  /**
   * When the controller loads, downscales or executes a command, this method will be called
   * to change the image that is currently displayed in the imagePanel, to the newly manipulated
   * image.
   * @param image the image that will now be visible to the user in the gui.
   */
  void setImage(WritableRenderedImage image);

  /**
   * Adds all the necessary action listeners to the gui. In this case, these are the options to
   * load/save, downscale, execute any of the commands, and quit the program. This method also
   * creates a combo box, which contains all the possible commands that the user can execute,
   * and allows them to call these commands on the image.
   * @param features The Features object from which these events are coming from. In this program,
   *                 the user enters input from the controller, and then each actionListener
   *                 will act accordingly depending on what the input is.
   */
  void addFeatures(Features features);

  /**
   * Displays each histogram on the gui. The gui should display histograms for all the
   * red, green, blue, and intensity value components of a certain image.
   * @param map this maps contains a list of integers mapped to an array of map.
   *            The array of maps contains the information for the histograms. Each
   *            map has the value components for each image mapped to the number of times
   *            they appear in the image.
   */
  void showHistogram(Map<Integer, Integer>[] map);
}
