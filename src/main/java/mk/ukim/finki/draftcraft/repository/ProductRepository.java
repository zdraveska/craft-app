package mk.ukim.finki.draftcraft.repository;

import mk.ukim.finki.draftcraft.domain.model.shop.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
