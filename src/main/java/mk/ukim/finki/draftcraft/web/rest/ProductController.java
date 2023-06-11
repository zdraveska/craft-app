package mk.ukim.finki.draftcraft.web.rest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.draftcraft.domain.common.Image;
import mk.ukim.finki.draftcraft.domain.shop.*;
import mk.ukim.finki.draftcraft.domain.users.Name;
import mk.ukim.finki.draftcraft.domain.users.User;
import mk.ukim.finki.draftcraft.dto.PostDto;
import mk.ukim.finki.draftcraft.dto.ProductDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreateProductDto;
import mk.ukim.finki.draftcraft.repository.*;
import mk.ukim.finki.draftcraft.service.PostService;
import mk.ukim.finki.draftcraft.service.ProductService;
import org.apache.commons.lang3.Range;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final PostService postService;

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final ShopRepository shopRepository;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        //"file",
        //                        "hello.png",
        //                        MediaType.IMAGE_PNG_VALUE,
        //                        "Hello, World!".getBytes()
        var imageUser = Image.builder()
                .image("User image".getBytes())
                .imageContentType(MediaType.IMAGE_PNG_VALUE)
                .imageName("image")
                .build();
        var imageProduct = Image.builder()
                .image("Product image".getBytes())
                .imageContentType(MediaType.IMAGE_PNG_VALUE)
                .imageName("image2")
                .build();
        var imageShop = Image.builder()
                .image("Shop image".getBytes())
                .imageContentType(MediaType.IMAGE_PNG_VALUE)
                .imageName("image3")
                .build();
        var imagePost = Image.builder()
                .image("Post image".getBytes())
                .imageContentType(MediaType.IMAGE_PNG_VALUE)
                .imageName("image4")
                .build();
        imageUser = imageRepository.save(imageUser);
        imageProduct = imageRepository.save(imageProduct);
        imageShop = imageRepository.save(imageShop);
        imagePost = imageRepository.save(imagePost);
        var user = User.builder()
                .email("email@mail.com")
                .username("username")
                .password("pass")
                .name(new Name("name", "surname"))
                .phoneNumber("071292523")
                .image(imageUser)
                .build();
        user = userRepository.save(user);
        var post = Post.builder()
                .date(LocalDate.now())
                .creator(user)
                .name("product idea")
                .description("blabla")
                .priceRange(Range.between(200,500))
                .shopCategory(ShopCategory.ONLINE)
                .productCategory(ProductCategory.CRAFTS)
                .tags(List.of("tag1", "tag2", "tag3"))
                .image(imagePost)
                .build();
        postRepository.save(post);
        var shop = Shop.builder()
                .name("shop1")
                .shopCategory(ShopCategory.STORE)
                .image(imageShop)
                .build();
        shop = shopRepository.save(shop);
        var product = Product.builder()
                .name("product1")
                .category(ProductCategory.CLOTHES)
                .description("description1")
                .price(123)
                .tags(List.of("obuvki","patiki","converse"))
                .quantity(2)
                .shop(shop)
                .image(imageProduct)
                .build();
        productRepository.save(product);
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDto> createProduct(HttpServletResponse response, @RequestBody @Valid CreateProductDto createProductDto) {
        return ResponseEntity.ok(productService.createProduct(createProductDto, shopRepository.findFirstByName("shop1").getId()));
    }
}
