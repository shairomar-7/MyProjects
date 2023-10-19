package cs3500.marblesolitaire.model.hw02;

/**
 * Marble, representing a marble of the english solitaire game.
 * A Marble contains data regarding its position on the board, and the state of the marble.
 * Where, a state is one of: empty, invalid, or marble
 * This was changed from the previous hw by making the state private, and instead, adding methods
 * to access the state (they will return a string), and adding methods to set the state by passing
 * in a string representation of the state.
 */
public class Marble implements Comparable<Marble> {
  private final Position pos;
  private MarbleSolitaireModelState.SlotState state;

  /**
   * Construct a Marble with the given position and the state(invalid, empty, or marble).
   *
   * @param pos   Position represents the pos of a marble of the peg solitaire game
   * @param state SlotState represents the state of the marble of the peg solitaire game
   * @throws IllegalArgumentException if the given position has a negative coordinate (c <0 || l <0)
   */
  public Marble(Position pos, MarbleSolitaireModelState.SlotState state)
          throws IllegalArgumentException {
    this.pos = pos;
    this.state = state;
  }

  /**
   * Gets the string representing of this marble's state. Used for testing. Sorry bout that.
   *
   * @return a String representing this marble's state.
   */
  public String getState() {
    if (this.state == MarbleSolitaireModelState.SlotState.Empty) {
      return "empty";
    } else if (this.state == MarbleSolitaireModelState.SlotState.Invalid) {
      return "invalid";
    } else {
      return "marble";
    }
  }

  /**
   * Checks whether the given position equals this marble's position.
   *
   * @param sRow int represents the row number
   * @param sCol int represents the column number
   * @return boolean telling us whether the given position(x, y) is equal to this marble's position
   */
  public boolean isPosEqual(int sRow, int sCol) {
    return this.pos.equals(new Position(sCol, sRow));
  }


  /**
   * Sets this marble's state to the given state in a string representation.
   * If we pass in "marble", the state is set to Marble, "invalid", then state is to Invalid,
   * "empty", statet is set to Empty, else, we throw an IllegalArgumentException.
   *
   * @param state String represents the given state we want to set this marble to.
   */
  public void set(String state) {
    if (state.equals("marble")) {
      this.state = MarbleSolitaireModelState.SlotState.Marble;
    } else if (state.equals("empty")) {
      this.state = MarbleSolitaireModelState.SlotState.Empty;
    } else if (state.equals("invalid")) {
      this.state = MarbleSolitaireModelState.SlotState.Invalid;
    } else {
      throw new IllegalArgumentException("invalid state passed to the state setter");
    }
  }

  /**
   * Override equals, in order to check two marbles for equality.
   *
   * @param o Object to be checked for equality with this Marble
   * @return boolean telling if this marble's position is equal to o's position or if o is this
   */
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Marble)) {
      return false;
    }
    Marble c = (Marble) o;
    return c.pos.equals(this.pos);
  }

  /**
   * Gets this marble's column number.
   *
   * @return int representing this marble's column number.
   */
  public int getCol() {
    return this.pos.getCol();
  }

  /**
   * Gets this marble's row number.
   *
   * @return int representing this marble's row number.
   */
  public int getRow() {
    return this.pos.getRow();
  }

  /**
   * Override hashCode, in order to override equals and check equality.
   *
   * @return int representing the hashcode of this Marble
   */
  public int hashCode() {
    return this.pos.hashCode();
  }

  @Override
  public int compareTo(Marble o) {
    return this.pos.compareTo(o.pos);
  }
}
