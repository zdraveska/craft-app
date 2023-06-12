package mk.ukim.finki.draftcraft.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.draftcraft.domain.exceptions.ProductNotFoundException;
import mk.ukim.finki.draftcraft.domain.exceptions.ShopNotFoundException;
import mk.ukim.finki.draftcraft.domain.shop.Product;
import mk.ukim.finki.draftcraft.domain.shop.Shop;
import mk.ukim.finki.draftcraft.dto.ProductDto;
import mk.ukim.finki.draftcraft.dto.ShopDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreateProductDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdateProductDto;
import mk.ukim.finki.draftcraft.mapper.ProductMapper;
import mk.ukim.finki.draftcraft.mapper.ShopMapper;
import mk.ukim.finki.draftcraft.repository.ProductRepository;
import mk.ukim.finki.draftcraft.service.ProductService;
import mk.ukim.finki.draftcraft.service.ShopService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final ShopService shopService;
  private final ShopMapper shopMapper;

  @Override
  public List<ProductDto> getProducts() {
    return productMapper.listToDto(productRepository.findAll());
  }

  @Override
  public ProductDto createProduct(CreateProductDto createProductDto, Long shopId) {
    Shop shop = shopService.getShop(shopId).
        orElseThrow(() -> new ShopNotFoundException(shopId));
    Product product = productMapper.createDtoToEntity(createProductDto, shop);

    productRepository.save(product);
    log.info("Product {} created", product);
    return productMapper.toDto(product);
  }

  @Override
  public void deleteShopProduct(Long id) {
    Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

    productRepository.delete(product);
    log.info("Product has been deleted");
  }

  @Override
  public ShopDto updateProduct(UpdateProductDto updateProductDto, Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    product = productMapper.updateDtoToEntity(product, updateProductDto);
    productRepository.save(product);
    log.info("Product {} modified", product);
    return shopMapper.toDto(product.getShop());
  }

}