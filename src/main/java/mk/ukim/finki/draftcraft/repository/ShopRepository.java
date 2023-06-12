package mk.ukim.finki.draftcraft.repository;

import mk.ukim.finki.draftcraft.domain.model.shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Shop findFirstByName(String name);

}
