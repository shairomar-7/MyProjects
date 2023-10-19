package imageprocessor.view;

import java.io.IOException;

/**
 * IView interface, represents the view component of this Image Processor.
 * This view interface only provides one method, which is used to render messages as text and
 * to output that message to a destination (Appendable).
 */
public interface IView {

  /**
   * Void methods renders a given message and appends it to its output destination.
   * This method should not (will not) add a new line!
   *
   * @param message String represents the message to be rendered and
   *                added to the output destination.
   * @throws IOException if there was some IO error when trying to append the message to this view's
   *                     output destination.
   */
  void renderMessage(String message) throws IOException;
}
