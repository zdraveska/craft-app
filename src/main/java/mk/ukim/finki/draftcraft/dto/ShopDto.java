package mk.ukim.finki.draftcraft.dto;

import mk.ukim.finki.draftcraft.domain.common.Address;
import mk.ukim.finki.draftcraft.domain.common.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.domain.shop.ShopCategory;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopDto {

  Long id;

  String name;

  String description;

  List<ProductDto> products;

  List<Address> addresses;

  List<String> phoneNumbers;

  Image image;

  ShopCategory shopCategory;

}