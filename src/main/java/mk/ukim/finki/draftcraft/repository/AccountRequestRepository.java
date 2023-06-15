package mk.ukim.finki.draftcraft.repository;

import mk.ukim.finki.draftcraft.domain.model.user.AccountRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRequestRepository extends JpaRepository<AccountRequest, Long> {

  Optional<AccountRequest> findByEmail(String email);

}