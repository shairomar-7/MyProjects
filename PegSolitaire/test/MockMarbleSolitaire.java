import java.io.IOException;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;

/**
 * Mock Marble Solitaire Model, serves as a tool to test the controller.
 */
public class MockMarbleSolitaire implements MarbleSolitaireModel {
  private final Appendable log;

  /**
   * An imposter MarbleSolitaireModel, with an appendable passed and to be retrieved later.
   * Later meaning probably after a method was called by controller and inputs are recorded.
   * @param log StringBuilder represents the appendable that will be used for testing purposes.
   *            WARNING TO THE IGNORANT CLIENT: DO NOT CALL THIS CLASS, USED FOR TESTINGGGGGGG!
   */
  MockMarbleSolitaire(Appendable log) {
    this.log = log;
  }

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol) throws IllegalArgumentException {
    try {
      this.log.append(String.format("fromRow = %d, fromCol= %d, toRow = %d, toCol = %d\n",
              fromRow, fromCol, toRow, toCol));
    }
    catch (IOException e) {
      throw new IllegalStateException("something went wrong!");
    }
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int getBoardSize() {
    return 0;
  }

  @Override
  public SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    try {
      this.log.append(String.format("row = %d, col= %d=\n", row, col));
    }
    catch (IOException e) {
      throw new IllegalStateException("something went wrong!");
    }
    return SlotState.Invalid;
  }

  @Override
  public int getScore() {
    return 0;
  }
}