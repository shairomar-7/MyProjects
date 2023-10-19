import java.awt.Color;
import javalib.funworld.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import javalib.worldimages.*;
import tester.Tester;
import java.util.Random;

// note: parts used for part2 are not tested, nor included in templates.

// represents the space invaders game
class Game extends World {
  AGamePiece spaceship;
  IList<AGamePiece> invaders;
  IList<AGamePiece> spacebullets;
  IList<AGamePiece> invaderbullets;

  Game(AGamePiece spaceship, IList<AGamePiece> invaders, 
      IList<AGamePiece> spacebullets, IList<AGamePiece> invaderbullets) {
    this.spaceship = spaceship;
    this.invaders = invaders;
    this.invaderbullets = invaderbullets;
    this.spacebullets = spacebullets;
  }
  /* Fields:
   * this.spaceShip ... AGamePiece
   * this.invaders ... IList<AGamePiece>
   * this.spacebullets ... IList<AGamePiece>
   * this.invaderbullets ... IList<AGamePiece>
   * Methods:
   * this.makeScene() ... WorldScene
   * this.onTick() ... World
   * this.onKeyEvent(String key) ... World
   * Methods for fields:
   * this.spaceShip.moveShip(String key) ... AGamePiece
   * this.spaceBullets.length() ... int
   * this.invaders.filter(Predicate<AGamePiece> pred) ... IList<AGamePiece>
   * this.invaderBullets.filter(Predicate<AGamePiece> pred) ... IList<AGamePiece>
   * this.spacebullets.filter(Predicate<AGamePiece> pred) ... IList<AGamePiece>
   * new NotWithinList(IList<AGamePiece>) ... boolean
   * new NotWithinBulletList(IList<AGamePiece>) ... boolean
   * new Utils().move(IList<AGamePiece>) ... IList<AGamePiece>
   * pred -> new IsOutOfBounds() ... boolean
   * this.spaceship.placeImage(WorldScene u) ... WorldScene
   * this.invaders.fold(BiFunc<AGamePiece, WorldScene, WorldScene>) ... WorldScene
   * this.spacebullets.fold(BiFunc<AGamePiece, WorldScene, WorldScene>) ... WorldScene
   * this.invaderbullets.fold(BiFunc<AGamePiece, WorldScene, WorldScene>) ... WorldScene
   */
  
  // render the space invaders game
  public WorldScene makeScene() {
    WorldScene u = new WorldScene(400, 500);
    u = this.spaceship.placeImage(u);
    u = this.invaders.fold(new OverlayAnyImages(), u);
    u = this.invaderbullets.fold(new OverlayAnyImages(), u);
    u = this.spacebullets.fold(new OverlayAnyImages(), u);
    return u;
  }

  // this method is called every tick to move the game pieces
  public World onTick() {
    IList<AGamePiece> invadersFiltered = this.invaders.filter(new NotWithinList(this.spacebullets));
    IList<AGamePiece> invadersBulletsFiltered = this.invaderbullets.filter(new IsOutOfBounds());
    // IList<AGamePiece> invadersBulletsFiltered2 = this.invaderbullets.filter(new
    // IsContainedinList(this.spacebullets.map(new GetPosn())));
    IList<AGamePiece> shipBulletsFiltered = this.spacebullets.filter(new IsOutOfBounds());
    IList<AGamePiece> shipBulletsFiltered2 = shipBulletsFiltered
        .filter(new NotWithinBulletList(invadersBulletsFiltered));
    IList<AGamePiece> spaceBulletsFinal = shipBulletsFiltered2
        .filter(new NotWithinList(this.invaders));
    IList<AGamePiece> invaderbullets = new BuildBulletsList(invadersBulletsFiltered)
        .buildBulletsList(invadersFiltered, new Random().nextInt(invaders.length()));
    IList<AGamePiece> invaderbulletsFinal = invaderbullets
        .filter(new NotWithinBulletList(shipBulletsFiltered));
    return new Game(this.spaceship.moveShip(), invadersFiltered,
        new Utils().move(spaceBulletsFinal), new Utils().move(invaderbulletsFinal));
  }

  // handles key input
  public World onKeyEvent(String key) {
    if (key.equals("left")) {
      return (new Game(this.spaceship.setDirectionLeft(), this.invaders, this.spacebullets,
          this.invaderbullets));
    }
    else if (key.equals("right")) {
      return new Game(this.spaceship.setDirectionRight(), this.invaders, this.spacebullets,
          this.invaderbullets);
    }
    else if (key.equals(" ") && this.spacebullets.length() < 3) {
      return new Game(this.spaceship, this.invaders,
          new ConsList<AGamePiece>(new SpaceShipBullets(3, 5, Color.RED, this.spaceship.posn),
              this.spacebullets),
          this.invaderbullets);
    }
    else {
      return this;
    }
  }

