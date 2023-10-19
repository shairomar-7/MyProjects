package cs3500.marblesolitaire.model.hw04;

import java.util.List;

import cs3500.marblesolitaire.model.hw02.Marble;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.Position;

/**
 * Abstract class for the MarbleSolitaireModel.
 * This class is helpful in reducing duplicate code between the different subclasses of the marble
 * solitaire model interface. The common fields are lifted, which includes the size of the sides,
 * the row number and column number of the initial empty slot, as well as the array of marbles,
 * which contains the different Marbles (state, and position). This class is also helpful in
 * abstracting out the constructors, by calling the abstract buildBoard method which builds the
 * list of marbles depending on the game properties and the type of marble solitaire.
 */
public abstract class AbstractModel implements MarbleSolitaireModel {
  protected final int armThickness;
  protected List<Marble> marbles;

  /**
   * Constructs an AbstractModel with the given game properties.
   * This model represents the commonality between different MarbleSolitaire models.
   *
   * @param armThickness int represents the size of the size of the board.
   * @param sRow         int represents the row num of the initial empty slot.
   * @param sCol         int represents the column num of the initial empty slot.
   */
  public AbstractModel(int armThickness, int sRow, int sCol) {
    this.armThickness = armThickness;
    this.marbles = this.buildBoard(this.armThickness, sRow, sCol);
  }


  /**
   * Computes a list of ints representing the index of the 3 marbles involved in a "move".
   *
   * @param fromRow int represents the from's row number
   * @param fromCol int represents the from's col number
   * @param toRow   int represents the to's row number
   * @param toCol   int represents the to's col number
   * @return int[] of size 3 representing fromIndex, inBetweenIndex, and toIndex repectively.
   *        returns an empty list of int if this is not a valid move (from -> to)
   * @throws IllegalArgumentException if the given from and to coordinates are the same
   */
  protected int[] checkFromToBetween(int fromRow, int fromCol, int toRow, int toCol)
          throws IllegalArgumentException {
    Position pos1 = new Position(fromRow, fromCol);
    Position pos2 = new Position(toRow, toCol);
    int listSize = this.marbles.size();
    if (pos1.equals(pos2)) {
      throw new IllegalArgumentException("from and to can't be the same, idiot!");
    }
    int fromIndex = this.computeMarbleIndex(fromRow, fromCol);
    int toIndex = this.computeMarbleIndex(toRow, toCol);
    if (this.areIndicesValid(fromIndex, toIndex, listSize)) {
      if (this.marbles.get(fromIndex).getState().equals("marble") &&
              this.marbles.get(toIndex).getState().equals("empty") &&
              this.checkDis(pos1, pos2)) {
        int inBetweenIndex = this.getInBetween(fromRow, fromCol, toRow, toCol);
        if (this.marbles.get(inBetweenIndex).getState().equals("marble")) {
          return new int[]{fromIndex, inBetweenIndex, toIndex};
        }
      }
    }
    return new int[]{};
  }


  /**
   * Checks if the given indices are valid based on the listSize representing a list's length.
   * The indices are valid if not negative and not greater or equal than the listSize.
   *
   * @param fromIndex int represents the index of the marble to be jumped from.
   * @param toIndex   int represents the index of the marble to be jumped to.
   * @param listSize  int represents the length of the list of marbles.
   * @return whether the indices are valid or not.
   */
  protected boolean areIndicesValid(int fromIndex, int toIndex, int listSize) {
    return (fromIndex < listSize && toIndex < listSize && fromIndex != -1 && toIndex != -1);
  }


  /**
   * Checks if the distance between pos1 and pos2 is valid for the move. For the MarbleSolitaire.
   * model and european, it would be a difference of 2 either horizontally or vertically.
   * For the Triangular, it would be a difference of 2 either horizontally and/or vertically.
   *
   * @param pos1 Position represents x marble's position.
   * @param pos2 Position represents y marble's position.
   * @return a boolean telling us whether the position is valid for a move or not, of course,
   *        based on the which MarbleSoltaire model we are talking about.
   */
  protected boolean checkDis(Position pos1, Position pos2) {
    return pos1.checkDistance(pos2);
  }

  /**
   * Computes some index math to determine the marble's index based on the given coordinates of
   * a certain marble. This function assumes the list of marbles is sorted by row, and by col.
   * This logic does not apply for TriangularSolitaire, and you will have to Override it and
   * probably just iterate the list of marbles and get the index of the marble with matching pos.
   *
   * @param row int represents the row number of a certain marble.
   * @param col int represents the column number of a certain marble.
   * @return an int representing the index of the marble in this list of marbles.
   */
  protected int computeMarbleIndex(int row, int col) {
    return col * this.getBoardSize() + row;
  }

