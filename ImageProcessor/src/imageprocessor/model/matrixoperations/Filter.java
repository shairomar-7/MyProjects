package imageprocessor.model.matrixoperations;

import java.util.ArrayList;
import java.util.List;
import imageprocessor.Pixel;
import imageprocessor.model.EnhancedModel;

/**
 * The abstract class for the filter operations (Blur and Sharpen). The logic
 * for which each filter is applied is exactly the same, the only difference is
 * the image kernel that each filter uses. Because of this, all the methods used
 * for applying a filter, aside from initializing the kernel, can be abstracted into
 * one class.
 */
public abstract class Filter implements ImageMatrixOperations {
  protected final String imageName;
  protected final String destName;
  protected List<List<Double>> matrix;

  /**
   * Represents a filter object. It takes in a name of an image that is to be modified,
   * as well as the name that the new image will be stored under. This constructor also
   * calls initMatrix(), which will create the kernel that is used for the filter.
   *
   * @param imageName the name of the image that is to be filtered
   * @param destName  the name that the new image will be stored under
   */
  protected Filter(String imageName, String destName) {
    this.imageName = imageName;
    this.destName = destName;
    this.initMatrix();
  }

  // Initializes the matrix.
  protected abstract void initMatrix();

  @Override
  public void execute(EnhancedModel m) throws IllegalArgumentException {
    List<List<Pixel>> pixels = m.getPixelsFromHistory(imageName);
    List<List<Pixel>> result = new ArrayList<>();
    for (int i = 0; i < pixels.size(); i++) {
      List<Pixel> inner = new ArrayList<>();
      for (int j = 0; j < pixels.get(i).size(); j++) {
        inner.add(this.computeSum(pixels, i, j));
      }
      result.add(inner);
    }
    m.addToHistory(result, destName);
  }

  // Computes the matrix operation sum for a certain pixel at i, j, and this pixel
  // aligns with the center of the Matrix.
  private Pixel computeSum(List<List<Pixel>> pixels, int i, int j) { // avg or sum ??
    int[] indexOffsetI = this.checkIndex(i - matrix.size() / 2, 0);
    int[] indexOffsetJ = this.checkIndex(j - matrix.size() / 2, 0);
    double sumR = 0;
    double sumG = 0;
    double sumB = 0;
    for (int k = indexOffsetI[1]; k < this.matrix.size(); k++) {
      int threshold1 = indexOffsetI[0] + k - indexOffsetI[1];
      if (threshold1 >= pixels.size()) {
        break;
      }
      for (int s = indexOffsetJ[1]; s < this.matrix.get(k).size(); s++) {
        int threshold2 = indexOffsetJ[0] + s - indexOffsetJ[1];
        if (threshold2 >= pixels.get(indexOffsetI[0]).size()) {
          break;
        }
        Pixel p = pixels.get(threshold1).get(threshold2);
        sumR += this.matrix.get(k).get(s) * p.getComponent(0);
        sumG += this.matrix.get(k).get(s) * p.getComponent(1);
        sumB += this.matrix.get(k).get(s) * p.getComponent(2);
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
}
