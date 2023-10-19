package cs3500.marblesolitaire.view;

import java.io.IOException;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

/**
 * Abstract View class, represents the class that will lift off similar chunks of code.
 * This abstract class implements the MarbleSolitaireView, and so now, the subclasses that were
 * implementing the MarbleSolitaireView, will now instead extend this AbstractView class.
 * Basically, the renderBoard, renderMessage, and toString were exactly the same in the subclasses,
 * so now, it is implemented in this abstract class to reduce code duplication.
 */
public abstract class AbstractView implements MarbleSolitaireView {
  protected final MarbleSolitaireModelState state;
  protected StringBuilder board;
  protected final int boardSize;
  protected final int armThickness;
  protected final Appendable out;

  /**
   * Constructs an Abstract View to store the commonality between the view subclasses.
   * This constructor is common for the TriangleSolitaireTextView and MarbleSolitaireTextView.
   * @param state MarbleSolitaireModelState represents the state of the MarbleSolitaire game.
   * @param destination Appendable represents the output destination for this view.
   * @throws IllegalArgumentException if given a null state or null destination.
   */
  protected AbstractView(MarbleSolitaireModelState state, Appendable destination)
          throws IllegalArgumentException {
    if (state == null || destination == null) {
      throw new IllegalArgumentException("Given argument is null! Either destination or state.");
    }
    this.state = state;
    this.out = destination;
    this.boardSize = this.state.getBoardSize();
    this.armThickness = (int) ((this.boardSize + 2) / 3);
  }

  /**
   * Constructs an Abstract View to store the commonality between the view subclasses.
   * This constructor is common for the TriangleSolitaireTextView and MarbleSolitaireTextView.
   * @param state MarbleSolitaireModelState represents the state of the MarbleSolitaire game.
   * @throws IllegalArgumentException if given a null state.
   */
  public AbstractView(MarbleSolitaireModelState state)
          throws IllegalArgumentException {
    if (state == null) {
      throw new IllegalArgumentException("Given argument is null! Either destination or state.");
    }
    this.state = state;
    this.out = System.out;
    this.boardSize = this.state.getBoardSize();
    this.armThickness = (int) ((this.boardSize + 2) / 3);
  }


  /**
   * Return a string that represents the current state of the board. The
   * string should have one line per row of the game board. Each slot on the
   * game board is a single character (O, _ or space for a marble, empty and
   * invalid position respectively). Slots in a row should be separated by a
   * space. Each row has no space before the first slot and after the last slot.
   * The reason why this is implemented in the abstract class is that it works for all
   * MarbleSolitaireModel implementations.
   *
   * @return the game state as a string representation
   */
  @Override
  public String toString() {
    this.board = new StringBuilder();
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        MarbleSolitaireModelState.SlotState state = this.state.getSlotAt(i, j);
        if (state == MarbleSolitaireModelState.SlotState.Empty) {
          this.board.append("_");
        } else if (state == MarbleSolitaireModelState.SlotState.Marble) {
          this.board.append("O");
        } else if (state == MarbleSolitaireModelState.SlotState.Invalid
                && j < this.boardSize - this.armThickness) {
          this.board.append(" ");
        }
        if (this.checkPos(i, j)) {
          this.board.append(" ");
        }
      }
      if (i != (this.boardSize - 1)) {
        this.board.append("\n");
      }
    }
    return this.board.toString();
  }


  /**
   * Checks if the given position is not at any of the right edges of the board.
   *
   * @param i int represents the row number of a certain marble
   * @param j int represents the col number of a certain marble
   * @return whether the given position is not at any of the right edges of the board.
   */
  protected abstract boolean checkPos(int i, int j);

  /**
   * Render the board to the provided data destination. The board should be rendered exactly
   * in the format produced by the toString method above
   *
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  public void renderBoard() throws IOException {
    this.out.append(this.toString() + "\n");
  }

  /**
   * Render a specific message to the provided data destination.
   *
   * @param message the message to be transmitted
   * @throws IOException if transmission of the board to the provided data destination fails
   */
  public void renderMessage(String message) throws IOException {
    this.out.append(message);
  }
}
