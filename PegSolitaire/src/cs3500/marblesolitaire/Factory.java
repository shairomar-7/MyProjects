package cs3500.marblesolitaire;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;

/**
 * Factory interface, represents an interface implemented to create the model and view.
 * This factory has two methods; one of them creates the Model, and the other will create the view.
 * Note: this factory method is only valid for the MarbleSolitaire game.
 */
public interface Factory {
  /**
   * Creates a MarbleSolitaireMarble. Please return null, and do not throw any exceptions.
   * The return of null will be handled quite well in the MarbleSolitaire main!
   * @return a MarbleSolitaireModel. This should be done according to some conditions that
   *        will ultimately determine exactly which constructor you meant to create.
   */
  MarbleSolitaireModel createModel();

  /**
   * Creates a marble solitaire view, otherwise well-known as the MarbleSolitaireView.
   * This will be created with the generous given model. Note, this function does not need to
   * handle the case when given a null model, because this will be handled in the main method of
   * the view. Yet, that would be a responsible thing to do, but still, I like some risk.
   * @param model MarbleSolitaireModel will be used to construct the MarbleSolitaireView. Note:
   *              you will call the constructor with one argument only!
   * @return a MarbleSolitaireView by calling the constructor that takes the model as its sole arg.
   */
  MarbleSolitaireView createView(MarbleSolitaireModel model);
}
