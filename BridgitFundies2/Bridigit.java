import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import tester.Tester;
import java.util.*;

// represents the bridgit game
class Bridgit extends World {
  ArrayList<Cell> cells;
  ArrayList<Cell> magentaPath = new ArrayList<Cell>();
  ArrayList<Cell> pinkPath = new ArrayList<Cell>();
  boolean turn1Huh = true;
  int grid;

  Bridgit(int grid) {
    this.grid = grid;
    if ((grid % 2) == 0 || grid < 3) {
      throw new IllegalArgumentException("Invalid grid input");
    }
    else {
      this.cells = new ArrayUtils().buildCells(grid);
    }
  }

  // renders the bridgit game; board containing the cells
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(440, 440);
    for (Cell cell : cells) {
      ws.placeImageXY(cell.renderCell(), cell.posn.x, cell.posn.y);
    }
    return ws;
  }

  // handles mouse-click inputs; basically connects player's cells when clicked
  public void onMouseClicked(Posn posn) {
    // boolean turn1Huh = true;
    for (Cell c : cells) {
      if (c.color == Color.WHITE && c.checkRange(posn)) {
        if (this.turn1Huh) {
          c.color = Color.PINK;
          c.bridgedHuh = true;
          c.updateBridge();
          this.turn1Huh = !this.turn1Huh;
          // add to path
          this.pinkPath.add(c);
          if (c.bridgeStatus().equals("top")) {
            this.pinkPath.add(c.top);
            this.pinkPath.add(c.bottom);
          }
          if (c.bridgeStatus().equals("side")) {
            this.pinkPath.add(c.right);
            this.pinkPath.add(c.left);
          }
        }
        else {
          c.color = Color.MAGENTA;
          c.bridgedHuh = true;
          c.updateBridge();
          this.turn1Huh = !this.turn1Huh;
          // add to path
          this.magentaPath.add(c);
          if (c.bridgeStatus().equals("top")) {
            this.magentaPath.add(c.top);
            this.magentaPath.add(c.bottom);
          }
          if (c.bridgeStatus().equals("side")) {
            this.magentaPath.add(c.right);
            this.magentaPath.add(c.left);
          }
        }
      }
    }
  }

  // ends the concentration game when all pairs have been matched
  public WorldEnd worldEnds() {
    WorldScene ws = new WorldScene(440, 440);
    WorldImage winner1 = new TextImage("Player 1 has won!", 50, Color.PINK);
    WorldImage winner2 = new TextImage("Player 2 has won!", 50, Color.MAGENTA);
    if (new ArrayUtils().pathEndMagenta(this.magentaPath, this.grid)) {
      ws.placeImageXY(winner2, 220, 220);
      return new WorldEnd(true, ws);
    }
    else if (new ArrayUtils().pathEndPink(this.pinkPath, this.grid)) {
      ws.placeImageXY(winner1, 220, 220);
      return new WorldEnd(true, ws);
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // handles key event inputed by players
  // EFFECT: resets the game when the "r" key is clicked
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.magentaPath = new ArrayList<Cell>();
      this.pinkPath = new ArrayList<Cell>();
      this.turn1Huh = true;
      this.cells = new ArrayUtils().buildCells(grid);
    }
  }
}

//represent a sing cell
class Cell {
  Color color;
  Posn posn;
  Cell top;
  Cell bottom;
  Cell right;
  Cell left;
  boolean bridgedHuh;
  int size = 40;

  Cell(Color color, Posn posn, Cell bottom, Cell top, Cell right, Cell left, boolean bridgedHuh) {
    this.color = color;
    this.posn = posn;
    this.bottom = bottom;
    this.right = right;
    this.left = left;
    this.bridgedHuh = bridgedHuh;
  }

  // renders this cell; cell is represented by a square with a color
  public WorldImage renderCell() {
    return new RectangleImage(this.size, this.size, "solid", this.color);
  }

  // checks if the given posn is found beside or on top/bottom of this cell
  public boolean checkRange(Posn posn) {
    return posn.x >= this.posn.x - 20 && posn.x <= this.posn.x + 20 && posn.y >= this.posn.y - 20
        && posn.y <= this.posn.y + 20;
  }

  // EFFECT: sets the bridge status of neighbors to true, if neighbor has same
  // color
  public void updateBridge() {
    if (this.bridgeStatus().equals("side")) {
      this.left.bridgedHuh = true;
      this.right.bridgedHuh = true;
    }
    if (this.bridgeStatus().equals("top")) {
      this.bottom.bridgedHuh = true;
      this.top.bridgedHuh = true;
    }
  }

