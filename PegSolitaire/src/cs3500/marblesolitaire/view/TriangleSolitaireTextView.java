package cs3500.marblesolitaire.view;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

/**
 * TriangleSolitaire View class, represents the view component of this Marble Solitaire game.
 * This class will provide a way for our client to visualize the game, simply by accessing
 * the MarbleSolitaireModel's state. This class extends the AbstractView to reduce code duplication
 * with the MarbleSolitaireTextView. The fields are lifted, and the renderBoard and renderMessage
 * are also lifted. The toString and checkPos are overridden, please refer to them for further
 * information.
 */
public class TriangleSolitaireTextView extends AbstractView {


  /**
   * Constructs a TriangleSolitaireTextView with the given destination, to output the results.
   * And a given marble solitaire model's state, to be able to render the game
   * according to its current state (marbles, score, size).
   * @param state MarbleSolitaireModelState represents the state of the marble solitaire game.
   * @param destination Appendable represents the output destination of the results
   *                   of this class's methods.
   * @throws IllegalArgumentException if given a null state or destination, unacceptable!
   */
  public TriangleSolitaireTextView(MarbleSolitaireModelState state, Appendable destination)
          throws IllegalArgumentException {
    super(state, destination);
  }


  /**
   * Constructs a TriangleSolitaireTextView with the output destination being System.out.
   * And a given marble solitaire model's state, to be able to render the game
   * according to its current state (marbles, score, size).
   * @param state MarbleSolitaireModelState represents the state of the marble solitaire game.
   * @throws IllegalArgumentException if given a null state, very unacceptable!
   */
  public TriangleSolitaireTextView(MarbleSolitaireModelState state)
          throws IllegalArgumentException {
    super(state, System.out);
  }


  /**
   * Return a string that represents the current state of the board. The
   * string should have one line per row of the game board. Each slot on the
   * game board is a single character (O, _ or space for a marble, empty and
   * invalid position respectively). Slots in a row should be separated by a
   * space. Each row has no space before the first slot and after the last slot.
   * The reason why this is overridden is because the logic I use in here is different from
   * the MarbleSolitaireTextView. What I basically do is add n number of spaces before the
   * first column, where n is the (boardSize - 1 - row). Also, my for loop is different
   * since it only iterates from 0 to row (inclusive), because we want a max number of columns
   * equal to the current row number. This will ultimately create the triangle shape.
   * @return the game state as a string.
   */
  @Override
  public String toString() {
    this.board = new StringBuilder();
    StringBuilder spaces = new StringBuilder();
    for (int j = 0; j < this.boardSize - 1; j ++) {
      spaces.append(" ");
    }
    String strSpaces = spaces.toString();
    for (int i = 0; i < this.boardSize; i++) { // row
      for (int j = 0; j < i + 1; j++) { //col
        MarbleSolitaireModelState.SlotState state = this.state.getSlotAt(i, j);
        if (j == 0 && strSpaces.length() != 0) {
          this.board.append(strSpaces);
          strSpaces = strSpaces.substring(1);
        }
        if (state == MarbleSolitaireModelState.SlotState.Empty) {
          this.board.append("_");
        } else if (state == MarbleSolitaireModelState.SlotState.Marble) {
          this.board.append("O");
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
   * The reason why this is overridden is because the logic is slightly different
   * from the MarbleSolitaireView subclass of the AbstractView. This logic basically returns
   * whether the marble's row is not equal to the column. For a triangle solitaire, this
   * would basically be the right diagonal edge. This is used to avoid adding spaces to the
   * right edges of the MarbleSolitaire game UI.
   * @param i int represents the row number of a certain marble
   * @param j int represents the col number of a certain marble
   * @return a boolean telling us whether the "certain" marble's row is equal to the column.
   */
  @Override
  protected boolean checkPos(int i, int j) {
    return i != j;
  }
}
