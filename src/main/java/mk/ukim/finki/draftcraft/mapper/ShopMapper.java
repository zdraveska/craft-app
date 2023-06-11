package mk.ukim.finki.draftcraft.mapper;

import mk.ukim.finki.draftcraft.domain.shop.Shop;
import mk.ukim.finki.draftcraft.dto.ShopDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShopMapper {

    ShopDto toDto(Shop Shop);

}