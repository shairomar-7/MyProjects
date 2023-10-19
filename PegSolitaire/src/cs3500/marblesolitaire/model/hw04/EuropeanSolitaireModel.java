package cs3500.marblesolitaire.model.hw04;

import java.util.List;

import cs3500.marblesolitaire.model.hw02.BoardBuilder;
import cs3500.marblesolitaire.model.hw02.Marble;


/**
 * Represents a european solitaire game's model.
 * This model allows for multiple constructions of the game, thus enabling flexibility for the
 * user. This model keeps track of the state of the game, and can modify the board
 * after calling the move method with valid positions of the from and to marbles, and the marble
 * in between MUST be EMPTY.
 */
public class EuropeanSolitaireModel extends AbstractModel {

  /**
   * Constructs the traditional european solitaire game with the below game properties.
   * The board forms an octagonal shape with sides of size 3, and the empty slot at the center.
   */
  public EuropeanSolitaireModel() {
    this(3, 3, 3);
  }

  /**
   * Constructs a non-traditional european solitaire game with the below game properties.
   * The board forms an octagonal shape with the given size, and the empty slot at the center.
   * @param armThickness int represents the size of the board's sides.
   * @throws IllegalArgumentException if the given armThickness is not odd integer >= 3.
   */
  public EuropeanSolitaireModel(int armThickness) throws IllegalArgumentException {
    this(armThickness, (int) ((1.5) * (armThickness - 1)), (int) ((1.5) * (armThickness - 1)));
  }

  /**
   * Constructs a non-traditional european solitaire game with the below game properties.
   * The board forms an octagonal shape with the given size, and the given coordinates of the
   * initial empty slot.
   * @param armThickness int represents the size of the board's sides.
   * @param sRow int represents the row number of the initial empty slot.
   * @param sCol int represents the column number of the initial empty slot.
   * @throws IllegalArgumentException if the given arm thickness is not an odd integer >= 3 or
   *        the given coordinates of the empty slot are not valid.
   *        They are considered invalid if beyond the board's dimension, or given a position
   *        where the marble's state is INVALID.
   */
  public EuropeanSolitaireModel(int armThickness, int sRow, int sCol)
          throws IllegalArgumentException {
    super(armThickness, sRow, sCol);
  }

  /**
   * Constructs a non-traditional european solitaire game with the below game properties.
   * The board forms an octagonal shape with side size of 3, and the given coordinates of the
   * initial empty slot.
   * @param sRow int represents the row number of the initial empty slot.
   * @param sCol int represents the column number of the initial empty slot.
   * @throws IllegalArgumentException if the given coordinates of the empty slot are not valid.
   *        They are considered invalid if beyond the board's dimension, or given a position
   *        where the marble's state is INVALID.
   */
  public EuropeanSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    this(3, sRow, sCol);
  }

  @Override
  protected List<Marble> buildBoard(int armThickness, int sRow, int sCol)
          throws IllegalArgumentException {
    return new BoardBuilder().buildBoard(armThickness, sRow, sCol, false);
  }
}