  // returns a string representation of the which neighbor has the same color as
  // this cell
  public String bridgeStatus() {
    if ((this.left != null && this.color == this.left.color)
        || (this.right != null && this.color == this.right.color)) {
      return "side";
    }
    else if ((this.top != null && this.color == this.top.color)
        || (this.bottom != null && this.color == this.bottom.color)) {
      return "top";
    }
    else {
      return "null";
    }
  }
}

// class for performing tasks on ArrayList and other utilities...
class ArrayUtils {

  // computes the required amount of white cells based on grid
  public int computeWhiteCell(int grid) {
    return ((grid - 1) / 2) * ((grid - 1) / 2);
  }

  // computes the required amount of pink and magenta cells based on grid
  public int computePinkMageCell(int grid) {
    return (((grid * grid) / 4) - 1 / 4);
  }

  // computes the remaining amount of white cells based on grid
  public int computeWhiteRest(int grid) {
    return ((grid - 2) / 2) * ((grid - 2) / 2);
  }

  // builds a list of cells:
  public ArrayList<Cell> buildCells(int grid) {
    ArrayList<Cell> whiteCells = new ArrayList<Cell>();
    ArrayList<Cell> magentaCells = new ArrayList<Cell>();
    ArrayList<Cell> pinkCells = new ArrayList<Cell>();
    ArrayList<Cell> totalCells = new ArrayList<Cell>();

    for (int i = 0; i < this.computeWhiteCell(grid); i++) {
      Posn posn = new Posn(60 + (i % ((grid - 1) / 2)) * 80, 60 + (i / ((grid - 1) / 2)) * 80);
      Cell whiteCell = new Cell(Color.WHITE, posn, null, null, null, null, false);
      whiteCells.add(whiteCell);
    }

    for (int s = 0; s < this.computeWhiteRest(grid); s++) {
      Posn posn = new Posn(100 + (s % ((grid - 2) / 2)) * 80, 100 + (s / ((grid - 2) / 2)) * 80);
      Cell whiteCell = new Cell(Color.WHITE, posn, null, null, null, null, false);
      whiteCells.add(whiteCell);
    }

    for (int j = 0; j < this.computePinkMageCell(grid); j++) {
      Posn posn = new Posn(60 + (j % ((grid - 1) / 2)) * 80, 20 + (j % ((grid + 1) / 2)) * 80);
      Cell magentaCell = new Cell(Color.MAGENTA, posn, null, null, null, null, false);
      magentaCells.add(magentaCell);
    }

    for (int k = 0; k < this.computePinkMageCell(grid); k++) {
      Posn posn = new Posn(20 + (k % ((grid + 1) / 2)) * 80, 60 + (k % ((grid - 1) / 2)) * 80);
      Cell pinkCell = new Cell(Color.PINK, posn, null, null, null, null, false);
      pinkCells.add(pinkCell);
    }
    totalCells.addAll(pinkCells);
    totalCells.addAll(magentaCells);
    totalCells.addAll(whiteCells);
    this.computeNeighbours(totalCells);
    return totalCells;
  }

  // returns the position of posn1 relative to posn2 as a string
  // note: a bit confusing because y-axis is opposite of cart coord
  public String checkRange(Posn posn1, Posn posn2) {
    if (posn1.x == posn2.x && posn1.y == posn2.y + 40) {
      return "bottom";
    }
    else if (posn1.x == posn2.x && posn1.y == posn2.y - 40) {
      return "top";
    }
    else if (posn1.x == posn2.x + 40 && posn1.y == posn2.y) {
      return "right";
    }
    else if (posn1.x == posn2.x - 40 && posn1.y == posn2.y) {
      return "left";
    }
    else {
      return "just an else case that will never be used";
    }
  }

  // computes the neighbours
  public void computeNeighbours(ArrayList<Cell> cells) {
    for (Cell i : cells) {
      for (Cell j : cells) {
        if (checkRange(i.posn, j.posn).equals("bottom")) {
          i.top = j;
          j.bottom = i;
        }
        else if (checkRange(i.posn, j.posn).equals("top")) {
          i.bottom = j;
          j.top = i;
        }
        else if (checkRange(i.posn, j.posn).equals("right")) {
          i.left = j;
          j.right = i;
        }
        else if (checkRange(i.posn, j.posn).equals("left")) {
          i.right = j;
          j.left = i;
        }
      }
    }
  }


