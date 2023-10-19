import java.io.IOException;

import cs3500.marblesolitaire.view.MarbleSolitaireView;


/**
 * Mock MarbleSolitaireView, serves as a tool to test the controller's calls to the view interface.
 */
public class MockMarbleSolitaireView implements MarbleSolitaireView {
  private final Appendable log;

  /**
   * An imposter MarbleSolitaireView, with an appendable passed and to be retrieved later.
   * Later meaning probably after a method was called by controller.
   * @param log StringBuilder represents the appendable that will be used for testing purposes.
   *            WARNING TO THE IGNORANT CLIENT: DO NOT CALL THIS CLASS, USED FOR TESTINGGGGGGG!
   */
  MockMarbleSolitaireView(Appendable log) {
    this.log = log;
  }

  @Override
  public void renderBoard() throws IOException {
    this.log.append("render board\n");
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.log.append("render message: " + message);
  }
}
