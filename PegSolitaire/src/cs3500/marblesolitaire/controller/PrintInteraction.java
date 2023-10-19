package cs3500.marblesolitaire.controller;

/**
 * Print Interaction, represents an interaction used for testing purposes.
 * This is specific to outputs.
 */
public class PrintInteraction implements Interaction {
  private final String[] lines;

  /**
   * Constructs an InputInteraction with the given string representing the input.
   * @param lines String[] or String representing the input.
   */
  public PrintInteraction(String... lines) {
    this.lines = lines;
  }

  @Override
  public void apply(StringBuilder in, StringBuilder out) {
    for (String s: this.lines) {
      out.append(s).append('\n');
    }
  }
}
