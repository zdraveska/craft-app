package mk.ukim.finki.draftcraft.dto;


import jakarta.persistence.ElementCollection;
import lombok.Getter;
import mk.ukim.finki.draftcraft.domain.shop.ProductCategory;
import mk.ukim.finki.draftcraft.domain.shop.ShopCategory;
import mk.ukim.finki.draftcraft.domain.users.User;
import org.apache.commons.lang3.Range;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PostDto {

    Long id;
    User creator;
    String name;
    String description;
    LocalDate date;
    Range<Integer> priceRange;
    ProductCategory productCategory;
    ShopCategory shopCategory;
    @ElementCollection
    List<String> tags;
}
