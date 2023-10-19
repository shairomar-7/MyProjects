import org.junit.Test;
import java.io.IOException;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;
import cs3500.marblesolitaire.model.hw04.EuropeanSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireTextView;
import cs3500.marblesolitaire.view.MarbleSolitaireView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Class designated to testing the MarbleSolitaireTextView class.
 */
public class MarbleSolitaireTextViewTest {
  MarbleSolitaireModel traditional = new EnglishSolitaireModel();
  MarbleSolitaireTextView viewer = new MarbleSolitaireTextView(traditional);
  MarbleSolitaireModel nonTraditional = new EnglishSolitaireModel(5, 5, 0);
  MarbleSolitaireTextView viewer2 = new MarbleSolitaireTextView(nonTraditional);
  MarbleSolitaireModel modelFive = new EnglishSolitaireModel(5);
  MarbleSolitaireTextView viewerFive = new MarbleSolitaireTextView(modelFive);
  MarbleSolitaireModel nonTraditional2 = new EnglishSolitaireModel(7);
  MarbleSolitaireTextView viewer3 = new MarbleSolitaireTextView(nonTraditional2);


  @Test
  public void testToStringEuropean() {
    MarbleSolitaireModel european = new EuropeanSolitaireModel();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(european);
    assertEquals("    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "O O O O O O O\n" +
            "  O O O O O\n" +
            "    O O O", viewer.toString());
    european.move(5, 3, 3, 3);
    assertEquals("    O O O\n" +
            "  O O O O O\n" +
            "O O O O O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\n" +
            "  O O _ O O\n" +
            "    O O O", viewer.toString());
  }

  @Test
  public void testToStringEuropean2() {
    MarbleSolitaireModel european = new EuropeanSolitaireModel(5, 5, 0);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(european);
    assertEquals("        O O O O O\n" +
            "      O O O O O O O\n" +
            "    O O O O O O O O O\n" +
            "  O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "_ O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "  O O O O O O O O O O O\n" +
            "    O O O O O O O O O\n" +
            "      O O O O O O O\n" +
            "        O O O O O", viewer.toString());
    european.move(5, 2, 5, 0);
    assertEquals("        O O O O O\n" +
            "      O O O O O O O\n" +
            "    O O O O O O O O O\n" +
            "  O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O _ _ O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "  O O O O O O O O O O O\n" +
            "    O O O O O O O O O\n" +
            "      O O O O O O O\n" +
            "        O O O O O", viewer.toString());
    european.move(7, 1, 5, 1);
    assertEquals("        O O O O O\n" +
            "      O O O O O O O\n" +
            "    O O O O O O O O O\n" +
            "  O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O _ O O O O O O O O O O\n" +
            "O _ O O O O O O O O O O O\n" +
            "O _ O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "  O O O O O O O O O O O\n" +
            "    O O O O O O O O O\n" +
            "      O O O O O O O\n" +
            "        O O O O O", viewer.toString());
    european.move(5, 0, 5, 2);
    assertEquals("        O O O O O\n" +
            "      O O O O O O O\n" +
            "    O O O O O O O O O\n" +
            "  O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "_ _ O O O O O O O O O O O\n" +
            "O _ O O O O O O O O O O O\n" +
            "O _ O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "  O O O O O O O O O O O\n" +
            "    O O O O O O O O O\n" +
            "      O O O O O O O\n" +
            "        O O O O O", viewer.toString());
  }

