import org.junit.Assert;
import org.junit.Test;
import java.util.List;

import cs3500.marblesolitaire.model.hw02.BoardBuilder;
import cs3500.marblesolitaire.model.hw02.Marble;

import static org.junit.Assert.assertEquals;

/**
 * Class designated to testing the BoardBuilder class.
 */
public class BoardBuilderTest {
  List<Marble> testing = new BoardBuilder().buildBoard(3, 3, 3, true);
  List<Marble> testing2 = new BoardBuilder().buildBoard(5, 4, 5, true);

  @Test(expected = IllegalArgumentException.class)
  public void testException() {
    List<Marble> marbles = new BoardBuilder().buildBoard(3, 7, 6,true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException2() {
    List<Marble> marbles = new BoardBuilder().buildBoard(4, 3, 3, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException3() {
    List<Marble> marbles = new BoardBuilder().buildBoard(1, 0, 0, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException4() {
    List<Marble> marbles = new BoardBuilder().buildTriangleBoard(0, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException5() {
    List<Marble> marbles = new BoardBuilder().buildTriangleBoard(2, 0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException6() {
    List<Marble> marbles = new BoardBuilder().buildTriangleBoard(5, 4, 5);
  }

  @Test
  public void testBuildTriangles() {
    assertEquals(21, new BoardBuilder().buildTriangleBoard(6, 0, 0).size());
    assertEquals(15, new BoardBuilder().buildTriangleBoard(5, 0, 0).size());
    assertEquals(10, new BoardBuilder().buildTriangleBoard(4, 0, 0).size());
    assertEquals(6, new BoardBuilder().buildTriangleBoard(3, 0, 0).size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuild() {
    assertEquals(49, new BoardBuilder().buildBoard(3, 3, 3, true).size());
    assertEquals(169, new BoardBuilder().buildBoard(5, 6, 6, true).size());
    assertEquals(169, new BoardBuilder().buildBoard(5, 7, 7, true).size());
    List<Marble> marbles = new BoardBuilder().buildBoard(5, 3, 3, true);
  }

  // the index math is as follows: marbles.get(col * this.getBoardSize() + row)
  @Test
  public void testBuild2() {
    Assert.assertEquals("empty",testing.get(3 * 7 + 3).getState());
    assertEquals("marble",testing.get(3 * 7 + 5).getState());
    assertEquals("invalid",testing.get(0 * 7 + 0).getState());
    assertEquals("invalid",testing.get(5 * 7 + 0).getState());
    assertEquals("empty", testing2.get(5 * 13 + 4).getState());
    assertEquals("marble", testing2.get(5 * 13 + 3).getState());
    assertEquals("invalid", testing2.get(0 * 13 + 0).getState());
  }
}