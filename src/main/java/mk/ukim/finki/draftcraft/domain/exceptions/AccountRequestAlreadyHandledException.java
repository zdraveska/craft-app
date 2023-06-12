package mk.ukim.finki.draftcraft.domain.exceptions;

import mk.ukim.finki.draftcraft.domain.model.user.AccountRequestStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountRequestAlreadyHandledException extends RuntimeException {

  public AccountRequestAlreadyHandledException(Long id, AccountRequestStatus status) {
    super(String.format("Account request: %d has already been %s", id, status.toString()));
  }

}