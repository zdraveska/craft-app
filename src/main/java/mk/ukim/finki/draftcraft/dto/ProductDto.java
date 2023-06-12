package mk.ukim.finki.draftcraft.dto;

import mk.ukim.finki.draftcraft.domain.model.common.Image;
import mk.ukim.finki.draftcraft.domain.model.shop.ProductCategory;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    Long id;
    String name;
    String description;
    ProductCategory category;
    List<String> tags;
    Image image;
    Integer price;
    Integer quantity;
}