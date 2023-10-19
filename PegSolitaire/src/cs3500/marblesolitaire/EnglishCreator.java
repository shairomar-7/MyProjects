package cs3500.marblesolitaire;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;

/**
 * EnglishCreator, class extends the AbstractCreator to lift off the fields, the constructor body.
 * And the methods: createModel, createView. This class will create a EuropeanSolitaire model
 * and a EuropeanSolitaireView at request from the main method. This class will make use of its
 * fields to create the EnglishCreator accordingly. This is ultimately done through
 * the AbstractCreator abstract class.
 */
public class EnglishCreator extends AbstractCreator {

  /**
   * Constructs an EnglishCreator by the given size, row, and col arguments. Note, the given
   * arguments are very valuable to the way we create the MarbleSolitaireModel.
   *
   * @param size int represents the size of the EuropeanSolitaire board.
   * @param row  int represents the initial row number of the EuropeanSolitaire board.
   * @param col  int represents the initial column number of the EuropeanSolitaire board.
   */
  public EnglishCreator(int size, int row, int col) {
    super(size, row, col);
  }


  // The reason this is overridden is because we will uniquely call the EnglishSolitaireModel's
  // constructor, which makes it impossible to abstract.
  @Override
  protected MarbleSolitaireModel callModelConstructor(int parameterSize) {
    try {
      if (parameterSize == 0) {
        return new EnglishSolitaireModel();
      } else if (parameterSize == 1) {
        return new EnglishSolitaireModel(size);
      } else if (parameterSize == 2) {
        return new EnglishSolitaireModel(row, col);
      } else if (parameterSize == 3) {
        return new EnglishSolitaireModel(size, row, col);
      } else {
        return null;
      }
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

}
