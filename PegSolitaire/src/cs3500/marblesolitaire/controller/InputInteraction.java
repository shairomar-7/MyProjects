package cs3500.marblesolitaire.controller;

/**
 * Input Interaction, represents an interaction used for testing purposes.
 * This is specific to inputs.
 */
public class InputInteraction implements Interaction {
  private final String input;

  /**
   * Constructs an InputInteraction with the given string representing the input.
   * @param input String representing the input.
   */
  public InputInteraction(String input) {
    this.input = input;
  }

  @Override
  public void apply(StringBuilder in, StringBuilder out) {
    in.append(input);
  }
}
