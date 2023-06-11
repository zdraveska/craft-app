package mk.ukim.finki.draftcraft.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.domain.users.AccountRequest;
import mk.ukim.finki.draftcraft.domain.users.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlTokenDto {

    String token;

    User user;

    LocalDateTime expiration;

    AccountRequest accountRequest;

}
