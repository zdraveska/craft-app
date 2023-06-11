package mk.ukim.finki.draftcraft.service;

import mk.ukim.finki.draftcraft.dto.ProductDto;
import mk.ukim.finki.draftcraft.dto.ShopDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreateProductDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdateProductDto;

import java.util.List;

public interface ProductService {

  List<ProductDto> getProducts();

  ProductDto createProduct(CreateProductDto createProductDto, Long shopId);

  void deleteShopProduct(Long id);

  ShopDto updateProduct(UpdateProductDto updateProductDto, Long ProductId);
}