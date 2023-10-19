package cs3500.marblesolitaire.model.hw02;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to build the board (the list of marbles) based on the game parameters.
 * These parameters are: the arm thickness, and the position of the initial empty slot.
 */
public class BoardBuilder {

  /**
   * This method will check if the pos is invalid, yet not beyond the board, and will
   * allow us to set our marbles' state as Invalid.
   * If we are checking if the pos is invalid for an english, then just check if the
   * column is greater than (boardSize - armThicnkess) or column is less than (armThickness - 1)
   * AND the row is either greater than (boardSize - armThickness) or row is less than armThickness
   * -1. If we are checking if the pos is invalid for a european, then just check if the
   * the row number is less than thicknessMinus1 and the column is less than thicknessMinus1 - row,
   * Or, that the col is greater than sizeMinusThickness + row. If the row number is greater than
   * sizeMinusThickness and the col is less than row - sizeMinutThickness or the column is
   * greater or equal to 2*boardSize - row - armThickness. If any of these conditions are met,
   * then we know the marble with the given coordinates must be of Invalid state.
   *
   * @param a         int represents the armThickness of the MarbleSolitaireModel
   * @param i         int represents the row number of the marble we are checking for invalidity.
   * @param j         int represents the col number of the marble we are checking for invalidity.
   * @param isEnglish boolean telling the function whether to compute the english solitaire
   *                  logic, or the european solitaire logic, which is quite different indeed.
   * @return a boolean telling us whether to set the marble with matching coordinates to Invalid
   *         State.
   */
  private boolean checkPosInvalid(int a, int i, int j, boolean isEnglish) {
    int boardSize = 3 * a - 2;
    int thicknessMinus1 = a - 1;
    int sizeMinusThickness = boardSize - a;
    if (!isEnglish) {
      return (i < thicknessMinus1 && ((j < thicknessMinus1 - i) || j > sizeMinusThickness + i))
              || ((i > sizeMinusThickness) && ((j < i - sizeMinusThickness)
              || j >= (2 * boardSize - i - a)));
    } else {
      return ((j > boardSize - a || j < a - 1) && ((i > boardSize - a) || (i < a - 1)));
    }
  }


  /**
   * Builds the Arraylist of marbles based on the game parameters and the type of game.
   * This method only works for an English/European Solitaire model, not a Triangle!
   * The Marble's state is determined by its position on the board (marble/invalid).
   * This function will have a nested for loop until each i (row) and j (col) are greater or
   * equal to the boardSize, and we will set each marble with the with i, j coordinates to a
   * certain state based on whether the position is Invalid but not beyond the board, or if the pos
   * is a Marble. This is determined for an English Solitaire model, and a European
   * Solitaire model, since the logic slightly varies.
   *
   * @param a         int represents the armThickness of the english solitaire game
   * @param isEnglish boolean tells us whether we are building the european or english board.
   * @return List of marble constructed based on its position on the board.
   */
  private List<Marble> buildMarbles(int a, boolean isEnglish) {
    int boardSize = 3 * a - 2; // should be fine assuming count <= limit of int
    List<Marble> marbles = new ArrayList<Marble>();
    for (int i = 0; i < boardSize; i++) { // i -> row
      for (int j = 0; j < boardSize; j++) { // j -> col
        Position p = new Position(i, j);
        if (this.checkPosInvalid(a, i, j, isEnglish)) {
          marbles.add(new Marble(p, MarbleSolitaireModelState.SlotState.Invalid));
        } else {
          marbles.add(new Marble(p, MarbleSolitaireModelState.SlotState.Marble));
        }
      }
    }
    return marbles;
  }

