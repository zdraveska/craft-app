package mk.ukim.finki.draftcraft.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("User not found");
  }

  public UserNotFoundException(Long id) {
    super(String.format("User with id: %d not found", id));
  }

  public UserNotFoundException(String stringIdentifier) {
    super(String.format("User: %s not found", stringIdentifier));
  }

}