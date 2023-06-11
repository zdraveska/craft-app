package mk.ukim.finki.draftcraft.dto.input.shop;

import mk.ukim.finki.draftcraft.annotation.ValidPhoneNumber;
import mk.ukim.finki.draftcraft.domain.shop.ShopCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateShopDto {

  @NotBlank
  @NotEmpty
  @Size(min = 1, max = 50)
  String name;

  @NotBlank
  @NotEmpty
  @Size(min = 1, max = 255)
  String address;

  @NotBlank
  @NotEmpty
  @ValidPhoneNumber
  @Size(min = 1, max = 50)
  String phoneNumber;

  @NotNull
  ShopCategory category;

}