package mk.ukim.finki.draftcraft.dto.input.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.domain.enumeration.ProductCategory;
import mk.ukim.finki.draftcraft.domain.model.common.Image;

import java.util.ArrayList;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductDto {

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 50)
    String name;

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 50)
    String description;

    @NotNull
    ProductCategory category;

    @NotNull
    Image image;

    @NotNull
    Integer price;

    @NotNull
    Integer quantity;

    @Size(min = 1)
    ArrayList<String> tags;
}