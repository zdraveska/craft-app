package mk.ukim.finki.draftcraft.dto.input.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.annotation.ValidPhoneNumber;
import mk.ukim.finki.draftcraft.domain.model.common.Address;
import mk.ukim.finki.draftcraft.domain.model.shop.ShopCategory;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateShopDto {

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 50)
    String name;

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 255)
    String description;

    @NotNull
    ShopCategory category;

    @NotEmpty
    @Size(min = 1)
    List<Address> addresses;

    @NotBlank
    @NotEmpty
    @ValidPhoneNumber
    @Size(min = 1, max = 50)
    String phoneNumber;



}