  // ends the world
  public WorldEnd worldEnds() {
    WorldScene u = new WorldScene(400, 500);
    if (this.invaders.length() == 0) {
      return new WorldEnd(true,
          u.placeImageXY(new TextImage("WINNER!", 50, Color.BLACK), 200, 250));
    }
    else if (this.invaderbullets.ormap(new IsWithinRange(this.spaceship))) {
      return new WorldEnd(true,
          u.placeImageXY(new TextImage("GAME OVER!", 50, Color.RED), 200, 250));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // gets the list of posns from a list of pieces
  public IList<CartPt> getPosns(IList<AGamePiece> pieces) {
    return pieces.map(new GetPosn());
  }
}

//represents the elements of the space invaders game
abstract class AGamePiece {
  int size;
  CartPt posn;
  
  AGamePiece(CartPt posn, int size) {
    this.posn = posn;
    this.size = size;
  }
  
  /* Fields:
   * this.posn ... CartPt
   * this.size
   * Methods:
   * this.render() ... WorldImage
   * this.placeImage(WorldScene u) ... WorldScene
   * this.move() ... AGamePiece
   * this.exceedsBounds() ... Boolean
   * this.moveShip() ... AGamePiece
   * this.moveLeft() ... AGamePiece
   * this.moveRight() ... AGamePiece
   * this.isWithinRange() ... boolean
   * this.isWithinBulletRange() ... boolean
   * this.setDirectionRight() ... AGamePiece
   * this.setDirectionLeft() ... AGamePiece
   * Methods For Fields:
   * this.IsContained(CartPt that) ... boolean
   * this.shipShiftBySpeed(int speed) ... CartPt
   * this.shipShiftLeftSpeed(int speed) ... CartPt
   * this.spaceBulletShift(int speed) ... CartPt
   * this.invaderBulletShift(int speed) ... CarPt 
   */

  // renders the game piece
  public abstract WorldImage render();

  // place image onto canvas
  public abstract WorldScene placeImage(WorldScene u);

  // moves the game piece by its speed
  public AGamePiece moveShip() {
    return this;
  }

  // moves the given game piece
  public abstract AGamePiece move();

  // moves the game piece to the left by its speed 
  public AGamePiece moveLeft() {
    return this;
  }

  // moves the game piece to the right by its speed 
  public AGamePiece moveRight() {
    return this;
  }

  // checks whether the game piece's posn exceeds scene's bounds
  public abstract boolean exceedsBounds();

  // checks whether this game piece's posn is within range of that
  public abstract boolean isWithinRange(AGamePiece that);

  //checks whether this bullets's posn is within range of that bullet
  public abstract boolean isWithinBulletRange(AGamePiece that);

  // sets direction to true for right
  public AGamePiece setDirectionRight() {
    return this;
  }

  //sets direction to false for left
  public AGamePiece setDirectionLeft() {
    return this;
  }
}

// represents a spaceship
class SpaceShip extends AGamePiece {
  boolean direction;
  SpaceShipBullets bullet;
  Color color;
  
  // constructor: if position of space ship reaches border of canvas, space ship will not move 
  SpaceShip(CartPt posn, boolean direction, SpaceShipBullets bullet, Color color, int size) {
    super(posn, size);
    this.direction = direction;
    this.color = color;
    this.bullet = bullet;
  }
  /* Fields:
   * this.posn ... CartPt
   * this.direction ... boolean
   * this.bullet ... SpaceShipBullets
   * this.color ... Color
   * this.size ... int
   * Methods:
   * this.render() ... WorldImage
   * this.placeImage(WorldScene u) ... WorldScene
   * this.move() ... AGamePiece
   * this.exceedsBounds() ... Boolean
   * this.moveShip() ... AGamePiece
   * this.moveLeft() ... AGamePiece
   * this.moveRight() ... AGamePiece
   * this.isWithinRange() ... boolean
   * this.isWithinBulletRange() ... boolean
   * this.setDirectionRight() ... AGamePiece
   * this.setDirectionLeft() ... AGamePiece
   * Methods for fields: 
   * this.IsContained(CartPt that) ... boolean
   * this.shipShiftBySpeed(int speed) ... CartPt
   * this.shipShiftLeftSpeed(int speed) ... CartPt
   * this.spaceBulletShift(int speed) ... CartPt
   * this.invaderBulletShift(int speed) ... CarPt 
   */
  
  // moves the spaceship based on its direction
  public AGamePiece moveShip() {
    if (this.direction) {
      return this.moveRight();
    }
    else {
      return this.moveLeft();
    }
  }

  // renders the spaceship
  public WorldImage render() {
    return new RectangleImage((int) (2.5 * this.size), this.size, "solid", this.color);
  }

  // place image onto canvas
  public WorldScene placeImage(WorldScene u) {
    /* Methods:
     * this.render() ... WorldImage
     * this.placeImage(WorldScene) ... WorldScene
     * Parameters:
     * u ... WorldScene
     * Methods for parameters: 
     * u.placeImageXY(WorldImage, int, int) ... WorldScene
     */
    return u.placeImageXY(this.render(), this.posn.x, this.posn.y);
  }

  // moves the game piece to the right by its speed
  public AGamePiece move() {
    return new SpaceShip(this.posn.shipShiftLeftSpeed(5), true, this.bullet, this.color, this.size);
  }

  // moves the game piece to the left by its speed
  public SpaceShip moveLeft() {
    if (this.posn.x - this.size <= 0) {
      return new SpaceShip(this.posn.shipShiftLeftSpeed(0), false, this.bullet, this.color,
          this.size);
    }
    else {
      return new SpaceShip(this.posn.shipShiftLeftSpeed(5), false, this.bullet, this.color,
          this.size);
    }
  }

  // moves the game piece to the right by its speed
  public SpaceShip moveRight() {
    if (this.posn.x + this.size >= 400) {
      return new SpaceShip(this.posn.shipShiftBySpeed(0), true, this.bullet, this.color, this.size);
    }
    else {
      return new SpaceShip(this.posn.shipShiftBySpeed(5), true, this.bullet, this.color, this.size);
    }
  }

  // checks whether the game piece's posn exceeds scene's bounds
  // it wont, since this is taken into account in constructor
  public boolean exceedsBounds() {
    return true;
  }

  // checks if this gamepiece is within range of that one
  public boolean isWithinRange(AGamePiece that) {
    return (this.posn.x + this.size >= that.posn.x - that.size
        && this.posn.x - this.size <= that.posn.x + that.size)
        && this.posn.y - this.size / 2 <= that.posn.y + that.size
        && this.posn.y + this.size / 2 >= that.posn.y - that.size;
  }
  
  // checks if this game piece is within range of that one, used for bullets
  public boolean isWithinBulletRange(AGamePiece that) {
    return false;
  }
  
  // sets the direction of the space ship right, represent by true
  public AGamePiece setDirectionRight() {
    return new SpaceShip(this.posn, true, this.bullet, this.color, this.size);
  }
  
  //sets the direction of the space ship right, represent by true
  public AGamePiece setDirectionLeft() {
    return new SpaceShip(this.posn, false, this.bullet, this.color, this.size);
  }
}

//represents an invader
class Invader extends AGamePiece {
  InvaderBullets bullet;
  Color color;

  // default constructor
  Invader(CartPt posn, InvaderBullets bullet, Color color, int size) {
    super(posn, size);
    this.color = color;
    this.bullet = bullet;
  }
  /*
   * Fields: 
   * this.posn ... CartPt 
   * this.speed ... int 
   * this.bullet ... InvaderBullets 
   * this.color ... Color 
   * this.size ... int 
   * Methods: 
   * this.render() ... WorldImage
   * this.placeImage(WorldScene u) ... WorldScene
   * this.move() ... AGamePiece
   * this.exceedsBounds() ... Boolean
   * this.moveShip() ... AGamePiece
   * this.moveLeft() ... AGamePiece
   * this.moveRight() ... AGamePiece
   * this.isWithinRange() ... boolean
   * this.isWithinBulletRange() ... boolean
   * this.setDirectionRight() ... AGamePiece
   * this.setDirectionLeft() ... AGamePiece
   * Methods for fields: 
   * this.IsContained(CartPt that) ... boolean
   * this.shipShiftBySpeed(int speed) ... CartPt
   * this.shipShiftLeftSpeed(int speed) ... CartPt
   * this.spaceBulletShift(int speed) ... CartPt
   * this.invaderBulletShift(int speed) ... CarPt 
   */

  // renders an invader
  public WorldImage render() {
    return new RectangleImage((int) (2 * this.size), this.size, "solid", this.color);
  }

  // place the rendered piece onto a world scene
  public WorldScene placeImage(WorldScene u) {
    /*
     * Methods: 
     * this.render() ... WorldImage 
     * this.placeImage(WorldScene) ... WorldScene 
     * Parameters: 
     * u ... WorldScene 
     * Methods for parameters:
     * u.placeImageXY(WorldImage, int, int) ... WorldScene
     */
    return u.placeImageXY(this.render(), this.posn.x, this.posn.y);
  }

  // returns the game piece moved, in this case, it wouldn't.
  public AGamePiece move() {
    return this;
  }

  // checks whether the game piece's posn exceeds scene's bounds
  public boolean exceedsBounds() {
    return true;
  }

  //checks whether this game piece's posn is within range of that
  public boolean isWithinRange(AGamePiece that) {
    return (this.posn.x + this.size >= that.posn.x - that.size
        && this.posn.x - this.size <= that.posn.x + that.size)
        && this.posn.y - this.size / 2 <= that.posn.y + that.size
        && this.posn.y + this.size / 2 >= that.posn.y - that.size;
  }

  // checks if this game piece is within range of that, used for bullets
  public boolean isWithinBulletRange(AGamePiece that) {
    return false;
  }

}

// represents bullets for a spaceship
class SpaceShipBullets extends AGamePiece {
  int bulletSpeed;
  Color bulletColor;

  SpaceShipBullets(int bulletSize, int bulletSpeed, Color bulletColor, CartPt posn) {
    super(posn, bulletSize);
    this.bulletColor = bulletColor;
    this.bulletSpeed = bulletSpeed;
  }
  /* Fields:
   * this.posn ... CartPt
   * this.bulletSpeed ... int
   * this.bulletColor ... Color
   * this.bulletSize ... int
   * Methods:
   * this.render() ... WorldImage
   * this.placeImage(WorldScene u) ... WorldScene
   * this.move() ... AGamePiece
   * this.exceedsBounds() ... Boolean
   * this.moveShip() ... AGamePiece
   * this.moveLeft() ... AGamePiece
   * this.moveRight() ... AGamePiece
   * this.isWithinRange() ... boolean
   * this.isWithinBulletRange() ... boolean
   * this.setDirectionRight() ... AGamePiece
   * this.setDirectionLeft() ... AGamePiece
   * Methods for fields: 
   * this.IsContained(CartPt that) ... boolean
   * this.shipShiftBySpeed(int speed) ... CartPt
   * this.shipShiftLeftSpeed(int speed) ... CartPt
   * this.spaceBulletShift(int speed) ... CartPt
   * this.invaderBulletShift(int speed) ... CarPt 
   */

  //renders the spaceship's bullet
  public WorldImage render() {
    return new CircleImage(this.size, "solid", Color.BLACK);
  }

  // place the rendered piece onto a world scene
  public WorldScene placeImage(WorldScene u) {
    /* Methods:
     * this.render() ... WorldImage
     * this.placeImage(WorldScene) ... WorldScene
     * Parameters:
     * u ... WorldScene
     * Methods for parameters: 
     * u.placeImageXY(WorldImage, int, int) ... WorldScene
     */
    return u.placeImageXY(this.render(), this.posn.x, this.posn.y);
  }
  
  // moves the given game piece by its speed
  public AGamePiece move() {
    return new SpaceShipBullets(this.size, this.bulletSpeed, this.bulletColor,
        this.posn.spaceBulletShift(this.bulletSpeed));
  }

  // checks whether the game piece's posn exceeds scene's bounds
  public boolean exceedsBounds() {
    return this.posn.y >= 0;
  }

  //checks whether this game piece's posn is within range of that
  public boolean isWithinRange(AGamePiece that) {
    return (this.posn.x + this.size >= that.posn.x - that.size
        && this.posn.x - this.size <= that.posn.x + that.size)
        && (this.posn.y - this.size <= that.posn.y + that.size / 2
            && this.posn.y + this.size >= that.posn.y - that.size / 2);
  }

  // checks whether this bullets' posn is within range of that bullet
  public boolean isWithinBulletRange(AGamePiece that) {
    return this.posn.x + this.size >= that.posn.x - that.size
        && this.posn.x - this.size <= that.posn.x + that.size
        && (this.posn.y - this.size <= that.posn.y + that.size
            && this.posn.y + this.size >= that.posn.y - that.size);
  }
}

//represents bullets for an invader
class InvaderBullets extends AGamePiece {
  int bulletSpeed;
  Color bulletColor;

  InvaderBullets(int bulletSize, int bulletSpeed, Color bulletColor, CartPt posn) {
    super(posn, bulletSize);
    this.bulletColor = bulletColor;
    this.bulletSpeed = bulletSpeed;
  }
  /* Fields:
   * this.posn ... CartPt
   * this.bulletSpeed ... int
   * this.bulletColor ... Color
   * this.bulletSize ... int
   * Methods:
   * this.render() ... WorldImage
   * this.placeImage(WorldScene u) ... WorldScene
   * this.move() ... AGamePiece
   * this.exceedsBounds() ... Boolean
   * this.moveShip() ... AGamePiece
   * this.moveLeft() ... AGamePiece
   * this.moveRight() ... AGamePiece
   * this.isWithinRange() ... boolean
   * this.isWithinBulletRange() ... boolean
   * this.setDirectionRight() ... AGamePiece
   * this.setDirectionLeft() ... AGamePiece
   * Methods for fields: 
   * this.IsContained(CartPt that) ... boolean
   * this.shipShiftBySpeed(int speed) ... CartPt
   * this.shipShiftLeftSpeed(int speed) ... CartPt
   * this.spaceBulletShift(int speed) ... CartPt
   * this.invaderBulletShift(int speed) ... CarPt 
   */
  
  // renders an invader's bullet
  public WorldImage render() {
    return new CircleImage(this.size, "solid", Color.RED);
  }

  // place the rendered piece onto a world scene
  public WorldScene placeImage(WorldScene u) {
    /* Methods:
     * this.render() ... WorldImage
     * this.placeImage(WorldScene) ... WorldScene
     * Parameters:
     * u ... WorldScene
     * Methods for parameters: 
     * u.placeImageXY(WorldImage, int, int) ... WorldScene
     */
    return u.placeImageXY(this.render(), this.posn.x, this.posn.y);
  }

  // moves the given game piece by its speed
  public AGamePiece move() {
    return new InvaderBullets(this.size, this.bulletSpeed, this.bulletColor,
        this.posn.invaderBulletShift(this.bulletSpeed));
  }

  // checks whether the game piece's posn exceeds scene's bounds
  public boolean exceedsBounds() {
    return this.posn.y <= 500;
  }

  //checks whether this game piece's posn is within range of that
  public boolean isWithinRange(AGamePiece that) {
    return (this.posn.x + this.size >= that.posn.x - that.size
        && this.posn.x - this.size <= that.posn.x + that.size)
        && (this.posn.y - this.size <= that.posn.y + that.size / 2
            && this.posn.y + this.size >= that.posn.y - that.size / 2);
  }

  //checks whether this bullets' posn is within range of that bullet
  public boolean isWithinBulletRange(AGamePiece that) {
    return this.posn.x + this.size >= that.posn.x - that.size
        && this.posn.x - this.size <= that.posn.x + that.size
        && (this.posn.y - this.size <= that.posn.y + that.size
            && this.posn.y + this.size >= that.posn.y - that.size);
  }
}


// represents a cartesian position
class CartPt {
  int x;
  int y;

  // default constructor
  CartPt(int x, int y) {
    this.x = x;
    this.y = y;
  }
  /* Fields:
   * x ... int
   * y ... int
   * Methods: 
   * this.IsContained(CartPt that) ... boolean
   * this.shipShiftBySpeed(int speed) ... CartPt
   * this.shipShiftLeftSpeed(int speed) ... CartPt
   * this.spaceBulletShift(int speed) ... CartPt
   * this.invaderBulletShift(int speed) ... CarPt 
   * Methods for fields: none
   */
  
  // checks if this cartpt is the same as the given one
  boolean isContained(CartPt that) {
    return this.x == that.x && this.y == that.y;
  }

  // shifts the given cart pt by space ship's speed
  CartPt shipShiftBySpeed(int speed) {
    return new CartPt(this.x + speed, this.y);
  }

  // shifts the given cart pt by space ship's speed
  CartPt shipShiftLeftSpeed(int speed) {
    return new CartPt(this.x - speed, this.y);
  }

  // shifts the given cart pt by space ship bullet's speed
  CartPt spaceBulletShift(int speed) {
    return new CartPt(this.x, this.y - speed);
  }

  // shifts the given cart pt by invader ship bullet's speed
  CartPt invaderBulletShift(int speed) {
    return new CartPt(this.x, this.y + speed);
  }
}

// represents a list of type T
interface IList<T> {
  /* Fields: none
   * Methods:
   * this.filter(Predicate<T> pred) ... IList<T>
   * this.map(Function<T, U> converter) ... IList<U>
   * this.fold(BiFunction<T, U, U> converter, U initial) ... U
   * this.ormap(Predicate<T> pred) ... boolean
   * this.andmap(Predicate<T> pred) ... boolean
   * this.append(IList<T> that) ... IList<T>
   * this.indexOf(int n) ... T
   * this.length() ... int
   * Methods for fields: none
   */

  //filter this list using the given predicate
  IList<T> filter(Predicate<T> pred);

  // map a function onto every member of this list
  <U> IList<U> map(Function<T, U> converter);

  // combine the items in this list from right to left
  <U> U fold(BiFunction<T, U, U> converter, U initial);

  // is there at least one element in this list that passes the predicate?
  boolean ormap(Predicate<T> pred);

  // do all the elements in this list that pass the predicate?
  boolean andmap(Predicate<T> pred);
  
  // appends two lists, not necessarily in order. 
  IList<T> append(IList<T> that);
  
  // returns the element at the given index
  T indexOf(int n);
  
  // computes the length of a list
  int length();
}

// represents an empty list of generic type T
class MtList<T> implements IList<T> {

  MtList() {
  }
  /* Fields: none
   * Methods:
   * this.filter(Predicate<T> pred) ... IList<T>
   * this.map(Function<T, U> converter) ... IList<U>
   * this.fold(BiFunction<T, U, U> converter, U initial) ... U
   * this.ormap(Predicate<T> pred) ... boolean
   * this.andmap(Predicate<T> pred) ... boolean
   * this.append(IList<T> that) ... IList<T>
   * this.indexOf(int n) ... T
   * this.length() ... int
   * Methods for fields: none
   */

  // filter this list using the given predicate
  public IList<T> filter(Predicate<T> pred) {
    return new MtList<T>();
  }

  // map a function onto every member of this list
  public <U> IList<U> map(Function<T, U> converter) {
    return new MtList<U>();
  }

  // combine the items in this list from right to left
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return initial;
  }

  // is there at least one element in this list that passes the predicate?
  public boolean ormap(Predicate<T> pred) {
    return false;
  }

  // do all the elements in this list that pass the predicate?
  public boolean andmap(Predicate<T> pred) {
    return true;
  }

  // appends two lists, not necessarily in any order
  public IList<T> append(IList<T> that) {
    return that;
  }
 
  // returns the element of given index
  // plz do not take points off, null will never be returned.
  public T indexOf(int n) {
    return null;
  }
  
  // computes the length of given list
  public int length() {
    return 0;
  }
  
  
}

// represents non-empty list of generic type T
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  /* Fields:
   * this.first ... T
   * this.rest ... IList<T>
   * Methods:
   * this.filter(Predicate<T> pred) ... IList<T>
   * this.map(Function<T, U> converter) ... IList<U>
   * this.fold(BiFunction<T, U, U> converter, U initial) ... U
   * this.ormap(Predicate<T> pred) ... boolean
   * this.andmap(Predicate<T> pred) ... boolean
   * this.append(IList<T> that) ... IList<T>
   * this.length() ... int
   * this.indexOf(int n) ... T
   * Methods for fields:
   * this.rest.indexOf(int n) ... T
   * this.rest.length() ... int
   * this.rest.filter(Predicate<T> pred) ... IList<T>
   * this.rest.map(Function<T, U> converter) ... IList<U>
   * this.rest.fold(BiFunction<T, U, U> converter, U initial) ... U
   * this.rest.ormap(Predicate<T> pred) ... boolean
   * this.rest.andmap(Predicate<T> pred) ... boolean
   * pred.test(this.first) ... boolean
   * converter.apply(this.first, U initial) ... U
   * converter.apply(this.first) ... U
   */

