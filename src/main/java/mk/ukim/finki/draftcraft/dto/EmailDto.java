package mk.ukim.finki.draftcraft.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDto {

  String to;

  String subject;

  String body;

  String toName;

  String toSurname;

  String url;

}