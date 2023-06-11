package mk.ukim.finki.draftcraft.impl;

import mk.ukim.finki.draftcraft.domain.exceptions.ShopNotFoundException;
import mk.ukim.finki.draftcraft.domain.shop.Product;
import mk.ukim.finki.draftcraft.domain.shop.Shop;
import mk.ukim.finki.draftcraft.dto.ShopDto;
import mk.ukim.finki.draftcraft.mapper.ShopMapper;
import mk.ukim.finki.draftcraft.repository.ProductRepository;
import mk.ukim.finki.draftcraft.repository.ShopRepository;
import mk.ukim.finki.draftcraft.service.impl.ShopServiceImpl;
import mk.ukim.finki.draftcraft.utils.BaseTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class ShopServiceTest extends BaseTestData {

  @Mock
  ShopRepository shopRepository;


  @Mock
  ProductRepository productRepository;

  ShopMapper shopMapper = Mappers.getMapper(ShopMapper.class);

  ShopServiceImpl shopService;

  @BeforeEach
  void setup() {
    shopService = new ShopServiceImpl(shopRepository, productRepository, shopMapper);
  }

  @Test
  public void shouldGetShop() {
    //given
    Shop shop = generateRandomShop(true);

    //when
    when(shopRepository.findById(shop.getId())).thenReturn(Optional.of(shop));

    Optional<Shop> actual = shopService.getShop(shop.getId());

    //then
    assertThat(actual).isNotNull();

  }

  @Test
//  @WithMockUser(roles = ADMIN)
  public void shouldUploadShop() {
    //given
    Shop shop = generateRandomShop(true);
    List<Product> products = generateProductsFromShop(shop);
    shop.setProducts(products);

    //when
    when(shopRepository.save(any(Shop.class))).thenReturn(shop);
    when(productRepository.saveAll(eq(products))).thenReturn(products);

    ShopDto actual = shopService.createShop(shop);
    ShopDto expected = shopMapper.toDto(shop);

    //then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(expected.getId());
    int actualProductsSize = actual.getProducts().size();
    int expectedProductsSize = expected.getProducts().size();
    assertThat(actualProductsSize).isEqualTo(expectedProductsSize).isEqualTo(products.size());
    assertThat(actual.getProducts().get(0).getId()).isEqualTo(expected.getProducts().get(0).getId());
  }

  @Test
//  @WithMockUser(roles = {USER, ADMIN})
  public void shouldFindShopById() {
    //given
    Shop shop = generateRandomShop(true);

    //when
    when(shopRepository.findById(shop.getId())).thenReturn(Optional.of(shop));

    ShopDto actual = shopService.findShopDtoById(shop.getId());

    //then
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(shop.getId());
    int actualProductsSize = actual.getProducts().size();
    int expectedProductsSize = shop.getProducts().size();
    assertThat(actualProductsSize).isEqualTo(expectedProductsSize);
  }

  @Test
//  @WithMockUser(roles = {USER, ADMIN})
  public void shouldThrowShopNotFoundException() {
    //given
    Shop shop = generateRandomShop(true);

    //when
    when(shopRepository.findById(shop.getId())).thenReturn(Optional.empty());

    //then
    Exception exception = assertThrows(ShopNotFoundException.class,
        () -> shopService.findShopDtoById(shop.getId()));
    String expectedMessage = String.format("Shop with id: %d not found", shop.getId());
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

//  @Test
//  public void shouldCleanUpShopEntity() {
//    //when
//    doNothing().when(shopRepository).deleteByRestaurant(null);
//
//    shopService.cleanUpShopEntity();
//
//    //then
//    verify(shopRepository, times(1)).deleteByRestaurant(null);
//  }

}