  @Override
  public void move(int fromRow, int fromCol, int toRow, int toCol)
          throws IllegalArgumentException {
    int[] indices = this.checkFromToBetween(fromRow, fromCol, toRow, toCol);
    if (indices.length == 0) {
      throw new IllegalArgumentException("Either from is not a Marble, "
              + "or to is not Empty, or distance is invalid!");
    }
    this.updateBoard(indices[0], indices[1], indices[2]);
  }


  /**
   * Determines the state based on the given string representation of the state.
   * This is used to avoid any mutation of the state of the marble. I could have used a getter,
   * and while that would have reduced the code, it is a risky move, and definelty not a safe move.
   *
   * @param state String represents the state of marble.
   * @return SlotState enum based on the given string.
   * @throws IllegalArgumentException if the given string representation of the state is
   *                                  not one of the following: "marble", "empty", "invalid".
   */
  protected SlotState determineState(String state) throws IllegalArgumentException {
    if (state.equals("marble")) {
      return SlotState.Marble;
    } else if (state.equals("empty")) {
      return SlotState.Empty;
    } else if (state.equals("invalid")) {
      return SlotState.Invalid;
    }
    throw new IllegalArgumentException("state given to determineState is invalid!");
  }

  /**
   * Checks if the given row and col of the marble is on our board's corners.
   *
   * @param row int represents the row number of a marble
   * @param col int represents the col number of a marble
   * @return 1 if marble is at upper edge, 2 right edge, 3 left edge, 4 bottom edge, else -50
   */
  protected int checkEdge(int row, int col) {
    int size = this.getBoardSize();
    if (row == 0) {
      return 1;
    } else if (col == size) {
      return 2;
    } else if (col == 0) {
      return 3;
    } else if (row == size - 1) {
      return 4;
    } else {
      return -50;
    }
  }

  @Override
  public int getBoardSize() {
    return 3 * this.armThickness - 2;
  }

  @Override
  public int getScore() {
    int score = 0;
    for (Marble m : this.marbles) {
      if (m.getState().equals("marble")) {
        score++;
      }
    }
    return score;
  }

  /**
   * Returns whether the given position has a negative x or y.
   *
   * @param row int represents the row num of the marble
   * @param col int represents the col num of the marble
   * @return true if either one of the given coordinates are neg, else false
   */
  protected boolean isBeyondBoard(int row, int col) {
    int size = this.getBoardSize();
    return row < 0 || col < 0 || row >= size || col >= size;
  }

  // This function was changed by using helper abstract helper methods that will allow us
  // to lift this method from the subclasses of this abstract class.
  // This methods works by first checking if the given row and col are beyond the board,
  // then it will throw an IllegalArgumentException. If not, it will then compute the index of the
  // marble with the given coordinates. If index is -1, it returns Invalid, if not, it will
  // call a method to determine state from the marble's getState string representation of state.
  @Override
  public SlotState getSlotAt(int row, int col) throws IllegalArgumentException {
    if (this.isBeyondBoard(row, col)) {
      throw new IllegalArgumentException("cell position beyond board dimensions" +
              " (" + row + "," + col + ")");
    }
    int index = this.computeMarbleIndex(row, col);
    if (index == -1) {
      return SlotState.Invalid;
    }
    return this.determineState(this.marbles.get(index).getState());
  }


  // This function will either check that the score is 1, or will iterate over this list of
  // marbles and would compute the 4 possible ways it could move: up, down, right, left.
  // Some constraints are put to make sure that the marble is not on an edge and the marble we are
  // jumping to is not beyond the board's dimensions. If it can make a move, it returns false.
  @Override
  public boolean isGameOver() {
    if (this.getScore() == 0) {
      return true;
    }
    for (int i = 0; i < this.marbles.size(); i++) {
      Marble m = this.marbles.get(i);
      if (m.getState().equals("marble")) {
        int row = m.getRow();
        int col = m.getCol();
        for (int k = 0; k < 4; k++) {
          if (this.tryPossibleMoves(k, row, col)) {
            return false;
          }
        }
      }
    }
    return true;
  }


  /**
   * This method will attempt to make a move from the given row and col, based on the moveDirection.
   * If moveDirection is 0, then up, if 1, then right, if 2, then left, 3, then down.
   * This function can be applied to both European and English, but not triangle.
   *
   * @param moveDirection int represents the direction to be moved (up, down, left, right).
   * @param row           int represents the row number to be attempted to move from.
   * @param col           int represents the col number to be attempted to move from.
   * @return boolean representing whether we were able to make a move, false if we were successful
   */
  protected boolean tryPossibleMoves(int moveDirection, int row, int col) {
    switch (moveDirection) {
      case 0:
        if (this.checkEdge(row, col) != 1 && !this.isBeyondBoard(row - 2, col)) {
          if (this.checkFromToBetween(row, col, row - 2, col).length != 0) {
            return true;
          }
        }
        break;
      case 1:
        if (this.checkEdge(row, col) != 2 && !this.isBeyondBoard(row, col + 2)) {
          if (this.checkFromToBetween(row, col, row, col + 2).length != 0) {
            return true;
          }
        }
        break;
      case 2:
        if (this.checkEdge(row, col) != 3 && !this.isBeyondBoard(row, col - 2)) {
          if (this.checkFromToBetween(row, col, row, col - 2).length != 0) {
            return true;
          }
        }
        break;
      case 3:
        if (this.checkEdge(row, col) != 4 && !this.isBeyondBoard(row + 2, col)) {
          if (this.checkFromToBetween(row, col, row + 2, col).length != 0) {
            return true;
          }
        }
        break;
      default:
        break;
    }
    return false;
  }

