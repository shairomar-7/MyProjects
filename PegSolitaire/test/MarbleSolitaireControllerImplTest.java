import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Random;

import cs3500.marblesolitaire.controller.InputInteraction;
import cs3500.marblesolitaire.controller.Interaction;
import cs3500.marblesolitaire.controller.MarbleSolitaireController;
import cs3500.marblesolitaire.controller.MarbleSolitaireControllerImpl;
import cs3500.marblesolitaire.controller.PrintInteraction;
import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw04.EuropeanSolitaireModel;
import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import cs3500.marblesolitaire.view.TriangleSolitaireTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Class dedicated to testing the MarbleSolitaireControllerImpl class.
 */
public class MarbleSolitaireControllerImplTest {

  Interaction[] interactions = new Interaction[]{
      new PrintInteraction("    O O O\n    " +
                  "O O O\nO O O O O O O\n" +
                  "O O O _ O O O\nO O O O O O O\n" +
                  "    O O O\n    O O O"),
      new InputInteraction("Q"),
      new PrintInteraction("Game quit!\n" +
                  "State of game when quit: \n" +
                  "    O O O\n" +
                  "    O O O\n" +
                  "O O O O O O O\n" +
                  "O O O _ O O O\n" +
                  "O O O O O O O\n" +
                  "    O O O\n" +
                  "    O O O\n" +
                  "Score: 32")
  };

  Appendable output = new StringBuilder();
  Readable input = new StringReader("1 2 3");
  MarbleSolitaireModel traditional = new EnglishSolitaireModel();
  MarbleSolitaireView viewer = new MarbleSolitaireTextView(traditional, output);
  MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(traditional,
          viewer, input);
  MarbleSolitaireController controllerTest;

  // Random player: returns a StringReader with 1000000 random inputs to be given to controller.
  private Readable randomPlayer() {
    int i = 0;
    Random rand = new Random();
    Appendable builder = new StringBuilder();
    while (i < 1000000) {
      i++;
      int next = rand.nextInt(7);
      while (next == 0) {
        next = rand.nextInt(7);
      }
      try {
        builder.append(Integer.toString(next) + '\n');
      }
      catch (IOException e) {
        throw new IllegalStateException("hmm something went wrong!!");
      }
    }
    try {
      builder.append("q");
    }
    catch (IOException s) {
      throw new IllegalStateException("hmm!");
    }
    return new StringReader(builder.toString());
  }

  //NOTE: this tests our controller through fuzzy testing, by generating 100000 random inputs,
  // with q followed at the end. Please run it! Some cool stuff. Wish I could spend more more
  // time developing it with some game logic.
  //  @Test
  //  public void testRandomPlay() {
  //    Readable randominputs= this.randomPlayer();
  //    Appendable log = new StringBuilder();
  //    MarbleSolitaireModel model = new EnglishSolitaireModel();
  //    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
  //    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(model,
  //            viewer, randominputs);
  //    controller.playGame();
  //    assertEquals("", log.toString());
  //  }


  @Test
  public void testPlayGameTriangle() {
    MarbleSolitaireModel model = new TriangleSolitaireModel();
    Interaction[] actions = new Interaction[] {
      new PrintInteraction("    _\n" +
                    "   O O\n" +
                    "  O O O\n" +
                    " O O O O\n" +
                    "O O O O O"),
      new InputInteraction("3 3 1 1\n"),
      new PrintInteraction("    O\n" +
                    "   O _\n" +
                    "  O O _\n" +
                    " O O O O\n" +
                    "O O O O O\n" +
                    "Score: 13"),
      new InputInteraction("s r vg ood 1 q"),
      new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "    O\n" +
                    "   O _\n" +
                    "  O O _\n" +
                    " O O O O\n" +
                    "O O O O O\n" +
                    "Score: 13")
    };

