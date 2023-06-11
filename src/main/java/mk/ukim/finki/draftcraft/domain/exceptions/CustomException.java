package mk.ukim.finki.draftcraft.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//TODO change exception name
public class CustomException extends RuntimeException {

  public CustomException(String message) {
    super(message);
  }

}