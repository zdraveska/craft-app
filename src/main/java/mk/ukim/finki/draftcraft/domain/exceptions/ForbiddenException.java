package mk.ukim.finki.draftcraft.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

  public ForbiddenException(String username, Long id) {
    super(String.format("User: %s does not have permission to update user with id: %d", username, id));
  }

  public ForbiddenException(String username) {
    super(String.format("User: %s does not have permission to update this field", username));
  }

}