import org.junit.Test;

import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;
import cs3500.marblesolitaire.model.hw04.EuropeanSolitaireModel;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the European Solitaire model.
 */
public class EuropeanSolitaireModelTest {
  MarbleSolitaireModel traditional;
  MarbleSolitaireModel nonTraditional1;
  MarbleSolitaireModel nonTraditional2;
  MarbleSolitaireModel nonTraditional3;
  MarbleSolitaireModelState.SlotState empty = MarbleSolitaireModelState.SlotState.Empty;
  MarbleSolitaireModelState.SlotState marble = MarbleSolitaireModelState.SlotState.Marble;
  MarbleSolitaireModelState.SlotState invalid = MarbleSolitaireModelState.SlotState.Invalid;

  // initializes the above models
  private void init() {
    this.traditional = new EuropeanSolitaireModel();
    this.nonTraditional1 = new EuropeanSolitaireModel(5);
    this.nonTraditional2 = new EuropeanSolitaireModel(3, 0, 2);
    this.nonTraditional3 = new EuropeanSolitaireModel(0, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMove1() {
    this.init();
    this.traditional.move(3, 1, 3, 3);
    this.traditional.move(5, 2, 3, 2);
    traditional.move(4, 0, 4, 2);
    traditional.move(4, 3, 4, 1);
    traditional.move(4, 5, 4, 3);
    traditional.move(6, 4, 4, 4);
    traditional.move(3, 4, 5, 4);
    traditional.move(6, 2, 6, 4);
    traditional.move(6, 4, 4, 4);
    traditional.move(2, 2, 4, 2);
    traditional.move(2, 0, 2, 2);
    traditional.move(1, 1, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMove2() {
    this.init();
    this.nonTraditional3.move(0, 4, 0, 2);
    this.nonTraditional3.move(2, 3, 0, 3);
    this.nonTraditional3.move(0, 2, 0, 4);
    this.nonTraditional3.move(1, 1, 1, 3);
    this.nonTraditional3.move(1, 4, 1, 2);

    this.nonTraditional3.move(2, 1, 2, 3);
    this.nonTraditional3.move(2, 4, 2, 2);

    this.nonTraditional3.move(2, 6, 2, 4);
    this.nonTraditional3.move(3, 4, 1, 4);

    this.nonTraditional3.move(0, 4, 2, 4);
    this.nonTraditional3.move(3, 2, 3, 4);

    this.nonTraditional3.move(1, 2, 1, 4);
  }

  @Test
  public void testMove3() {
    MarbleSolitaireModel model8 = new EuropeanSolitaireModel();
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
    assertEquals(false, model8.isGameOver());
    model8.move(1, 3, 3, 3);
    assertEquals(true, model8.isGameOver());
    assertEquals(5, model8.getScore());
    assertEquals(marble, model8.getSlotAt(1, 1));
    assertEquals(marble, model8.getSlotAt(3, 3));
    assertEquals(marble, model8.getSlotAt(1, 5));
    assertEquals(marble, model8.getSlotAt(5, 5));
    assertEquals(marble, model8.getSlotAt(5, 1));
  }

  @Test
  public void testGetBoardSize() {
    this.init();
    MarbleSolitaireModel model = new EuropeanSolitaireModel(9);
    assertEquals(7, this.traditional.getBoardSize());
    assertEquals(13, this.nonTraditional1.getBoardSize());
    assertEquals(7, this.nonTraditional2.getBoardSize());
    assertEquals(25, model.getBoardSize());
  }

  @Test
  public void testInitialScore() {
    this.init();
    MarbleSolitaireModel model = new EuropeanSolitaireModel(9);
    assertEquals(36, this.traditional.getScore());
    assertEquals(128, this.nonTraditional1.getScore());
    assertEquals(36, this.nonTraditional2.getScore());
    assertEquals(480, model.getScore());
  }

  @Test
  public void testConstructor1() {
    this.init();
    assertEquals(36, this.traditional.getScore());
    assertEquals(7, this.traditional.getBoardSize());
    assertEquals(empty, this.traditional.getSlotAt(3, 3));

    assertEquals(invalid, this.traditional.getSlotAt(0, 0));
    assertEquals(invalid, this.traditional.getSlotAt(0, 1));
    assertEquals(marble, this.traditional.getSlotAt(0, 2));
    assertEquals(invalid, this.traditional.getSlotAt(1,  0));
    assertEquals(marble, this.traditional.getSlotAt(1, 1));
    assertEquals(marble, this.traditional.getSlotAt(1, 2));
    assertEquals(marble, this.traditional.getSlotAt(2, 0));
    assertEquals(invalid, this.traditional.getSlotAt(0, 5));
    assertEquals(invalid, this.traditional.getSlotAt(0, 6));
    assertEquals(marble, this.traditional.getSlotAt(1, 5));
    assertEquals(invalid, this.traditional.getSlotAt(1, 6));
    assertEquals(invalid, this.traditional.getSlotAt(1, 6));
    assertEquals(invalid, this.traditional.getSlotAt(5, 0));
    assertEquals(marble, this.traditional.getSlotAt(5, 1));
    assertEquals(invalid, this.traditional.getSlotAt(6, 0));
    assertEquals(invalid, this.traditional.getSlotAt(6, 1));
    assertEquals(marble, this.traditional.getSlotAt(5, 5));
    assertEquals(invalid, this.traditional.getSlotAt(5, 6));
    assertEquals(invalid, this.traditional.getSlotAt(6, 6));
    assertEquals(invalid, this.traditional.getSlotAt(6, 5));
  }

  @Test
  public void testMoveSideMarbles() {
    MarbleSolitaireModel model = new EuropeanSolitaireModel(0, 4);
    model.move(0, 2, 0, 4);
    assertEquals(empty, model.getSlotAt(0, 2));
    assertEquals(empty, model.getSlotAt(0, 3));
    assertEquals(marble, model.getSlotAt(0, 4));
  }

  @Test
  public void testConstructor2() {
    this.init();
    assertEquals(128, this.nonTraditional1.getScore());
    assertEquals(13, this.nonTraditional1.getBoardSize());
    assertEquals(empty, this.nonTraditional1.getSlotAt(6, 6));

    assertEquals(invalid, this.nonTraditional1.getSlotAt(0, 3));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(0, 2));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(0, 1));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(0, 0));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(1,  0));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(2,  0));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(3,  0));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(2,  1));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(1,  1));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(1,  2));

    assertEquals(invalid, this.nonTraditional1.getSlotAt(0, 9));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(0, 10));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(0, 11));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(0, 12));
    assertEquals(marble, this.nonTraditional1.getSlotAt(1,  9));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(1,  10));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(1,  11));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(1,  12));
    assertEquals(marble, this.nonTraditional1.getSlotAt(2,  9));
    assertEquals(marble, this.nonTraditional1.getSlotAt(2,  10));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(2,  11));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(2,  12));
    assertEquals(marble, this.nonTraditional1.getSlotAt(3,  9));
    assertEquals(marble, this.nonTraditional1.getSlotAt(3,  10));
    assertEquals(marble, this.nonTraditional1.getSlotAt(3,  11));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(3,  12));

    assertEquals(invalid, this.nonTraditional1.getSlotAt(9, 0));
    assertEquals(marble, this.nonTraditional1.getSlotAt(9, 1));
    assertEquals(marble, this.nonTraditional1.getSlotAt(9, 2));
    assertEquals(marble, this.nonTraditional1.getSlotAt(9, 3));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(10,  0));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(10,  1));
    assertEquals(marble, this.nonTraditional1.getSlotAt(10,  2));
    assertEquals(marble, this.nonTraditional1.getSlotAt(10,  3));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(11,  0));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(11,  1));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(11,  2));
    assertEquals(marble, this.nonTraditional1.getSlotAt(11,  3));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(12,  0));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(12,  1));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(12,  2));
    assertEquals(invalid, this.nonTraditional1.getSlotAt(12,  3));
  }

  @Test
  public void testConstructor3() {
    this.init();
    assertEquals(36, this.nonTraditional2.getScore());
    assertEquals(7, this.nonTraditional2.getBoardSize());
    assertEquals(empty, this.nonTraditional2.getSlotAt(0, 2));

    assertEquals(invalid, this.nonTraditional2.getSlotAt(0, 0));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(0, 1));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(1,  0));
    assertEquals(marble, this.nonTraditional2.getSlotAt(1, 1));
    assertEquals(marble, this.nonTraditional2.getSlotAt(1, 2));
    assertEquals(marble, this.nonTraditional2.getSlotAt(2, 0));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(0, 5));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(0, 6));
    assertEquals(marble, this.nonTraditional2.getSlotAt(1, 5));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(1, 6));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(1, 6));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(5, 0));
    assertEquals(marble, this.nonTraditional2.getSlotAt(5, 1));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(6, 0));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(6, 1));
    assertEquals(marble, this.nonTraditional2.getSlotAt(5, 5));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(5, 6));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(6, 6));
    assertEquals(invalid, this.nonTraditional2.getSlotAt(6, 5));
  }

  @Test
  public void testConstructor4() {
    this.init();
    assertEquals(36, this.nonTraditional3.getScore());
    assertEquals(7, this.nonTraditional3.getBoardSize());
    assertEquals(empty, this.nonTraditional3.getSlotAt(0, 2));

    assertEquals(invalid, this.nonTraditional3.getSlotAt(0, 0));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(0, 1));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(1,  0));
    assertEquals(marble, this.nonTraditional3.getSlotAt(1, 1));
    assertEquals(marble, this.nonTraditional3.getSlotAt(1, 2));
    assertEquals(marble, this.nonTraditional3.getSlotAt(2, 0));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(0, 5));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(0, 6));
    assertEquals(marble, this.nonTraditional3.getSlotAt(1, 5));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(1, 6));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(1, 6));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(5, 0));
    assertEquals(marble, this.nonTraditional3.getSlotAt(5, 1));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(6, 0));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(6, 1));
    assertEquals(marble, this.nonTraditional3.getSlotAt(5, 5));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(5, 6));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(6, 6));
    assertEquals(invalid, this.nonTraditional3.getSlotAt(6, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException1() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException2() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel(4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException3() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel(6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException4() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel(6, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException5() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel(7, 0, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException6() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel(3, 1, 6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException7() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel(3, 7, 6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException8() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel(3, -1, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException9() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel( -1, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException10() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel( 6, 6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException11() {
    MarbleSolitaireModel testFail = new EuropeanSolitaireModel( 5, 7);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove1() {
    MarbleSolitaireModel model8 = new EuropeanSolitaireModel();
    model8.move(8, 4, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove2() {
    MarbleSolitaireModel model8 = new EuropeanSolitaireModel();
    model8.move(3, 5, 6, 7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove3() {
    MarbleSolitaireModel model8 = new EuropeanSolitaireModel();
    model8.move(3, 3, 3, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove4() {
    MarbleSolitaireModel model8 = new EuropeanSolitaireModel();
    model8.move(5, 3, 5, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionMove5() {
    MarbleSolitaireModel model8 = new EuropeanSolitaireModel();
    model8.move(5, 3, 5, 1);
  }
}