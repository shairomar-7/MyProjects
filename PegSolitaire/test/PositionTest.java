import org.junit.Test;

import cs3500.marblesolitaire.model.hw02.Position;

import static org.junit.Assert.assertEquals;

/**
 * Class designated to testing the Positon class.
 */
public class PositionTest {
  Position pos1 = new Position(1, 1);
  Position pos2 = new Position(2, 2);
  Position pos3 = new Position(3, 3);
  Position pos4 = new Position(2, 2);
  Position pos5 = new Position(1, 15);
  Position pos6 = new Position(4, 2);
  Position pos7 = new Position(0, 2);
  Position pos8 = new Position(0, 0);

  @Test
  public void testGetters() {
    assertEquals(1, pos1.getRow());
    assertEquals(1, pos1.getCol());
    assertEquals(2, pos2.getCol());
    assertEquals(3, pos3.getCol());
    assertEquals(1, pos5.getCol());
    assertEquals(15, pos5.getRow());
  }

  @Test
  public void testCompareTo() {
    assertEquals(-1, pos1.compareTo(pos2));
    assertEquals(1, pos2.compareTo(pos1));
    assertEquals(-1, pos1.compareTo(pos3));
    assertEquals(1, pos3.compareTo(pos4));
    assertEquals(1, pos5.compareTo(pos1));
    assertEquals(0, pos5.compareTo(pos5));
  }

  @Test
  public void testEquals() {
    assertEquals(true, pos2.equals(pos4));
    assertEquals(false, pos2.equals(pos3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException() {
    assertEquals(new IllegalArgumentException(), new Position(-1, 2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException2() {
    assertEquals(new IllegalArgumentException(), new Position(1, -2));
  }
}