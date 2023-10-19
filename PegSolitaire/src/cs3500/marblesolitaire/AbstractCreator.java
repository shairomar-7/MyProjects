package cs3500.marblesolitaire;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;
import cs3500.marblesolitaire.view.MarbleSolitaireView;

/**
 * AbstractCreator, is an abstract class that will lift the common fields, constructor bodies.
 * And methods from the EuropeanCreator, the EnglishCreator, and the TriangleCreator.
 */
public abstract class AbstractCreator implements Factory {
  int row;
  int col;
  int size;

  /**
   * Constructs an AbstractCreator with the given size, row and column.
   *
   * @param size int represents the size of the MarbleSolitaire game that we are creating.
   * @param row int represents the row num of the initial empty slot of the MarbleSolitaire
   *           game that we are creating.
   * @param col int represents the column num of the initial empty slot of the MarbleSolitaire
   *           game that we are creating.
   */
  public AbstractCreator(int size, int row, int col) {
    this.row = row;
    this.size = size;
    this.col = col;
  }


  // We have completely abstracted the createModel for all the subclasses of this abstract creator.
  // This method will call the create the model by calling the callModelConstrutor method, which
  // takes the number of parameters to be passed to the MarbleSolitaire model's constructor, which
  // is determined from the method determineConstructor which uses this class's valuable fields.
  @Override
  public MarbleSolitaireModel createModel() {
    return this.callModelConstructor(this.determineConstructor());
  }

  // We have successfully abstracted the createModel for the EuropeanCreator and EnglishCreator.
  // We are unable to abstract that for the TriangleSolitaire because the view of that game
  // is unique.
  @Override
  public MarbleSolitaireView createView(MarbleSolitaireModel model) {
    return new MarbleSolitaireTextView(model);
  }

  /**
   * This method will call the constructor of the MarbleSolitaireModel based on the parameterSize.
   * The parameterSize tells us which constructor to call in order to create a Marble Solitaire
   * model.
   * @param parameterSize int represents the number of parameters of the constructor to be called
   *                      to create a MarbleSolitaireModel.
   * @return a MarbleSolitaireModel based on the parameterSize.
   */
  protected abstract MarbleSolitaireModel callModelConstructor(int parameterSize);

  /**
   * This method will determine the parameterSize of the constructor to be called and created.
   * Based on the fields of this class; if all the fields are equal to -1, then we know the user
   * did not pass any inputs, and so we call the empty constructor. If size is greater than -1
   * and the row and col are -1, then the user intended to call the constructor with 1 arg.
   * If the size and the row and col are greater than -1, then the user intended to call
   * the constructor with 3 arg. If the size is -1, and the row and col are greater than -1, then
   * the user intended to call the constructor with 2 args.
   * @return the number of arguments to be passed to the constructor of the MarbleSolitaireModel.
   *        If the constructor is not determined for some reason, we return -1.
   */
  protected int determineConstructor() {
    if (row == -1 && col == -1 && size == -1) {
      return 0;
    } else if (row == -1 && col == -1 && size > -1) {
      return 1;
    } else if (size == -1 && row > -1 && col > -1) {
      return 2;
    } else if (size > -1 && row > -1 && col > -1) {
      return 3;
    }
    return -1;
  }
}
