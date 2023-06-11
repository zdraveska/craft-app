package mk.ukim.finki.draftcraft.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageException extends RuntimeException {

  public ImageException(String imageType) {
    super(String.format("Invalid image type: %s", imageType));
  }

  public ImageException() {
    super("Error while processing image data");
  }

  public ImageException(Long id) {
    super(String.format("Image not found for user: %d", id));
  }
}