  /**
   * Builds the Arraylist of marbles based on the game parameters (size/width/arm thickness).
   * This method only works for a Triangle Solitaire model, not a European/English!
   * The Marble's state is determined by its position on the board (marble/invalid).
   * This function will have a nested for loop until each i (row) and j (col) are greater or
   * equal to the boardSize, and we will set each marble with the with i, j coordinates to a
   * certain state based on whether the position is Invalid but not beyond the board, or if the pos
   * is a Marble. This is determined for an English Solitaire model, and a European
   * Solitaire model, since the logic slightly varies.
   *
   * @param a int represents the armThickness of the english solitaire game
   * @return List of marble constructed based on its position on the board.
   */
  private List<Marble> buildTriangleMarbles(int a) throws IllegalArgumentException {
    if (a < 1) {
      throw new IllegalArgumentException("given arm thickness is invalid!");
    }
    List<Marble> result = new ArrayList<>();
    for (int i = 0; i < a; i++) { // i -> row
      for (int j = 0; j < i + 1; j++) { // j -> col
        Marble m = new Marble(new Position(j, i), MarbleSolitaireModelState.SlotState.Marble);
        result.add(m);
      }
    }
    return result;
  }

  /**
   * Builds the TriangleSolitaireModel's board by passing in the armThickness, and empty slot coord.
   * Start off by building the list of marbles by calling the buildTriangleMarbles helper method.
   * Then, iterate over the result, and check if the position is equal to the given empty slot,
   * then just set that marble's state to empty. If we couldn't find the marble with matching
   * coordinates, we throw an IllegalArgumentException. If we did, we return the mutated list of
   * marbles.
   *
   * @param a    int represents the width of the triangle solitaire model.
   * @param sRow int represents the row num of initial empty slot of the triangle solitaire model.
   * @param sCol int represents the col num of initial empty slot of the triangle solitaire model.
   * @return a List of marbles for the TriangleSolitaireModel based on the given size, and the
   *        coordinates of the initial empty slot.
   * @throws IllegalArgumentException if given a width less than 1 or if the empty slot coordiantes
   *                                  are invalid.
   */
  public List<Marble> buildTriangleBoard(int a, int sRow, int sCol)
          throws IllegalArgumentException {
    List<Marble> result = this.buildTriangleMarbles(a);
    boolean soFar = false;
    for (Marble m : result) {
      if (m.isPosEqual(sRow, sCol)) {
        if (m.getState().equals("marble")) {
          m.set("empty");
          soFar = true;
        }
      }
    }
    if (!soFar) {
      throw new IllegalArgumentException("Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
    return result;
  }


  /**
   * ArrayList of marble, representing the marbles on an englishSolitaire game.
   * Start off by building the list of marbles by calling the buildMarbles helper method.
   * Then, iterate over the result, and check if the position is equal to the given empty slot,
   * then just set that marble's state to empty. If we couldn't find the marble with matching
   * coordinates, we throw an IllegalArgumentException. If we did, we return the mutated list of
   * marbles.
   *
   * @param a         int represents the armThickness of an englishSolitaire game
   * @param sRow      int represents the row number of the empty slot of the game
   * @param sCol      int represents the column number of the empty slot of the game
   * @param isEnglish boolean tells us whether to build board for an english solitaire model or
   *                  a european solitaire model.
   * @return List of Marble that corresponds to the inputs representing game's initial state
   * @throws IllegalArgumentException if the given emptySlot coordinates are invalid (does not
   *                                  belong to the list of valid positions),
   *                                  or armThickness is not a pos odd integer greater
   *                                  or equal to 3.
   */
  public List<Marble> buildBoard(int a, int sRow, int sCol, boolean isEnglish)
          throws IllegalArgumentException {
    List<Marble> result = this.buildMarbles(a, isEnglish);
    boolean soFar = false;
    if (a % 2 == 0 || a < 3) {
      throw new IllegalArgumentException("Invalid armThickness given! Should be pos odd >=3!!");
    }
    for (Marble m : result) {
      if (m.isPosEqual(sRow, sCol)) {
        if (m.getState().equals("marble")) {
          m.set("empty");
          soFar = true;
        }
      }
    }
    if (!soFar) {
      throw new IllegalArgumentException("Invalid empty cell position (" + sRow + "," + sCol + ")");
    }
    return result;
  }
}

