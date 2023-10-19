package imageprocessor.model.matrixoperations;

import java.util.ArrayList;
import java.util.List;

import imageprocessor.Pixel;
import imageprocessor.model.EnhancedModel;

/**
 * Downsize, class used to down size an image in the model. It may seem weird that this class is
 * in the ImageMatrixOperations, but that is simple because the name of the interface is a bit
 * misleading, it could truly perform any operation in the model with the execute method.
 */
public class Downsize implements ImageMatrixOperations {
  private final String imageName;
  private final String destName;
  private final int height;
  private final int width;

  /**
   * Constructs a Downsize with the given image name, the destination name, the width and height.
   * This constructor is kind, and will accept all inputs and will not yell by throwing an
   * exception, however, the execute function is
   *
   * @param imageName String represents the name of the image to be downsized.
   * @param destName String represents the desired name of the image to be downsized
   * @param height int represents the new height of the image
   * @param width int represents the new width of the image
   */
  public Downsize(String imageName, String destName, int height, int width) {
    this.imageName = imageName;
    this.destName = destName;
    this.height = height;
    this.width = width;
  }

  @Override
  public void execute(EnhancedModel m) {
    List<List<Pixel>> pixels = this.downscale(m.getPixelsFromHistory(imageName));
    m.addToHistory(pixels, destName);
  }

  //
  private List<List<Pixel>> downscale(List<List<Pixel>> pixels) {
    List<List<Pixel>> result = new ArrayList<>();
    double multX = (double) pixels.get(0).size() / (double) width;
    double multY = (double) pixels.size() / (double) height;
    for (int i = 0; i < height; i++) {
      double y = i * multY;
      ArrayList<Pixel> inner = new ArrayList<>();
      if (y < pixels.size()) {
        for (int j = 0; j < width; j++) {
          double x = j * multX;
          if (x < pixels.get(0).size()) {
            // inner.add(pixels.get((int) y).get((int) x));
            if (x - (int) x != 0.0 || y - (int) y != 0.0) {
              Pixel p = this.downscaleHelper(pixels, y, x);
              inner.add(p);
            } else {
              inner.add(pixels.get((int) y).get((int) x));
            }
          }
        }
      }
      result.add(inner);
    }
    return result;
  }

  //
  private Pixel downscaleHelper(List<List<Pixel>> pixels, double x, double y) {
    int newX = (int) x;
    int newY = (int) y;
    List<Integer> components = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      double m = downscaleHelperHelper(pixels, newX + 1, newY, i) * (x - newX)
              + downscaleHelperHelper(pixels, newX, newY, i) * (newX + 1 - x);
      double n = downscaleHelperHelper(pixels, newX + 1, newY + 1, i) * (x - newX)
              + downscaleHelperHelper(pixels, newX, newY + 1, i) * ((newX + 1) - x);
      int cp = (int) Math.round(n * (y - newY) + m * ((newY + 1) - y));
      components.add(cp);
    }
    return new Pixel(components.get(0), components.get(1), components.get(2)).clamp();
  }

  //
  private int downscaleHelperHelper(List<List<Pixel>> pixels, int x, int y, int component) {
    try {
      int result = pixels.get(x).get(y).getComponent(component);
      return result;
    } catch (IndexOutOfBoundsException e) {
      return 0;
    }
  }
}
