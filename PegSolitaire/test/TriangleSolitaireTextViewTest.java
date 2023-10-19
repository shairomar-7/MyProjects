
import org.junit.Test;

import java.io.IOException;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModel;
import cs3500.marblesolitaire.view.MarbleSolitaireView;
import cs3500.marblesolitaire.view.TriangleSolitaireTextView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Class designated to testing the TriangleSolitaireTextView class, which is the view component of
 * the TriangleSolitaire game.
 */
public class TriangleSolitaireTextViewTest {

  MarbleSolitaireModel traditional;
  MarbleSolitaireView viewerTraditional;
  MarbleSolitaireModel nonTraditional1;
  MarbleSolitaireView viewerNonTraditional1;
  MarbleSolitaireModel nonTraditional2;
  MarbleSolitaireView viewerNonTraditional2;
  MarbleSolitaireModel nonTraditional3;
  MarbleSolitaireView viewerNonTraditional3;


  private void init() {
    this.traditional = new TriangleSolitaireModel();
    this.viewerTraditional = new TriangleSolitaireTextView(traditional);
    this.nonTraditional1 = new TriangleSolitaireModel(2);
    this.viewerNonTraditional1 = new TriangleSolitaireTextView(nonTraditional1);
    this.nonTraditional2 =  new TriangleSolitaireModel(3, 2, 2);
    this.viewerNonTraditional2 = new TriangleSolitaireTextView(this.nonTraditional2);
    this.nonTraditional3 =  new TriangleSolitaireModel(2, 2);
    this.viewerNonTraditional3 = new TriangleSolitaireTextView(this.nonTraditional3);
  }

  @Test
  public void testToString() {
    this.init();
    assertEquals("    _\n" +
            "   O O\n" +
            "  O O O\n" +
            " O O O O\n" +
            "O O O O O", viewerTraditional.toString());
    assertEquals(" _\n" +
            "O O", viewerNonTraditional1.toString());
    assertEquals("  O\n" +
            " O O\n" +
            "O O _", viewerNonTraditional2.toString());
    assertEquals("    O\n" +
            "   O O\n" +
            "  O O _\n" +
            " O O O O\n" +
            "O O O O O", viewerNonTraditional3.toString());
  }

  @Test
  public void testRenderMessage() {
    Appendable output = new StringBuilder();
    this.init();
    MarbleSolitaireView view = new TriangleSolitaireTextView(traditional, output);

    try {
      view.renderMessage("any, doesn't really matter!");
      assertEquals("any, doesn't really matter!", output.toString());
      view.renderMessage("ANY!!!!");
      assertEquals("any, doesn't really matter!ANY!!!!", output.toString());
    }

    catch (IOException e) {
      fail("hmm something went wrong!");
    }
  }

  @Test
  public void testRenderBoard() {
    Appendable output = new StringBuilder();
    this.init();
    MarbleSolitaireView view = new TriangleSolitaireTextView(traditional, output);

    try {
      view.renderBoard();
      assertEquals("    _\n" +
              "   O O\n" +
              "  O O O\n" +
              " O O O O\n" +
              "O O O O O\n", output.toString());
      traditional.move(2, 2, 0, 0);
      assertEquals("    _\n" +
              "   O O\n" +
              "  O O O\n" +
              " O O O O\n" +
              "O O O O O\n", output.toString());
      view.renderBoard();
      assertEquals("    _\n" +
              "   O O\n" +
              "  O O O\n" +
              " O O O O\n" +
              "O O O O O\n" +
              "    O\n" +
              "   O _\n" +
              "  O O _\n" +
              " O O O O\n" +
              "O O O O O\n", output.toString());
    }

    catch (IOException e) {
      fail("hmm something went wrong!");
    }
  }

  @Test(expected = IOException.class)
  public void testRenderMessageException() throws IOException {
    Appendable brokenAppendable = new BrokenAppendable();
    this.init();
    MarbleSolitaireView view = new TriangleSolitaireTextView(traditional, brokenAppendable);

    try {
      view.renderMessage("any message!");
    }

    catch (IOException e) {
      throw e;
    }
  }

  @Test(expected = IOException.class)
  public void testRenderBoardException() throws IOException {
    Appendable brokenAppendable = new BrokenAppendable();
    this.init();
    MarbleSolitaireView view = new TriangleSolitaireTextView(traditional, brokenAppendable);

    try {
      view.renderBoard();
    }

    catch (IOException e) {
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException1() {
    MarbleSolitaireView view = new TriangleSolitaireTextView(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    MarbleSolitaireView view = new TriangleSolitaireTextView(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException3() {
    MarbleSolitaireView view = new TriangleSolitaireTextView(new TriangleSolitaireModel(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException4() {
    MarbleSolitaireView view = new TriangleSolitaireTextView(null, new StringBuilder());
  }
}