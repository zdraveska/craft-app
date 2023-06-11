package mk.ukim.finki.draftcraft.dto.input.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.annotation.ValidEmail;
import mk.ukim.finki.draftcraft.annotation.ValidPhoneNumber;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserDto {

  @NotBlank
  @Size(min = 1, max = 50)
  String name;

  @NotBlank
  @Size(min = 1, max = 50)
  String surname;

  @ValidEmail
  @NotNull
  @Size(min = 15, max = 128)
  String email;

  @NotBlank
  @Size(min = 1, max = 50)
  String username;

  @NotBlank
  @Size(min = 1, max = 50)
  String password;

  @NotBlank
  @NotEmpty
  @ValidPhoneNumber
  @Size(min = 1, max = 50)
  String phoneNumber;

  @NotBlank
  @NotEmpty
  String role;
}