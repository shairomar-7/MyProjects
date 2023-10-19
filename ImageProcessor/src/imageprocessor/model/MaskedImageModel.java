package imageprocessor.model;

import java.util.ArrayList;
import java.util.List;

import imageprocessor.Pixel;

/**
 * Extension of EnhancedModelImpl. This class performs all of the methods
 * that are supported by EnhancedModelImpl
 * (excluding flipping and downscaling), except it only partially manipulates
 * an image. It will perform an operation on a pixel only if that pixel's intensity component is
 * less than 127.
 */
public class MaskedImageModel extends EnhancedModelImpl implements EnhancedModel {
  private double[][] blur = new double[][]{{0.0625, 0.125, 0.0625}, {0.125, 0.25, 0.125},
    {0.0625, 0.125, 0.0625}};
  private double[][] sharpen = new double[][]{{-0.125, -0.125, -0.125, -0.125, -0.125},
    {-0.125, 0.25, 0.25, 0.25, 0.25, -0.125}, {-0.125, 0.25, 0.25, 1, 0.25, -0.125},
    {-0.125, 0.25, 0.25, 0.25, 0.25, -0.125}, {-0.125, -0.125, -0.125, -0.125, -0.125}};
  private double[][] sepia = new double[][]{{0.393, 0.769, 0.189},
    {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}};
  private double[][] luma = new double[][]{{0.2126, 0.7152, 0.0722},
    {0.2126, 0.7152, 0.0722}, {0.2126, 0.7152, 0.0722}};

  /**
   * Calls super to initialize this using the EnhancedModelImpl constructor. Sets max depth
   * for Pixel to 255, and initializes hashmap to store image manipulations.
   */
  public MaskedImageModel() {
    super();
  }

  /**
   * Takes in a 2D list of pixels, and the height and width for an image, and
   * calls super() to use the EnhancedModel Impl to initialize each field.
   *
   * @param pixels the pixels of an image
   * @param width  the height of an image
   * @param height the width of an image
   * @throws IllegalArgumentException if the dimensions of the pixel
   *         differ from the height * width, or if height/width is negative
   */
  public MaskedImageModel(List<List<Pixel>> pixels, int width, int height)
          throws IllegalArgumentException {
    super(pixels, width, height);
  }

  // Used to create a masked version of an image.
  private void mask(String imageName, String destName) {
    List<List<Pixel>> pixels = this.getPixelsFromHistory(imageName);
    List<List<Pixel>> bAndW = new ArrayList<>();
    this.createBlackAndWhite(pixels, bAndW);
    this.addToHistory(bAndW, destName);
  }

  // creates a black and white version of an image (the masked version).
  private void createBlackAndWhite(List<List<Pixel>> pixels, List<List<Pixel>> bAndW) {
    for (int i = 0; i < pixels.size(); i++) {
      List<Pixel> inner = new ArrayList<Pixel>();
      for (int j = 0; j < pixels.get(i).size(); j++) {
        Pixel p = pixels.get(i).get(j);
        if (p.executeCommand("intensity").getComponent(0) > 127) {
          inner.add(new Pixel(255, 255, 255));
        } else {
          inner.add(new Pixel(0, 0, 0));
        }
      }
      bAndW.add(inner);
    }
  }

