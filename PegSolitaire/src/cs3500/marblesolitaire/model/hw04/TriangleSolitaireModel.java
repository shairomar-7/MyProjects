package cs3500.marblesolitaire.model.hw04;

import java.util.List;

import cs3500.marblesolitaire.model.hw02.BoardBuilder;
import cs3500.marblesolitaire.model.hw02.Marble;
import cs3500.marblesolitaire.model.hw02.Position;

/**
 * Triangle Solitaire Model, represents the model component of our marble solitaire game.
 * This model got abstracted some time ago; we lifted its fields, the bodies of the constructors,
 * and some methods. Unfortunately, we had to implement some methods due to this game's unique
 * set of rules from the typical marble solitaire games out there. This is one of a kind.
 */
public class TriangleSolitaireModel extends AbstractModel {

  /**
   * Constructs a TriangleSolitaireModel, with the default empty slot coordinates.
   * These values are: an arm thickness (width) of 5, and the initial empty slot at the top, 0 0.
   */
  public TriangleSolitaireModel() {
    super(5, 0, 0);
  }

  /**
   * Constructs a TriangleSolitaireModel, with the given width and default initial empty slot coord.
   * @param armThickness int represents the width/arm thickness of the triangle solitaire model.
   * @throws IllegalArgumentException if given an arm thickness less than 1.
   */
  public TriangleSolitaireModel(int armThickness) throws IllegalArgumentException {
    super(armThickness, 0, 0);
  }

  /**
   * Constructs a TriangleSolitaireModel, with the given width and empty slot coordinates.
   * @param armThickness int represents the width/arm thickness of the triangle solitaire model.
   * @param sCol int represents the col number of the initial empty slot.
   * @param sRow int represents the row number of the initial empty slot.
   * @throws IllegalArgumentException if given an arm thickness less than 1,
   *          or the given coordinates of the initial empty slot are Invalid
   *          or beyond the board dimensions.
   *
   */
  public TriangleSolitaireModel(int armThickness, int sRow, int sCol)
          throws IllegalArgumentException {
    super(armThickness, sRow, sCol);
  }

  /**
   * Constructs a TriangleSolitaireModel, with default width and given initial empty slot value.
   * The default width is: 5.
   * @param sCol int represents the col number of the initial empty slot.
   * @param sRow int represents the row number of the initial empty slot.
   * @throws IllegalArgumentException if given an arm thickness less than 1,
   *          or the given coordinates of the initial empty slot are Invalid
   *          or beyond the board dimensions.
   *
   */
  public TriangleSolitaireModel(int sRow, int sCol) throws IllegalArgumentException {
    super(5, sRow, sCol);
  }

  @Override
  public int getBoardSize() {
    return this.armThickness;
  }

  // document why overridden.
  @Override
  protected Position computeMarbleInBetween(int fromRow, int fromCol, int toRow, int toCol) {
    Position pos;
    int dy = fromRow - toRow;
    int dx = fromCol - toCol;
    if (dy == 0 && Math.abs(dx) == 2) {
      if (dx == 2) {
        pos = new Position(fromCol - 1, fromRow);
      } else {
        pos = new Position(fromCol + 1, fromRow);
      }
    } else if (dx == 0 && Math.abs(dy) == 2) {
      if (dy == 2) {
        pos = new Position(fromCol, fromRow - 1);
      } else {
        pos = new Position(fromCol, fromRow + 1);
      }
    } else { // dx == 2 and dy == 2;
      if (dx == -2 && dy == -2) {
        pos = new Position(fromCol + 1, fromRow + 1);
      } else {
        pos = new Position(fromCol - 1, fromRow - 1);
      }
    }
    return pos;
  }

  // DOCUMENT WHY OVERRIDDEN (5,3) -> (3,3) can move to (5,1), (5,5), (3,1) and ,
  @Override
  protected boolean tryPossibleMoves(int moveDirection, int row, int col) {
    switch (moveDirection) {
      case 0:
        if (this.checkEdge(row, col) != 1) {
          if (!this.isBeyondBoard(row - 2, col) &&
                  this.checkFromToBetween(row, col, row - 2, col).length != 0) {
            return true;
          } else if (this.checkEdge(row, col) != 3
                  && !this.isBeyondBoard(row - 2, col - 2)
                  && this.checkFromToBetween(row, col, row - 2, col - 2).length != 0) {
            return true;
          }
        }
        break;
      case 1:
        if (this.checkEdge(row, col) != 2) {
          if (!this.isBeyondBoard(row, col + 2)
                  && this.checkFromToBetween(row, col, row, col + 2).length != 0) {
            return true;
          } else if (this.checkEdge(row, col) != 4
                  && !this.isBeyondBoard(row + 2, col + 2)
                  && this.checkFromToBetween(row, col, row + 2, col + 2).length != 0) {
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

  // I could not perform index math on the TriangleSolitaireModel due to the way the board
  // was built. We will have to iterate the list of marbles and get the index of the marble
  // that has equal row and col numbers (we do this because of the invariant/assumption that
  // each marble in this list of marbles has a unique position.
  @Override
  protected int computeMarbleIndex(int row, int col) {
    for (int i = 0; i < this.marbles.size(); i++) {
      if (this.marbles.get(i).isPosEqual(row, col)) {
        return i;
      }
    }
    return -1;
  }

  @Override
  protected List<Marble> buildBoard(int armThickness, int sRow, int sCol)
          throws IllegalArgumentException {
    return new BoardBuilder().buildTriangleBoard(armThickness, sRow, sCol);
  }


  // The reason this is overridden is because a valid distance for the move is different in the
  // triangular than other marble solitaires out there.
  @Override
  protected boolean checkDis(Position pos1, Position pos2) {
    return pos1.checkDistanceTriangle(pos2);
  }
}