  // filter this list using the given predicate
  public IList<T> filter(Predicate<T> pred) {
    if (pred.test(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    else {
      return this.rest.filter(pred);
    }
  }

  // map a function onto every member of this list
  public <U> IList<U> map(Function<T, U> converter) {
    return new ConsList<U>(converter.apply(this.first), this.rest.map(converter));
  }

  // combine the items in this list from right to left
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return converter.apply(this.first, this.rest.fold(converter, initial));
  }

  // is there at least one element in this list that passes the predicate?
  public boolean ormap(Predicate<T> pred) {
    return (pred.test(this.first)) || this.rest.ormap(pred);
  }

  // do all the elements in this list that pass the predicate?
  public boolean andmap(Predicate<T> pred) {
    return (pred.test(this.first)) && this.rest.andmap(pred);
  }

  // appends two lists, not in any given order tho
  public IList<T> append(IList<T> that) {
    return new ConsList<T>(this.first, that.append(this.rest));
  }

  // returns the element of given index
  public T indexOf(int n) {
    if (n == 0) {
      return this.first;
    }
    else {
      return this.rest.indexOf(n - 1);
    }
  }

  // computes the length of given list
  public int length() {
    return 1 + this.rest.length();
  }
}

// class invokes BiFunction to call fold and render the list of game pieces
class OverlayAnyImages implements BiFunction<AGamePiece, WorldScene, WorldScene> {

