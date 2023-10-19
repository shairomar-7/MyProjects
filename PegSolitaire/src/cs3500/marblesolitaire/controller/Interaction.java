package cs3500.marblesolitaire.controller;

/**
 * Represents an Interaction, which would be an input or an output.
 * This class would be used to test our game's controller to make sure the inputs and outputs are
 * truly what we expect.
 */
public interface Interaction {

  /**
   * Void method applies the given I/O and saves to the corresponding class.
   * @param in StringBuilder represents the given input.
   * @param out StringBuilder represents the given output.
   */
  void apply(StringBuilder in, StringBuilder out);
}

