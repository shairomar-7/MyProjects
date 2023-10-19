package imageprocessor;

import java.util.Objects;

/**
 * Represents a Pixel, that contains red, green, and blue components.
 * These components will be private final, to avoid mutation,
 * and to hide information from the client.
 * Invariants:
 * r/g/b/a components will remain positive integers (because the constructor checks for that
 * condition, and the methods of this class preserve this invariant).
 * r/g/b/a components will
 */
public class Pixel {
  private final int r;
  private final int g;
  private final int b;
  private final int a;

  /**
   * Constructs a Pixel with the given r, g, and b values, with a transparency of 255.
   *
   * @param r int represents the red component of a pixel
   * @param g int represents the green component of a pixel
   * @param b int represents the blue component of a pixel
   * @throws IllegalArgumentException if given an r, g, or b less than 0.
   */
  public Pixel(int r, int g, int b) throws IllegalArgumentException {
    if (r < 0 || g < 0 || b < 0) {
      throw new IllegalArgumentException("invalid r, g, b values, must be positive!");
    }
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = 255;
  }

  /**
   * Constructs a Pixel with the given r, g, b, and a values.
   *
   * @param r int represents the red component of a pixel
   * @param g int represents the green component of a pixel
   * @param b int represents the blue component of a pixel
   * @param a int represents the alpha component of a pixel.
   * @throws IllegalArgumentException if given an r, g, or b or a less than 0.
   */
  public Pixel(int r, int g, int b, int a) throws IllegalArgumentException {
    if (r < 0 || g < 0 || b < 0 || a < 0) {
      throw new IllegalArgumentException("invalid r, g, b, a values, must be positive!");
    }
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }

  /**
   * Returns a new Pixel that is the red/green/blue-component version of this Pixel.
   * Valid components are the following: "r", "g", "b"
   *
   * @param component String represents the component to be visualized.
   * @return a new Pixel with new r g b values based on the given component.
   * @throws IllegalArgumentException if given an invalid component.
   */
  private Pixel setToColor(String component) throws IllegalArgumentException {
    switch (component) {
      case "r":
        return new Pixel(r, r, r, a);
      case "g":
        return new Pixel(g, g, g, a);
      case "b":
        return new Pixel(b, b, b, a);
      default:
        throw new IllegalArgumentException("Undefined componenet given!");
    }
  }

  /**
   * Executes the -component command of this Image Processor.
   * This method will return a new Pixel with new rgb values based on which component will be
   * visualized.
   * Valid components/command: "luma", "intensity", "value", "r", "g", "b"
   *
   * @param command String represents the given command/component to be visualized.
   * @return a new Pixel with new rgb values based on which command/component was given.
   * @throws IllegalArgumentException if the given command/component is unidentified.
   */
  public Pixel executeCommand(String command) throws IllegalArgumentException {
    switch (command) {
      case "luma":
        return this.setToLuma();
      case "intensity":
        return this.setToAvg();
      case "value":
        return this.setToMax();
      case "sepia":
        return this.setToSepia();
      default:
        return this.setToColor(command);
    }
  }

  /**
   * Returns a new Pixel with its rgb values set to this pixel's max component value.
   *
   * @return a new Pixel with new rgb values
   */
  private Pixel setToMax() {
    int max = Math.max(r, Math.max(g, b));
    return new Pixel(max, max, max, a);
  }

  /**
   * If a pixel value component is greater than 255,
   * this method will clamp it to be 255 exactly.
   * @return a new pixel with adjusted value components
   */
  public Pixel clamp() {
    int red = this.handleRange(r, 255);
    int green = this.handleRange(g, 255);
    int blue = this.handleRange(b, 255);
    return new Pixel(red, green, blue, a);
  }

  /**
   * Returns a new Pixel with its rgb values set to this pixel's avg component values.
   *
   * @return a new Pixel with new rgb values
   */
  private Pixel setToAvg() {
    int avg = (r + g + b) / 3;
    return new Pixel(avg, avg, avg, a);
  }

  /**
   * Returns a new Pixel with its rgb values set to this pixel's luma value.
   *
   * @return a new Pixel with new rgb values
   */
  private Pixel setToLuma() {
    int luma = this.handleRange((int) (0.2126 * (double) r +
            0.7152 * (double) g + 0.0722 * (double) b), 255);
    return new Pixel(luma, luma, luma, a).clamp();
  }

  /**
   * Returns a new Pixel with its rgb values transformed to sepia values.
   * @return a new Pixel with new rgb values
   */
  private Pixel setToSepia() {
    int sepiaR = this.handleRange((int) (0.393 * (double) r + 0.769 *
            (double) g + 0.189 * (double) b), 255);
    int sepiaG = this.handleRange((int) (0.349 * (double) r + 0.686 *
            (double) g + 0.168 * (double) b), 255);
    int sepiaB = this.handleRange((int) (0.272 * (double) r + 0.534 *
            (double) g + 0.131 * (double) b), 255);
    return new Pixel(sepiaR, sepiaG, sepiaB, a).clamp();
  }


  /**
   * Returns a new Pixel with its values equal to this pixel's value + increment.
   * If the result of adding the increment is greater than the given max depth,
   * then set to max depth, and if the result is less than 0, set it to zero.
   *
   * @param increment int represents how much the client would like to increment the pixel's
   *                  components by.
   * @param maxDepth  int represents the maximum value of each component of this Pixel.
   * @return a new Pixel with its values set to this pixel + increment.
   */
  public Pixel incrementPixel(int increment, int maxDepth) {
    int red = this.handleRange(r + increment, maxDepth);
    int green = this.handleRange(g + increment, maxDepth);
    int blue = this.handleRange(b + increment, maxDepth);
    return new Pixel(red, green, blue, a);
  }

  /**
   * If component is less than 0, then it returns 0, if greater than maxDepth, return maxDepth.
   * Helper method for the incrementPixel method.
   *
   * @param componentValue int represents some pixels r g or b value.
   * @param maxDepth       int represents the maximum value of some pixel's value.
   * @return an int representing the handled componentValue based on the given range
   *          (0 <= componentValue <= maxDepth)
   */
  private int handleRange(int componentValue, int maxDepth) {
    if (componentValue < 0) {
      return 0;
    } else if (componentValue > maxDepth) {
      return maxDepth;
    }
    return componentValue;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pixel)) {
      return false;
    }
    if (o == this) {
      return true;
    }
    return ((Pixel) o).b == b && ((Pixel) o).r == r && ((Pixel) o).g == g && ((Pixel) o).a == a;
  }

  @Override
  public int hashCode() {
    return Objects.hash(r, g, b, a);
  }

  /**
   * Returns an int representing this pixel's r g or b component.
   * If given 0, then r, 1, then g, 2 then b.
   *
   * @param component int represents which component of this pixel is to be returned
   * @return an int representing a certain component of this pixel.
   */
  public int getComponent(int component) {
    switch (component) {
      case 0:
        return this.r;
      case 1:
        return this.g;
      case 2:
        return this.b;
      case 3:
        return this.a;
      default:
        throw new IllegalArgumentException("invalid component given");
    }
  }
}
