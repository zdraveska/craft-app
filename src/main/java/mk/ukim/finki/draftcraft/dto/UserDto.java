package mk.ukim.finki.draftcraft.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.domain.model.common.Image;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

  Long id;
  String name;
  String surname;
  String username;
  String email;
  String phoneNumber;
  Image image;

}