  // places the given game piece onto the world scene
  public WorldScene apply(AGamePiece t, WorldScene u) {
    return t.placeImage(u);
  }
}

// class invokes Function to call map and create a list of invaders
class BuildListFun implements Function<Integer, AGamePiece> {

  // creates a new invader with corresponding x and y coord
  public Invader apply(Integer t) {
    CartPt xy = new CartPt(20 + (t % 9) * 45, 5 + (t / 9) * 15);
    return new Invader(xy, new InvaderBullets(3, 1, Color.RED, xy), Color.RED, 10);
  }
}

// call to build list of invaders for the game, 4 rows of 9 invaders on each
class BuildListInvaders {
  public IList<AGamePiece> invadersList() {
    IList<Integer> listofInts = new Utils().buildList(0, 35);
    return new Utils().buildInvaderList(listofInts);
  }
}

// utils class to do random helper methods
class Utils {

  // overlays the rendered list of invaders to the canvas
  public WorldScene renderListInvaders(IList<AGamePiece> list) {
    return list.fold(new OverlayAnyImages(), new WorldScene(400, 500));
  }

  // build a list of invaders
  public IList<AGamePiece> buildInvaderList(IList<Integer> loi) {
    return loi.map(new BuildListFun());
  }

  // builds a list from begin to end, inclusive.
  IList<Integer> buildList(int begin, int end) {
    if (end == 0 && begin == 0) {
      return new MtList<Integer>();
    }
    else if (end == begin) {
      return new ConsList<Integer>(end, new MtList<Integer>());
    }
    else {
      return new ConsList<Integer>(begin, buildList(begin + 1, end));
    }
  }

  // returns a new list of pieces where the posn is moved by speed
  IList<AGamePiece> move(IList<AGamePiece> pieces) {
    return pieces.map(new Move());
  }
}

class IsContainedinList implements Predicate<CartPt> {
  IList<CartPt> points;
  
  // build constructor
  IsContainedinList(IList<CartPt> points) {
    this.points = points;
  }
  /* Fields:
   * points ...IList<CartPt>
   * Methods:
   * this.IsContainedinList(CartPt t) ... boolean
   * this.test(CartPt t) ... boolean
   * Methods for fields:
   * points.ormap(Predicate<CartPt>) ... boolean
   */
  
  // checks if this cartpt is between all the cartpts of list
  public boolean isFoundInList(CartPt t) {
    return points.ormap(new IsContained(t));
  }
  
  // test method: checks if the given cart point is contained in any of points
  public boolean test(CartPt t) {
    return points.ormap(new IsContained(t));
  }
}

class NotContainedinList implements Predicate<AGamePiece> {
  IList<AGamePiece> points;
  
  NotContainedinList(IList<AGamePiece> points) {
    this.points = points;
  }
  /* Fields:
   * points ...IList<CartPt>
   * Methods:
   * this.test(AGamePiece t) ... boolean
   * Methods for fields:
   * points.ormap(Predicate<CartPt>) ... boolean
   */
  
  // test method: checks if given game piece is not contained in all of points
  public boolean test(AGamePiece t) {
    return points.andmap(new NotContained(t));
  }
}

//predicate function object to check if this carpt equals that one
class NotContained implements Predicate<AGamePiece> {
  AGamePiece that;

  NotContained(AGamePiece that) {
    this.that = that;
  }
  /*
   * Fields: 
   * that ... AGamePiece 
   * Methods: 
   * this.test(AGamePiece t) ... boolean
   * Methods for fields: none
   */

  // check if this game piece's carpt is not equal to that one
  // methods for parameter t: t.posn.IsContained(CartPt) ... booleam
  public boolean test(AGamePiece t) {
    return !t.posn.isContained(that.posn);
  }
}

// predicate function object to check if this carpt equals that one
class IsContained implements Predicate<CartPt> {
  CartPt that;

  IsContained(CartPt that) {
    this.that = that;
  }
  /* Fields:
   * that ... CartPt
   * Methods:
   * this.test(CartPt t) ... boolean
   * Methods for fields: none
   */
  
  // check if this carpt equals that one
  public boolean test(CartPt t) {
    return t.isContained(that);
  }
}

//predicate function object to check if this carpt equals that one
class NotWithinRange implements Predicate<AGamePiece> {
  AGamePiece that;

  NotWithinRange(AGamePiece that) {
    this.that = that;
  }
  /*
   * Fields: 
   * that ... AGamePiece 
   * Methods: 
   * this.test(AGamePiece t) ... boolean
   * Methods for fields: 
   * that.isWithinRange(AGamepiece t) ... boolean
   */

  // check if this game piece's carpt is not within range of that one
  public boolean test(AGamePiece t) {
    return !that.isWithinRange(t);
  }
}

//checks whether given game piece is not within range of one of pieces
class NotWithinList implements Predicate<AGamePiece> {
  IList<AGamePiece> pieces;

  NotWithinList(IList<AGamePiece> pieces) {
    this.pieces = pieces;
  }
  /* Fields:
  * pieces ... IList<AGamePiece>
  * Methods:
  * this.test(AGamePiece t) ... boolean
  * Methods for fields: 
  * this.pieces.andmap(Predicate<AGamePiece>) ... boolean
  */

  // checks whether this given t is not within range of one of pieces
  public boolean test(AGamePiece t) {
    return this.pieces.andmap(new NotWithinRange(t));
  }
}

// class that checks if that agamepiece is not within range of this
class NotWithinBulletRange implements Predicate<AGamePiece> {
  AGamePiece that;

  NotWithinBulletRange(AGamePiece that) {
    this.that = that;
  }
  /*
   * Fields: 
   * that ... AGamePiece 
   * Methods: 
   * this.test(AGamePiece t) ... boolean
   * Methods for fields: 
   * that.isWithinBulletRange(AGamePiece t) ... boolean
   */

  // check if that carpt is not within range of this one
  public boolean test(AGamePiece t) {
    return !that.isWithinBulletRange(t);
  }
}

// class that checks if agamiece is not within range of all pieces
class NotWithinBulletList implements Predicate<AGamePiece> {
  IList<AGamePiece> pieces;

  NotWithinBulletList(IList<AGamePiece> pieces) {
    this.pieces = pieces;
  }
  /*
   * Fields: 
   * pieces ... IList<AGamePiece> 
   * Methods: 
   * this.test(AGamePiece t) ... boolean 
   * Methods for fields: 
   * this.pieces.andmap(Predicate<AGamePiece>) ... boolean
   */

  // checks whether given agampiece is not within range of all of pieces
  public boolean test(AGamePiece t) {
    return this.pieces.andmap(new NotWithinRange(t));
  }
}

// predicate function object to check if this carpt equals that one
class IsWithinRange implements Predicate<AGamePiece> {
  AGamePiece that;

  IsWithinRange(AGamePiece that) {
    this.that = that;
  }
  /*
   * Fields: 
   * that ... AGamePiece 
   * Methods: 
   * this.test(AGamePiece t) ... boolean
   * Methods for fields: 
   * that.isWithinRange(AGamePiece t) ... boolean
   */

  // check if this game piece is within range of that one
  public boolean test(AGamePiece t) {
    return that.isWithinRange(t);
  }
}

// class for moving a game piece
class Move implements Function<AGamePiece, AGamePiece> {

  // moves the game piece by its given speed
  public AGamePiece apply(AGamePiece t) {
    return t.move();
  }
}

// builds list of invader bullets, making sure less than 10
class BuildBulletsList {
  IList<AGamePiece> pieces;

  BuildBulletsList(IList<AGamePiece> pieces) {
    this.pieces = pieces;
  }
  /*
   * Fields: 
   * pieces ... IList<AGamePiece> 
   * Methods: 
   * this.buildBulletsList(IList<AGamePiece> invaders, int n) ... IList<AGamePiece> 
   * Methods for fields: 
   * this.pieces.length() ... int
   * new BuildBullets().buildBullet(CartPt, pieces)... AGamePiece
   */


  // build list of invader bullets
  public IList<AGamePiece> buildBulletsList(IList<AGamePiece> invaders, int n) {
    if (this.pieces.length() < 10) {
      return new ConsList<AGamePiece>(
          new BuildBullets().buildBullet(invaders.map(new GetPosn()).indexOf(n)), pieces);
    }
    else {
      return pieces;
    }
  }
}

// build a new random invader bullets
class BuildBullets {

