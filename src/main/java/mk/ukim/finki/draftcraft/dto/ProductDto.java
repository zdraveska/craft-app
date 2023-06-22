package mk.ukim.finki.draftcraft.dto;

import lombok.*;
import mk.ukim.finki.draftcraft.domain.enumeration.ProductCategory;
import mk.ukim.finki.draftcraft.domain.model.common.Image;

import java.util.ArrayList;

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
    ArrayList<String> tags;
    Image image;
    Integer price;
    Integer quantity;
}