package mk.ukim.finki.draftcraft.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountRequestNotFoundException extends RuntimeException {

  public AccountRequestNotFoundException(Long id) {
    super(String.format("Account request with id: %d not found.", id));
  }

}