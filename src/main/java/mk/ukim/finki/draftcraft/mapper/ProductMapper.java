package mk.ukim.finki.draftcraft.mapper;

import mk.ukim.finki.draftcraft.domain.model.shop.Product;
import mk.ukim.finki.draftcraft.domain.model.shop.Shop;
import mk.ukim.finki.draftcraft.dto.ProductDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreateProductDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdateProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "shop", source = "shop")
    @Mapping(target = "name", source = "createProductDto.name")
    @Mapping(target = "description", source = "createProductDto.description")
    @Mapping(target = "image", source = "createProductDto.image")
    @Mapping(target = "id", ignore = true)
    Product createDtoToEntity(CreateProductDto createProductDto, Shop shop);

    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    Product updateDtoToEntity(@MappingTarget Product product, UpdateProductDto updateProductDto);

//    @Mapping(target = "name", source = "user", qualifiedByName = "createFullName")
    List<ProductDto> listToDto(List<Product> products);

}