  // creates a bullet from a given cart posn
  AGamePiece buildBullet(CartPt posn) {
    return new InvaderBullets(3, 5, Color.RED, posn);
  }
}

// class to get the posn
class GetPosn implements Function<AGamePiece, CartPt> {

  // gets the cart pt of given game piece
  public CartPt apply(AGamePiece t) {
    return t.posn;
  }
}

// class to check if given cartpt is out of bounds
class IsOutOfBounds implements Predicate<AGamePiece> {

  // check if given agamepiece is out of bounds
  public boolean test(AGamePiece t) {
    return t.exceedsBounds();
  }
}

// random class
class Rand {
  Random rand;

  // The constructor for use in "real" games
  Rand() {
    this(new Random());
  }
  /* Fields:
   * rand ... Random
   * Methods: none
   * Methods for fields: none
   */

  // The constructor for use in testing, with a specified Random object
  Rand(Random rand) {
    this.rand = rand;
  }
}

// examples and check-expects
class ExamplesSpaceInvaders {
  CartPt posn1 = new CartPt(200, 450);
  CartPt posn2 = new CartPt(200, 400);
  CartPt posn21 = new CartPt(120, 350);
  CartPt posn22 = new CartPt(200, 250);
  CartPt posn23 = new CartPt(100, 100);
  CartPt posn24 = new CartPt(50, 70);
  CartPt outOfBounds = new CartPt(0, -5);
  CartPt posn3 = new CartPt(120, 250);
  CartPt posn4 = new CartPt(20, 5);
  CartPt posn41 = new CartPt(18, 3);
  CartPt posn42 = new CartPt(60, 4);
  CartPt posn5 = new CartPt(65, 5);
  CartPt posn6 = new CartPt(110, 5);
  CartPt posn7 = new CartPt(155, 5);
  CartPt posn8 = new CartPt(200, 5);
  CartPt posn9 = new CartPt(245, 5);
  CartPt posn10 = new CartPt(290, 5);
  CartPt posn11 = new CartPt(335, 5);
  CartPt posn12 = new CartPt(380, 5);
  CartPt posn13 = new CartPt(290, 5);
  CartPt posn14 = new CartPt(335, 5);
  CartPt posn15 = new CartPt(380, 5);
  CartPt posnTest = new CartPt(200, 110);
  CartPt posnTest2 = new CartPt(120, 60);
  CartPt pt1 = new CartPt(200, 395);
  CartPt pt2 = new CartPt(120, 345);
  CartPt pt3 = new CartPt(65, 6);

  SpaceShipBullets bulletTest = new SpaceShipBullets(3, 5, Color.BLACK, this.posnTest);
  SpaceShipBullets bulletTest2 = new SpaceShipBullets(3, 5, Color.BLACK, this.posnTest2);
  SpaceShipBullets bullet1 = new SpaceShipBullets(3, 5, Color.BLACK, this.posn2);
  SpaceShipBullets bullet111 = new SpaceShipBullets(3, 5, Color.RED, this.posn1);
  InvaderBullets invaderBullet1 = new InvaderBullets(3, 5, Color.BLACK, this.posn2);
  InvaderBullets bullet2 = new InvaderBullets(3, 50, Color.RED, this.posn3);
  InvaderBullets bullet23 = new InvaderBullets(3, 50, Color.RED, this.posn23);
  InvaderBullets bullet24 = new InvaderBullets(3, 50, Color.RED, this.posn24);
  SpaceShipBullets bullet21 = new SpaceShipBullets(3, 5, Color.RED, this.posn21);
  SpaceShipBullets bullet22 = new SpaceShipBullets(3, 5, Color.RED, this.posn22);
  SpaceShipBullets spaceBullet4 = new SpaceShipBullets(3, 50, Color.RED, this.posn4);
  SpaceShipBullets spaceBullet41 = new SpaceShipBullets(3, 50, Color.RED, this.posn41);
  SpaceShipBullets spaceBullet42 = new SpaceShipBullets(3, 50, Color.RED, this.posn42);
  SpaceShipBullets spaceBullet5 = new SpaceShipBullets(3, 50, Color.RED, this.outOfBounds);
  InvaderBullets bullet3 = new InvaderBullets(3, 1, Color.RED, this.posn4);
  InvaderBullets bullet31 = new InvaderBullets(3, 5, Color.RED, this.posn4);
  InvaderBullets bullet32 = new InvaderBullets(3, 5, Color.RED, this.posn4);
  InvaderBullets bullet4 = new InvaderBullets(3, 1, Color.RED, this.posn5);
  InvaderBullets bullet5 = new InvaderBullets(3, 1, Color.RED, this.posn6);
  SpaceShip spaceShip = new SpaceShip(this.posn1, true, this.bullet1, Color.BLACK, 20);
  Invader initialInvader = new Invader(new CartPt(20, 5), this.bullet2, Color.RED, 30);
  IList<AGamePiece> mt = new MtList<AGamePiece>();
  Invader invader1 = new Invader(this.posn4, this.bullet3, Color.RED, 10);
  Invader invaderResult = new Invader(this.posn5, this.bullet3, Color.RED, 10);
  Invader invader2 = new Invader(this.posn5, this.bullet4, Color.RED, 10);
  Invader invader3 = new Invader(this.posn6, this.bullet5, Color.RED, 10);
  SpaceShipBullets spaceBulletTest1 = new SpaceShipBullets(3, 5, Color.BLACK, this.pt1);
  SpaceShipBullets spaceBulletTest2 = new SpaceShipBullets(3, 5, Color.RED, this.pt2);
  AGamePiece invaderBulletTest3 = new InvaderBullets(3, 1, Color.RED, this.pt3);

  IList<AGamePiece> listofBulletsResult = new ConsList<AGamePiece>(this.spaceBulletTest1,
      new ConsList<AGamePiece>(this.spaceBulletTest2,
          new ConsList<AGamePiece>(this.invaderBulletTest3, this.mt)));
  IList<AGamePiece> listofInvaders1 = new ConsList<AGamePiece>(this.invader1,
      new ConsList<AGamePiece>(this.invader2, new ConsList<AGamePiece>(this.invader3, this.mt)));
  IList<CartPt> loCpt = new ConsList<CartPt>(this.posn1,
      new ConsList<CartPt>(this.posn2, new ConsList<CartPt>(this.posn3, new MtList<CartPt>())));

  IList<Integer> mtloi = new MtList<Integer>();
  IList<Integer> loi1 = new ConsList<Integer>(1,
      new ConsList<Integer>(2, new ConsList<Integer>(3, new MtList<Integer>())));
  IList<Integer> loi3 = new ConsList<Integer>(0,
      new ConsList<Integer>(1, new ConsList<Integer>(2, new MtList<Integer>())));
  IList<Integer> loi2 = new ConsList<Integer>(4,
      new ConsList<Integer>(5, new ConsList<Integer>(6, new MtList<Integer>())));

  IList<AGamePiece> listofgamePieces = new ConsList<AGamePiece>(this.spaceShip,
      new ConsList<AGamePiece>(this.bullet1, new ConsList<AGamePiece>(this.bullet2, this.mt)));
  IList<AGamePiece> listofgamePieces2 = new ConsList<AGamePiece>(this.bullet1,
      new ConsList<AGamePiece>(this.bullet2, this.mt));
  IList<AGamePiece> listofgamePieces3 = new ConsList<AGamePiece>(this.spaceShip,
      new ConsList<AGamePiece>(this.bullet2, this.mt));
  IList<AGamePiece> listofgamePiecesResult = new ConsList<AGamePiece>(this.spaceShip.move(),
      new ConsList<AGamePiece>(this.bullet1.move(),
          new ConsList<AGamePiece>(this.bullet2.move(), this.mt)));
  IList<AGamePiece> listofSpaceBullets = new ConsList<AGamePiece>(this.bullet1,
      new ConsList<AGamePiece>(this.bullet21, new ConsList<AGamePiece>(this.bullet4, this.mt)));
  IList<AGamePiece> listofSpaceBulletsComplete = new ConsList<AGamePiece>(this.bullet1,
      new ConsList<AGamePiece>(this.bullet21, this.mt));
  IList<AGamePiece> listofSpaceBulletsResult = new ConsList<AGamePiece>(this.bullet21,
      new ConsList<AGamePiece>(this.bullet4, this.mt));
  IList<AGamePiece> listofSpaceBullets2 = new ConsList<AGamePiece>(this.bullet1,
      new ConsList<AGamePiece>(this.spaceBullet42,
          new ConsList<AGamePiece>(this.spaceBullet41, this.mt)));
  IList<AGamePiece> listofSpaceBulletsTest = new ConsList<AGamePiece>(this.bulletTest,
      new ConsList<AGamePiece>(this.bulletTest2, this.mt));
  IList<AGamePiece> listofSpaceBullets3 = new ConsList<AGamePiece>(this.spaceBullet5,
      new ConsList<AGamePiece>(this.spaceBullet4,
          new ConsList<AGamePiece>(this.bullet22, this.mt)));
  IList<AGamePiece> listofInvaderBullets = new ConsList<AGamePiece>(this.bullet2,
      new ConsList<AGamePiece>(this.bullet23, new ConsList<AGamePiece>(this.bullet24, this.mt)));
  IList<AGamePiece> listofElements = new ConsList<AGamePiece>(this.spaceShip,
      new ConsList<AGamePiece>(this.bullet1,
          new ConsList<AGamePiece>(this.bullet2, new MtList<AGamePiece>())))
              .append(new BuildListInvaders().invadersList());
  
