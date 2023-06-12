package mk.ukim.finki.draftcraft.repository;

import mk.ukim.finki.draftcraft.domain.model.shop.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
