package mk.ukim.finki.draftcraft.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.domain.model.common.Image;
import mk.ukim.finki.draftcraft.domain.model.user.Name;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

  Long id;

  Name name;

  String email;

  Image image;

}