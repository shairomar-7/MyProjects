package imageprocessor.view;

import java.io.IOException;

/**
 * View class, represents the view component of this Image Processor.
 * This class is responsible for visualizing text for our client. The text might be a message
 * informing the user of a successful save/load, or if the user inputs an invalid command, or
 * invalid arguments, or if simply the client would like to render a menu of instructions.
 * The possibilities are endless...
 * Invariants:
 * The Appendable destination of this view is never null, this is because
 */
public class View implements IView {
  private final Appendable destination;

  /**
   * Constructs a view with the given output destination.
   * This constructor will be used for testing, to access the given Appendable, and check if
   * that is truly what we expected the output to contain.
   *
   * @param destination Appendable represents the output destination of this view.
   * @throws IllegalArgumentException if the given destination is null!
   */
  public View(Appendable destination) throws IllegalArgumentException {
    if (destination == null) {
      throw new IllegalArgumentException("State and destination must be given!");
    }
    this.destination = destination;
  }

  /**
   * Constructs a view with no given arguments.
   * This constructor will be used for running the program, and not for testing.
   * This constructor will set this view's output destination to be System.out
   */
  public View() {
    this.destination = System.out;
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.destination.append(message);
  }
}
