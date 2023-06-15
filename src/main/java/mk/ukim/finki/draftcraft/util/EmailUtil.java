package mk.ukim.finki.draftcraft.util;


import mk.ukim.finki.draftcraft.domain.enumeration.EmailType;
import mk.ukim.finki.draftcraft.dto.EmailDto;

public final class EmailUtil {

  public static EmailDto generateEmail(String emailAddress, String name, String surname, EmailType type,
                                       String url) {
    return EmailDto.builder()
        .to(emailAddress)
        .subject(type.getSubject())
        .body(type.getBody())
        .toName(name)
        .toSurname(surname)
        .url(url)
        .build();
  }

}