    try {
      this.testRunTriangle(model, actions);
    }
    catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }


  @Test
  public void testPlayGameTriangleSize1() {
    MarbleSolitaireModel model = new TriangleSolitaireModel(1);
    Interaction[] actions = new Interaction[] {
      new PrintInteraction("_\n" +
                    "Game over!\n" +
                    "_\n" +
                    "Score: 0"),
    };

    try {
      this.testRunTriangle(model, actions);
    }
    catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }

  @Test
  public void testPlayGameTriangleSize2() {
    MarbleSolitaireModel model = new TriangleSolitaireModel(2);
    Interaction[] actions = new Interaction[] {
      new PrintInteraction(" _\n" +
                    "O O\n" +
                    "Game over!\n" +
                    " _\n" +
                    "O O\n" +
                    "Score: 2"),
    };

    try {
      this.testRunTriangle(model, actions);
    }
    catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }

  @Test
  public void testPlayGameTriangleSize3() {
    MarbleSolitaireModel model = new TriangleSolitaireModel(3, 0,0);
    Interaction[] actions = new Interaction[] {
      new PrintInteraction("  _\n" +
                    " O O\n" +
                    "O O O"),
      new InputInteraction("3 3 1 1 "),
      new PrintInteraction("  O\n" +
                    " O _\n" +
                    "O O _\n" +
                    "Score: 4"),
      new InputInteraction("3 1 3 3 "),
      new PrintInteraction("  O\n" +
                    " O _\n" +
                    "_ _ O\n" +
                    "Score: 3"),
      new InputInteraction("1 1 3 1 "),
      new PrintInteraction("  _\n" +
                    " _ _\n" +
                    "O _ O\n" +
                    "Score: 2\n" +
                    "Game over!\n" +
                    "  _\n" +
                    " _ _\n" +
                    "O _ O\n" +
                    "Score: 2"),
    };

    try {
      this.testRunTriangle(model, actions);
    }
    catch (IllegalStateException e) {
      fail("unexpected exception thrown!");
    }
  }

  @Test
  public void testPlayGameEuropean1() {
    MarbleSolitaireModel model = new EuropeanSolitaireModel();
    Interaction[] actions = new Interaction[] {
      new PrintInteraction("    O O O\n" +
                    "  O O O O O\n" +
                    "O O O O O O O\n" +
                    "O O O _ O O O\n" +
                    "O O O O O O O\n" +
                    "  O O O O O\n" +
                    "    O O O"),
      new InputInteraction("Q"),
      new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "    O O O\n" +
                    "  O O O O O\n" +
                    "O O O O O O O\n" +
                    "O O O _ O O O\n" +
                    "O O O O O O O\n" +
                    "  O O O O O\n" +
                    "    O O O\n" +
                    "Score: 36")
    };
    try {
      this.testRun(model, actions);
    }
    catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }


  @Test
  public void testPlayGameEuropean2() {
    MarbleSolitaireModel model = new EuropeanSolitaireModel();
    Interaction[] actions = new Interaction[] {
      new PrintInteraction("    O O O\n" +
                    "  O O O O O\n" +
                    "O O O O O O O\n" +
                    "O O O _ O O O\n" +
                    "O O O O O O O\n" +
                    "  O O O O O\n" +
                    "    O O O"),
      new InputInteraction("6 4 4 4\n"),
      new PrintInteraction("    O O O\n" +
                    "  O O O O O\n" +
                    "O O O O O O O\n" +
                    "O O O O O O O\n" +
                    "O O O _ O O O\n" +
                    "  O O _ O O\n" +
                    "    O O O\n" +
                    "Score: 35"),
      new InputInteraction("s t v p r u s v m 2 3 4 q"),
      new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "    O O O\n" +
                    "  O O O O O\n" +
                    "O O O O O O O\n" +
                    "O O O O O O O\n" +
                    "O O O _ O O O\n" +
                    "  O O _ O O\n" +
                    "    O O O\n" +
                    "Score: 35")
    };
    try {
      this.testRun(model, actions);
    }
    catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }

  @Test
  public void testPlayGameEuropeanSize5() {
    MarbleSolitaireModel model = new EuropeanSolitaireModel(5);
    Interaction[] actions = new Interaction[] {
      new PrintInteraction("        O O O O O\n" +
                    "      O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "      O O O O O O O\n" +
                    "        O O O O O"),
      new InputInteraction("9 7 7 7\n"),
      new PrintInteraction("        O O O O O\n" +
                    "      O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "      O O O O O O O\n" +
                    "        O O O O O\n" +
                    "Score: 127"),
      new InputInteraction("s t v p r u s v m 2 4 q"),
      new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "        O O O O O\n" +
                    "      O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "      O O O O O O O\n" +
                    "        O O O O O\n" +
                    "Score: 127")
    };
    try {
      this.testRun(model, actions);
    }
    catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }

  @Test
  public void testPlayGameEuropeanSize5CustomEmptySlot() {
    MarbleSolitaireModel model = new EuropeanSolitaireModel(5, 0, 6);
    Interaction[] actions = new Interaction[] {
      new PrintInteraction("        O O _ O O\n" +
                    "      O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "      O O O O O O O\n" +
                    "        O O O O O"),
      new InputInteraction("9 7 7 7\n"),
      new PrintInteraction("Invalid move. Play again.Either from is not a Marble," +
                    " or to is not Empty, or distance is invalid!"),
      new InputInteraction("s t v p r u s v m 2 4 q"),
      new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "        O O _ O O\n" +
                    "      O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "  O O O O O O O O O O O\n" +
                    "    O O O O O O O O O\n" +
                    "      O O O O O O O\n" +
                    "        O O O O O\n" +
                    "Score: 128"),
    };
    try {
      this.testRun(model, actions);
    }
    catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }


  // Test for arm thickness 5, and quit as first input
  @Test
  public void testRunThick5() {
    MarbleSolitaireModel model = new EnglishSolitaireModel(5);
    Interaction[] actions = new Interaction[]{
        new PrintInteraction("        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O"),
        new InputInteraction("Q"), // scanner issue??
        new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "Score: 104")};
    try {
      this.testRun(model, actions);
    } catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }


  @Test
  public void testRunTriangleTraditionalMockView() {
    StringBuilder mockLogs = new StringBuilder();
    Appendable viewMockLogs = new StringBuilder();
    Readable inputs = new StringReader("4 4 6 4 q");
    MarbleSolitaireModel model = new TriangleSolitaireModel();
    MarbleSolitaireView mockView = new MockMarbleSolitaireView(viewMockLogs);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(model,
            mockView, inputs);
    controller.playGame();
    assertEquals("render board\n" +
            "render message: Invalid move. Play again.Either from is not a Marble," +
            " or to is not Empty, or distance is invalid!\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 14\n", viewMockLogs.toString());
  }

  @Test
  public void testRunTriangleTraditionalMockView2() {
    StringBuilder mockLogs = new StringBuilder();
    Appendable viewMockLogs = new StringBuilder();
    Readable inputs = new StringReader("3 3 1 1 q");
    MarbleSolitaireModel model = new TriangleSolitaireModel();
    MarbleSolitaireView mockView = new MockMarbleSolitaireView(viewMockLogs);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(model,
            mockView, inputs);
    controller.playGame();
    assertEquals("render board\n" +
            "render board\n" +
            "render message: Score: 13\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 13\n", viewMockLogs.toString());
  }


  @Test
  public void testRunTriangleTraditionalMockView3() {
    StringBuilder mockLogs = new StringBuilder();
    Appendable viewMockLogs = new StringBuilder();
    Readable inputs = new StringReader("3 3 1 1 s p u t 5 6 qq q");
    MarbleSolitaireModel model = new TriangleSolitaireModel();
    MarbleSolitaireView mockView = new MockMarbleSolitaireView(viewMockLogs);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(model,
            mockView, inputs);
    controller.playGame();
    assertEquals("render board\n" +
            "render board\n" +
            "render message: Score: 13\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 13\n", viewMockLogs.toString());
  }

  @Test
  public void testRunTriangleTraditionalMockModelTriangleView() {
    StringBuilder mockLogs = new StringBuilder();
    Appendable viewMockLogs = new StringBuilder();
    Readable inputs = new StringReader("3 3 1 1 s p u t 5 6 qq q");
    MarbleSolitaireModel model = new MockMarbleSolitaire(mockLogs);
    MarbleSolitaireView mockView = new TriangleSolitaireTextView(model, viewMockLogs);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(model,
            mockView, inputs);
    controller.playGame();
    assertEquals("fromRow = 2, fromCol= 2, toRow = 0, toCol = 0\n", mockLogs.toString());
    assertEquals("\n" +
            "\n" +
            "Score: 0\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "\n" +
            "Score: 0\n", viewMockLogs.toString());
  }


  @Test
  public void testRunThick3CustomSlot() {
    StringBuilder mockLogs = new StringBuilder();
    Appendable viewMockLogs = new StringBuilder();
    Readable inputs = new StringReader("4 4 6 4 q");
    MarbleSolitaireModel modelThick3Slot64 = new EnglishSolitaireModel(3,
            6, 4);
    MarbleSolitaireModel mockModel = new MockMarbleSolitaire(mockLogs);
    MarbleSolitaireView mockView = new MockMarbleSolitaireView(viewMockLogs);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mockModel,
            mockView, inputs);
    controller.playGame();
    assertEquals("fromRow = 3, fromCol= 3, toRow = 5, toCol = 3\n", mockLogs.toString());
    assertEquals("render board\n" +
            "render board\n" +
            "render message: Score: 0\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 0\n", viewMockLogs.toString());
  }

  @Test // this is an invalid move, and obviously the mock model will not throw an exception.
  public void testRunThick3CustomSlot2() {
    StringBuilder mockLogs = new StringBuilder();
    Appendable viewMockLogs = new StringBuilder();
    Readable inputs = new StringReader("5 s 5 5 5 q");
    MarbleSolitaireModel modelThick3Slot64 = new EnglishSolitaireModel(3,
            6, 4);
    MarbleSolitaireModel mockModel = new MockMarbleSolitaire(mockLogs);
    MarbleSolitaireView mockView = new MockMarbleSolitaireView(viewMockLogs);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mockModel,
            mockView, inputs);
    controller.playGame();
    assertEquals("fromRow = 4, fromCol= 4, toRow = 4, toCol = 4\n", mockLogs.toString());
    assertEquals("render board\n" +
            "render board\n" +
            "render message: Score: 0\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 0\n", viewMockLogs.toString());
  }


  @Test // this is an invalid move, and obviously the mock model will not throw an exception.
  public void testRunThick3CustomSlot3() {
    StringBuilder mockLogs = new StringBuilder();
    Appendable viewMockLogs = new StringBuilder();
    Readable inputs = new StringReader("-15 s 20 69 96 420 q");
    MarbleSolitaireModel modelThick3Slot64 = new EnglishSolitaireModel(3,
            6, 4);
    MarbleSolitaireModel mockModel = new MockMarbleSolitaire(mockLogs);
    MarbleSolitaireView mockView = new MockMarbleSolitaireView(viewMockLogs);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mockModel, mockView,
            inputs);
    controller.playGame();
    assertEquals("fromRow = 19, fromCol= 68, toRow = 95, toCol = 419\n",
            mockLogs.toString());
    assertEquals("render board\n" +
                    "render board\n" +
                    "render message: Score: 0\n" +
                    "render message: Game quit!\n" +
                    "render message: State of game when quit:\n" +
                    "render board\n" +
                    "render message: Score: 0\n",
            viewMockLogs.toString());
  }

  @Test
  public void testPlayeFullGameTriangle() {
    Readable inputs = new StringReader("3 3 1 1 3 1 3 3 1 1 3 1 4 1 2 1 4 4 2 2 4 2 4 4 " +
            "5 5 3 3 3\n" +
            "3 1 1 1 1 3 1 5 3 5 5 5 1 5 3");
    MarbleSolitaireModel model = new TriangleSolitaireModel();
    Appendable log = new StringBuilder();
    MarbleSolitaireView view = new TriangleSolitaireTextView(model, log);
    MarbleSolitaireController control = new MarbleSolitaireControllerImpl(model, view, inputs);
    control.playGame();
    assertEquals("    _\n" +
            "   O O\n" +
            "  O O O\n" +
            " O O O O\n" +
            "O O O O O\n" +
            "    O\n" +
            "   O _\n" +
            "  O O _\n" +
            " O O O O\n" +
            "O O O O O\n" +
            "Score: 13\n" +
            "    O\n" +
            "   O _\n" +
            "  _ _ O\n" +
            " O O O O\n" +
            "O O O O O\n" +
            "Score: 12\n" +
            "    _\n" +
            "   _ _\n" +
            "  O _ O\n" +
            " O O O O\n" +
            "O O O O O\n" +
            "Score: 11\n" +
            "    _\n" +
            "   O _\n" +
            "  _ _ O\n" +
            " _ O O O\n" +
            "O O O O O\n" +
            "Score: 10\n" +
            "    _\n" +
            "   O O\n" +
            "  _ _ _\n" +
            " _ O O _\n" +
            "O O O O O\n" +
            "Score: 9\n" +
            "    _\n" +
            "   O O\n" +
            "  _ _ _\n" +
            " _ _ _ O\n" +
            "O O O O O\n" +
            "Score: 8\n" +
            "    _\n" +
            "   O O\n" +
            "  _ _ O\n" +
            " _ _ _ _\n" +
            "O O O O _\n" +
            "Score: 7\n" +
            "    O\n" +
            "   O _\n" +
            "  _ _ _\n" +
            " _ _ _ _\n" +
            "O O O O _\n" +
            "Score: 6\n" +
            "    _\n" +
            "   _ _\n" +
            "  O _ _\n" +
            " _ _ _ _\n" +
            "O O O O _\n" +
            "Score: 5\n" +
            "    _\n" +
            "   _ _\n" +
            "  O _ _\n" +
            " _ _ _ _\n" +
            "O O _ _ O\n" +
            "Score: 4\n" +
            "    _\n" +
            "   _ _\n" +
            "  O _ _\n" +
            " _ _ _ _\n" +
            "_ _ O _ O\n" +
            "Score: 3\n" +
            "Game over!\n" +
            "    _\n" +
            "   _ _\n" +
            "  O _ _\n" +
            " _ _ _ _\n" +
            "_ _ O _ O\n" +
            "Score: 3\n", log.toString());
  }

  @Test
  public void testPlayFullGameWithoutQuittingEuropean() {
    Readable inputs = new StringReader("4 2 4 4 6 3 4 3 5 1 5 3 5 4 " +
            "5 2 5 6 5 4 7 5 5 5 4 5 6 5 7 3 7 5 7 5 5 5 3 3 5 3 1 3 3 3 2 5 4 5 4 5 6 5 6 5 6 3 " +
            "6 3 4 3 4 3 2 3 3 1 5 1 5 1 5 3 5 3 5 5 3 7 3 5 3 4 3 6 5 7 3 7 3 7 3 5 1 5 1" +
            " 3 1 3 3 3 3 2 3 4 3 4 3 6 3 6 5 6 5 6 5 4 5 4 3 4 2 4 4 4");
    MarbleSolitaireModel model = new EuropeanSolitaireModel();
    Appendable log = new StringBuilder();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(model, viewer, inputs);
    controller.playGame();
    assertEquals("    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "  O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O _ _ O O O O\n" +
            "O O O O O O O\n" +
            "  O O O O O\n" +
            "    O O O\n" +
            "Score: 35\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "O O _ O O O O\n" +
            "  O _ O O O\n" +
            "    O O O\n" +
            "Score: 34\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "_ _ O O O O O\n" +
            "  O _ O O O\n" +
            "    O O O\n" +
            "Score: 33\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "_ O _ _ O O O\n" +
            "  O _ O O O\n" +
            "    O O O\n" +
            "Score: 32\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "_ O _ O _ _ O\n" +
            "  O _ O O O\n" +
            "    O O O\n" +
            "Score: 31\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "_ O _ O O _ O\n" +
            "  O _ O _ O\n" +
            "    O O _\n" +
            "Score: 30\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O _ O O _ O O\n" +
            "_ O _ O _ _ O\n" +
            "  O _ O O O\n" +
            "    O O _\n" +
            "Score: 29\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O _ O O _ O O\n" +
            "_ O _ O _ _ O\n" +
            "  O _ O O O\n" +
            "    _ _ O\n" +
            "Score: 28\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O _ O O _ O O\n" +
            "_ O _ O O _ O\n" +
            "  O _ O _ O\n" +
            "    _ _ _\n" +
            "Score: 27\n" +
            "    O O O\n" +
            "  O O O O O\n" +
            "O O _ O O O O\n" +
            "O _ _ O _ O O\n" +
            "_ O O O O _ O\n" +
            "  O _ O _ O\n" +
            "    _ _ _\n" +
            "Score: 26\n" +
            "    _ O O\n" +
            "  O _ O O O\n" +
            "O O O O O O O\n" +
            "O _ _ O _ O O\n" +
            "_ O O O O _ O\n" +
            "  O _ O _ O\n" +
            "    _ _ _\n" +
            "Score: 25\n" +
            "    _ O O\n" +
            "  O _ O _ O\n" +
            "O O O O _ O O\n" +
            "O _ _ O O O O\n" +
            "_ O O O O _ O\n" +
            "  O _ O _ O\n" +
            "    _ _ _\n" +
            "Score: 24\n" +
            "    _ O O\n" +
            "  O _ O _ O\n" +
            "O O O O _ O O\n" +
            "O _ _ O _ O O\n" +
            "_ O O O _ _ O\n" +
            "  O _ O O O\n" +
            "    _ _ _\n" +
            "Score: 23\n" +
            "    _ O O\n" +
            "  O _ O _ O\n" +
            "O O O O _ O O\n" +
            "O _ _ O _ O O\n" +
            "_ O O O _ _ O\n" +
            "  O O _ _ O\n" +
            "    _ _ _\n" +
            "Score: 22\n" +
            "    _ O O\n" +
            "  O _ O _ O\n" +
            "O O O O _ O O\n" +
            "O _ O O _ O O\n" +
            "_ O _ O _ _ O\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 21\n" +
            "    _ O O\n" +
            "  O O O _ O\n" +
            "O O _ O _ O O\n" +
            "O _ _ O _ O O\n" +
            "_ O _ O _ _ O\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 20\n" +
            "    _ O O\n" +
            "  O O O _ O\n" +
            "_ O _ O _ O O\n" +
            "_ _ _ O _ O O\n" +
            "O O _ O _ _ O\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 19\n" +
            "    _ O O\n" +
            "  O O O _ O\n" +
            "_ O _ O _ O O\n" +
            "_ _ _ O _ O O\n" +
            "_ _ O O _ _ O\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 18\n" +
            "    _ O O\n" +
            "  O O O _ O\n" +
            "_ O _ O _ O O\n" +
            "_ _ _ O _ O O\n" +
            "_ _ _ _ O _ O\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 17\n" +
            "    _ O O\n" +
            "  O O O _ O\n" +
            "_ O _ O O _ _\n" +
            "_ _ _ O _ O O\n" +
            "_ _ _ _ O _ O\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 16\n" +
            "    _ O O\n" +
            "  O O O _ O\n" +
            "_ O _ _ _ O _\n" +
            "_ _ _ O _ O O\n" +
            "_ _ _ _ O _ O\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 15\n" +
            "    _ O O\n" +
            "  O O O _ O\n" +
            "_ O _ _ _ O O\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 14\n" +
            "    _ O O\n" +
            "  O O O _ O\n" +
            "_ O _ _ O _ _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 13\n" +
            "    O _ _\n" +
            "  O O O _ O\n" +
            "_ O _ _ O _ _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 12\n" +
            "    _ _ _\n" +
            "  O _ O _ O\n" +
            "_ O O _ O _ _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 11\n" +
            "    _ _ _\n" +
            "  O _ O _ O\n" +
            "_ _ _ O O _ _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 10\n" +
            "    _ _ _\n" +
            "  O _ O _ O\n" +
            "_ _ _ _ _ O _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 9\n" +
            "    _ _ _\n" +
            "  O _ O _ O\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ _ O O _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 8\n" +
            "    _ _ _\n" +
            "  O _ O _ O\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 7\n" +
            "    _ _ _\n" +
            "  O _ O _ O\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 6\n" +
            "    _ _ _\n" +
            "  O _ _ _ O\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 5\n" +
            "Game over!\n" +
            "    _ _ _\n" +
            "  O _ _ _ O\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "  O _ _ _ O\n" +
            "    _ _ _\n" +
            "Score: 5\n", log.toString());
  }

  @Test
  public void testPlayFullGameWithoutQuitting() {
    Readable inputs = new StringReader("4 2 4 4 6 3 4 3 5 1 5 3 5 4 " +
            "5 2 5 6 5 4 7 5 5 5 4 5 6 5 7 3 7 5 7 5 5 5 3 3 5 3 1 3 3 3 2 5 4 5 4 5 6 5 6 5 6 3 " +
            "6 3 4 3 4 3 2 3 3 1 5 1 5 1 5 3 5 3 5 5 3 7 3 5 3 4 3 6 5 7 3 7 3 7 3 5 1 5 1" +
            " 3 1 3 3 3 3 2 3 4 3 4 3 6 3 6 5 6 5 6 5 4 5 4 3 4 2 4 4 4");
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    Appendable log = new StringBuilder();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(model, viewer, inputs);
    controller.playGame();
    assertEquals("    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ _ O O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 31\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "O O _ O O O O\n" +
            "    _ O O\n" +
            "    O O O\n" +
            "Score: 30\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "_ _ O O O O O\n" +
            "    _ O O\n" +
            "    O O O\n" +
            "Score: 29\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "_ O _ _ O O O\n" +
            "    _ O O\n" +
            "    O O O\n" +
            "Score: 28\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "_ O _ O _ _ O\n" +
            "    _ O O\n" +
            "    O O O\n" +
            "Score: 27\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ O O O O O\n" +
            "_ O _ O O _ O\n" +
            "    _ O _\n" +
            "    O O _\n" +
            "Score: 26\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ O O _ O O\n" +
            "_ O _ O _ _ O\n" +
            "    _ O O\n" +
            "    O O _\n" +
            "Score: 25\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ O O _ O O\n" +
            "_ O _ O _ _ O\n" +
            "    _ O O\n" +
            "    _ _ O\n" +
            "Score: 24\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O _ O O _ O O\n" +
            "_ O _ O O _ O\n" +
            "    _ O _\n" +
            "    _ _ _\n" +
            "Score: 23\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O _ O O O O\n" +
            "O _ _ O _ O O\n" +
            "_ O O O O _ O\n" +
            "    _ O _\n" +
            "    _ _ _\n" +
            "Score: 22\n" +
            "    _ O O\n" +
            "    _ O O\n" +
            "O O O O O O O\n" +
            "O _ _ O _ O O\n" +
            "_ O O O O _ O\n" +
            "    _ O _\n" +
            "    _ _ _\n" +
            "Score: 21\n" +
            "    _ O O\n" +
            "    _ O _\n" +
            "O O O O _ O O\n" +
            "O _ _ O O O O\n" +
            "_ O O O O _ O\n" +
            "    _ O _\n" +
            "    _ _ _\n" +
            "Score: 20\n" +
            "    _ O O\n" +
            "    _ O _\n" +
            "O O O O _ O O\n" +
            "O _ _ O _ O O\n" +
            "_ O O O _ _ O\n" +
            "    _ O O\n" +
            "    _ _ _\n" +
            "Score: 19\n" +
            "    _ O O\n" +
            "    _ O _\n" +
            "O O O O _ O O\n" +
            "O _ _ O _ O O\n" +
            "_ O O O _ _ O\n" +
            "    O _ _\n" +
            "    _ _ _\n" +
            "Score: 18\n" +
            "    _ O O\n" +
            "    _ O _\n" +
            "O O O O _ O O\n" +
            "O _ O O _ O O\n" +
            "_ O _ O _ _ O\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 17\n" +
            "    _ O O\n" +
            "    O O _\n" +
            "O O _ O _ O O\n" +
            "O _ _ O _ O O\n" +
            "_ O _ O _ _ O\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 16\n" +
            "    _ O O\n" +
            "    O O _\n" +
            "_ O _ O _ O O\n" +
            "_ _ _ O _ O O\n" +
            "O O _ O _ _ O\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 15\n" +
            "    _ O O\n" +
            "    O O _\n" +
            "_ O _ O _ O O\n" +
            "_ _ _ O _ O O\n" +
            "_ _ O O _ _ O\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 14\n" +
            "    _ O O\n" +
            "    O O _\n" +
            "_ O _ O _ O O\n" +
            "_ _ _ O _ O O\n" +
            "_ _ _ _ O _ O\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 13\n" +
            "    _ O O\n" +
            "    O O _\n" +
            "_ O _ O O _ _\n" +
            "_ _ _ O _ O O\n" +
            "_ _ _ _ O _ O\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 12\n" +
            "    _ O O\n" +
            "    O O _\n" +
            "_ O _ _ _ O _\n" +
            "_ _ _ O _ O O\n" +
            "_ _ _ _ O _ O\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 11\n" +
            "    _ O O\n" +
            "    O O _\n" +
            "_ O _ _ _ O O\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 10\n" +
            "    _ O O\n" +
            "    O O _\n" +
            "_ O _ _ O _ _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 9\n" +
            "    O _ _\n" +
            "    O O _\n" +
            "_ O _ _ O _ _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 8\n" +
            "    _ _ _\n" +
            "    _ O _\n" +
            "_ O O _ O _ _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 7\n" +
            "    _ _ _\n" +
            "    _ O _\n" +
            "_ _ _ O O _ _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 6\n" +
            "    _ _ _\n" +
            "    _ O _\n" +
            "_ _ _ _ _ O _\n" +
            "_ _ _ O _ O _\n" +
            "_ _ _ _ O _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 5\n" +
            "    _ _ _\n" +
            "    _ O _\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ _ O O _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 4\n" +
            "    _ _ _\n" +
            "    _ O _\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 3\n" +
            "    _ _ _\n" +
            "    _ O _\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 2\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 1\n" +
            "Game over!\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "_ _ _ O _ _ _\n" +
            "_ _ _ _ _ _ _\n" +
            "    _ _ _\n" +
            "    _ _ _\n" +
            "Score: 1\n", log.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testExceptionNoMoreInputs() {
    Interaction[] interaction6 = new Interaction[]{
      new InputInteraction("6 4 4 4\n"),
      new PrintInteraction("") // we dont care, because no more inputs, so errors!
    };
    MarbleSolitaireModel model = new EnglishSolitaireModel(7, 7, 7);
    this.testRun(model, interaction6);
  }

  @Test(expected = IllegalStateException.class)
  public void testExceptionController1() {
    MarbleSolitaireModel any = new EnglishSolitaireModel();
    Interaction[] empty = new Interaction[]{};
    try {
      this.testRun(any, empty);
    } catch (IllegalStateException e) {
      throw e;
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testExceptionController2() {
    MarbleSolitaireModel any = new EnglishSolitaireModel();
    Interaction[] empty = new Interaction[]{
        new InputInteraction("6 4 4 4"),
        new PrintInteraction(""),
    };
    try {
      this.testRun(any, empty);
    } catch (IllegalStateException e) {
      throw e;
    }
  }

  @Test
  public void testMockControllerInput1() {
    Readable inputs = new StringReader("6 4 4 4 q");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("fromRow = 5, fromCol= 3, toRow = 3, toCol = 3\n", log.toString());
  }

  @Test
  public void testMockControllerInput2() {
    Readable inputs = new StringReader("2 4 4 4 q");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("fromRow = 1, fromCol= 3, toRow = 3, toCol = 3\n", log.toString());
  }

  @Test
  public void testMockControllerInput3() {
    Readable inputs = new StringReader("q 4 4 4 4");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("", log.toString());
  }

  // QUESTIONS:
  // HANDINS, MOCKMARBLESolitaire (is it right? What should i be testing for other than that?)

  @Test
  public void testMockControllerInput5() {
    Readable inputs = new StringReader("s 6 4 4 4 q");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("fromRow = 5, fromCol= 3, toRow = 3, toCol = 3\n", log.toString());
  }

  @Test
  public void testMockControllerInput4() {
    Readable inputs = new StringReader("s 4 4 4 4 q");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("fromRow = 3, fromCol= 3, toRow = 3, toCol = 3\n", log.toString());
  }

  @Test
  public void testMockControllerInput6() {
    Readable inputs = new StringReader("5 q 4 9 4 q");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("", log.toString());
  }

  @Test
  public void testMockControllerInput7() {
    Readable inputs = new StringReader("5 5 4 9 5 6 7 8 q");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("fromRow = 4, fromCol= 4, toRow = 3, toCol = 8\n" +
            "fromRow = 4, fromCol= 5, toRow = 6, toCol = 7\n", log.toString());
  }

  @Test
  public void testMockControllerInput8() {
    Readable inputs = new StringReader("5 5 4 9 5 6 7 8 1 1 1 1 q");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("fromRow = 4, fromCol= 4, toRow = 3, toCol = 8\n" +
            "fromRow = 4, fromCol= 5, toRow = 6, toCol = 7\n" +
            "fromRow = 0, fromCol= 0, toRow = 0, toCol = 0\n", log.toString());
  }

  @Test
  public void testMockControllerInput9() {
    Readable inputs = new StringReader("5 5 4 9 5 6 7 8 1 1 1 1 5 5 5 q");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("fromRow = 4, fromCol= 4, toRow = 3, toCol = 8\n" +
            "fromRow = 4, fromCol= 5, toRow = 6, toCol = 7\n" +
            "fromRow = 0, fromCol= 0, toRow = 0, toCol = 0\n", log.toString());
  }

  // this test shows how my controller ignores negative values.
  @Test
  public void testMockControllerInput10() {
    Readable inputs = new StringReader("5 -1 4 9 5 6 7 8 1 1 1 1 q");
    StringBuilder log = new StringBuilder();
    Appendable weDontCareOutput = new StringBuilder();
    MarbleSolitaireModel mock = new MockMarbleSolitaire(log);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(mock, weDontCareOutput);
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(mock, viewer, inputs);
    controller.playGame();
    assertEquals("fromRow = 4, fromCol= 3, toRow = 8, toCol = 4\n" +
            "fromRow = 5, fromCol= 6, toRow = 7, toCol = 0\n", log.toString());
  }

  @Test
  public void testMockViewerInput1() {
    Readable input = new StringReader("5 1 1 1 q");
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(log);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model, mockViewer,
            input);
    contoerller.playGame();
    assertEquals("render board\n" +
            "render message: Invalid move. Play again.Either from is not a Marble," +
            " or to is not Empty, or distance is invalid!\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 32\n", log.toString());
  }

  @Test
  public void testMockViewerInput2() {
    Readable input = new StringReader("q 1 5 3 2 5 69 96");
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(log);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model, mockViewer,
            input);
    contoerller.playGame();
    assertEquals("render board\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 32\n", log.toString());
  }

  @Test
  public void testMockViewerInput3() {
    Readable input = new StringReader("6 4 4 4 6 2 6 4 Q");
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(log);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model, mockViewer,
            input);
    contoerller.playGame();
    assertEquals("render board\n" +
            "render board\n" +
            "render message: Score: 31\n" +
            "render message: Invalid move. Play again." +
            "Either from is not a Marble, or to is not Empty, or distance is invalid!\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 31\n", log.toString());
  }

  @Test
  public void testMockViewerInput4() {
    Readable input = new StringReader("6 4 4 4 6 2 6 4 Q");
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(log);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    contoerller.playGame();
    assertEquals("render board\n" +
            "render board\n" +
            "render message: Score: 31\n" +
            "render message: Invalid move. Play again." +
            "Either from is not a Marble, or to is not Empty, or distance is invalid!\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 31\n", log.toString());
  }

  @Test
  public void testMockViewerAndModelInput() {
    Readable input = new StringReader("6 4 4 4 6 2 6 4 Q");
    Appendable logModel = new StringBuilder();
    Appendable logViewer = new StringBuilder();
    MarbleSolitaireModel model = new MockMarbleSolitaire(logModel);
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(logViewer);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    contoerller.playGame();
    assertEquals("fromRow = 5, fromCol= 3, toRow = 3, toCol = 3\n" +
            "fromRow = 5, fromCol= 1, toRow = 5, toCol = 3\n", logModel.toString());
    assertEquals("render board\n" +
            "render board\n" +
            "render message: Score: 0\n" +
            "render board\n" +
            "render message: Score: 0\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 0\n", logViewer.toString());
  }

  @Test
  public void testMockViewerAndModelInput1() {
    Readable input = new StringReader("6 s 4 4 6 2 6 4 Q");
    Appendable logModel = new StringBuilder();
    Appendable logViewer = new StringBuilder();
    MarbleSolitaireModel model = new MockMarbleSolitaire(logModel);
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(logViewer);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    contoerller.playGame();
    assertEquals("fromRow = 5, fromCol= 3, toRow = 3, toCol = 5\n", logModel.toString());
    assertEquals("render board\n" +
            "render board\n" +
            "render message: Score: 0\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 0\n", logViewer.toString());
  }

  @Test
  public void testMockViewerAndModelInput2() {
    Readable input = new StringReader("6 s V G H K1 K2 OODWHO? 4 4 6 2 6 4 Q");
    Appendable logModel = new StringBuilder();
    Appendable logViewer = new StringBuilder();
    MarbleSolitaireModel model = new MockMarbleSolitaire(logModel);
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(logViewer);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    contoerller.playGame();
    assertEquals("fromRow = 5, fromCol= 3, toRow = 3, toCol = 5\n", logModel.toString());
    assertEquals("render board\n" +
            "render board\n" +
            "render message: Score: 0\n" +
            "render message: Game quit!\n" +
            "render message: State of game when quit:\n" +
            "render board\n" +
            "render message: Score: 0\n", logViewer.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testMockViewerAndModelNoMoreInputs() {
    Readable input = new StringReader("6 4 4 6 2 6 4");
    Appendable logModel = new StringBuilder();
    Appendable logViewer = new StringBuilder();
    MarbleSolitaireModel model = new MockMarbleSolitaire(logModel);
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(logViewer);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    try {
      contoerller.playGame();
    }
    catch (IllegalStateException e) {
      throw e;
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMockViewerAndModelNoMoreInputs2() {
    Readable input = new StringReader("6 4 4 6 2 6 4 qq");
    Appendable logModel = new StringBuilder();
    Appendable logViewer = new StringBuilder();
    MarbleSolitaireModel model = new MockMarbleSolitaire(logModel);
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(logViewer);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    try {
      contoerller.playGame();
    }
    catch (IllegalStateException e) {
      throw e;
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testMockViewerAndModelNoMoreInputs3() {
    Readable input = new StringReader("6 4 4 6 2 6 4 quit");
    Appendable logModel = new StringBuilder();
    Appendable logViewer = new StringBuilder();
    MarbleSolitaireModel model = new MockMarbleSolitaire(logModel);
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(logViewer);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    try {
      contoerller.playGame();
    }
    catch (IllegalStateException e) {
      throw e;
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testControllerIOExceptionHandling1() {
    Readable input = new StringReader("6 kisEmak");
    Appendable logModel = new StringBuilder();
    Appendable logViewer = new BrokenAppendable();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(logViewer);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    try {
      contoerller.playGame();
    }
    catch (IllegalStateException e) {
      throw e;
    }
  }


  @Test(expected = IllegalStateException.class)
  public void testControllerIOExceptionHandling() {
    Readable input = new StringReader("6 4 4 6 2 6 4 q");
    Appendable logModel = new StringBuilder();
    Appendable logViewer = new BrokenAppendable();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(logViewer);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    try {
      contoerller.playGame();
    }
    catch (IllegalStateException e) {
      throw e;
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testControllerIOExceptionHandling2() {
    Readable input = new StringReader("q");
    Appendable logModel = new StringBuilder();
    Appendable logViewer = new BrokenAppendable();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView mockViewer = new MockMarbleSolitaireView(logViewer);
    MarbleSolitaireController contoerller = new MarbleSolitaireControllerImpl(model,
            mockViewer, input);
    try {
      contoerller.playGame();
    }
    catch (IllegalStateException e) {
      throw e;
    }
  }

  // test arm thcikness of 5, invalid, then valid, then obviously, quit!!
  @Test
  public void testRunThick52() {
    MarbleSolitaireModel model = new EnglishSolitaireModel(5);
    Interaction[] actions = new Interaction[]{
        new PrintInteraction("        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O"),
        new InputInteraction("6 7 7 7\n"),
        new PrintInteraction("Invalid move. Play again.Either from is not a Marble," +
                    " or to is not Empty, or distance is invalid!"),
        new InputInteraction("5 7 7 7\n"), // scanner issue??
        new PrintInteraction("        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "Score: 103"),
        new InputInteraction("Q\n"),
        new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "Score: 103")
    };
    try {
      this.testRun(model, actions);
    } catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }

  // Test for arm thickness 5, and quit as second input, after valid move
  @Test
  public void testRunThick51() {
    MarbleSolitaireModel model = new EnglishSolitaireModel(5);
    Interaction[] actions = new Interaction[]{
        new PrintInteraction("        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O"),
        new InputInteraction("5 7 7 7\n"), // scanner issue??
        new PrintInteraction("        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "Score: 103"),
        new InputInteraction("q\n"), // scanner issue??
        new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O _ O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "O O O O O O O O O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "        O O O O O\n" +
                    "Score: 103"),
    };
    try {
      this.testRun(model, actions);
    } catch (IllegalStateException e) {
      fail("unexpected IOexception thrown!");
    }
  }

  @Test
  public void testCOntroller3() {
    Readable input = new StringReader("6 5 4 4 q");
    Appendable output = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireTextView viewer = new MarbleSolitaireTextView(model, output);
    MarbleSolitaireController vontrol = new MarbleSolitaireControllerImpl(model, viewer, input);
    vontrol.playGame();
    assertEquals("    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Invalid move. Play again." +
            "Either from is not a Marble, or to is not Empty, or distance is invalid!\n" +
            "Game quit!\n" +
            "State of game when quit:\n" +
            "    O O O\n" +
            "    O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "    O O O\n" +
            "    O O O\n" +
            "Score: 32\n", output.toString());
  }


  /**
   * Function is used to test the marble controller class.
   * With the given arguments, the function will imitate the given inputs to the controller by
   * using the Interaction class. So the controller will take in our given model, the viewer
   *
   * @param model MarbleSolitaireModel model represents the model of the game.
   * @param interactions Interaction... represents the different interactions.
   * @throws IllegalStateException if the game could not be played for whatever reason.
   */
  public void testRun(MarbleSolitaireModel model, Interaction... interactions)
          throws IllegalStateException {
    StringBuilder actualOutput = new StringBuilder();
    MarbleSolitaireTextView viewer = new MarbleSolitaireTextView(model, actualOutput);
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();
    for (Interaction interaction : interactions) {
      interaction.apply(fakeUserInput, expectedOutput);
    }
    Reader input = new StringReader(fakeUserInput.toString());
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(model, viewer, input);
    try {
      controller.playGame();
      assertEquals(expectedOutput.toString(), actualOutput.toString());
    } catch (IllegalStateException e) {
      throw e;
    }
  }

  /**
   * Function is used to test the marble controller class.
   * With the given arguments, the function will imitate the given inputs to the controller by
   * using the Interaction class. So the controller will take in our given model, the viewer
   *
   * @param triangleModel MarbleSolitaireModel model represents the model of the game, a Triangle
   *                      Solitaire model to be passed in!
   * @param interactions Interaction... represents the different interactions.
   * @throws IllegalStateException if the game could not be played for whatever reason.
   */
  public void testRunTriangle(MarbleSolitaireModel triangleModel, Interaction... interactions)
          throws IllegalStateException {
    StringBuilder actualOutput = new StringBuilder();
    MarbleSolitaireView viewer = new TriangleSolitaireTextView(triangleModel, actualOutput);
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();
    for (Interaction interaction : interactions) {
      interaction.apply(fakeUserInput, expectedOutput);
    }
    Reader input = new StringReader(fakeUserInput.toString());
    MarbleSolitaireController controller = new MarbleSolitaireControllerImpl(triangleModel,
            viewer, input);
    try {
      controller.playGame();
      assertEquals(expectedOutput.toString(), actualOutput.toString());
    } catch (IllegalStateException e) {
      throw e;
    }
  }

  @Test
  public void testGame() {
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    Appendable output = new StringBuilder();
    MarbleSolitaireTextView viewer = new MarbleSolitaireTextView(model, output);
    Interaction[] interactions = new Interaction[]{
        new PrintInteraction("    O O O\n" +
                    "    O O O\n" +
                    "O O O O O O O\n" +
                    "O O O _ O O O\n" +
                    "O O O O O O O\n" +
                    "    O O O\n" +
                    "    O O O"),
        new InputInteraction("6 4 4 4"), // scanner issue??
        new PrintInteraction("Invalid move. Play again.Either from is not a Marble," +
                " or to is not Empty, or distance is invalid!"),
        new InputInteraction("6 2 q\n"),
        new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "    O O O\n" +
                    "    O O O\n" +
                    "O O O O O O O\n" +
                    "O O O _ O O O\n" +
                    "O O O O O O O\n" +
                    "    O O O\n" +
                    "    O O O\n" +
                    "Score: 32"),
    };
    try {
      testRun(model, interactions);
    } catch (IllegalStateException e) {
      fail("something bad happened!");
    }
  }

  @Test
  public void testGame2() {
    MarbleSolitaireModel model = new EnglishSolitaireModel(3, 1, 2);
    Appendable output = new StringBuilder();
    MarbleSolitaireTextView viewer = new MarbleSolitaireTextView(model, output);
    Interaction[] interactions = new Interaction[] {
        new PrintInteraction("    O O O\n" +
                    "    _ O O\n" +
                    "O O O O O O O\n" +
                    "O O O O O O O\n" +
                    "O O O O O O O\n" +
                    "    O O O\n" +
                    "    O O O"),
        new InputInteraction("6 4 4 4\n"), // scanner issue??
        new PrintInteraction("Invalid move. Play again." +
                    "Either from is not a Marble, or to is not Empty, or distance is invalid!"),
        new InputInteraction("4 3 2 3\n"), // scanner issue??
        new PrintInteraction("    O O O\n" +
                    "    O O O\n" +
                    "O O _ O O O O\n" +
                    "O O _ O O O O\n" +
                    "O O O O O O O\n" +
                    "    O O O\n" +
                    "    O O O\n" +
                    "Score: 31"),
        new InputInteraction("3 1 3 3 q"),
        new PrintInteraction("    O O O\n" +
                    "    O O O\n" +
                    "_ _ O O O O O\n" +
                    "O O _ O O O O\n" +
                    "O O O O O O O\n" +
                    "    O O O\n" +
                    "    O O O\n" +
                    "Score: 30"),
        new PrintInteraction("Game quit!\n" +
                    "State of game when quit:\n" +
                    "    O O O\n" +
                    "    O O O\n" +
                    "_ _ O O O O O\n" +
                    "O O _ O O O O\n" +
                    "O O O O O O O\n" +
                    "    O O O\n" +
                    "    O O O\n" +
                    "Score: 30"),
    };
    try {
      testRun(model, interactions);
    } catch (IllegalStateException e) {
      fail("something bad happened!");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNull1() {
    controllerTest = new MarbleSolitaireControllerImpl(this.traditional, null, this.input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNull4() {
    controllerTest = new MarbleSolitaireControllerImpl(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNull2() {
    controllerTest = new MarbleSolitaireControllerImpl(null, this.viewer, this.input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNull3() {
    controllerTest = new MarbleSolitaireControllerImpl(this.traditional, this.viewer, null);
  }
}