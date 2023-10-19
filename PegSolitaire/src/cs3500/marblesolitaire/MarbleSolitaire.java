package cs3500.marblesolitaire;

import java.io.InputStreamReader;
import java.util.Arrays;

import cs3500.marblesolitaire.controller.MarbleSolitaireController;
import cs3500.marblesolitaire.controller.MarbleSolitaireControllerImpl;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;

/**
 * Class represents the MarbleSolitaire program, which allows users to configure and run
 * the game of their desire through valid command line arguments.
 */
public final class MarbleSolitaire {

  /**
   * The main program is responsible for running this class.
   * The main will analyse the user inputs (cmd args) and will decide what to do with them.
   * Basically, it will parse the rest of the args (args[1->end]), assuming the first arg would
   * be the model name (english, triangular, european), and will look for additional configurations
   * of the MarbleSolitaire instance. It could be something like ("modelName -size 5", in this ex,
   * the model to be build is modelName with the given configuration of size 5).
   *
   * @param args String[] represents the command line arguments given to the main method.
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("invalid inputs");
    } else {
      int[] modelInputs = parseIntegerInputs(Arrays.copyOfRange(args, 1, args.length));
      Factory factory = determineFactory(args[0], modelInputs);
      if (factory == null || modelInputs == null) {
        System.out.println("Yo! Invalid inputs, try again dummy!");
      } else {
        MarbleSolitaireModel model = factory.createModel();
        if (model != null) {
          MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(model,
                  factory.createView(model), new InputStreamReader(System.in));
          controller.playGame();
        } else {
          System.out.println("Yo! Invalid inputs, try again dummy!");
        }
      }
    }
  }

  /**
   * Static function will determine which factory to call based on the given String representation.
   * If the given model in str representation is: english, triangular, european. Then the factory
   * will be determined. If not, it will return a null, which is indeed handled by the main method.
   * Instead of throwing an exception in here, and catching it in the main, I decided to make use
   * of Java's null. The factory will be constructed based on values, which would represent the
   * user's valid inputs for either size (1 int input) and/or hole (2 int inputs).
   *
   * @param model  String represents the model to be built by the corresponding factory.
   * @param values int... represents a vargs that should be valid user inputs regarding the model's
   *               configuration (size, or hole).
   * @return a Factory which represents a class responsible for building and customizing our
   *        Model and View based on the values argument.
   */
  private static Factory determineFactory(String model, int... values) {
    try {
      if (model.equals("english")) {
        return new EnglishCreator(values[0], values[1], values[2]);
      } else if (model.equals("triangular")) {
        return new TriangleCreator(values[0], values[1], values[2]);
      } else if (model.equals("european")) {
        return new EuropeanCreator(values[0], values[1], values[2]);
      }
    } catch (IllegalArgumentException e) {
      return null;
    }
    return null;
  }

  /**
   * Static method will parse the given inputs to the main method (excluding the first).
   * And will attempt to convert these String inputs into numbers based on the commands.
   * For example, if the given a was -> "", then we would return an int[] of size 3 with equal
   * values of negative -1. If given a was -> "-size 5", then we would return an int[] of size 3
   * with the first values set to 5, and the rest -1. If you given it "size 5" or any other
   * command input that is not registered in the main, then we return null. The reason I return
   * null instead of just throwing an exception, is because handling null leads to shorter and
   * more readable code then a try/catch. Don't worry, I am very careful when it comes to null.
   *
   * @param a String[] represents the given inputs to the main method excluding the first.
   * @return an int[] of size 3 representing the [size, rowNumEmptySlot, colNumEmptySlot].
   *        The list is initialized with values set to -1 so that my factory method createModel
   *        is able to analyse the inputs based on whether they were intended to be passed, or
   *        were simply left default. In the case where the inputs are invalid(either commands are
   *        misspelled (for ex: -Size) , or simply the user did not pass in an int
   *        (for ex: -size ood), we return null, and will handle it in the main method!
   */
  private static int[] parseIntegerInputs(String[] a) {
    int[] result = new int[3];
    result[0] = -1;
    result[1] = -1;
    result[2] = -1;
    try {
      switch (a.length) {
        case 0:
          return result;
        case 2:
          if (a[0].equals("-size")) {
            result[0] = Integer.parseInt(a[1]);
          }
          break;
        case 3:
          if (a[0].equals("-hole")) {
            result[1] = Integer.parseInt(a[1]);
            result[2] = Integer.parseInt(a[2]);
          }
          break;
        case 5:
          if (a[0].equals("-size") && a[2].equals("-hole")) {
            result[0] = Integer.parseInt(a[1]);
            result[1] = Integer.parseInt(a[3]);
            result[2] = Integer.parseInt(a[4]);
          } else if (a[0].equals("-hole") && a[3].equals("-size")) {
            result[0] = Integer.parseInt(a[4]);
            result[1] = Integer.parseInt(a[1]);
            result[2] = Integer.parseInt(a[2]);
          }
          break;
        default:
          return null;
      }
    } catch (NumberFormatException e) {
      return null;
    }
    return result;
  }
}