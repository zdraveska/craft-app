package mk.ukim.finki.draftcraft.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFileException extends RuntimeException {

  public InvalidFileException(String originalFilename) {
    super(String.format("Invalid file: %s", originalFilename));
  }

}
