package imageprocessor.controller.io;

import java.awt.image.WritableRenderedImage;
import java.util.List;

import imageprocessor.Pixel;

/**
 * ImageIOHelper, interface represents operations that deal with I/O of an image.
 * This interface will help the controller to read a file from the given file path, and will
 * help the controller to save a file, from the given file path and 2d list of pixels.
 * The reason why I chose to create this interface is to support other formats in the future
 * that will lead to minimal change in the controller (add the new formats in the knownFormats
 * map, and create a class that implements this interface and implement the methods).
 */
public interface ImageIOHelper {

  /**
   * Reads an image file from the given file path, and returns the pixels of the image in a 2d list.
   * @param filePath String represents the file path of the image file to be read.
   * @return a 2D List of pixels which represents the pixels of the image file.
   * @throws IllegalArgumentException if the given filePath is invalid, or the image file itself is
   *                                 corrupt or this function faced some IO issue.
   */
  List<List<Pixel>> readFile(String filePath) throws IllegalArgumentException;

  /**
   * Saves an image file from the given file path, and 2D List of pixels.
   * @param filePath String represents the file path of the image to be saved.
   * @param pixelsOfImage 2D list of pixels represents the pixels of the image to be saved.
   * @throws IllegalArgumentException if given an invalid filePath, or an I/O issue occurred.
   */
  WritableRenderedImage saveFile(String filePath, List<List<Pixel>> pixelsOfImage)
          throws IllegalArgumentException;
}
