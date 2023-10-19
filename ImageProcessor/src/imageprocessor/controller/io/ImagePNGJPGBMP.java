package imageprocessor.controller.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import imageprocessor.Pixel;

/**
 * ImagePNGJPGBMP is a ImageIOHelper, and is used to save/read image files (bmp, png, jpeg, jpg).
 * This class will use java's ImageIO class to assist with the process of reading the image
 * contents, and saving the image contents. One important note to keep in mind is the fact that
 * jpg/jpeg compresses image data and can lose the original properties of the image (color).
 * Thus, it is expected that the 2D array of pixels reading from a png will be different than that
 * of a jpg, even if the images look exactly the same to your eyes.
 */
public class ImagePNGJPGBMP implements ImageIOHelper {

  // This method uses ImageIO's read function to read the file with the given filePath.
  // A BufferedImage is returned, which is used to get the height/width of the image and a pixel
  // at the given x, y coordinates. Using these methods, we will be able to construct the
  // 2D list of pixels according to the values given by BufferedImage. This method will assume
  // that BufferedImage can do the job right!
  @Override
  public List<List<Pixel>> readFile(String filePath) throws IllegalArgumentException {
    File file = new File(filePath);
    try {
      BufferedImage buff = ImageIO.read(file);
      if (buff == null) {
        throw new IllegalArgumentException("invalid file!");
      }
      List<List<Pixel>> result = new ArrayList();
      for (int i = 0; i < buff.getHeight(); i++) {
        List<Pixel> inner = new ArrayList();
        for (int j = 0; j < buff.getWidth(); j++) {
          int color = buff.getRGB(j, i);
          int blue = color & 0xff;
          int green = (color & 0xff00) >> 8;
          int red = (color & 0xff0000) >> 16;
          int alpha = (color & 0xff000000) >>> 24;
          inner.add(new Pixel(red, green, blue, alpha));
        }
        result.add(inner);
      }
      return result;
    } catch (IOException e) {
      throw new IllegalArgumentException("File path was invalid!");
    }
  }

  // This method will use ImageIO's write function to write the array of pixels to the file.
  // However, before we can do so, we need to parse the 2d array of Pixels, and add each pixel
  // to a BufferedImage using the set method. But we must convert the Pixel to a 4 Byte int
  // representation.
  @Override
  public WritableRenderedImage saveFile(String filePath, List<List<Pixel>> pixelsOfImage)
          throws IllegalArgumentException {
    BufferedImage image = this.determineImage(filePath,
            pixelsOfImage.get(0).size(), pixelsOfImage.size());
    for (int i = 0; i < pixelsOfImage.size(); i++) {
      for (int j = 0; j < pixelsOfImage.get(i).size(); j++) {
        Pixel p = pixelsOfImage.get(i).get(j);
        image.setRGB(j, i, this.getColorBit(p));
      }
    }
    return image;
  }

  /**
   * Determines which BufferedImaged to construct based on the given filePath.
   * The difference b/w the 2 buffered images are the type of pixel to be used, where one includes
   * the alpha channel, and the other excludes it.
   *
   * @param filePath String represents the path of the image to be read/saved.
   * @param width    int represents the width of the image to be read/saved.
   * @param height   int represents the height of the image to be read/saved.
   * @return the corresponding BufferedImage based on the image format:
   *          "png" -> include alpha values
   *          anything other than png -> exclude alpha values
   */
  private BufferedImage determineImage(String filePath, int width, int height) {
    String format = figureFormatOut(filePath);
    if (format.equals("png")) {
      return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  /**
   * Helper method returns the int 4 byte representation of the given Pixel.
   *
   * @param p Pixel represents the given pixel to be converted into an int.
   * @return an int representing the pixel.
   */
  private int getColorBit(Pixel p) {
    Color color = new Color(p.getComponent(0), p.getComponent(1),
            p.getComponent(2), p.getComponent(3));
    return color.getRGB();
  }

  // This method will try to determine the informal format of the image, in order to
  // give to the ImageIO's write static function.
  private String figureFormatOut(String filePath) {
    if (filePath.endsWith("png")) {
      return "png";
    } else if (filePath.endsWith("jpeg") || filePath.endsWith("jpg")) {
      return "jpeg";
    } else {
      return "bmp";
    }
  }
}
