import org.junit.Test;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModel;
import cs3500.marblesolitaire.model.hw02.MarbleSolitaireModelState;
import cs3500.marblesolitaire.model.hw04.TriangleSolitaireModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Class designated to testing the TriangleSolitaireModel class.
 */
public class TriangleSolitaireModelTest {
  MarbleSolitaireModel traditional;
  MarbleSolitaireModel nonTraditional1;
  MarbleSolitaireModel nonTraditional2;
  MarbleSolitaireModel nonTraditional3;
  MarbleSolitaireModelState.SlotState empty = MarbleSolitaireModelState.SlotState.Empty;
  MarbleSolitaireModelState.SlotState marble = MarbleSolitaireModelState.SlotState.Marble;
  MarbleSolitaireModelState.SlotState invalid = MarbleSolitaireModelState.SlotState.Invalid;

  /**
   * Initializes a few fields of this class to avoid declaration repetition.
   */
  private void init() {
    traditional = new TriangleSolitaireModel();
    nonTraditional1 = new TriangleSolitaireModel(2, 0, 0);
    nonTraditional3 = new TriangleSolitaireModel( 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMove1() {
    this.init();
    traditional.move(2, 0, 0, 0);
    assertEquals(empty,traditional.getSlotAt(2, 0));
    assertEquals(empty,traditional.getSlotAt(1, 0));
    assertEquals(marble,traditional.getSlotAt(0, 0));
    assertEquals(13, traditional.getScore());
    traditional.move(2, 2, 2, 0);
    assertEquals(empty,traditional.getSlotAt(2, 2));
    assertEquals(empty,traditional.getSlotAt(2, 1));
    assertEquals(marble,traditional.getSlotAt(2, 0));
    assertEquals(12, traditional.getScore());
    // jumps over non empty marble
    traditional.move(2, 0, 2, 2);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testMove3() {
    this.init();
    traditional.move(2, 0, 0, 0);
    assertEquals(empty,traditional.getSlotAt(2, 0));
    assertEquals(empty,traditional.getSlotAt(1, 0));
    assertEquals(marble,traditional.getSlotAt(0, 0));
    assertEquals(13, traditional.getScore());
    traditional.move(2, 2, 2, 0);
    assertEquals(empty,traditional.getSlotAt(2, 2));
    assertEquals(empty,traditional.getSlotAt(2, 1));
    assertEquals(marble,traditional.getSlotAt(2, 0));
    assertEquals(12, traditional.getScore());
    traditional.move(4, 1, 2, 1);
    assertEquals(empty,traditional.getSlotAt(4, 1));
    assertEquals(empty,traditional.getSlotAt(3, 1));
    assertEquals(marble,traditional.getSlotAt(2, 1));
    assertEquals(11, traditional.getScore());
    traditional.move(0, 0, 2, 2);
    assertEquals(empty,traditional.getSlotAt(0, 0));
    assertEquals(empty,traditional.getSlotAt(1, 1));
    assertEquals(marble,traditional.getSlotAt(2, 2));
    assertEquals(10, traditional.getScore());
    traditional.move(3, 2, 1, 0);
    traditional.move(2, 0, 0, 0);
    assertFalse(traditional.isGameOver());
    traditional.move(4, 0, 2, 0);
    assertFalse(traditional.isGameOver());
    traditional.move(4, 3, 4, 1);
    assertFalse(traditional.isGameOver());
    traditional.move(3, 3, 1, 1);
    assertFalse(traditional.isGameOver());
    traditional.move(0, 0, 2, 2);
    assertTrue(traditional.isGameOver());
    assertEquals(4, traditional.getScore());
    traditional.move(4, 4, 1, 1);
  }



  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromDoesNotExist() {
    this.init();
    traditional.move(0, 2, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDiagonally() {
    this.init();
    traditional.move(2, 2, 0, 0);
    traditional.move(2, 2, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDiagonally1() {
    this.init();
    traditional.move(2, 2, 0, 0);
    traditional.move(1, 0, 2, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToDoesNotExist() {
    this.init();
    traditional.move(2, 2, 0, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBetweenEmpty() {
    this.init();
    traditional.move(2, 2, 0, 0);
    traditional.move(0, 0, 2, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistanceTooFar() {
    this.init();
    traditional.move(2, 2, 0, 0);
    traditional.move(4, 0, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistanceInvalid() {
    this.init();
    traditional.move(2, 2, 0, 0);
    traditional.move(3, 2, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistanceInvalid2() {
    this.init();
    traditional.move(2, 2, 0, 0);
    traditional.move(2, 0, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDistanceInvalid3() {
    this.init();
    traditional.move(2, 2, 0, 0);
    traditional.move(2, 0, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetSlotAtInvalid() {
    this.init();
    assertEquals(invalid, traditional.getSlotAt(0, 1));
    assertEquals(invalid, traditional.getSlotAt(0, 2));
    assertEquals(invalid, traditional.getSlotAt(0, 3));
    assertEquals(invalid, traditional.getSlotAt(0, 4));
    assertEquals(invalid, traditional.getSlotAt(1, 2));
    assertEquals(invalid, traditional.getSlotAt(2, 3));
    assertEquals(invalid, traditional.getSlotAt(2, 4));
    // the below test will throw an exception, given coordinates is beyond board dimensions!
    assertEquals(invalid, traditional.getSlotAt(0, 5));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testConstructor1() {
    this.init();
    assertEquals(5, traditional.getBoardSize());
    assertEquals(14, traditional.getScore());
    assertEquals(empty, traditional.getSlotAt(0, 0));

    assertEquals(marble, traditional.getSlotAt(1, 0));
    assertEquals(marble, traditional.getSlotAt(1, 1));
    assertEquals(marble, traditional.getSlotAt(2, 0));
    assertEquals(marble, traditional.getSlotAt(2, 1));
    assertEquals(marble, traditional.getSlotAt(2, 2));
    assertEquals(marble, traditional.getSlotAt(3, 0));
    assertEquals(marble, traditional.getSlotAt(3, 1));
    assertEquals(marble, traditional.getSlotAt(3, 2));
    assertEquals(marble, traditional.getSlotAt(3, 3));
    assertEquals(marble, traditional.getSlotAt(4, 0));
    assertEquals(marble, traditional.getSlotAt(4, 1));
    assertEquals(marble, traditional.getSlotAt(4, 2));
    assertEquals(marble, traditional.getSlotAt(4, 3));
    assertEquals(marble, traditional.getSlotAt(4, 4));

    assertEquals(marble, traditional.getSlotAt(5, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor2() {
    this.init();
    assertEquals(2, nonTraditional1.getBoardSize());
    assertEquals(2, nonTraditional1.getScore());
    assertEquals(empty, nonTraditional1.getSlotAt(0, 0));
    assertEquals(marble, nonTraditional1.getSlotAt(1, 0));
    assertEquals(marble, nonTraditional1.getSlotAt(1, 1));
    //the below test is indeed supposed to fail this test, thus passing it.
    assertEquals(marble, nonTraditional1.getSlotAt(2, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor3() {
    this.init();
    assertEquals(5, nonTraditional3.getBoardSize());
    assertEquals(14, nonTraditional3.getScore());
    assertEquals(empty, nonTraditional3.getSlotAt(0, 0));
    assertEquals(marble, nonTraditional3.getSlotAt(1, 0));
    assertEquals(marble, nonTraditional3.getSlotAt(1, 1));
    assertEquals(marble, nonTraditional3.getSlotAt(4, 4));
    //the below test is indeed supposed to fail this test, thus passing it.
    assertEquals(marble, nonTraditional3.getSlotAt(5, 4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor4() {
    MarbleSolitaireModel thicknessOne = new TriangleSolitaireModel(1);
    assertEquals(1, thicknessOne.getBoardSize());
    assertEquals(0, thicknessOne.getScore());
    assertEquals(empty, thicknessOne.getSlotAt(0, 0));
    //the below test is indeed supposed to fail this test, thus passing it.
    assertEquals(marble, thicknessOne.getSlotAt(1, 0));
  }

  @Test
  public void testSize1() {
    MarbleSolitaireModel testPass = new TriangleSolitaireModel(1);
    assertTrue(testPass.isGameOver());
  }

  @Test
  public void testSize2() {
    MarbleSolitaireModel testPass = new TriangleSolitaireModel(2);
    assertTrue(testPass.isGameOver());
  }


  @Test(expected = IllegalArgumentException.class)
  public void testException1() {
    MarbleSolitaireModel testFail = new TriangleSolitaireModel(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException2() {
    MarbleSolitaireModel testFail = new TriangleSolitaireModel(5, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException3() {
    MarbleSolitaireModel testFail = new TriangleSolitaireModel(4, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException4() {
    MarbleSolitaireModel testFail = new TriangleSolitaireModel(-1, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException5() {
    MarbleSolitaireModel testFail = new TriangleSolitaireModel(2, 2, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException6() {
    MarbleSolitaireModel testFail = new TriangleSolitaireModel(15, 15, 0);
  }
}
