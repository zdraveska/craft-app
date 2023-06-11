package mk.ukim.finki.draftcraft.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomPhoneNumber.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {

  String message() default "Invalid phoneNumber";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}