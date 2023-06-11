package mk.ukim.finki.draftcraft.dto.input.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import mk.ukim.finki.draftcraft.annotation.ValidEmail;


@Data
@Builder
public class CreateAccountRequestDto {

  @NotBlank
  @Size(min = 1, max = 50)
  String name;

  @NotBlank
  @Size(min = 1, max = 50)
  String surname;

  @NotBlank
  @Size(min = 1, max = 50)
  String username;

  @ValidEmail
  @NotBlank
  @Size(min = 15, max = 128)
  String email;

  @NotBlank
  String role;

  String password;

}