  // checks if neighbours are bridged
  public boolean checkNeighbours(Cell start, Cell end, ArrayList<Cell> soFar) {
    boolean flag = false;
    if (start.posn.x == end.posn.x && start.posn.y == end.posn.y) {
      flag = true;
    }
    else {

      if (start.bridgedHuh && !soFar.contains(start.left) && start.left != null
          && start.left.color == start.color) {
        soFar = (ArrayList<Cell>) soFar.clone();
        soFar.add(start);
        // soFar.add(start);
        flag = this.checkNeighbours(start.left, end, soFar);
      }
      if (start.bridgedHuh && !soFar.contains(start.right) && start.right != null
          && start.right.color == start.color) {
        soFar = (ArrayList<Cell>) soFar.clone();
        soFar.add(start);
        // soFar.add(start);
        flag = this.checkNeighbours(start.right, end, soFar);
      }
      if (start.bridgedHuh && !soFar.contains(start.bottom) && start.bottom != null
          && start.bottom.color == start.color) {
        soFar = (ArrayList<Cell>) soFar.clone();
        soFar.add(start);
        // soFar.add(start);
        flag = this.checkNeighbours(start.bottom, end, soFar);
      }
      if (start.bridgedHuh && !soFar.contains(start.top) && start.top != null
          && start.top.color == start.color) {
        soFar = (ArrayList<Cell>) soFar.clone();
        soFar.add(start);

        // soFar.add(start);
        flag = this.checkNeighbours(start.top, end, soFar);
      }
    }
    return flag;
  }

  // returns whether the given pink path has reached the end of the board
  public boolean pathEndPink(ArrayList<Cell> path, int grid) {
    boolean flag = false;
    ArrayList<Cell> start = new ArrayList<Cell>();
    ArrayList<Cell> end = new ArrayList<Cell>();
    for (Cell i : path) {
      if (i.posn.x == 20) {
        start.add(i);
      }
      if (i.posn.x == 20 + 40 * (grid - 1)) {
        end.add(i);
      }
    }
    for (Cell i : start) {
      for (Cell j : end) {
        if (new ArrayUtils().checkNeighbours(i, j, new ArrayList<Cell>())) {
          flag = true;
        }
      }
    }
    return flag;
  }

  // returns whether the given magenta path has reached the end of the board
  public boolean pathEndMagenta(ArrayList<Cell> path, int grid) {
    boolean flag = false;
    ArrayList<Cell> start = new ArrayList<Cell>();
    ArrayList<Cell> end = new ArrayList<Cell>();
    for (Cell i : path) {
      if (i.posn.y == 20) {
        start.add(i);
      }
      if (i.posn.y == 20 + 40 * (grid - 1)) {
        end.add(i);
      }
    }
    for (Cell i : start) {
      for (Cell j : end) {
        if (new ArrayUtils().checkNeighbours(i, j, new ArrayList<Cell>())) {
          flag = true;
        }
      }
    }
    return flag;
  }
}

// examples and check-expects:
class Examples {
  Bridgit game = new Bridgit(11);
  Bridgit game2 = new Bridgit(9);
  Bridgit gameTest = new Bridgit(3);
  Bridgit gamer = new Bridgit(11);

  Bridgit gameTest2;

  Cell cell1 = new Cell(Color.WHITE, new Posn(60, 60), null, null, null, null, false);
  Cell cell2 = new Cell(Color.MAGENTA, new Posn(100, 100), null, null, null, null, false);
  Cell cell3 = new Cell(Color.PINK, new Posn(60, 100), null, null, null, null, false);
  Posn posn1 = new Posn(40, 40);
  Posn posn2 = new Posn(80, 85);
  Posn posn3 = new Posn(150, 300);

  Cell cell4 = new Cell(Color.MAGENTA, new Posn(60, 20), null, null, null, null, false);
  Cell cell5 = new Cell(Color.WHITE, new Posn(60, 60), null, null, null, null, false);
  Cell cell6 = new Cell(Color.PINK, new Posn(20, 60), null, null, null, null, false);
  Cell cell7 = new Cell(Color.PINK, new Posn(100, 60), null, null, null, null, false);
  Cell cell8 = new Cell(Color.MAGENTA, new Posn(60, 100), null, null, null, null, false);