  Game gameComplete = new Game(this.spaceShip, new BuildListInvaders().invadersList(),
      this.listofSpaceBulletsComplete, this.listofInvaderBullets);
  Game gameCompleteComplete = new Game(this.spaceShip, new BuildListInvaders().invadersList(),
      this.mt, this.mt);
  Game gameTestResult = new Game(this.spaceShip, new BuildListInvaders().invadersList(),
      new ConsList<AGamePiece>(this.bullet1, this.mt), this.mt);
  Game gameTest = new Game(this.spaceShip, this.mt, new ConsList<AGamePiece>(this.bullet1, this.mt),
      new ConsList<AGamePiece>(this.bullet2, this.mt));
  Game mtGame = new Game(this.spaceShip, this.mt, this.mt, this.mt);

  // render the game
  boolean testRenderGame(Tester t) {
    this.gameCompleteComplete.bigBang(400, 500, 0.05);
    return true;
  }

  //test moveShip() 
  boolean testMoveShip(Tester t) {
    return t.checkExpect(this.spaceShip.moveShip(), this.spaceShip.moveRight())
        && t.checkExpect(this.bullet1.moveShip(), this.bullet1)
        && t.checkExpect(this.bullet2.moveShip(), this.bullet2)
        && t.checkExpect(this.invader1.moveShip(), this.invader1)
        && t.checkExpect(new SpaceShip(this.posn1, false, this.bullet1, Color.BLACK, 20).moveShip(),
            new SpaceShip(this.posn1, false, this.bullet1, Color.BLACK, 20).moveLeft());
  }

  // test move()
  boolean testMove(Tester t) {
    return t.checkExpect(this.spaceShip.move(),
        new SpaceShip(new CartPt(195, 450), true, this.bullet1, Color.BLACK, 20))
        && t.checkExpect(this.invader1.move(), this.invader1)
        && t.checkExpect(this.bullet1.move(),
            new SpaceShipBullets(3, 5, Color.BLACK, new CartPt(200, 395)))
        && t.checkExpect(this.bullet2.move(),
            new InvaderBullets(3, 50, Color.RED, new CartPt(120, 300)));
  }

  // test moveLeft()
  boolean testMoveLeft(Tester t) {
    return t.checkExpect(this.spaceShip.moveLeft(),
        new SpaceShip(new CartPt(195, 450), false, this.bullet1, Color.BLACK, 20))
        && t.checkExpect(
            new SpaceShip(new CartPt(10, 450), true, this.bullet1, Color.BLACK, 20).moveLeft(),
            new SpaceShip(new CartPt(10, 450), false, this.bullet1, Color.BLACK, 20))
        && t.checkExpect(this.invader1.moveLeft(),
            new Invader(this.posn4, this.bullet3, Color.RED, 10))
        && t.checkExpect(this.bullet1.moveLeft(),
            new SpaceShipBullets(3, 5, Color.BLACK, this.posn2))
        && t.checkExpect(this.bullet2.moveLeft(), new InvaderBullets(3, 50, Color.RED, this.posn3));
  }

  // test moveRight()
  boolean testMoveRight(Tester t) {
    return t.checkExpect(this.spaceShip.moveRight(),
        new SpaceShip(new CartPt(205, 450), true, this.bullet1, Color.BLACK, 20))
        && t.checkExpect(
            new SpaceShip(new CartPt(400, 450), true, this.bullet1, Color.BLACK, 20).moveRight(),
            new SpaceShip(new CartPt(400, 450), true, this.bullet1, Color.BLACK, 20))
        && t.checkExpect(this.invader1.moveRight(),
            new Invader(this.posn4, this.bullet3, Color.RED, 10))
        && t.checkExpect(this.bullet1.moveRight(),
            new SpaceShipBullets(3, 5, Color.BLACK, this.posn2))
        && t.checkExpect(this.bullet2.moveRight(),
            new InvaderBullets(3, 50, Color.RED, this.posn3));
  }

  // test exceedsBounds()
  boolean testExceedsBounds(Tester t) {
    return t.checkExpect(this.spaceShip.exceedsBounds(), true)
        && t.checkExpect(this.invader1.exceedsBounds(), true)
        && t.checkExpect(this.bullet1.exceedsBounds(), true)
        && t.checkExpect(
            new SpaceShipBullets(3, 5, Color.BLACK, new CartPt(200, -10)).exceedsBounds(), false)
        && t.checkExpect(this.bullet2.exceedsBounds(), true) && t.checkExpect(
            new InvaderBullets(3, 50, Color.RED, new CartPt(200, 510)).exceedsBounds(), false);
  }

  // test isWithinRange()
  boolean testIsWithinRange(Tester t) {
    return t.checkExpect(this.spaceShip.isWithinRange(this.bullet1), false)
        && t.checkExpect(this.spaceShip.isWithinRange(this.invader1), false)
        && t.checkExpect(this.spaceShip.isWithinRange(this.bullet2), false)
        && t.checkExpect(new SpaceShip(new CartPt(200, 400), true, this.bullet1, Color.BLACK, 20)
            .isWithinRange(this.bullet1), true)
        && t.checkExpect(new SpaceShip(new CartPt(20, 5), true, this.bullet1, Color.BLACK, 20)
            .isWithinRange(this.invader1), true)
        && t.checkExpect(new SpaceShip(new CartPt(120, 250), true, this.bullet1, Color.BLACK, 20)
            .isWithinRange(this.bullet2), true)
        && t.checkExpect(this.invader1.isWithinRange(this.bullet1), false)
        && t.checkExpect(this.invader1.isWithinRange(this.spaceShip), false)
        && t.checkExpect(this.invader1.isWithinRange(this.bullet2), false)
        && t.checkExpect(new Invader(new CartPt(200, 400), this.bullet3, Color.RED, 10)
            .isWithinRange(this.bullet1), true)
        && t.checkExpect(new Invader(new CartPt(200, 450), this.bullet3, Color.RED, 10)
            .isWithinRange(this.spaceShip), true)
        && t.checkExpect(new Invader(new CartPt(120, 250), this.bullet3, Color.RED, 10)
            .isWithinRange(this.bullet2), true)
        && t.checkExpect(this.bullet1.isWithinRange(this.spaceShip), false)
        && t.checkExpect(this.bullet1.isWithinRange(this.invader1), false)
        && t.checkExpect(this.bullet1.isWithinRange(this.bullet2), false)
        && t.checkExpect(
            new SpaceShipBullets(3, 5, Color.BLACK, new CartPt(20, 5)).isWithinRange(this.invader1),
            true)
        && t.checkExpect(new SpaceShipBullets(3, 5, Color.BLACK, new CartPt(200, 450))
            .isWithinRange(this.spaceShip), true)
        && t.checkExpect(new SpaceShipBullets(3, 5, Color.BLACK, new CartPt(120, 250))
            .isWithinRange(this.bullet2), true)
        && t.checkExpect(this.bullet2.isWithinRange(this.bullet1), false)
        && t.checkExpect(this.bullet2.isWithinRange(this.spaceShip), false)
        && t.checkExpect(this.bullet2.isWithinRange(this.invader1), false)
        && t.checkExpect(
            new InvaderBullets(3, 50, Color.RED, new CartPt(200, 400)).isWithinRange(this.bullet1),
            true)
        && t.checkExpect(new InvaderBullets(3, 50, Color.RED, new CartPt(200, 450))
            .isWithinRange(this.spaceShip), true)
        && t.checkExpect(
            new InvaderBullets(3, 50, Color.RED, new CartPt(20, 5)).isWithinRange(this.invader1),
            true);
  }

  // test for isWithinBulletRange(AGamePiece)
  boolean testIsWithinBulletRange(Tester t) {
    return t.checkExpect(this.spaceShip.isWithinBulletRange(this.bullet1), false)
        && t.checkExpect(this.invader1.isWithinBulletRange(this.bullet1), false)
        && t.checkExpect(this.bullet1.isWithinBulletRange(this.bullet1), true)
        && t.checkExpect(this.bullet1.isWithinBulletRange(this.bullet2), false)
        && t.checkExpect(this.bullet1.isWithinBulletRange(this.spaceShip), false)
        && t.checkExpect(this.bullet1.isWithinBulletRange(this.invader1), false)
        && t.checkExpect(this.bullet2.isWithinBulletRange(this.bullet2), true)
        && t.checkExpect(this.bullet2.isWithinBulletRange(this.spaceShip), false)
        && t.checkExpect(this.bullet2.isWithinBulletRange(this.invader1), false);
  }

