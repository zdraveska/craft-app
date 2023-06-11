package mk.ukim.finki.draftcraft.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenIsNotValidException extends RuntimeException {

  public TokenIsNotValidException() {
    super("Token not valid.");
  }

}