  Cell cell9 = new Cell(Color.PINK, new Posn(20, 20), null, null, null, null, true);
  Cell cell10 = new Cell(Color.PINK, new Posn(60, 20), null, null, null, null, true);
  Cell cell11 = new Cell(Color.PINK, new Posn(100, 20), null, null, null, null, true);
  Cell cell12 = new Cell(Color.PINK, new Posn(140, 20), null, null, null, null, true);
  Cell cell13 = new Cell(Color.PINK, new Posn(180, 20), null, null, null, null, true);
  Cell cell14 = new Cell(Color.PINK, new Posn(220, 20), null, null, null, null, true);
  Cell cell15 = new Cell(Color.PINK, new Posn(260, 20), null, null, null, null, true);
  Cell cell16 = new Cell(Color.PINK, new Posn(300, 20), null, null, null, null, true);
  Cell cell17 = new Cell(Color.PINK, new Posn(340, 20), null, null, null, null, true);
  Cell cell18 = new Cell(Color.PINK, new Posn(380, 20), null, null, null, null, true);
  Cell cell19 = new Cell(Color.PINK, new Posn(420, 20), null, null, null, null, true);
  // Cell cell20 = new Cell(Color.PINK, new Posn(60, 60), null, null, null, null,
  // true);
  Cell cellFail = new Cell(Color.PINK, new Posn(50, 150), null, null, null, null, true);

  ArrayList<Cell> cells = new ArrayList<Cell>(
      Arrays.asList(this.cell9, this.cell10, this.cell11, this.cell12, this.cell13, this.cell14,
          this.cell15, this.cell16, this.cell17, this.cell18, this.cell19, this.cellFail));

  ArrayList<Cell> cellsFail = new ArrayList<Cell>(
      Arrays.asList(this.cell9, this.cell10, this.cell11, this.cell12, this.cell13, this.cell14,
          this.cell15, this.cell16, this.cell17, this.cell18, this.cellFail));

  void initData() {
    new ArrayUtils().computeNeighbours(this.cells);
    this.gameTest2 = new Bridgit(3);
  }

  ArrayList<Cell> cellResult1 = new ArrayList<Cell>(
      Arrays.asList(this.cell6, this.cell5, this.cell7, this.cell8));
  ArrayList<Cell> cellResult = new ArrayList<Cell>(
      Arrays.asList(this.cell4, this.cell5, this.cell6, this.cell7, this.cell8));

  // initialize and play the game
  void testBigBang(Tester t) {
    // this.initDat();
    this.gamer.bigBang(440, 440, 1);
  }

  void testWorldEnd(Tester t) {
    this.initData();
    t.checkExpect(new ArrayUtils().pathEndPink(this.gameTest2.pinkPath, this.gameTest2.grid),
        false);
    t.checkExpect(this.gameTest2.worldEnds(), new WorldEnd(false, this.gameTest2.makeScene()));
    this.gameTest2.onMouseClicked(new Posn(60, 60));
    t.checkExpect(new ArrayUtils().pathEndPink(this.gameTest2.pinkPath, this.gameTest2.grid), true);
    WorldScene ws = new WorldScene(440, 440);
    WorldImage winner1 = new TextImage("Player 1 has won!", 50, Color.PINK);
    WorldImage winner2 = new TextImage("Player 2 has won!", 50, Color.MAGENTA);
    ws.placeImageXY(winner1, 220, 220);
    t.checkExpect(this.gameTest2.worldEnds(), new WorldEnd(true, ws));
  }

  void testPinkEndPath(Tester t) {
    this.initData();
    t.checkExpect(new ArrayUtils().checkNeighbours(this.cell9, this.cell9, new ArrayList<Cell>()),
        true);
    t.checkExpect(
        new ArrayUtils().checkNeighbours(this.cellFail, this.cell9, new ArrayList<Cell>()), false);
    t.checkExpect(new ArrayUtils().checkNeighbours(this.cell10, this.cell15, new ArrayList<Cell>()),
        true);
    t.checkExpect(new ArrayUtils().checkNeighbours(this.cell9, this.cell19, new ArrayList<Cell>()),
        true);
    t.checkExpect(new ArrayUtils().checkNeighbours(this.cell19, this.cell9, new ArrayList<Cell>()),
        true);
    t.checkExpect(new ArrayUtils().checkNeighbours(this.cell19, this.cell9, new ArrayList<Cell>()),
        true);
    t.checkExpect(new ArrayUtils().pathEndPink(this.cells, 11), true);
    t.checkExpect(new ArrayUtils().pathEndPink(this.cellsFail, 11), false);
    t.checkExpect(new ArrayUtils().pathEndMagenta(this.cells, 11), false);
    t.checkExpect(new ArrayUtils().pathEndMagenta(this.cellsFail, 11), false);
  }

  void testWhiteCells(Tester t) {
    t.checkExpect(new ArrayUtils().computeWhiteRest(11), 16);
    t.checkExpect(new ArrayUtils().computeWhiteRest(9), 9);
    t.checkExpect(new ArrayUtils().computeWhiteRest(7), 4);
    t.checkExpect(new ArrayUtils().computeWhiteRest(5), 1);
    t.checkExpect(new ArrayUtils().computeWhiteRest(3), 0);
  }