  // test for shipShiftBySpeed(int speed)
  boolean testShipShiftBySpeed(Tester t) {
    return t.checkExpect(this.posn1.shipShiftBySpeed(5), new CartPt(205, 450))
        && t.checkExpect(this.posn1.shipShiftBySpeed(0), new CartPt(200, 450))
        && t.checkExpect(this.posn1.shipShiftBySpeed(10), new CartPt(210, 450));
  }

  // test for shipShiftLeftSpeed
  boolean testShipShiftLeftSpeed(Tester t) {
    return t.checkExpect(this.posn1.shipShiftLeftSpeed(5), new CartPt(195, 450))
        && t.checkExpect(this.posn1.shipShiftLeftSpeed(0), new CartPt(200, 450))
        && t.checkExpect(this.posn1.shipShiftLeftSpeed(10), new CartPt(190, 450));
  }

  // test for spaceBulletShift(int speed)
  boolean testSpaceBulletShift(Tester t) {
    return t.checkExpect(this.posn1.spaceBulletShift(5), new CartPt(200, 445))
        && t.checkExpect(this.posn1.spaceBulletShift(0), new CartPt(200, 450))
        && t.checkExpect(this.posn1.spaceBulletShift(10), new CartPt(200, 440));
  }

  // test for invaderBulletShift(int speed)
  boolean testInvaderBulletShift(Tester t) {
    return t.checkExpect(this.posn1.invaderBulletShift(5), new CartPt(200, 455))
        && t.checkExpect(this.posn1.invaderBulletShift(0), new CartPt(200, 450))
        && t.checkExpect(this.posn1.invaderBulletShift(10), new CartPt(200, 460));
  }

  // test for ontick
  boolean testOnTick(Tester t) {
    return t.checkExpect(
        new Game(this.spaceShip, new ConsList<AGamePiece>(this.invader1, this.mt),
            new ConsList<AGamePiece>(this.bullet1, this.mt),
            new ConsList<AGamePiece>(this.bullet2, this.mt)).onTick(),
        new Game(new SpaceShip(new CartPt(205, 450), true, this.bullet1, Color.BLACK, 20),
            new ConsList<AGamePiece>(this.invader1, this.mt),
            new ConsList<AGamePiece>(new SpaceShipBullets(3, 5, Color.BLACK, new CartPt(200, 395)),
                this.mt),
            new ConsList<AGamePiece>(new InvaderBullets(3, 5, Color.RED, new CartPt(20, 10)),
                new ConsList<AGamePiece>(new InvaderBullets(3, 50, Color.RED, new CartPt(120, 300)),
                    this.mt))))
        && t.checkExpect(
            new Game(this.spaceShip, new ConsList<AGamePiece>(this.invader1, this.mt),
                new ConsList<AGamePiece>(this.bullet21, this.mt),
                new ConsList<AGamePiece>(this.bullet3, this.mt)).onTick(),
            new Game(new SpaceShip(new CartPt(205, 450), true, this.bullet1, Color.BLACK, 20),
                new ConsList<AGamePiece>(this.invader1, this.mt),
                new ConsList<AGamePiece>(
                    new SpaceShipBullets(3, 5, Color.RED, new CartPt(120, 345)), this.mt),
                new ConsList<AGamePiece>(new InvaderBullets(3, 5, Color.RED, new CartPt(20, 10)),
                    new ConsList<AGamePiece>(new InvaderBullets(3, 1, Color.RED, new CartPt(20, 6)),
                        this.mt))));
  }

  // test for onKeyEvent()
  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(
        new Game(this.spaceShip, new ConsList<AGamePiece>(this.invader1, this.mt),
            new ConsList<AGamePiece>(this.bullet1, this.mt),
            new ConsList<AGamePiece>(this.bullet2, this.mt)).onKeyEvent("left"),
        new Game(new SpaceShip(new CartPt(200, 450), false, this.bullet1, Color.BLACK, 20),
            new ConsList<AGamePiece>(this.invader1, this.mt),
            new ConsList<AGamePiece>(this.bullet1, this.mt),
            new ConsList<AGamePiece>(this.bullet2, this.mt)))
        && t.checkExpect(
            new Game(this.spaceShip, new ConsList<AGamePiece>(this.invader1, this.mt),
                new ConsList<AGamePiece>(this.bullet1, this.mt),
                new ConsList<AGamePiece>(this.bullet2, this.mt)).onKeyEvent("right"),
            new Game(new SpaceShip(new CartPt(200, 450), true, this.bullet1, Color.BLACK, 20),
                new ConsList<AGamePiece>(this.invader1, this.mt),
                new ConsList<AGamePiece>(this.bullet1, this.mt),
                new ConsList<AGamePiece>(this.bullet2, this.mt)))
        && t.checkExpect(
            new Game(this.spaceShip, new ConsList<AGamePiece>(this.invader1, this.mt),
                new ConsList<AGamePiece>(this.bullet1, this.mt),
                new ConsList<AGamePiece>(this.bullet2, this.mt)).onKeyEvent(" "),
            new Game(new SpaceShip(new CartPt(200, 450), true, this.bullet1, Color.BLACK, 20),
                new ConsList<AGamePiece>(this.invader1, this.mt),
                new ConsList<AGamePiece>(new SpaceShipBullets(3, 5, Color.RED, this.spaceShip.posn),
                    new ConsList<AGamePiece>(this.bullet1, this.mt)),
                new ConsList<AGamePiece>(this.bullet2, this.mt)))
        && t.checkExpect(
            new Game(this.spaceShip, new ConsList<AGamePiece>(this.invader1, this.mt),
                new ConsList<AGamePiece>(this.bullet1, this.mt),
                new ConsList<AGamePiece>(this.bullet2, this.mt)).onKeyEvent("blue"),
            new Game(this.spaceShip, new ConsList<AGamePiece>(this.invader1, this.mt),
                new ConsList<AGamePiece>(this.bullet1, this.mt),
                new ConsList<AGamePiece>(this.bullet2, this.mt)));
  }

  boolean testOnKey(Tester t) {
    Game gameCompleteLocal = new Game(this.spaceShip, this.mt, this.mt, this.mt);
    Game gameTestResultLocal = new Game(this.spaceShip, this.mt,
        new ConsList<AGamePiece>(this.bullet111, this.mt), this.mt);
    return t.checkExpect(gameCompleteLocal.onKeyEvent(" "), gameTestResultLocal);
  }

  // test all methods that deal with fold:
  boolean testFold(Tester t) {
    return t.checkExpect(new Utils().renderListInvaders(this.listofgamePieces),
        (new WorldScene(400, 500).placeImageXY(this.spaceShip.render(), 200, 450))
            .placeImageXY(this.bullet1.render(), 200, 400)
            .placeImageXY(this.bullet2.render(), 120, 250))
        && t.checkExpect(new Utils().renderListInvaders(this.mt), new WorldScene(400, 500))
        && t.checkExpect(this.gameTest.makeScene(),
            ((new WorldScene(400, 500).placeImageXY(this.spaceShip.render(), 200, 450))
                .placeImageXY(this.bullet1.render(), 200, 400)).placeImageXY(this.bullet2.render(),
                    120, 250))
        && t.checkExpect(this.mtGame.makeScene(),
            new WorldScene(400, 500).placeImageXY(this.spaceShip.render(), 200, 450));
  }

  // test all methods that deal with map:
  boolean testMap(Tester t) {
    return t.checkExpect(new Utils().buildInvaderList(this.loi3), this.listofInvaders1)
        && t.checkExpect(new Utils().buildInvaderList(this.mtloi), this.mt)
        && t.checkExpect(this.loi1.map(s -> s + 1),
            new ConsList<Integer>(2,
                new ConsList<Integer>(3, new ConsList<Integer>(4, this.mtloi))))
        && t.checkExpect(this.mtloi.map(s -> s + 69), this.mtloi);
  }

  // test all methods that deal with ormap:
  boolean testOrMap(Tester t) {
    return t.checkExpect(this.loi1.ormap(s -> s > 100), false)
        && t.checkExpect(this.mtloi.ormap(s -> s == 1), false)
        && t.checkExpect(this.loi1.ormap(s -> s == 1), true)
        && t.checkExpect(new IsContainedinList(this.loCpt).isFoundInList(this.posn1), true)
        && t.checkExpect(new IsContainedinList(this.loCpt).isFoundInList(this.posn4), false)
        && t.checkExpect(new IsContainedinList(this.loCpt).isFoundInList(this.posn2), true)
        && t.checkExpect(new IsContainedinList(new MtList<CartPt>()).isFoundInList(this.posn1),
            false);
  }

