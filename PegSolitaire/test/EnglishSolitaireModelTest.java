import org.junit.Assert;
import org.junit.Test;

import cs3500.marblesolitaire.model.hw02.EnglishSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;

import static org.junit.Assert.assertEquals;

/**
 * Class designated to testing the EnglishSolitaireModel class.
 */
public class EnglishSolitaireModelTest {

  MarbleSolitaireModel model1 = new EnglishSolitaireModel();
  MarbleSolitaireModel model2 = new EnglishSolitaireModel(2, 0);
  MarbleSolitaireModel model3 = new EnglishSolitaireModel(3, 4, 6);
  MarbleSolitaireModel model4 = new EnglishSolitaireModel(5, 4, 6);
  MarbleSolitaireModel model5 = new EnglishSolitaireModel(3);
  MarbleSolitaireModel model6 = new EnglishSolitaireModel(5);
  MarbleSolitaireModel model7 = new EnglishSolitaireModel(7);
  MarbleSolitaireModel model8;
  MarbleSolitaireModel model9;


  private void init() {
    model8 = new EnglishSolitaireModel();
    model9 = new EnglishSolitaireModel(5);
  }


  @Test
  public void testScore() {
    this.init();
    Assert.assertEquals(MarbleSolitaireModelState.SlotState.Empty, model3.getSlotAt(4, 6));
    assertEquals(32, model1.getScore());
    model8.move(5,3,3, 3);
    assertEquals(31, model8.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(5, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(3, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 3));
    model8.move(4,1,4, 3);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(5, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(3, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(4, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 2));
    model8.move(6, 2, 4, 2);
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(4, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(4, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(5, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(5, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(6, 2));
    model8.move(3, 2, 5, 2);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(4, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(5, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(5, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(6, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(3, 2));
    model8.move(3, 4, 3, 2);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(3, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(3, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(3, 3));
    model8.move(2, 1, 4, 1);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(3, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(4, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(2, 1));
    assertEquals(26, model8.getScore());
    model8.move(4, 0, 4, 2);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(4, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 0));
    assertEquals(25, model8.getScore());
    model8.move(2, 0, 4, 0);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(3, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(4, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(2, 0));
    assertEquals(24, model8.getScore());
    model8.move(2, 3, 2, 1);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(2, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(2, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(2, 1));
    assertEquals(23, model8.getScore());
    model8.move(4, 3, 4, 1);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(4, 1));
    assertEquals(22, model8.getScore());
    assertEquals(false, model8.isGameOver());
    model8.move(4, 0, 4, 2);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(4, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(4, 0));
    assertEquals(21, model8.getScore());
    assertEquals(false, model8.isGameOver());
    model8.move(0, 2, 2, 2);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(1, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(2, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(0, 2));
    model8.move(0, 3, 2, 3);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(1, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(2, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(0, 3));
    model8.move(3, 2, 1, 2);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(2, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(1, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(3, 2));
    model8.move(2, 4, 2, 2);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(2, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(2, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(2, 4));
    model8.move(1, 2, 3, 2);
    model8.move(4, 2, 2, 2);
    model8.move(2, 1, 2, 3);
    model8.move(0, 4, 2, 4);
    model8.move(6, 4, 6, 2);
    model8.move(6, 2, 4, 2);
    model8.move(4, 5, 4, 3);
    model8.move(4, 3, 4, 1);
    model8.move(2, 4, 2, 2);
    model8.move(2, 5, 4, 5);
    model8.move(4, 6, 4, 4);
    model8.move(5, 4, 3, 4);
    model8.move(2, 6, 4, 6);
    assertEquals(true, model8.isGameOver());
    assertEquals(4, model8.getScore());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGameSolver() {
    this.init();
    model8.move(3, 1, 3, 3);
    model8.move(5, 2, 3, 2);
    model8.move(4, 0, 4, 2);
    model8.move(4, 3, 4, 1);
    model8.move(4, 5, 4, 3);
    model8.move(6, 4, 4, 4);
    model8.move(3, 4, 5, 4);
    model8.move(6, 2, 6, 4);
    model8.move(6, 4, 4, 4);
    model8.move(2, 2, 4, 2);
    model8.move(0, 2, 2, 2);
    model8.move(1, 4, 3, 4);
    model8.move(3, 4, 5, 4);
    model8.move(5, 4, 5, 2);
    model8.move(5, 2, 3, 2);
    model8.move(3, 2, 1, 2);
    model8.move(2, 0, 4, 0);
    model8.move(4, 0, 4, 2);
    model8.move(4, 2, 4, 4);
    model8.move(2, 6, 2, 4);
    model8.move(2, 3, 2, 5);
    model8.move(4, 6, 2, 6);
    model8.move(2, 6, 2, 4);
    model8.move(0, 4, 0, 2);
    model8.move(0, 2, 2, 2);
    model8.move(2, 1, 2, 3);
    model8.move(2, 3, 2, 5);
    model8.move(2, 5, 4, 5);
    model8.move(4, 5, 4, 3);
    model8.move(4, 3, 2, 3);
    model8.move(1, 3, 3, 3);
    assertEquals(1, model8.getScore());
    assertEquals(true, model8.isGameOver());
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(3, 3));
    model8.move(3,3, 1, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMove2() {
    this.init();
    assertEquals(104, model9.getScore());
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model9.getSlotAt(6,6));
    model9.move(4, 6, 6, 6);
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model9.getSlotAt(6,6));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model9.getSlotAt(4,6));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model9.getSlotAt(5,6));
    model9.move(2, 6, 4, 6);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model9.getSlotAt(2,6));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model9.getSlotAt(4,6));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model9.getSlotAt(3,6));
    assertEquals(102, model9.getScore());
    model9.move(1, 5,2, 6);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testGetSlotAtEdges() {
    this.init();
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, model8.getSlotAt(0, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(0, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(1, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(0, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(1, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, model8.getSlotAt(6, 6));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, model9.getSlotAt(0, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, model9.getSlotAt(0, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model9.getSlotAt(6, 6));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model9.getSlotAt(4, 12));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model9.getSlotAt(0, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model9.getSlotAt(13, 12));
  }


  @Test
  public void testMove() {
    this.init();
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(3, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(3, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(3, 2));
    assertEquals(32, model8.getScore());
    model8.move(3, 1, 3, 3);
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model8.getSlotAt(3, 1));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model8.getSlotAt(3, 3));
    assertEquals(31, model8.getScore());
  }

  @Test
  public void testGetBoardSize() {
    assertEquals(7, model1.getBoardSize());
    assertEquals(7, model2.getBoardSize());
    assertEquals(7, model3.getBoardSize());
    assertEquals(13, model4.getBoardSize());
    assertEquals(7, model5.getBoardSize());
    assertEquals(19, model7.getBoardSize());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSlotState() {
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model1.getSlotAt(0, 2));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model1.getSlotAt(3, 3));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model4.getSlotAt(4, 6));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model3.getSlotAt(4, 6));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model6.getSlotAt(6, 6));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model1.getSlotAt(0, 4));
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, model1.getSlotAt(0, 5));
    assertEquals(new IllegalArgumentException(""), model1.getSlotAt(-1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSlotStateException() {
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model7.getSlotAt(9, 9));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model7.getSlotAt(5, 9));
    assertEquals(MarbleSolitaireModelState.SlotState.Empty, model2.getSlotAt(2, 0));
    assertEquals(MarbleSolitaireModelState.SlotState.Marble, model2.getSlotAt(3, 3));
    assertEquals(new IllegalArgumentException(""), model2.getSlotAt(6, 7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSlotAtException() {
    assertEquals(MarbleSolitaireModelState.SlotState.Invalid, model2.getSlotAt(0, 0));
    assertEquals(new IllegalArgumentException(""), model1.getSlotAt(0, 7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException1() {
    assertEquals(new IllegalArgumentException(""), new EnglishSolitaireModel(7, 7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException2() {
    assertEquals(new IllegalArgumentException(""), new EnglishSolitaireModel(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException3() {
    assertEquals(new IllegalArgumentException(""), new EnglishSolitaireModel(4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException4() {
    assertEquals(new IllegalArgumentException(""), new EnglishSolitaireModel(4, 3, 3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException5() {
    assertEquals(new IllegalArgumentException(""), new EnglishSolitaireModel(3, 6, 7));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException6() {
    assertEquals(new IllegalArgumentException(""), new EnglishSolitaireModel(7, -1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove1() {
    this.model8 = new EnglishSolitaireModel();
    this.model8.move(8, 4, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove2() {
    this.model8 = new EnglishSolitaireModel();
    this.model8.move(3, 5, 6, 7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove3() {
    this.model8 = new EnglishSolitaireModel();
    this.model8.move(3, 3, 3, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove4() {
    this.model8 = new EnglishSolitaireModel();
    this.model8.move(5, 3, 5, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove5() {
    this.model8 = new EnglishSolitaireModel();
    this.model8.move(5, 3, 5, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException7() {
    assertEquals(new IllegalArgumentException(""), new EnglishSolitaireModel(1));
  }
}