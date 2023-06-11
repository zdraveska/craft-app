package mk.ukim.finki.draftcraft.domain.users;

import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.draftcraft.domain.shop.Product;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
public class Buyer extends User{

    @ManyToMany
    List<Product> boughtProducts;
    @ManyToMany
    List<Product> favouritesProducts;
}
