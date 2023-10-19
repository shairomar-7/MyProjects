package imageprocessor.controller.io;

import java.awt.image.WritableRenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import imageprocessor.Pixel;

/**
 * ImagePPM, class is a ImageIOHelper, and is used to help the controller with IO for ppm images.
 * This class implements the read and save methods.
 */
public class ImagePPM implements ImageIOHelper {

  // Gets the content of the file as an Appendable, by iterating over each line and appending
  // it to the result. We ignore lines starting with # because that would be a comment.
  private Appendable getFileContentsAsString(String filePath) throws IllegalArgumentException {
    Scanner sc;
    try {
      sc = new Scanner(new FileInputStream(filePath));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("file could not be found!");
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }
    return builder;
  }

  /**
   * Attempts to read the image file in the given filePath and sends the processed data to the
   * Model. By iterating over the list with values i and j set to the height and width, we can
   * ensure that the given 2d array of pixels's dimensions, is equal to the dimensions of the image.
   * If it so happens that the scanner still has strings to read, then we throw an exception.
   * If it so happens that one of the pixels has an r/g/b value is greater than the provided
   * max depth, which is indeed a required specification, then we also throw an exception.
   * If it so happens that the scanner no longer has any string to read while we are still
   * constructing the 2d array of pixels, based on the given specification,
   * then we throw an exception.
   * If it so happens that an image format other than ppm is given, we also throw an exception,
   * only because our current image processor operates on ppm file. This is open for extension.
   * @param filePath String represents the path of the image file to be read.
   * @throws IllegalArgumentException if the given filePath is invalid, or the image is corrupt,
   *         or the file type is unsupported (for now).
   */
  @Override
  public List<List<Pixel>> readFile(String filePath) throws IllegalArgumentException {
    Appendable builder = this.getFileContentsAsString(filePath);
    Scanner sc = new Scanner(builder.toString());
    try {
      String token = sc.next();
      if (!token.equals("P3")) {
        throw new IllegalArgumentException("Invalid ppm file, should start with P3!");
      }
      int width = sc.nextInt();
      int height = sc.nextInt();
      int maxDepth = sc.nextInt();
      List<List<Pixel>> result = new ArrayList<>();
      for (int i = 0; i < height; i++) {
        List<Pixel> inner = new ArrayList<>();
        for (int j = 0; j < width; j++) {
          int r = this.isInvalidDepth(sc.nextInt(), 255);
          int g = this.isInvalidDepth(sc.nextInt(), 255);
          int b = this.isInvalidDepth(sc.nextInt(), 255);
          Pixel pixel = new Pixel(r, g, b);
          inner.add(pixel);
        }
        result.add(inner);
      }
      if (sc.hasNext()) {
        throw new IllegalArgumentException("Invalid ppm file given, more elements than needed!");
      }
      return result;
    } catch (NoSuchElementException n) {
      throw new IllegalArgumentException("Invalid ppm file given, no more elements to read!");
    }
  }

  /**
   * Checks if the given component of a pixel is valid.
   * To be valid, it must be less than the given depth (max value of each component of the image),
   * and it must be greater or equal to zero. If not, we throw an exception.
   * @param component int represents the value of r/g/b of a pixel of some image.
   * @param depth int represents the maximum value of r/g/b of all pixel of an image.
   * @return the component if it is valid
   * @throws IllegalArgumentException if the given component is greater than the max depth, or
   *        the given component is less than 0.
   */
  private int isInvalidDepth(int component, int depth) throws IllegalArgumentException {
    if (component > depth || component < 0) {
      throw new IllegalArgumentException("Given ppm file is invalid.");
    }
    return component;
  }

  @Override
  public WritableRenderedImage saveFile(String filePath, List<List<Pixel>> pixelsOfImage)
          throws IllegalArgumentException {
    OutputStream os;
    File file = new File(filePath);
    try {
      List<List<Pixel>> pixels = pixelsOfImage;
      try {
        os = new FileOutputStream(file);
        OutputStreamWriter output = new OutputStreamWriter(os);
        BufferedWriter writer = new BufferedWriter(output);
        writer.write("P3");
        writer.newLine();
        writer.write(pixels.get(0).size() + " ");
        writer.write(pixels.size() + "");
        writer.newLine();
        writer.write("255");
        writer.newLine();
        for (int i = 0; i < pixels.size(); i++) { // rows
          for (int j = 0; j < pixels.get(i).size(); j++) { //cols
            for (int k = 0; k < 3; k++) { // rgb
              writer.write(pixels.get(i).get(j).getComponent(k) + "");
              if (k != 3) {
                writer.write(" ");
              }
              if (j != pixels.get(0).size() - 1) {
                writer.write(" ");
              }
            }
          }
          writer.newLine();
        }
        os.flush();
        writer.close();
        os.close();
        return ImageIO.read(new File(filePath));
      } catch (FileNotFoundException f) {
        throw new IllegalArgumentException("Given file not found, make sure path is valid!");
      }
    } catch (IOException s) {
      throw new IllegalArgumentException("Something went wrong! IO issue.");
    }
  }
}
