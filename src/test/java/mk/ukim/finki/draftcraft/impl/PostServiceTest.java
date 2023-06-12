package mk.ukim.finki.draftcraft.impl;

import mk.ukim.finki.draftcraft.domain.model.shop.Post;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.PostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreatePostDto;
import mk.ukim.finki.draftcraft.mapper.PostMapper;
import mk.ukim.finki.draftcraft.repository.PostRepository;
import mk.ukim.finki.draftcraft.service.PostService;
import mk.ukim.finki.draftcraft.service.UserService;
import mk.ukim.finki.draftcraft.service.impl.PostServiceImpl;
import mk.ukim.finki.draftcraft.utils.BaseServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest extends BaseServiceTest {

  @Mock
  PostRepository postRepository;

  PostMapper postMapper = Mappers.getMapper(PostMapper.class);

  PostService postService;

  @Mock
  UserService userService;


  @BeforeEach
  public void setup() {
    postService = new PostServiceImpl(postRepository, postMapper, userService);
  }

  @Test
  @WithMockUser(username = USER_EMAIL)
  public void shouldCreatePost() {
    //given
    User user = generateRandomUser(true);
    user.setEmail(USER_EMAIL);
    CreatePostDto createPostDto = getCreatePostDto();
    Post post = postMapper.createDtoToEntity(createPostDto, user);

    //when
    when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    when(postRepository.save(any(Post.class))).thenReturn(post);

    PostDto actual = postService.createPost(createPostDto);

    //then
    assertThat(actual).isNotNull();
//    assertThat(actual.getId()).isEqualTo(post.getId());
    assertThat(actual.getName()).isEqualTo(post.getName());
    assertThat(actual.getProductCategory()).isEqualTo(post.getProductCategory());
    assertThat(actual.getShopCategory()).isEqualTo(post.getShopCategory());
    assertThat(actual.getDescription()).isEqualTo(post.getDescription());
    assertThat(actual.getTags()).isEqualTo(post.getTags());
    assertThat(actual.getPriceRange()).isEqualTo(post.getPriceRange());
    assertThat(actual.getCreator()).isEqualTo(user);
    assertThat(actual.getDate()).isEqualTo(post.getDate());
  }
//
//  @Test
//  public void shouldThrowShopNotFoundException() {
//    //given
//    CreateProductDto createProductDto = getCreateProductDto();
//    Shop shop = generateRandomShop(true);
//
//    //when
//    when(shopService.getShop(shop.getId())).thenReturn(Optional.empty());
//
//    //then
//    Exception exception = assertThrows(ShopNotFoundException.class,
//        () -> productService.createProduct(createProductDto, shop.getId()));
//    String expectedMessage = String.format("Shop with id: %d not found", shop.getId());
//    String actualMessage = exception.getMessage();
//    assertTrue(actualMessage.contains(expectedMessage));
//  }
//
//  @Test
//  public void shouldDeleteProduct() {
//    //given
//    Product product = generateRandomProduct(true);
//
//    //when
//    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
//    doNothing().when(productRepository).delete(product);
//
//    productService.deleteShopProduct(product.getId());
//
//    //then
//    verify(productRepository, times(1)).delete(product);
//  }
//
//  @Test
//  public void shouldNotDeleteProduct() {
//    //given
//    Product product = generateRandomProduct(true);
//
//    //when
//    when(productRepository.findById(product.getId())).thenReturn(Optional.empty());
//
//    //then
//    Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteShopProduct(product.getId()));
//  }
//
//  @Test
//  public void shouldUpdateProduct() {
//    //given
//    UpdateProductDto updateProductDto = getUpdateProductDto();
//    Shop shop = generateRandomShop(true);
//    Product product = generateRandomProduct(true);
//    product.setShop(shop);
//    Product expectedProduct = productMapper.updateDtoToEntity(product, updateProductDto);
//
//    //when
//    when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
//    when(productRepository.save(any(Product.class))).thenReturn(product);
//
//    ShopDto actual = productService.updateProduct(updateProductDto, product.getId());
//    Product actualProduct = productRepository.findById(product.getId()).get();
//
//    //then
//    assertThat(actual).isNotNull();
//    assertThat(actual.getId()).isEqualTo(shop.getId());
//    assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName());
//    assertThat(actualProduct.getCategory()).isEqualTo(expectedProduct.getCategory());
//    assertThat(actualProduct.getDescription()).isEqualTo(expectedProduct.getDescription());
//    assertThat(actualProduct.getTags()).isEqualTo(expectedProduct.getTags());
//  }

}