  // test all methods that deal with andmap:
  boolean testAndMap(Tester t) {
    return t.checkExpect(this.loi1.andmap(s -> s <= 3), true)
        && t.checkExpect(this.loi1.andmap(s -> s < 3), false)
        && t.checkExpect(this.mtloi.andmap(s -> s + 420 >= 0), true);
  }

  // test all methods that deal with filter:
  boolean testFilter(Tester t) {
    return t.checkExpect(this.loi1.filter(s -> s == 1), new ConsList<Integer>(1, this.mtloi))
        && t.checkExpect(this.loi1.filter(s -> s == 420), this.mtloi)
        && t.checkExpect(this.loCpt.filter(s -> s.x <= 250), this.loCpt)
        && t.checkExpect(this.mtloi.filter(s -> s < 5), this.mtloi);

  }

  // test IsContained function object:
  boolean testIsContained(Tester t) {
    return t.checkExpect(new IsContained(this.posn1).test(this.posn1), true)
        && t.checkExpect(new IsContained(this.posn2).test(this.posn4), false)
        && t.checkExpect(new IsContained(this.posn5).test(this.posn1), false);
  }

  // test buildListFun function object:
  boolean testBuilListFun(Tester t) {
    return t.checkExpect(new BuildListFun().apply(0),
        new Invader(this.posn4, new InvaderBullets(3, 1, Color.RED, this.posn4), Color.RED, 10))
        && t.checkExpect(new BuildListFun().apply(1), new Invader(this.posn5,
            new InvaderBullets(3, 1, Color.RED, this.posn5), Color.RED, 10));
  }

  // test render method:
  boolean testRender(Tester t) {
    return t.checkExpect(this.spaceShip.render(), new RectangleImage(50, 20, "solid", Color.BLACK))
        && t.checkExpect(this.initialInvader.render(),
            new RectangleImage(60, 30, "solid", Color.RED))
        && t.checkExpect(this.bullet1.render(), new CircleImage(3, "solid", Color.BLACK));
  }

  // test placeImage method:
  boolean testPlaceImage(Tester t) {
    return t.checkExpect(this.spaceShip.placeImage(new WorldScene(400, 500)),
        new WorldScene(400, 500).placeImageXY(this.spaceShip.render(), 200, 450))
        && t.checkExpect(this.initialInvader.placeImage(new WorldScene(400, 500)),
            new WorldScene(400, 500).placeImageXY(this.initialInvader.render(), 20, 5))
        && t.checkExpect(this.bullet1.placeImage(new WorldScene(400, 500)),
            new WorldScene(400, 500).placeImageXY(this.bullet1.render(), 200, 400))
        && t.checkExpect(this.bullet2.placeImage(new WorldScene(400, 500)),
            new WorldScene(400, 500).placeImageXY(this.bullet2.render(), 120, 250));
  }

  // test buildList method:
  boolean testBuild(Tester t) {
    return t.checkExpect(new Utils().buildList(0, 4),
        new ConsList<Integer>(0,
            new ConsList<Integer>(1,
                new ConsList<Integer>(2,
                    new ConsList<Integer>(3, new ConsList<Integer>(4, new MtList<Integer>()))))))
        && t.checkExpect(new Utils().buildList(0, 0), new MtList<Integer>());
  }

  // test append method:
  boolean testAppend(Tester t) {
    return t.checkExpect(this.loi1.append(this.loi2),
        new ConsList<Integer>(1,
            new ConsList<Integer>(4, new ConsList<Integer>(2, new ConsList<Integer>(5,
                new ConsList<Integer>(3, new ConsList<Integer>(6, new MtList<Integer>())))))));
  }

  boolean testIsWithinList(Tester t) {
    return t
        .checkExpect(this.listofSpaceBullets2
            .filter(new NotWithinList(new BuildListInvaders().invadersList())).length(), 1)
        && t.checkExpect(new BuildListInvaders().invadersList()
            .filter(new NotWithinList(this.listofSpaceBullets2)).length(), 34)
        && t.checkExpect(new BuildListInvaders().invadersList()
            .filter(new NotWithinList(this.listofSpaceBullets2)).length(), 34)
        && t.checkExpect(this.listofSpaceBulletsTest
            .filter(new NotWithinList(new BuildListInvaders().invadersList())).length(), 2)
        && t.checkExpect(
            this.mt.filter(new NotWithinList(new BuildListInvaders().invadersList())).length(), 0);
  }

  // test IsOutOfBounds:
  // Its working don't worry.
  boolean testIsOutOfBounds(Tester t) {
    return t.checkExpect(this.listofSpaceBullets3.filter(new IsOutOfBounds()),
        new ConsList<AGamePiece>(this.spaceBullet4,
            new ConsList<AGamePiece>(this.bullet22, this.mt)))
        && t.checkExpect(this.mt.filter(new IsOutOfBounds()), this.mt);

  }

  // test Random.nextInt method:
  boolean testRand(Tester t) {
    return t.checkExpect(new Random(100).nextInt(10), 5)
        && t.checkExpect(new Random(100).nextInt(15), 10)
        && t.checkExpect(new Random(100).nextInt(5), 0);
  }

  // test BuildBullets() with random:
  // note: could not explicitly test function object, but this is an alternative
  boolean testBuilRandomBullets(Tester t) {
    return t.checkExpect(new BuildBullets().buildBullet(new CartPt(245, 5)),
        new InvaderBullets(3, 5, Color.RED, new CartPt(245, 5)))
        && t.checkExpect(new BuildBullets().buildBullet(new CartPt(65, 20)),
            new InvaderBullets(3, 5, Color.RED, new CartPt(65, 20)))
        && t.checkExpect(new BuildBullets().buildBullet(new CartPt(20, 5)),
            new InvaderBullets(3, 5, Color.RED, new CartPt(20, 5)));
  }

  // test move method, within utils class:
  boolean testUtilsMove(Tester t) {
    return t.checkExpect(new Utils().move(this.mt), this.mt)
        && t.checkExpect(new Utils().move(this.listofSpaceBullets), this.listofBulletsResult)
        && t.checkExpect(new Utils().move(this.listofgamePieces), this.listofgamePiecesResult);
  }

  // test move method, within utils class:
  boolean testNotContained(Tester t) {
    return t.checkExpect(this.listofgamePieces.filter(new NotContainedinList(this.listofElements)),
        this.mt)
        && t.checkExpect(this.mt.filter(new NotContainedinList(this.listofElements)), this.mt)
        && t.checkExpect(this.listofgamePieces.filter(new NotContainedinList(this.mt)),
            this.listofgamePieces)
        && t.checkExpect(this.listofgamePieces.filter(new NotContained(this.spaceShip)),
            this.listofgamePieces2);
  }

  // test NotWithinRange method:
  boolean testNotWithinRange(Tester t) {
    return t.checkExpect(this.listofgamePieces.filter(new NotWithinRange(this.spaceShip)),
        this.listofgamePieces2)
        && t.checkExpect(this.mt.filter(new NotWithinRange(this.spaceShip)), this.mt)
        && t.checkExpect(this.listofgamePieces.filter(new NotWithinRange(this.bullet1)),
            this.listofgamePieces3);
  }

  // test NotWithinBulletRange
  boolean testNotWithinBulletRangeList(Tester t) {
    return t.checkExpect(
        this.listofInvaderBullets.filter(new NotWithinBulletRange(this.spaceBullet4)),
        this.listofInvaderBullets)
        && t.checkExpect(this.mt.filter(new NotWithinBulletRange(this.spaceBullet4)), this.mt)
        && t.checkExpect(
            this.listofSpaceBullets.filter(new NotWithinBulletRange(this.invaderResult)),
            this.listofSpaceBullets)
        && t.checkExpect(this.listofSpaceBullets.filter(new NotWithinBulletRange(this.bullet1)),
            this.listofSpaceBulletsResult);
  }

  // test set directions methods:
  boolean testSetDirection(Tester t) {
    return t.checkExpect(this.spaceShip.setDirectionLeft(),
        new SpaceShip(this.posn1, false, this.bullet1, Color.BLACK, 20))
        && t.checkExpect(this.spaceShip.setDirectionRight(), this.spaceShip);
  }

  // test indexOf method:
  boolean testIndexOf(Tester t) {
    return t.checkExpect(this.mt.indexOf(0), null) && t.checkExpect(this.loi1.indexOf(0), 1)
        && t.checkExpect(this.loi1.indexOf(3), null);
  }

  boolean testBuildBulletsList(Tester t) {
    return t.checkExpect(
        new BuildBulletsList(this.listofInvaderBullets).buildBulletsList(this.listofInvaders1,
            new Random(100).nextInt(5)),
        new ConsList<AGamePiece>(this.bullet31, this.listofInvaderBullets));
  }
}
