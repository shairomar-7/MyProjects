import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Note: we have  modified the game so that the cards only get thrown out of the game when they 
// have the same value and the same suit color to make it harder

// represents the most boring card game: concentration
class Concentration extends World {
  ArrayList<Card> cards = new ArrayUtils().create(new CreateDeck());
  ArrayList<Card> cards2;
  ArrayList<Card> shuffledCards = new ArrayList<Card>();
  Random rand = new Random();
  int score = 26;
  int time = 0;

  // constructor for testing
  Concentration(ArrayList<Card> cardsTest) {
    this.shuffledCards = this.shuffleTest(cardsTest);
  }

  // default constructor
  Concentration() {
    this.shuffledCards = this.shuffle();
  }

  // shuffles the list of cards every time the game is run
  ArrayList<Card> shuffle() {
    ArrayList<Card> alc = new ArrayList<Card>(52);
    for (int i = 0; i < 52; i++) {
      alc.add(this.cards.get(i));
    }
    for (int i = 51; i > 0; i--) {
      int randomNumber = this.rand.nextInt(i);
      new ArrayUtils().swap(alc, i, randomNumber);
    }
    for (int i = 0; i < 52; i++) {
      Card c = alc.get(i);
      c.posn.x = 50 + (i % 13) * 50;
      c.posn.y = i / 13 * 70 + 70;
    }
    return alc;
  }

  // shuffles the quarter deck using a seeded random generator
  // used for testing purposes
  ArrayList<Card> shuffleTest(ArrayList<Card> list) {
    ArrayList<Card> imposterDeck = new ArrayList<Card>();
    ArrayList<Card> randomizedDeck = new ArrayList<Card>();
    for (Card c : list) {
      imposterDeck.add(c);
    }
    for (int i = 0; imposterDeck.size() != 0; i++) {
      int randIndex = new Random(i + 1).nextInt(imposterDeck.size());
      Card randElement = imposterDeck.get(randIndex);
      randElement.posn.x = 50 + (i % 13) * 50;
      randElement.posn.y = i / 13 * 70 + 70;
      randomizedDeck.add(randElement);
      imposterDeck.remove(randIndex);
    }
    return randomizedDeck;
  }

