package cs3500.marblesolitaire.controller;

import java.io.IOException;
import java.util.Scanner;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;

/**
 * Represents the Marble Solitaire game's controller, which reacts to inputs of the game.
 * The controller may call the model to take a certain action, or compute useful data.
 * The controller may also call the view to render the board and/or a message to the user.
 */
public class MarbleSolitaireControllerImpl implements MarbleSolitaireController {
  private final Readable in;
  private final MarbleSolitaireModel model;
  private final MarbleSolitaireView viewer;

  /**
   * Constructs a Marble Solitaire Controller that accepts a certain model, a viewer, and inputs.
   *
   * @param model  MarbleSolitaireModel represents the marble solitaire model.
   * @param viewer MarbleSolitaireView represents the marble solitaire view.
   * @param inputs Readable represents the given inputs to the controller, ie this.
   * @throws IllegalArgumentException if one of the args are null.
   */
  public MarbleSolitaireControllerImpl(MarbleSolitaireModel model,
                                       MarbleSolitaireView viewer, Readable inputs)
          throws IllegalArgumentException {
    if (model == null || viewer == null || inputs == null) {
      throw new IllegalArgumentException("One or more of the given arguments is null!");
    }
    this.model = model;
    this.viewer = viewer;
    this.in = inputs;
  }

  /**
   * Returns the scanner's next integer if valid, 0 if q/Q, else, it would keep iterating.
   *
   * @return the next integer if its valid, 0 if given input is q or Q.
   * @throws IllegalStateException if there are no more inputs to read from, and the method still
   *                               did not return a valid input.
   */
  private int validateInput(Scanner scanner) throws IllegalStateException {
    while (scanner.hasNext()) {
      String next = scanner.next();
      int nextInt = 0;
      try {
        nextInt = Integer.parseInt(next);
        if (nextInt > 0) {
          return nextInt;
        }
      } catch (NumberFormatException n) {
        if (next.equals("Q") || next.equals("q")) {
          return 0;
        }
      }
    }
    throw new IllegalStateException("Scanner has no more inputs to read!");
  }

  /**
   * Checks the inputs of given scanner and assigns to the list of ints if valid.
   * Returns a list of ints of size 4 only if the inputs are valid (int >0), if not, then empty.
   * @return a list of int representing the fromRow, fromCol, toRow, toCol to be passed to move().
   * @throws IllegalStateException if there are no more inputs to be read and validated
   *                               by validateInput.
   */
  private int[] scanInputs(Scanner scanner) throws IllegalStateException {
    int[] positions = new int[4];
    while (positions[0] == 0 || positions[1] == 0 || positions[2] == 0 || positions[3] == 0) {
      int validationResult;
      try {
        validationResult = this.validateInput(scanner);
      } catch (IllegalStateException e) {
        throw e;
      }
      if (validationResult <= 0) {
        return new int[]{};
      } else {
        if (positions[0] == 0) {
          positions[0] = validationResult;
        } else if (positions[1] == 0) {
          positions[1] = validationResult;
        } else if (positions[2] == 0) {
          positions[2] = validationResult;
        } else if (positions[3] == 0) {
          positions[3] = validationResult;
        }
      }
    }
    return new int[]{positions[0], positions[1], positions[2], positions[3]};
  }

  /**
   * Calls the move function of this model, passes the given coordinates and renders the board.
   * If the move was invalid for any reason, the move method will throw an exception, we will then
   * catch that, and inform the user that the move was invalid (invalidUserMove). If the move
   * was indeed valid, we would just render the game "typically".
   * @param positions int[] represents the fromRow fromCol toRow toCol coordinates.
   */
  private void callModelMove(int[] positions) {
    boolean isInvalid = false;
    try {
      model.move(positions[0] - 1,
              positions[1] - 1, positions[2] - 1, positions[3] - 1);
    } catch (IllegalArgumentException e) {
      isInvalid = true;
      this.invalidUserMove(e);
    }
    if (!isInvalid) {
      this.typicalRenderGame();
    }
  }

  /**
   * Renders the typical game view, with the state of the board, and the score right below.
   *
   * @throws IllegalStateException if could not render the board or the score message, probably
   *        due to an IOException thrown in either renderMessage or renderBoard. Must be an error
   *        with the Appendable object in the MarbleSolitaireView interface.
   */
  private void typicalRenderGame() throws IllegalStateException {
    try {
      this.viewer.renderBoard();
      this.viewer.renderMessage("Score: " + this.model.getScore() + '\n');
    } catch (IOException e) {
      throw new IllegalStateException("Could not render the board, and/or the score message!");
    }
  }

  /**
   * Renders the board after the user has entered an invalid move, and the message explains why.
   *
   * @param e Illegal Argument Exception which is thrown based on the invalid arguments given to
   *          the move method.
   * @throws IllegalStateException if could not render the invalid move message, probably
   *           due to an IOException thrown in renderBoard. Must be an error
   *           with the Appendable object in the MarbleSolitaireView interface.
   */
  private void invalidUserMove(IllegalArgumentException e) throws IllegalStateException {
    try {
      this.viewer.renderMessage("Invalid move. Play again." + e.getMessage() + "\n");
    } catch (IOException e2) {
      throw new IllegalStateException("Could not render the invalid move message!");
    }
  }

  /**
   * Renders game result depending on whether it was quit, or if the game is simply over.
   * If we request this function to result in game quit, we should pass 0, else 1.
   *
   * @param isOver int represents whether the game is over (1, it is, 0 it's not, so must be quit)
   * @throws IllegalStateException if the board could not render the game after it was quit or
   *     was simply over. Must be an IOException that we caught and convert to state exception.
   */
  private void gameOverQuit(int isOver) throws IllegalStateException {
    if (isOver == 1) {
      try {
        this.viewer.renderMessage("Game over!\n");
        this.typicalRenderGame();
      } catch (IOException e) {
        throw new IllegalStateException("Could not result in a game over state!");
      }
    } else {
      try {
        this.viewer.renderMessage("Game quit!\n");
        this.viewer.renderMessage("State of game when quit:\n");
        this.typicalRenderGame();
      } catch (IOException e) {
        throw new IllegalStateException("Could not result in a game quit state!");
      }
    }
  }

  // Will throw a state exception if the scanner has no more inputs to read or
  // there were some issues rendering the board(IOException due to .append).
  // If you want the details, please check out the java docs of the function calls below.
  // This function will basically keep looping as long as the game is not over, and will attempt
  // to read our Readable inputs using the Scanner object, and will try to make make a move
  // through our model, then would render the game through our view.
  @Override
  public void playGame() throws IllegalStateException {
    Scanner scanner = new Scanner(this.in);
    boolean quitHuh = false;
    try {
      this.viewer.renderBoard();
    } catch (IOException i) {
      throw new IllegalStateException("Something bad happened, could not init game UI!");
    }
    while (!this.model.isGameOver()) {
      if (quitHuh) {
        break;
      }
      int[] positions = this.scanInputs(scanner);
      if (positions.length == 4) {
        this.callModelMove(positions);
      } else {
        scanner.close();
        quitHuh = true;
        this.gameOverQuit(0);
      }
    }
    if (!quitHuh) {
      this.gameOverQuit(1);
    }
  }
}
