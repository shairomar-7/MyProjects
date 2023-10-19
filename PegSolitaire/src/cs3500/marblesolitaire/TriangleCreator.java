package cs3500.marblesolitaire;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import cs3500.marblesolitaire.view.TriangleSolitaireTextView;

/**
 * This class represents a TriangleCreator, which is a factory creator for triangle solitaire.
 * This factory is constructed by giving the size of the triangle solitaire, and the initial
 * coordinates of the empty slot. This class has two inhereted methods from the Factory interface:
 * createView, and the abstracted createModel. This class also has an inherited abstract method
 * from the AbstarctCreator class: callModelConstructor, which calls the Triangle Solitaire
 * constructor and returns a new Model with the creator's size, row, and col fields.
 */
public class TriangleCreator extends AbstractCreator {

  /**
   * Constructs a TriangleCreator by passing in a size, and the coordinates of the init empty slot.
   *
   * @param size int represents the size of the board.
   * @param row  int represents the row num of the init empty slot.
   * @param col  int represents the col num of the init empty slot.
   */
  public TriangleCreator(int size, int row, int col) {
    super(size, row, col);
  }

  // This method will create a TriangleSolitaireTextView with the given Model argument.
  // Note: you must pass in a TriangleSolitaireModel. This is your responsibility!
  @Override
  public MarbleSolitaireView createView(MarbleSolitaireModel model) {
    return new TriangleSolitaireTextView(model);
  }

  //This function will construct a TriangleSolitaireModel based on the given parameterSize.
  //The parameterSize represents the number of arguments to be passed to the model constructor.
  @Override
  protected MarbleSolitaireModel callModelConstructor(int parameterSize) {
    try {
      if (parameterSize == 0) {
        return new TriangleSolitaireModel();
      } else if (parameterSize == 1) {
        return new TriangleSolitaireModel(size);
      } else if (parameterSize == 2) {
        return new TriangleSolitaireModel(row, col);
      } else if (parameterSize == 3) {
        return new TriangleSolitaireModel(size, row, col);
      } else {
        return null;
      }
    } catch (IllegalArgumentException e) {
      return null;
    }
  }
}