  /**
   * visualizes a pixel in an image according to the passed in component, only if
   * the intensity component of the image is less than 127.
   *
   * @param component String represents the component of the image to be visualized.
   * @param imageName String represents the image that the user created/loaded and would like
   *                  to manipulate.
   * @param destName  String represents the desired name of the
   *                 created image to be added to history.
   * @throws IllegalArgumentException if given an imageName that is not in this model's history
   *                                  of images, or if the given component is invalid.
   */
  public void visualizeComponent(String component, String imageName, String destName)
          throws IllegalArgumentException {
    List<List<Pixel>> result = new ArrayList<>();
    List<List<Pixel>> pixelsOfFile = this.getPixelsFromHistory(imageName);

    this.mask(imageName, imageName + "mask");
    List<List<Pixel>> mask = this.getPixelsFromHistory(imageName + "mask");

    for (int i = 0; i < pixelsOfFile.size(); i++) {
      List<Pixel> inner = new ArrayList<>();
      for (int j = 0; j < pixelsOfFile.get(i).size(); j++) {
        if (mask.get(i).get(j).getComponent(0) == 0) {
          inner.add(pixelsOfFile.get(i).get(j).executeCommand(component));
        } else {
          inner.add(pixelsOfFile.get(i).get(j));
        }
      }
      result.add(inner);
    }
    this.addToHistory(result, destName);
  }

  /**
   * Brightens/darkens a pixel in an image only if the intensity component of that
   * pixel is less than 127.
   *
   * @param increment int represents the increment of the image to be brightened/darkened.
   * @param imageName String represents the image that the user created/loaded and would like
   *                  to manipulate.
   * @param destName  String represents the desired name of the created
   *                 image to be added to history.
   * @throws IllegalArgumentException if given an imageName that is not in this model's history
   *                                  of images, or if the given component is invalid.
   */
  public void brightenOrDarkenBy(int increment, String imageName, String destName)
          throws IllegalArgumentException {
    List<List<Pixel>> result = new ArrayList<>();
    List<List<Pixel>> pixelsOfFile = this.getPixelsFromHistory(imageName);

    this.mask(imageName, imageName + "mask");
    List<List<Pixel>> mask = this.getPixelsFromHistory(imageName + "mask");

    for (int i = 0; i < pixelsOfFile.size(); i++) {
      List<Pixel> inner = new ArrayList<>();
      for (int j = 0; j < pixelsOfFile.get(0).size(); j++) {
        if (mask.get(i).get(j).getComponent(0) == 0) {
          inner.add(pixelsOfFile.get(i).get(j).incrementPixel(increment, this.getDepth()));
        } else {
          inner.add(pixelsOfFile.get(i).get(j));
        }
      }
      result.add(inner);
    }
    this.addToHistory(result, destName);
  }

  /**
   * Filters an image. See interface for more information.
   * @param filterMethod the type of filter (blur, sharpen) that is to be performed on a
   *                     certain image
   * @param imageName    the name of the image that is to be filtered
   * @param destName     the name that the new image will be stored under
   * @throws IllegalArgumentException if the filter method is invalid or if
   *                                  imageName is not in history
   */
  public void filter(String filterMethod, String imageName, String destName) {
    if (filterMethod.equals("blur")) {
      this.filterHelper(imageName, destName, this.blur);
    } else {
      this.filterHelper(imageName, destName, this.sharpen);
    }
  }

  /*
   * For the filter method. Applies the appropriate kernel (arr) to a pixel, and computes
   * the new value.
   */
  private void filterHelper(String imageName, String destName, double[][] arr) {
    List<List<Pixel>> pixels = this.getPixelsFromHistory(imageName);
    List<List<Pixel>> result = new ArrayList<>();
    this.mask(imageName, imageName + "mask");
    List<List<Pixel>> mask = this.getPixelsFromHistory(imageName + "mask");
    for (int i = 0; i < pixels.size(); i++) {
      List<Pixel> inner = new ArrayList<>();
      for (int j = 0; j < pixels.get(i).size(); j++) {
        if (mask.get(i).get(j).getComponent(0) == 0) {
          inner.add(this.computeSum(pixels, i, j, arr));
        } else {
          inner.add(pixels.get(i).get(j));
        }
      }
      result.add(inner);
    }
    this.addToHistory(result, destName);
  }

