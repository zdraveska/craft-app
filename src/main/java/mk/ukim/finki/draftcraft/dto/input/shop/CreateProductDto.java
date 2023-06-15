package mk.ukim.finki.draftcraft.dto.input.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.domain.model.common.Image;
import mk.ukim.finki.draftcraft.domain.enumeration.ProductCategory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductDto {

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 50)
    String name;

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 255)
    String description;

    @NotNull
    ProductCategory category;

    @Size(min = 1)
    List<String> tags;

//    @NotNull
    Image image;

    @NotNull
    Integer price;

    @NotNull
    Integer quantity;

}