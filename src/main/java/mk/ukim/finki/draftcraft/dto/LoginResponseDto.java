package mk.ukim.finki.draftcraft.dto;

import mk.ukim.finki.draftcraft.domain.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {

  UserDto userDto;

  UserRole role;

}
