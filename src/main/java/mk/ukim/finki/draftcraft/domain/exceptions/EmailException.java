package mk.ukim.finki.draftcraft.domain.exceptions;

public class EmailException extends RuntimeException {

  public EmailException(String message) {
    super(String.format("%s", message));
  }
}
