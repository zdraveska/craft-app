package mk.ukim.finki.draftcraft.domain.model.user;

import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.draftcraft.domain.model.shop.Shop;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
public class Seller extends User{

    @ManyToMany
    List<Shop> employedInShops;
}
