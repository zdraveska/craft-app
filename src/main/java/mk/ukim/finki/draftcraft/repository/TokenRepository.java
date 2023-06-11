package mk.ukim.finki.draftcraft.repository;


import mk.ukim.finki.draftcraft.domain.common.UrlToken;
import mk.ukim.finki.draftcraft.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<UrlToken, Long> {

  Optional<UrlToken> findByToken(String token);

  Optional<UrlToken> findByUser(User user);

}