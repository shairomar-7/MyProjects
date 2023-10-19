package imageprocessor.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Collections;
import java.util.Map;

import javax.swing.JPanel;

/**
 * HistogramPanel class is A Jpanel and is basically used to create a single histogram.
 * This histogram displays the pixels ranging from 0-255 and draws rectangles with a height
 * based on the count of each value.
 */
public class HistogramPanel extends JPanel {
  private Map<Integer, Integer> map;

  /**
   * Constructs a HistogramPanel with the given map of integer as key, and value. This map should
   * represents the rgb values ranging from 0-255 as the key, and the count as the value.
   * We are also setting the size to be 300 by 300, and setting the visibility on.
   * @param map represents the rgb values ranging from 0-255 as the key, and the count as the value.
   * @throws IllegalArgumentException if given a null map!
   */
  public HistogramPanel(Map<Integer,Integer> map) {
    super();
    if (map == null) {
      throw new IllegalArgumentException("Given map is null idiot@");
    }
    this.map = map;
    this.setPreferredSize(new Dimension(300, 300));
    this.setVisible(true);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.drawHistogram(map, g);
  }

  // draws the histogram with the given map, which would be this class's map. This function
  // also takes in Graphics in order to draw rectangles with a height based on the count in of each
  // value from 0-256 (exclusive)
  private void drawHistogram(Map<Integer,Integer> map, Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(5, 295 - 256, 2, 256);
    g.fillRect(5, 293, 256, 2);
    if (map.size() != 0) {
      int max = Collections.max(map.values(), null);
      double percentDecrease = 0;
      if (max > 256) {
        percentDecrease = (max - 256) / (double) max;
      }
      g.setColor(Color.RED);
      for (int i = 0; i < 256; i++) {
        Integer count = map.get(i);
        if (count == null || count == 0) {
          g.fillRect(i + 7, 0, 1, 0);
        }
        else {
          count =  count - (int) (count * percentDecrease);
          g.fillRect(i + 7, 293 - count, 1, count);
        }
      }
      this.revalidate();
      this.repaint();
    }
  }

  /**
   * sets this histogram's map to the given map, throws an exception if given a null map.
   * @param map the map containig the values for each histogram.
   * @throws IllegalArgumentException if the map is null
   */
  public void setMap(Map<Integer, Integer> map) throws IllegalArgumentException {
    if (map == null) {
      throw new IllegalArgumentException("Given map was null, idiot@");
    }
    this.map = map;
    repaint();
  }
}
