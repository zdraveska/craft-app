package mk.ukim.finki.draftcraft.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;


@Slf4j
public class CustomEmail implements ConstraintValidator<ValidEmail, String> {

  @Override
  public void initialize(ValidEmail validEmail) {
  }

  @Override
  public boolean isValid(String email,
      ConstraintValidatorContext cxt) {
    EmailValidator emailValidator = new EmailValidator();
    boolean valid = email != null && emailValidator.isValid(email, cxt) && email.matches("^(.+)@mail.com");
    if (!valid) {
      log.debug("Invalid email");
    }
    return valid;
  }

}