  // render the game, containing the list of cards, a timer, and a score
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(700, 600);
    String s2 = "Score: " + Integer.toString(this.score) + " Points";
    String s3 = "Time: " + Integer.toString(this.time) + " Seconds";
    WorldImage wi2 = new TextImage(s2, 15, Color.BLACK);
    WorldImage wi3 = new TextImage(s3, 15, Color.BLACK);
    ws.placeImageXY(wi2, 280, 10);
    ws.placeImageXY(wi3, 420, 10);
    for (int i = 0; i < this.shuffledCards.size(); i++) {
      Card c = this.shuffledCards.get(i);
      ws.placeImageXY(c.renderCard(), c.posn.x, c.posn.y);
    }
    return ws;
  }

  // handles mouse(click) inputs for a given posn
  // EFFECT: flip the card when user clicks on it
  public void onMouseClicked(Posn posn) {
    new ArrayUtils().flip(this.shuffledCards, new CheckPosition(posn.x, posn.y), new ChangeFlag1());
  }

  // updates the world state on every tick
  // EFFECT: mutates the world state, by either filtering cards if they match, or
  // flipping them
  // back if they do not match. Timer value is mutated for every tick.
  public void onTick() {
    int num = this.numFlag();
    time = time + 1;
    score = this.shuffledCards.size() / 2;
    if (num == 2) {
      int size = this.shuffledCards.size();
      Card c2 = new ArrayUtils().find(this.shuffledCards, new CheckFlag());
      if (this.shuffledCards.removeIf(new CheckVal(c2))) {
        new ArrayUtils().filter(this.shuffledCards, new CheckFlag());
      }
      if (this.shuffledCards.size() == this.shuffledCards.size()) {
        new ArrayUtils().flipDown(this.shuffledCards, new ChangeFlag2());
      }
    }
    if (num > 2) {
      new ArrayUtils().flipDown(this.shuffledCards, new ChangeFlag2());
    }
  }

  // handles key inputs:
  // specifically used to reset the game when "r" key is pressed
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      // this.cards2 = new ArrayUtils().create(new CreateDeck());
      this.cards = new ArrayUtils().create(new CreateDeck());
      this.shuffledCards = this.shuffle();
    }
  }

  // ends the concentration game when all pairs have been matched
  public WorldEnd worldEnds() {
    WorldScene ws = new WorldScene(700, 600);
    String s1 = "Bravo!";
    String s2 = "Your Final Score is: " + Integer.toString(this.score - 1) + " Points, Obviously!";
    String s3 = "You won the game in: " + Integer.toString(this.time / 60) + " minutes";
    WorldImage wi1 = new TextImage(s2, 30, Color.magenta);
    WorldImage wi2 = new TextImage(s3, 30, Color.BLACK);
    WorldImage wi3 = new TextImage(s1, 50, Color.RED);
    ws.placeImageXY(wi1, 350, 250);
    ws.placeImageXY(wi2, 350, 400);
    ws.placeImageXY(wi3, 350, 100);
    if (this.shuffledCards.size() == 0) {
      return new WorldEnd(true, ws);
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // returns a count of the number of cards with a true flag (flipped over)
  public int numFlag() {
    int num = 0;
    for (int i = 0; i < this.shuffledCards.size(); i++) {
      Card c1 = this.shuffledCards.get(i);
      if (c1.flag) {
        num = num + 1;
      }
    }
    return num;
  }
}

// represents a card with a value, a suit, a boolean indicating whether it is face up/down,
// and its position on the game's board 
class Card {
  int val;
  String suit;
  boolean flag;
  Posn posn;

  // default constructor
  Card(int val, String suit, boolean flag) {
    this.val = val;
    this.suit = suit;
    this.flag = flag;
    this.posn = new Posn(0, 0);
  }

  // empty constructor
  Card() {
  }

  Card(int val, String suit, boolean flag, Posn posn) {
    this.val = val;
    this.suit = suit;
    this.flag = flag;
    this.posn = posn;
  }

  // renders this card; this includes its value and its suit unless flipped over
  public WorldImage renderCard() {
    if (this.flag) {
      return new OverlayOffsetImage(new TextImage(Integer.toString(this.val), 12, Color.RED), 8, 14,
          new OverlayOffsetImage(new TextImage(Integer.toString(this.val), 12, Color.RED), -5, -14,
              new OverlayImage(new TextImage(this.suit, 9, Color.BLACK),
                  new RectangleImage(30, 50, OutlineMode.SOLID, Color.LIGHT_GRAY))));
    }
    else {
      return new RectangleImage(30, 50, OutlineMode.SOLID, Color.BLACK);
    }
  }

  // builds a card based on its given suit and given value
  public Card cardBuilder(int suit, int value) {
    if (suit == 0) {
      return new Card(value, "♥", false);
    }
    else if (suit == 1) {
      return new Card(value, "♣", false);
    }
    else if (suit == 2) {
      return new Card(value, "♦", false);
    }
    else {
      return new Card(value, "♠", false);
    }
  }

  // check if this card's suit and the given's are of same color
  public boolean suitColor(Card other) {
    if (this.suit.equals("♥") || this.suit.equals("♦")) {
      return other.suit.equals("♥") || other.suit.equals("♦");
    }
    else {
      return other.suit.equals("♣") || other.suit.equals("♠");
    }
  }
}

// represents an IFunc of generic type T
interface IFunction1<T> {
  ArrayList<T> apply(ArrayList<T> alt);
}

// class to create the deck of cards 
class CreateDeck implements IFunction1<Card> {

  // returns a list of cards valued from 1-13 with 4 suits each
  public ArrayList<Card> apply(ArrayList<Card> alt) {
    for (int i = 1; i < 14; i++) {
      for (int j = 0; j < 4; j++) {
        Card c = new Card().cardBuilder(j, i);

        alt.add(c);
      }
    }
    return alt;
  }
}

// predicate class to check if given card's posn is within range of this
class CheckPosition implements Predicate<Card> {
  int x;
  int y;

  // default constructor
  CheckPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // returns whether this x and y are within range of the card's position
  public boolean test(Card t) {
    return Math.abs(t.posn.x - x) < 30 && Math.abs(t.posn.y - y) < 30;
  }
}

// predicate class to check if flag is true
class CheckFlag implements Predicate<Card> {

  // predicate returns if flag is true
  public boolean test(Card t) {
    return t.flag;
  }
}

// predicate class to check if given card is equal to this
class CheckVal implements Predicate<Card> {
  Card c;

  // default constructor
  CheckVal(Card c) {
    this.c = c;
  }

  // predicate returns whether this card has the same value as given one
  // condition: both of them have to be flipped over; we shouldn't be comparing
  // same element
  public boolean test(Card t) {
    if (c == t) {
      return false;
    }
    else {
      return t.val == c.val && t.flag == c.flag && t.suitColor(c);
    }
  }
}

// generic function object of type T
interface IFunction2<T> {
  Void apply(T t);
}

// class to flip the given card
class ChangeFlag1 implements IFunction2<Card> {

  // EFFECT: flips the given card by making flag = true
  public Void apply(Card t) {
    t.flag = true;
    return null;
  }
}

// class to flip the given card
class ChangeFlag2 implements IFunction2<Card> {

  // EFFECT: flips the given card by making flag = false
  public Void apply(Card t) {
    t.flag = false;
    return null;
  }
}

// utils class, specifically for ArrayLists
class ArrayUtils {

  // creates the list of cards
  public <T> ArrayList<T> create(IFunction1<T> fun) {
    ArrayList<T> alt = new ArrayList<T>(52);
    fun.apply(alt);
    return alt;
  }

  // swaps element at index 1 with element at index2 in the given array
  <T> void swap(ArrayList<T> arr, int index1, int index2) {
    T oldValIndex2 = arr.get(index2);
    arr.set(index2, arr.get(index1));
    arr.set(index1, oldValIndex2);
  }

  // applies fun when predicate passes for the given array
  <T> ArrayList<T> flip(ArrayList<T> arr, Predicate<T> pred, IFunction2<T> fun) {
    int index = 0;
    return flipHelp(arr, pred, fun, index);
  }

  // helper method to apply fun if the predicate passes for the given array at the
  // given initial index
  <T> ArrayList<T> flipHelp(ArrayList<T> arr, Predicate<T> pred, IFunction2<T> fun, int index) {
    if (arr.size() <= index) {
      return arr;
    }
    else if (pred.test(arr.get(index))) {
      fun.apply(arr.get(index));
    }
    else {
      flipHelp(arr, pred, fun, index + 1);
    }
    return arr;
  }

  // not tested since not included in this part
  // finds the element that passes the predicate in given array
  <T> T find(ArrayList<T> arr, Predicate<T> pred) {
    int index = 0;
    return findHelp(arr, pred, index);
  }

  // not tested since not included in the part
  // helper method to find element that passes
  // the predicate in given array starting at the given index
  <T> T findHelp(ArrayList<T> arr, Predicate<T> pred, int index) {
    if (pred.test(arr.get(index))) {
      return arr.get(index);
    }
    else {
      return findHelp(arr, pred, index + 1);
    }
  }

  // not tested since it is not part of this assigment
  // applies fun on all the elements of the given array
  <T> void flipDown(ArrayList<T> arr, IFunction2<T> fun) {
    for (int i = 0; i < arr.size(); i++) {
      fun.apply(arr.get(i));
    }
  }

  // not tested since it is not part of this assignment
  // filter elements of type T when they pass the predicate
  <T> void filter(ArrayList<T> arr, Predicate<T> pred) {
    for (int i = 0; i < arr.size(); i++) {
      if (pred.test(arr.get(i))) {
        arr.remove(i);
        i--;
      }
    }
  }
}

//class for examples and check-expects
class ExamplesConcentration {
  Concentration c1 = new Concentration();
  Card card1;
  Card card2;
  Card card3;
  Card card4;
  Card card5;
  Card card6;
  Card card10;
  Card card7;
  Card card8;
  Card card9;
  ArrayList<Card> cards;
  ArrayList<Card> cards2;
  ArrayList<Card> cards8;
  ArrayList<Card> cards4;
  ArrayList<Card> cards5;
  ArrayList<Card> cards3;
  ArrayList<Card> cardsResult;
  Concentration cTest;
  Concentration cTest2;
  Concentration cTest3;
  ArrayList<Card> cards7;
  ArrayList<Card> cards9;
  Concentration mt;
  ArrayList<Card> mtDeck;

  void initData() {
    this.card1 = new Card(5, "♠", true);
    this.card2 = new Card(6, "♣", true);
    this.card3 = new Card(5, "♣", true);
    this.card4 = new Card(10, "♣", false);
    this.card5 = new Card(9, "♦", false);
    this.card6 = new Card(11, "♥", false);
    this.card10 = new Card(5, "♦", true);
    this.card7 = new Card(9, "♥", true, new Posn(0, 0));
    this.cards = new ArrayList<Card>();
    this.cards2 = new ArrayList<Card>();
    this.cards7 = new ArrayList<Card>();
    this.cards8 = new ArrayList<Card>();
    this.cards9 = new ArrayList<Card>();
    this.cards.add(card1);
    this.cards.add(card2);
    this.cards.add(card3);
    this.cards.add(card4);
    this.cards.add(card5);
    this.cards.add(card6);
    this.cardsResult = new ArrayList<Card>();
    this.cardsResult.add(card4);
    this.cardsResult.add(card5);
    this.cardsResult.add(card3);
    this.cardsResult.add(card6);
    this.cardsResult.add(card2);
    this.cardsResult.add(card1);
    this.cards2.add(card1);
    this.cards2.add(card4);
    this.cards8.add(this.card10);
    this.cards8.add(this.card1);
    this.cards8.add(this.card4);
    this.cards8.add(this.card5);
    this.cards7.add(this.card1);
    this.cards7.add(this.card3);
    this.cards7.add(this.card6);
    this.cards7.add(this.card5);
    this.mtDeck = new ArrayList<Card>();
    this.mt = new Concentration(this.mtDeck);
    this.cards3 = new ArrayUtils().create(new CreateDeck());
    this.cards4 = new ArrayList<Card>(Arrays.asList(this.card4, this.card5, this.card6));
    this.cards5 = new ArrayList<Card>(Arrays.asList(this.card1, this.card2, this.card3));
    this.cTest = new Concentration(this.cards);
  }

  void testBigbang(Tester t) {
    c1.bigBang(700, 600, 1);
  }

  // test onKeyEvent:
  void testOnKeyEvent(Tester t) {
    this.initData();
    t.checkExpect(this.mt.shuffledCards.size(), 0);
    this.mt.onKeyEvent("r");
    t.checkExpect(this.mt.shuffledCards.size(), 52);
    // t.checkExpect(this.cTest.shuffledCards.size(), 6);
    this.cTest.onKeyEvent("x");
    t.checkExpect(this.cTest.shuffledCards.size(), 6);
    this.cTest.onKeyEvent("r");
    t.checkExpect(this.cTest.shuffledCards.size(), 52);
  }

  // test onTick:
  void testOnTick(Tester t) {
    this.initData();
    ArrayList<Card> mt2 = new ArrayList<Card>();
    t.checkExpect(this.mt.shuffledCards.size(), 0);
    t.checkExpect(this.mt.shuffledCards, mt2);
    t.checkExpect(this.mt.time, 0);
    t.checkExpect(this.mt.score, 26);
    this.mt.onTick();
    t.checkExpect(this.mt.shuffledCards.size(), 0);
    t.checkExpect(this.mt.shuffledCards, mt2);
    t.checkExpect(this.mt.time, 1);
    t.checkExpect(this.mt.score, 0);
  }

  // test onTick:
  void testOnTick2(Tester t) {
    this.initData();
    ArrayList<Card> ali = new ArrayList<Card>();
    ali.add(this.card4);
    ali.add(this.card1);
    this.cTest2 = new Concentration(this.cards2);
    t.checkExpect(this.cTest2.score, 26);
    t.checkExpect(this.cTest2.shuffledCards, ali);
    t.checkExpect(this.cTest2.time, 0);
    this.cTest2.onTick();
    t.checkExpect(this.cTest2.score, 1);
    t.checkExpect(this.cTest2.shuffledCards, ali);
    t.checkExpect(this.cTest2.time, 1);
  }

  // test onTick:
  // two flipped items, eqal value and same color
  void testOnTick3(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards7);
    t.checkExpect(this.cTest2.shuffledCards.size(), 4);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(3).flag, true);
    t.checkExpect(this.cTest2.score, 26);
    t.checkExpect(this.cTest2.time, 0);
    this.cTest2.onTick();
    t.checkExpect(this.cTest2.shuffledCards.size(), 2);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, false);
    t.checkExpect(this.cTest2.score, 2);
    t.checkExpect(this.cTest2.time, 1);
    this.cTest2.onTick();
    t.checkExpect(this.cTest2.time, 2);
  }

  // test onTick:
  // two flipped items, equal val but diff color
  void testOnTick4(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards8);
    t.checkExpect(this.cTest2.shuffledCards.size(), 4);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(3).flag, true);
    t.checkExpect(this.cTest2.score, 26);
    t.checkExpect(this.cTest2.time, 0);
    this.cTest2.onTick();
    t.checkExpect(this.cTest2.shuffledCards.size(), 4);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(3).flag, false);
    t.checkExpect(this.cTest2.score, 2);
    t.checkExpect(this.cTest2.time, 1);
  }

  // test onTick:
  // two flipped items, not equal whatsover
  void testOnTick5(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards);
    t.checkExpect(this.cTest2.shuffledCards.size(), 6);
    t.checkExpect(this.cTest2.shuffledCards.get(4).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(5).flag, true);
    t.checkExpect(this.cTest2.score, 26);
    t.checkExpect(this.cTest2.time, 0);
    this.cTest2.onTick();
    t.checkExpect(this.cTest2.shuffledCards.size(), 6);
    t.checkExpect(this.cTest2.shuffledCards.get(4).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(5).flag, false);
    t.checkExpect(this.cTest2.score, 3);
    t.checkExpect(this.cTest2.time, 1);
  }

  // test onMouseClicked:
  void testOnMouse(Tester t) {
    this.initData();
    t.checkExpect(this.mt, new Concentration(this.mtDeck));
    this.mt.onMouseClicked(new Posn(50, 65));
    t.checkExpect(this.mt, new Concentration(this.mtDeck));
  }

  // test onMouseClicked:
  void testOnMouse2(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards2);
    this.card1.flag = false;
    ArrayList<Card> resultList = new ArrayList<Card>();
    resultList.add(this.card1);
    resultList.add(this.card4);
    Concentration result = new Concentration(resultList);
    for (Card c : this.cards2) {
      System.out.println(c.posn.x);
      System.out.println(c.posn.y);
    }
    this.cTest2.onMouseClicked(new Posn(100, 70));
    t.checkExpect(this.cTest2, result);

    this.cTest2.onMouseClicked(new Posn(100, 70));
    this.cTest2.onMouseClicked(new Posn(50, 70));
    this.card1.flag = true;
    this.card4.flag = false;
    ArrayList<Card> resultList2 = new ArrayList<Card>();
    resultList2.add(this.card1);
    resultList2.add(this.card4);
    Concentration result2 = new Concentration(resultList);
    t.checkExpect(this.cTest2, result2);
    this.cTest2.onMouseClicked(new Posn(100, 350));
    t.checkExpect(this.cTest2, this.cTest2);
  }

  // test makeScene:
  void testMakeScene(Tester t) {
    this.initData();
    WorldScene ws = new WorldScene(700, 600);
    String s2 = "Score: " + Integer.toString(this.mt.score) + " Points";
    String s3 = "Time: " + Integer.toString(this.mt.time) + " Seconds";
    WorldImage wi2 = new TextImage(s2, 15, Color.BLACK);
    WorldImage wi3 = new TextImage(s3, 15, Color.BLACK);
    ws.placeImageXY(wi2, 280, 10);
    ws.placeImageXY(wi3, 420, 10);
    t.checkExpect(this.mt.makeScene(), ws);
  }

  // test makeScene:
  void testMakeScene2(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards2);
    WorldScene ws = new WorldScene(700, 600);
    String s2 = "Score: " + Integer.toString(this.mt.score) + " Points";
    String s3 = "Time: " + Integer.toString(this.mt.time) + " Seconds";
    WorldImage wi2 = new TextImage(s2, 15, Color.BLACK);
    WorldImage wi3 = new TextImage(s3, 15, Color.BLACK);
    ws.placeImageXY(wi2, 280, 10);
    ws.placeImageXY(wi3, 420, 10);
    ws.placeImageXY(this.card1.renderCard(), this.card1.posn.x, this.card1.posn.y);
    ws.placeImageXY(this.card4.renderCard(), this.card4.posn.x, this.card4.posn.y);
    t.checkExpect(this.cTest2.makeScene(), ws);
  }

  // test makeScene:
  void testMakeScene3(Tester t) {
    this.initData();
    this.cTest3 = new Concentration(this.cards);
    WorldScene ws = new WorldScene(700, 600);
    String s2 = "Score: " + Integer.toString(this.mt.score) + " Points";
    String s3 = "Time: " + Integer.toString(this.mt.time) + " Seconds";
    WorldImage wi2 = new TextImage(s2, 15, Color.BLACK);
    WorldImage wi3 = new TextImage(s3, 15, Color.BLACK);
    ws.placeImageXY(wi2, 280, 10);
    ws.placeImageXY(wi3, 420, 10);
    ws.placeImageXY(this.card1.renderCard(), this.card1.posn.x, this.card1.posn.y);
    ws.placeImageXY(this.card2.renderCard(), this.card2.posn.x, this.card2.posn.y);
    ws.placeImageXY(this.card3.renderCard(), this.card3.posn.x, this.card3.posn.y);
    ws.placeImageXY(this.card4.renderCard(), this.card4.posn.x, this.card4.posn.y);
    ws.placeImageXY(this.card5.renderCard(), this.card5.posn.x, this.card5.posn.y);
    ws.placeImageXY(this.card6.renderCard(), this.card6.posn.x, this.card6.posn.y);
    t.checkExpect(this.cTest3.makeScene(), ws);
  }

  // test shuffleTest and shuffle:
  void testShuffle(Tester t) {
    this.initData();
    this.cTest = new Concentration(this.cards);
    t.checkExpect(this.cards, this.cards);
    t.checkExpect(this.cTest.shuffledCards, this.cardsResult);
    t.checkExpect(this.cTest.shuffledCards, this.cardsResult);
  }

  // test renderCard
  void testRender(Tester t) {
    this.initData();
    t.checkExpect(this.card1.renderCard(),
        new OverlayOffsetImage(new TextImage(Integer.toString(this.card1.val), 12, Color.RED), 8,
            14,
            new OverlayOffsetImage(new TextImage(Integer.toString(this.card1.val), 12, Color.RED),
                -5, -14, new OverlayImage(new TextImage(this.card1.suit, 9, Color.BLACK),
                    new RectangleImage(30, 50, OutlineMode.SOLID, Color.LIGHT_GRAY)))));
    t.checkExpect(this.card2.renderCard(),
        new OverlayOffsetImage(new TextImage(Integer.toString(this.card2.val), 12, Color.RED), 8,
            14,
            new OverlayOffsetImage(new TextImage(Integer.toString(this.card2.val), 12, Color.RED),
                -5, -14, new OverlayImage(new TextImage(this.card2.suit, 9, Color.BLACK),
                    new RectangleImage(30, 50, OutlineMode.SOLID, Color.LIGHT_GRAY)))));
    t.checkExpect(this.card4.renderCard(),
        new RectangleImage(30, 50, OutlineMode.SOLID, Color.BLACK));
    t.checkExpect(this.card6.renderCard(),
        new RectangleImage(30, 50, OutlineMode.SOLID, Color.BLACK));
  }

  // test cardBuilder:
  void testCardBuilder(Tester t) {
    this.initData();
    t.checkExpect(this.card1.cardBuilder(1, 5), new Card(5, "♣", false));
    t.checkExpect(this.card1.cardBuilder(0, 69), new Card(69, "♥", false));
    t.checkExpect(this.card1.cardBuilder(2, 0), new Card(0, "♦", false));
    t.checkExpect(this.card1.cardBuilder(3, 13), new Card(13, "♠", false));
  }

  // test suitColor:
  void testsuitColor(Tester t) {
    this.initData();
    t.checkExpect(this.card1.suitColor(this.card5), false);
    t.checkExpect(this.card1.suitColor(this.card3), true);
    t.checkExpect(this.card3.suitColor(this.card3), true);
    t.checkExpect(this.card2.suitColor(this.card4), true);
  }

  // contains only one test case for this method this everything would create the
  // same list of cards
  void testCreateDeck(Tester t) {
    this.initData();
    t.checkExpect(new CreateDeck().apply(new ArrayList<Card>()), this.cards3);
  }

  void testCheckPosition(Tester t) {
    this.initData();
    t.checkExpect(new CheckPosition(30, 30).test(this.card2), false);
    t.checkExpect(new CheckPosition(15, 15).test(this.card2), false);
    t.checkExpect(new CheckPosition(0, 0).test(this.card7), true);

  }

  void testCheckFlag(Tester t) {
    this.initData();
    t.checkExpect(new CheckFlag().test(this.card4), false);
    t.checkExpect(new CheckFlag().test(this.card2), true);
  }

  void testCheckVal(Tester t) {
    this.initData();
    t.checkExpect(new CheckVal(this.card3).test(this.card1), true);
    t.checkExpect(new CheckVal(this.card4).test(this.card1), false);

  }

  void testChangeFlag1(Tester t) {
    this.initData();
    new ChangeFlag1().apply(this.card1);
    t.checkExpect(this.card1.flag, true);
    new ChangeFlag1().apply(this.card4);
    t.checkExpect(this.card4.flag, true);
  }

  void testChangeFlag2(Tester t) {
    this.initData();
    t.checkExpect(this.card1.flag, true);
    new ChangeFlag2().apply(this.card1);
    t.checkExpect(this.card1.flag, false);
    t.checkExpect(this.card4.flag, false);
    new ChangeFlag2().apply(this.card4);
    t.checkExpect(this.card4.flag, false);
  }

  void testCreate(Tester t) {
    this.initData();
    t.checkExpect(new ArrayUtils().create(new CreateDeck()), this.cards3);
  }

  void testSwap(Tester t) {
    this.initData();
    t.checkExpect(this.cards4.get(0), this.card4);
    t.checkExpect(this.cards4.get(2), this.card6);
    new ArrayUtils().swap(this.cards4, 0, 2);
    t.checkExpect(this.cards4.get(0), this.card6);
    t.checkExpect(this.cards4.get(2), this.card4);
    new ArrayUtils().swap(this.cards4, 0, 2);
    t.checkExpect(this.cards4.get(0), this.card4);
    t.checkExpect(this.cards4.get(2), this.card6);
  }

  void testFlip(Tester t) {
    this.initData();
    t.checkExpect(this.cards4.get(0).flag, false);
    t.checkExpect(this.cards4.get(1).flag, false);
    t.checkExpect(this.cards4.get(2).flag, false);
    new ArrayUtils().flip(this.cards4, new CheckPosition(0, 0), new ChangeFlag1());
    t.checkExpect(this.cards4.get(0).flag, false);
    t.checkExpect(this.cards4.get(1).flag, false);
    t.checkExpect(this.cards4.get(2).flag, false);
  }

  void testFlip2(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards5);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, true);
    new ArrayUtils().flip(this.cards4, new CheckPosition(50, 70), new ChangeFlag1());
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, true);
  }

  void testFlipHelp(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards4);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, false);
    new ArrayUtils().flipHelp(this.cards4, new CheckPosition(100, 70), new ChangeFlag1(), 0);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, false);
    new ArrayUtils().flipHelp(this.cards4, new CheckPosition(50, 70), new ChangeFlag1(), 0);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, false);
    new ArrayUtils().flipHelp(this.cards4, new CheckPosition(20, 70), new ChangeFlag1(), 0);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, false);
  }

  // test case when no card is flipped back
  void testFlipHelp2(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards5);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, true);
    new ArrayUtils().flipHelp(this.cards5, new CheckPosition(100, 70), new ChangeFlag2(), 1);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, true);
    new ArrayUtils().flipHelp(this.cards5, new CheckPosition(50, 70), new ChangeFlag2(), 0);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, true);
  }

  void testFlipHelp3(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards4);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, false);
    new ArrayUtils().flipHelp(this.cards4, new CheckPosition(100, 70), new ChangeFlag1(), 0);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(2).flag, false);
  }

  void testFlip3(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards2);
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, false);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    new ArrayUtils().flip(this.cTest2.shuffledCards, new CheckPosition(50, 70), new ChangeFlag1());
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, true);
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
    new ArrayUtils().flip(this.cTest2.shuffledCards, new CheckPosition(52, 68), new ChangeFlag2());
    t.checkExpect(this.cTest2.shuffledCards.get(0).flag, false); // should be false
    t.checkExpect(this.cTest2.shuffledCards.get(1).flag, true);
  }

  // test numFlag:
  void testNumFlag(Tester t) {
    this.initData();
    this.cTest2 = new Concentration(this.cards2);
    t.checkExpect(this.cTest.numFlag(), 3);
    t.checkExpect(this.mt.numFlag(), 0);
    t.checkExpect(this.cTest2.numFlag(), 1);
  }
}