  // checks the value of the sum for r/g/b
  private Pixel computeSum(List<List<Pixel>> pixels, int i, int j, double[][] arr) {
    int[] indexOffsetI = this.checkIndex(i - arr.length / 2, 0);
    int[] indexOffsetJ = this.checkIndex(j - arr.length / 2, 0);
    double sumR = 0;
    double sumG = 0;
    double sumB = 0;
    for (int k = indexOffsetI[1]; k < arr.length; k++) {
      int threshold1 = indexOffsetI[0] + k - indexOffsetI[1];
      if (threshold1 >= pixels.size()) {
        break;
      }
      for (int s = indexOffsetJ[1]; s < arr[k].length; s++) {
        int threshold2 = indexOffsetJ[0] + s - indexOffsetJ[1];
        if (threshold2 >= pixels.get(indexOffsetI[0]).size()) {
          break;
        }
        Pixel p = pixels.get(threshold1).get(threshold2);
        sumR += arr[k][s] * p.getComponent(0);
        sumG += arr[k][s] * p.getComponent(1);
        sumB += arr[k][s] * p.getComponent(2);
      }
    }
    return new Pixel((int) this.checkValue(sumR),
            (int) this.checkValue(sumG), (int) this.checkValue(sumB)).clamp();
  }

  // checks the value of the sum for r/g/b
  private double checkValue(double sum) {
    if (sum < 0) {
      return 0;
    }
    return sum;
  }

  // Returns the correct index and offset. If index is below zero, we add one to it and to the
  // offset until it is equal to zero. We then return an array of size 2, where first index
  // is the index, and the 2nd is the offset.
  private int[] checkIndex(int index, int offSet) {
    while (index < 0) {
      index++;
      offSet++;
    }
    return new int[]{index, offSet};
  }

  /**
   * Performs a color transformation on a pixel, according to the given
   * transform method, only if that pixel's intensity component is less
   * than 127.
   *
   * @param transformMethod the type of transformation that is to be done
   *                        on the image, this program offers the ability
   *                        to do a sepia or luma color transformation
   * @param imageName       the name of the image that is to be transformed
   * @param destName        the name that the new image will be stored under
   * @throws IllegalArgumentException if the given transform method is invalid,
   *                                  or if imageName is not in history
   */
  public void colorTransform(String transformMethod, String imageName, String destName) {
    if (transformMethod.equals("luma")) {
      this.transformHelper(imageName, destName, this.luma);
    } else {
      this.transformHelper(imageName, destName, this.sepia);
    }
  }

  //  Help for the transform method. Iterates through a list of pixels and
  //  applies the correct color transformation (arr) to each pixel.
  private void transformHelper(String imageName, String destName, double[][] arr) {
    List<List<Pixel>> result = new ArrayList();
    List<List<Pixel>> pixels = this.getPixelsFromHistory(imageName);
    this.mask(imageName, imageName + "mask");
    List<List<Pixel>> mask = this.getPixelsFromHistory(imageName + "mask");
    for (int i = 0; i < pixels.size(); i++) {
      List<Pixel> inner = new ArrayList();
      for (int j = 0; j < pixels.get(i).size(); j++) {
        if (mask.get(i).get(j).getComponent(0) == 0) {
          inner.add(this.transformPixelHelper(arr, pixels.get(i).get(j)));
        } else {
          inner.add(pixels.get(i).get(j));
        }
      }
      result.add(inner);
    }
    this.addToHistory(result, destName);
  }

  // Applies a color transformation to a pixel and returns the new value.
  private Pixel transformPixelHelper(double[][] arr, Pixel p) {
    int[] values = new int[3];
    for (int i = 0; i < 3; i++) {
      int newComponent = (int) ((arr[i][0] * (double) p.getComponent(0))
              + (arr[i][1] * (double) p.getComponent(1)) +
              (arr[i][2] * (double) p.getComponent(2)));
      values[i] = newComponent;
    }
    int r = values[0];
    int g = values[1];
    int b = values[2];
    return new Pixel(r, g, b).clamp();
  }

}
