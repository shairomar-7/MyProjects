package cs3500.marblesolitaire.view;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

/**
 * Represents the viewer for the marble solitaire game.
 * The way I changed this class from the previous submission is by making this class extend
 * the AbstractView class instead of implementing MarbleSolitaireView interface. This fields of
 * this class were lifted to the Abstract class, so was the body of the constructors, and all the
 * methods except the checkPos method. The only change I made to this class is by making my
 * checkPos logic generic to be applied to both the EnglishSolitaire and EuropeanSolitaire games.
 */
public class MarbleSolitaireTextView extends AbstractView {

  /**
   * Constructs a viewer for the marble solitaire game, ie, a MarbleSolitaireTextView.
   * @param state MarbleSolitaireModelState represents the state of the game.
   * @param destination Appendable is used by this viewer as a destination.
   * @throws IllegalArgumentException if the given destination or state is null
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState state, Appendable destination)
          throws IllegalArgumentException {
    super(state, destination);
  }

  /**
   * Constructs the viewer for the Marble Solitaire game.
   *
   * @param state MarbleSolitaireModelState represents the state of the marble solitaire game
   * @throws IllegalArgumentException if the given state is null.
   */
  public MarbleSolitaireTextView(MarbleSolitaireModelState state) throws IllegalArgumentException {
    super(state);
  }

  /**
   * The reason why this is OverRidden is because for the EnglishSolitaire and the European,
   * what you basically want to do is check if the marble to the right of this current marble's
   * coordinates is an invalid. If it is, then checkPos should return false,
   * and we will not add a space. If it is not, then we return whether this marble's coordinates
   * are at the far right side of the board.
   * @param i int represents the row number of a certain marble
   * @param j int represents the column number of a certain marble
   * @return a boolean telling us whether the "certain" marble's coordinates allows us to add a
   *        a space after this "certain" marble, because remember!, we are trying to prevent
   *        an extra space to the right edges of this MarbleSolitaire UI.
   */
  @Override
  protected boolean checkPos(int i, int j) { // i row , j col
    if (j < this.boardSize - 1 && j >= this.boardSize - armThickness) {
      return this.state.getSlotAt(i, j + 1) != MarbleSolitaireModelState.SlotState.Invalid;
    }
    return j != this.boardSize - 1;
  }
}