  @Test
  public void testRenderBoardAndMessageEuropean() {
    MarbleSolitaireModel model = new EuropeanSolitaireModel();
    Appendable output = new StringBuilder();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, output);
    try {
      viewer.renderBoard();
      assertEquals("    O O O\n" +
              "  O O O O O\n" +
              "O O O O O O O\n" +
              "O O O _ O O O\n" +
              "O O O O O O O\n" +
              "  O O O O O\n" +
              "    O O O\n", output.toString());
      model.move(5, 3, 3,3 );
      viewer.renderBoard();
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
              "O O O O O O O\n" +
              "O O O _ O O O\n" +
              "  O O _ O O\n" +
              "    O O O\n", output.toString());
      viewer.renderMessage("any message!");
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
              "O O O O O O O\n" +
              "O O O _ O O O\n" +
              "  O O _ O O\n" +
              "    O O O\n" +
              "any message!", output.toString());
    }

    catch (IOException e) {
      fail("hmm, somethign went wrong!");
    }

  }

  @Test
  public void testRenderBoardAndMessageEuropean2() {
    MarbleSolitaireModel model = new EuropeanSolitaireModel(7);
    Appendable output = new StringBuilder();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, output);
    try {
      viewer.renderBoard();
      assertEquals("            O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O _ O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "            O O O O O O O\n", output.toString());
      model.move(9, 7, 9,9);
      viewer.renderBoard();
      assertEquals("            O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O _ O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "            O O O O O O O\n" +
              "            O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O _ _ O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "            O O O O O O O\n", output.toString());
      viewer.renderMessage("any message!");
      assertEquals("            O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O _ O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "            O O O O O O O\n" +
              "            O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O _ _ O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "O O O O O O O O O O O O O O O O O O O\n" +
              "  O O O O O O O O O O O O O O O O O\n" +
              "    O O O O O O O O O O O O O O O\n" +
              "      O O O O O O O O O O O O O\n" +
              "        O O O O O O O O O O O\n" +
              "          O O O O O O O O O\n" +
              "            O O O O O O O\n" +
              "any message!", output.toString());
    }

    catch (IOException e) {
      fail("hmm, somethign went wrong!");
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testToString() {
    assertEquals("    O O O\n    " +
            "O O O\n" +
            "O O O O O O O\n" +
            "O O O _ O O O\nO O O O O O O\n    O O O\n    O O O", viewer.toString());
    traditional.move(5, 3, 3, 3);
    assertEquals("    O O O\n    " +
            "O O O\nO O O O O O O\n" +
            "O O O O O O O\nO O O _ O O O\n" +
            "    O _ O\n    O O O", viewer.toString());
    assertEquals("        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "_ O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O", this.viewer2.toString());
    nonTraditional.move(5, 2, 5, 0);
    assertEquals("        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O _ _ O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O",this.viewer2.toString());
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, nonTraditional.getSlotAt(5,
            0));
    nonTraditional.move(7, 1, 5, 1);
    assertEquals("        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "O O _ O O O O O O O O O O\n" +
            "O _ O O O O O O O O O O O\n" +
            "O _ O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O",this.viewer2.toString());
    nonTraditional.move(5, 0, 5, 2);
    assertEquals("        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "_ _ O O O O O O O O O O O\n" +
            "O _ O O O O O O O O O O O\n" +
            "O _ O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O\n" +
            "        O O O O O",this.viewer2.toString());
    assertEquals("        O O O O O\n" +
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
            "        O O O O O", viewerFive.toString());
    assertEquals("            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O O O _ O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O", viewer3.toString());
    nonTraditional2.move(9, 7, 9, 9);
    assertEquals("            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O _ _ O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "O O O O O O O O O O O O O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O\n" +
            "            O O O O O O O", viewer3.toString());
    nonTraditional.move(5, 2, 5, 5);
  }

  @Test
  public void testRenderBoar1d() {
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    model.move(3, 1, 3, 3);
    model.move(5, 2, 3, 2);
    model.move(4, 0, 4, 2);
    model.move(4, 3, 4, 1);
    model.move(4, 5, 4, 3);
    model.move(6, 4, 4, 4);
    model.move(3, 4, 5, 4);
    model.move(6, 2, 6, 4);
    model.move(6, 4, 4, 4);
    model.move(2, 2, 4, 2);
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
    try {
      viewer.renderBoard();
      assertEquals("    O O O\n" +
              "    O O O\n" +
              "O O _ O O O O\n" +
              "O _ _ O _ O O\n" +
              "_ O O O O _ O\n" +
              "    _ O _\n" +
              "    _ _ _\n", log.toString());
    }

    catch (IOException e) {
      fail("Test failed, something unexpected happened!");
    }
  }

  @Test
  public void testRenderBoard() {
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
    try {
      viewer.renderBoard();
      assertEquals("    O O O\n" +
             "    O O O\n" +
             "O O O O O O O\n" +
             "O O O _ O O O\n" +
             "O O O O O O O\n" +
             "    O O O\n" +
             "    O O O\n", log.toString());
    }

    catch (IOException e) {
      fail("Test failed, something unexpected happened!");
    }
  }

  @Test
  public void testRenderBoard2() {
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
    model.move(5, 3, 3, 3);
    try {
      viewer.renderBoard();
      assertEquals("    O O O\n" +
              "    O O O\n" +
              "O O O O O O O\n" +
              "O O O O O O O\n" +
              "O O O _ O O O\n" +
              "    O _ O\n" +
              "    O O O\n", log.toString());
    }

    catch (IOException e) {
      fail("Test failed, something unexpected happened!");
    }

  }

  @Test
  public void testRenderBoard3() {
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
    model.move(5, 3, 3, 3);
    model.move(2, 3, 4, 3);
    try {
      viewer.renderBoard();
      assertEquals("    O O O\n" +
              "    O O O\n" +
              "O O O _ O O O\n" +
              "O O O _ O O O\n" +
              "O O O O O O O\n" +
              "    O _ O\n" +
              "    O O O\n", log.toString());
    }

    catch (IOException e) {
      fail("Test failed, something unexpected happened!");
    }

  }

  @Test
  public void testRenderMessage1() {
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
    try {
      viewer.renderMessage("Yo!");
      assertEquals("Yo!", log.toString());
    }

    catch (IOException e) {
      fail("Test failed, something unexpected happened!");
    }

  }

  @Test
  public void testRenderMessage2() {
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
    try {
      viewer.renderMessage("Yo! Yo! Yo!");
      assertEquals("Yo! Yo! Yo!", log.toString());
    }

    catch (IOException e) {
      fail("Test failed, something unexpected happened!");
    }

  }

  @Test
  public void testRenderMessage3() {
    Appendable log = new StringBuilder();
    MarbleSolitaireModel model = new EnglishSolitaireModel();
    MarbleSolitaireView viewer = new MarbleSolitaireTextView(model, log);
    try {
      viewer.renderMessage("These tests are just a waste of our time! You prob skipped over this");
      assertEquals("These tests are just a waste of our time! You prob skipped over this"
              , log.toString());
    }

    catch (IOException e) {
      fail("Test failed, something unexpected happened!");
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testException() {
    MarbleSolitaireTextView testError = new MarbleSolitaireTextView(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException2() {
    MarbleSolitaireTextView testError = new MarbleSolitaireTextView(null,
            new StringBuilder());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException3() {
    MarbleSolitaireTextView testError = new MarbleSolitaireTextView(new EnglishSolitaireModel(),
            null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException4() {
    MarbleSolitaireTextView testError = new MarbleSolitaireTextView(null, null);
  }

  @Test(expected = IOException.class)
  public void testExceptionRender5() throws IOException {
    try {
      MarbleSolitaireModel dummy = new EnglishSolitaireModel();
      MarbleSolitaireView viewer = new MarbleSolitaireTextView(dummy, new BrokenAppendable());
      viewer.renderBoard();
    }

    catch (IOException e) {
      System.out.println("failed");
      throw e;
    }

  }

  @Test(expected = IOException.class)
  public void testExceptionRenderMessage() throws IOException {
    try {
      MarbleSolitaireModel dummy = new EnglishSolitaireModel();
      MarbleSolitaireView viewer = new MarbleSolitaireTextView(dummy, new BrokenAppendable());
      viewer.renderMessage("any message!");
    }

    catch (IOException e) {
      System.out.println("failed");
      throw e;
    }

  }

  @Test(expected = IOException.class)
  public void testExceptionRenderMessageEuropean() throws IOException {
    try {
      MarbleSolitaireModel dummy = new EuropeanSolitaireModel();
      MarbleSolitaireView viewer = new MarbleSolitaireTextView(dummy, new BrokenAppendable());
      viewer.renderMessage("any message!");
    }

    catch (IOException e) {
      System.out.println("failed");
      throw e;
    }

  }

  @Test(expected = IOException.class)
  public void testExceptionRenderBoardEuropean() throws IOException {
    try {
      MarbleSolitaireModel dummy = new EuropeanSolitaireModel();
      MarbleSolitaireView viewer = new MarbleSolitaireTextView(dummy, new BrokenAppendable());
      viewer.renderBoard();
    }

    catch (IOException e) {
      System.out.println("failed");
      throw e;
    }

  }
}