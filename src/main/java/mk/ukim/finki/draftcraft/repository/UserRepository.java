package mk.ukim.finki.draftcraft.repository;

import mk.ukim.finki.draftcraft.domain.users.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  List<User> findAllByOrderByNameAsc(Pageable pageable);

}