  void initDat() {
    new ArrayUtils().computeNeighbours(this.cellResult);
    new ArrayUtils().computeNeighbours(this.cellResult1);
  }

  // test makeScene:
  void testMakeScene(Tester t) {
    this.initDat();
    new ArrayUtils().computeNeighbours(this.cellResult);
    WorldScene ws = new WorldScene(440, 440);
    for (Cell c : this.cellResult) {
      ws.placeImageXY(c.renderCell(), c.posn.x, c.posn.y);
    }
    t.checkExpect(this.gameTest.makeScene(), ws);
    WorldScene ws2 = new WorldScene(440, 440);
    for (Cell c : this.game.cells) {
      ws2.placeImageXY(c.renderCell(), c.posn.x, c.posn.y);
    }
    t.checkExpect(this.game.makeScene(), ws2);
  }

  // test buildCells:
  void tesBuildCells(Tester t) {
    this.initDat();
    // new ArrayUtils().computeNeighbours(this.cellResult);
    t.checkExpect(new ArrayUtils().buildCells(3), this.cellResult1);
  }

  void testSomething(Tester t) {
    t.checkExpect(this.game.cells.size(), 101);
  }

  void testRenderCell(Tester t) {
    t.checkExpect(this.game.cells.get(0).renderCell(),
        new RectangleImage(40, 40, "solid", Color.PINK));
    t.checkExpect(this.game.cells.get(35).renderCell(),
        new RectangleImage(40, 40, "solid", Color.MAGENTA));
    t.checkExpect(this.game.cells.get(80).renderCell(),
        new RectangleImage(40, 40, "solid", Color.WHITE));
  }

  void testCheckRange(Tester t) {
    t.checkExpect(this.cell1.bridgedHuh, false);
    t.checkExpect(this.cell1.color, Color.WHITE);
    t.checkExpect(this.cell1.checkRange(posn1), true);
    t.checkExpect(this.cell1.bridgedHuh, false);
    t.checkExpect(this.cell1.color, Color.WHITE);
    t.checkExpect(this.cell2.checkRange(this.posn2), true);
    t.checkExpect(this.cell2.checkRange(this.posn1), false);
    t.checkExpect(this.cell1.checkRange(this.posn2), false);
  }

  void testComputeCells(Tester t) {
    System.out.println(new ArrayUtils().computePinkMageCell(11));
    t.checkExpect(new ArrayUtils().computePinkMageCell(11), 30);
    t.checkExpect(new ArrayUtils().computeWhiteCell(11), 25);
    t.checkExpect(new ArrayUtils().computePinkMageCell(9), 20);
    t.checkExpect(new ArrayUtils().computeWhiteCell(9), 16);
    t.checkExpect(new ArrayUtils().computePinkMageCell(7), 12);
    t.checkExpect(new ArrayUtils().computeWhiteCell(7), 9);
    t.checkExpect(new ArrayUtils().computePinkMageCell(5), 6);
    t.checkExpect(new ArrayUtils().computeWhiteCell(5), 4);
  }

  void testCheckRange2(Tester t) {
    t.checkExpect(new ArrayUtils().checkRange(this.posn1, this.posn2),
        "just an else case that will never be used");
    t.checkExpect(new ArrayUtils().checkRange(new Posn(20, 20), new Posn(20, 60)), "top");
    t.checkExpect(new ArrayUtils().checkRange(new Posn(20, 60), new Posn(20, 20)), "bottom");
    t.checkExpect(new ArrayUtils().checkRange(new Posn(20, 20), new Posn(60, 20)), "left");
    t.checkExpect(new ArrayUtils().checkRange(new Posn(60, 20), new Posn(20, 20)), "right");
  }

  void testBridgeStatus(Tester t) {
    this.initData();
    t.checkExpect(this.cell9.bridgeStatus(), "side");
  }

  void testOnMouse(Tester t) {
    int whiteCellCount = 0;
    int result = 0;
    int result1 = 0;
    for (Cell i : game.cells) {
      if (i.color == Color.white) {
        whiteCellCount++;
      }
    }
    t.checkExpect(whiteCellCount, 41);
    game.onMouseClicked(new Posn(60, 60));
    for (Cell i : game.cells) {
      if (i.color == Color.white) {
        result++;
      }
    }
    t.checkExpect(result, 40);
    game.onMouseClicked(new Posn(85, 85));

    for (Cell i : game.cells) {
      if (i.color == Color.white) {
        result1++;
      }
    }
    t.checkExpect(result1, 39);
  }

}
