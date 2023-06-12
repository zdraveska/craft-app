package mk.ukim.finki.draftcraft.dto.input.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreatePostDto {

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 50)
    String name;

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 255)
    String description;

    @NotNull
    @NotEmpty
    Integer priceRangeMin;

    @NotNull
    @NotEmpty
    Integer priceRangeMax;

    @NotEmpty
    String productCategory;

    @NotEmpty
    String shopCategory;

    @NotEmpty
    @Size(min = 1)
    List<String> tags;

}
