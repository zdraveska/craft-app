package mk.ukim.finki.draftcraft.dto.input.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import mk.ukim.finki.draftcraft.annotation.ValidEmail;
import mk.ukim.finki.draftcraft.annotation.ValidPhoneNumber;


@Data
@Builder
public class CreateAccountRequestDto {

  @NotBlank
  @Size(min = 1, max = 50)
  String name;

  @NotBlank
  @Size(min = 1, max = 50)
  String surname;

  @ValidEmail
  @NotBlank
  @Size(min = 15, max = 128)
  String email;

  @NotBlank
  @NotEmpty
  @ValidPhoneNumber
  @Size(min = 1, max = 50)
  String phoneNumber;

  @NotBlank
  String role;

}