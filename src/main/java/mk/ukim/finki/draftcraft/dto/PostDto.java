package mk.ukim.finki.draftcraft.dto;


import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.domain.enumeration.ProductCategory;
import mk.ukim.finki.draftcraft.domain.enumeration.ShopCategory;
import mk.ukim.finki.draftcraft.domain.model.common.Image;
import org.apache.commons.lang3.Range;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    Long id;
    UserDto creator;
    String name;
    String description;
    LocalDate date;
    Range<Integer> priceRange;
    ProductCategory productCategory;
    ShopCategory shopCategory;
    @ElementCollection
    ArrayList<String> tags;
    Image image;
}