  /**
   * Builds the board according to the size of the sides, and the coordinates of the empty slot.
   * Returns an array of marbles containing the position and the state of the marble.
   * This array will ultimately allow us to keep track of the state of the game through a bunch of
   * methods in the MarbleSolitaireModelState and will allows us to mutate the board (ie the list)
   * through a bunch of void methods in the MarbleSolitaireModel.
   *
   * @param armThickness int represents the arm thickness of the marble solitaire board to be built.
   * @param sRow         int represents the row number of the initial empty slot.
   * @param sCol         int represents the column number of the initial empty slot.
   * @return a List representing the baord based on the given arm thickness, and
   *        the coordinates of the initial empty marble.
   * @throws IllegalArgumentException if given an invalid armThickness, or coordinates for the
   *                                  initial empty slot that are invalid (beyond board, or
   *                                  just Invalid).
   */
  protected abstract List<Marble> buildBoard(int armThickness, int sRow, int sCol)
          throws IllegalArgumentException;

  /**
   * EFFECT: Mutates the board according to the move.
   * (the from and inbetween would be empty, and the to becomes a marble)
   * The update method changed slightly from last time, due to making the state of the marble
   * empty. This way, there is no way we could mutate it.
   *
   * @param fromIndex      int represents the index of the marble that will jump
   * @param inBetweenIndex int represents the index of the marble that will be jumped on top of
   * @param toIndex        int represents the index of the marble that will be jumped to/on
   * @param toIndex        int represents the index of the marble that will be jumped to/on
   */
  private void updateBoard(int fromIndex, int inBetweenIndex, int toIndex) {
    Marble from = this.marbles.get(fromIndex);
    Marble to = this.marbles.get(toIndex);
    Marble inBetween = this.marbles.get(inBetweenIndex);
    from.set("empty");
    to.set("marble");
    inBetween.set("empty");
  }

  /**
   * Gets the index of the element inBetween the fromMarble and the toMarble.
   *
   * @param fromRow int represents the row num of the marble that will jump
   * @param fromCol int represents the col num of the marble that will jump
   * @param toRow   int represents the row num of the marble that will be jumped to/on
   * @param toCol   int represents the col num of the marble that will be jumped to/on
   * @return int representing index of marble in between the given positions
   * @throws IllegalArgumentException if the given Position contains an x or y less than 0,
   *                                  or simply, something odd went wrong, hopefully not though!
   */
  private int getInBetween(int fromRow, int fromCol, int toRow, int toCol)
          throws IllegalArgumentException {
    Position pos = this.computeMarbleInBetween(fromRow, fromCol, toRow, toCol);
    int index = this.computeMarbleIndex(pos.getRow(), pos.getCol());
    if (index >= this.marbles.size() || index == -1) {
      throw new IllegalArgumentException("something went wrong, we couldn't find the inBetween!");
    }
    return index;
  }

  /**
   * This method will compute the marble in between the given from and to coordinates.
   * This method will be used for the european solitaire model and the english solitaire model.
   * If the row of the from and to are equal, then based on the columns of the from and to,
   * we get the one in between. If the col of the from and to are equal,
   * then based on the row of the from and to, we get the one in between.
   *
   * @param fromRow int represents the row number of the marble to be moved from.
   * @param fromCol int represents the col number of the marble to be moved from.
   * @param toRow   int represents the row number of the marble to be moved to.
   * @param toCol   int represents the col number of the marble to be moved to.
   * @return a Position based on the given coordinates of the marble to be jumped from, and the
   *        marble to be jumped to.
   */
  protected Position computeMarbleInBetween(int fromRow, int fromCol, int toRow, int toCol) {
    Position pos;
    if (fromRow == toRow) {
      if (fromCol < toCol) {
        pos = new Position(toCol - 1, fromRow);
      } else {
        pos = new Position(fromCol - 1, fromRow);
      }
    } else {
      if (fromRow < toRow) {
        pos = new Position(toCol, toRow - 1);
      } else {
        pos = new Position(fromCol, fromRow - 1);
      }
    }
    return pos;
  }
}
