package cs3500.marblesolitaire.model.hw02;

import java.util.List;

import cs3500.marblesolitaire.model.hw04.AbstractModel;

/**
 * English Solitaire, represents a specific variant of the Marble Solitaire board.
 * It is characterized by the '+' shaped board.
 * This class was changed by: reduce code duplication, so we made it extend an abstract class
 * instead of implementing the MarbleSolitaireModel interface. This allowed us to lift the fields,
 * the bodies of the constructors, and many methods, actually, all methods except the buildBoard.
 * I made my functions more generic by adding helper methods, that can be used in the abstract
 * class by making them abstract, or by implementing them in the AbstractClass. Some of these are:
 * buildBoard (instead of directly building the board through the constructor), tryPossibleMoves,
 * which is used by the isGameOver to see if any move is possible or not, and determineState,
 * which will determine the state of the marble based on a string representation of the state.
 * These functions have now made my code more readable, and safer.
 */
public class EnglishSolitaireModel extends AbstractModel {

  /**
   * This instance of english solitaire represents the traditional EnglishSoltaire game.
   * with an armThickness of 3, and the empty slot being at the center (3,3)
   */
  public EnglishSolitaireModel() {
    this(3, 3, 3);
  }

  /**
   * This instance of english solitaire represents the traditional EnglishSolitaire game.
   * With an armThickness of 3, except the empty slot is given, and not necessarily at center.
   *
   * @param sRow int represents the row number of the empty slot
   * @param sCol int represents the column number of the empty slot
   * @throws IllegalArgumentException if the given coordinates sRow and sCol of the empty slot
   *     are invalid coordinates for an EnglishSolitaire game with armThickness of 3
   */
  public EnglishSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    this(3, sRow, sCol);
  }

  /**
   * This instance of english solitaire represents the game.
   * with a given arm thickness,and the empty slot is at the center.
   *
   * @param armThickness int represents the number of marbles at the top row of the board
   * @throws IllegalArgumentException if the given armThickness is not a odd integer >=3
   */
  public EnglishSolitaireModel(int armThickness) throws IllegalArgumentException {
    this(armThickness, (int) ((1.5) * (armThickness - 1)), (int) ((1.5) * (armThickness - 1)));
  }

  /**
   * This instance of english solitaire represents the game with.
   * a given armthickness, and given coordinates sRow and sCol of the empty slot
   *
   * @param armThickness int represents the number of marbles at the top row of the board
   * @param sRow         int represents the row number of the empty slot
   * @param sCol         int represents the column number of the empty slot
   * @throws IllegalArgumentException if the given armThickness is not a odd integer >=3, or
   *     the given coordinates sRow and sCol of the empty slot are invalid coordinates of the board.
   */
  public EnglishSolitaireModel(int armThickness, int sRow, int sCol)
          throws IllegalArgumentException {
    super(armThickness, sRow, sCol);
  }

  @Override
  protected List<Marble> buildBoard(int armThickness, int sRow, int sCol)
          throws IllegalArgumentException {
    return new BoardBuilder().buildBoard(armThickness, sRow, sCol, true);
  }
}
