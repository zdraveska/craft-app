package mk.ukim.finki.draftcraft.repository;

import mk.ukim.finki.draftcraft.domain.common.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}