package mk.ukim.finki.draftcraft.impl;

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
import mk.ukim.finki.draftcraft.service.impl.ProductServiceImpl;
import mk.ukim.finki.draftcraft.utils.BaseServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest extends BaseServiceTest {

  @Mock
  ProductRepository productRepository;

  ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

  ProductService productService;

  ShopMapper shopMapper = Mappers.getMapper(ShopMapper.class);

  @Mock
  ShopService shopService;

  @BeforeEach
  public void setup() {
    productService = new ProductServiceImpl(productRepository, productMapper, shopService, shopMapper);
  }

  @Test
  public void shouldCreateProduct() {
    //given
    CreateProductDto createProductDto = getCreateProductDto();
    Shop shop = generateRandomShop(true);
    Product product = productMapper.createDtoToEntity(createProductDto, shop);

    //when
    when(shopService.getShop(shop.getId())).thenReturn(Optional.of(shop));
    when(productRepository.save(any(Product.class))).thenReturn(product);

    ProductDto actual = productService.createProduct(createProductDto, shop.getId());

    //then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(product.getId());
    assertThat(actual.getName()).isEqualTo(product.getName());
    assertThat(actual.getCategory()).isEqualTo(product.getCategory());
    assertThat(actual.getDescription()).isEqualTo(product.getDescription());
    assertThat(actual.getTags()).isEqualTo(product.getTags());
    assertThat(shop).isEqualTo(product.getShop());
  }

  @Test
  public void shouldThrowShopNotFoundException() {
    //given
    CreateProductDto createProductDto = getCreateProductDto();
    Shop shop = generateRandomShop(true);

    //when
    when(shopService.getShop(shop.getId())).thenReturn(Optional.empty());

    //then
    Exception exception = assertThrows(ShopNotFoundException.class,
        () -> productService.createProduct(createProductDto, shop.getId()));
    String expectedMessage = String.format("Shop with id: %d not found", shop.getId());
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void shouldDeleteProduct() {
    //given
    Product product = generateRandomProduct(true);

    //when
    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
    doNothing().when(productRepository).delete(product);

    productService.deleteShopProduct(product.getId());

    //then
    verify(productRepository, times(1)).delete(product);
  }

  @Test
  public void shouldNotDeleteProduct() {
    //given
    Product product = generateRandomProduct(true);

    //when
    when(productRepository.findById(product.getId())).thenReturn(Optional.empty());

    //then
    Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteShopProduct(product.getId()));
  }

  @Test
  public void shouldUpdateProduct() {
    //given
    UpdateProductDto updateProductDto = getUpdateProductDto();
    Shop shop = generateRandomShop(true);
    Product product = generateRandomProduct(true);
    product.setShop(shop);
    Product expectedProduct = productMapper.updateDtoToEntity(product, updateProductDto);

    //when
    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
    when(productRepository.save(any(Product.class))).thenReturn(product);

    ShopDto actual = productService.updateProduct(updateProductDto, product.getId());
    Product actualProduct = productRepository.findById(product.getId()).get();

    //then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(shop.getId());
    assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName());
    assertThat(actualProduct.getCategory()).isEqualTo(expectedProduct.getCategory());
    assertThat(actualProduct.getDescription()).isEqualTo(expectedProduct.getDescription());
    assertThat(actualProduct.getTags()).isEqualTo(expectedProduct.getTags());
  }

}
