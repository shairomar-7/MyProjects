import java.io.IOException;

/**
 * From the name, you guess it! Its a broken appendable object, that just throws IOExceptions.
 * This will be used to test the IOExceptions that we are handling in the controller.
 * By handling, I mean: an IOException is thrown, we catch, and throw state exception instead.
 * WARNING TO THE IGNORANT CLIENT: DO NOT CALL THIS CLASS, USED FOR TESTINGGGGGGG!
 */
public class BrokenAppendable implements Appendable {

  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("yep");
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("yep");
  }

  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("yep");
  }
}
