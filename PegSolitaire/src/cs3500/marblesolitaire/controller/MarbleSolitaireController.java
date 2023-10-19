package cs3500.marblesolitaire.controller;

/**
 * Represents a controller for the marble solitaire game.
 */
public interface MarbleSolitaireController {

  /**
   * play a new game of Marble Solitaire.
   * @throws IllegalStateException only if
   *     the controller is unable to successfully read input or transmit output
   */
  void playGame() throws IllegalStateException;
}
