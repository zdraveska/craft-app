package mk.ukim.finki.draftcraft.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShopNotFoundException extends RuntimeException {

  public ShopNotFoundException(Long id) {
    super(String.format("Shop with id: %d not found.", id));
  }

}