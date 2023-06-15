package mk.ukim.finki.draftcraft.dto;


import jakarta.persistence.ElementCollection;
import lombok.Getter;
import mk.ukim.finki.draftcraft.domain.model.common.Image;
import mk.ukim.finki.draftcraft.domain.enumeration.ProductCategory;
import mk.ukim.finki.draftcraft.domain.enumeration.ShopCategory;
import org.apache.commons.lang3.Range;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PostDto {

    UserDto creator;
    String name;
    String description;
    LocalDate date;
    Range<Integer> priceRange;
    ProductCategory productCategory;
    ShopCategory shopCategory;
    @ElementCollection
    List<String> tags;
    Image image;
}
