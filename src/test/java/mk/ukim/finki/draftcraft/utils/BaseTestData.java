package mk.ukim.finki.draftcraft.utils;

import lombok.SneakyThrows;
import mk.ukim.finki.draftcraft.domain.enumeration.*;
import mk.ukim.finki.draftcraft.domain.model.common.Address;
import mk.ukim.finki.draftcraft.domain.model.common.Image;
import mk.ukim.finki.draftcraft.domain.model.common.UrlToken;
import mk.ukim.finki.draftcraft.domain.model.shop.Post;
import mk.ukim.finki.draftcraft.domain.model.shop.Product;
import mk.ukim.finki.draftcraft.domain.model.shop.Shop;
import mk.ukim.finki.draftcraft.domain.model.user.AccountRequest;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import mk.ukim.finki.draftcraft.dto.EmailDto;
import mk.ukim.finki.draftcraft.dto.UrlTokenDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreatePostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreateProductDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdatePostDto;
import mk.ukim.finki.draftcraft.dto.input.shop.UpdateProductDto;
import mk.ukim.finki.draftcraft.dto.input.user.*;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

import static mk.ukim.finki.draftcraft.domain.enumeration.UserRole.BUYER;

public class BaseTestData {

//  protected static final String USER = "USER";
//
//  protected static final String ADMIN = "ADMIN";

    protected final String USER_EMAIL = "username@gmail.com";

    protected final String BASE_URL = "http://localhost:8080";

    protected final int URL_EXPIRATION = 1440;

  protected final String RESET_PASSWORD_PATH = "/api/users/reset-password?token=";

    @SneakyThrows
    protected Image generateImage() {
        MockMultipartFile mockMultipartFile = generateMultiPartFile();

        return Image.builder()
                .image(mockMultipartFile.getBytes())
                .imageContentType(mockMultipartFile.getContentType())
                .imageName(mockMultipartFile.getName()).build();
    }

    protected MockMultipartFile generateMultiPartFile() {
        return new MockMultipartFile(
                "file",
                "hello.png",
                MediaType.IMAGE_PNG_VALUE,
                "Hello, World!".getBytes()
                );
    }

    protected MockMultipartFile generateInvalidMultiPartFile() {
        return new MockMultipartFile(
                "file",
                "hello.svg",
                MediaType.IMAGE_GIF_VALUE,
                "Hello, World!".getBytes()
                );
    }

    protected Product generateRandomProduct(Boolean withId) {
        long random = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + new Random().nextLong();
        var tags = new ArrayList<>(List.of("obuvki", "patiki", "converse"));
        return getProduct(withId ? random : null, "name", ProductCategory.CLOTHES, "description", 1000, 10, tags, generateRandomShop(true), generateImage());
    }

    private Product getProduct(Long id, String name, ProductCategory category, String description, int price, int quantity, ArrayList<String> tags, Shop shop, Image image) {
        return Product.builder()
                .id(id)
                .name(name)
                .category(category)
                .description(description)
                .price(price)
                .quantity(quantity)
                .tags(tags)
                .shop(shop)
                .image(image)
                .build();
    }

    @SneakyThrows
    protected Shop generateRandomShop(Boolean withId) {
        return getShop(withId ? new Date().getTime() : null, "shop name", ShopCategory.ONLINE, "description", generateImage());
    }

    protected Shop getShop(Long id, String name, ShopCategory category, String description, Image image) {
        List<Product> products = new ArrayList<>();
        List<User> employess = new ArrayList<>();
        List<Address> addresses = new ArrayList<>();
        return Shop.builder()
                .id(id)
                .name(name)
                .shopCategory(category)
                .description(description)
                .products(products)
                .image(image)
                .employees(employess)
                .addresses(addresses)
                .build();
    }

    protected Post generateRandomPost(Boolean withId, User user) {
        return getPost(withId ? new Date().getTime() : null, "post name", user, ProductCategory.CLOTHES, ShopCategory.ONLINE, "description", generateImage());
    }

    protected Post getPost(Long id, String name, User creator, ProductCategory productCategory, ShopCategory shopCategory, String description, Image image) {
        return Post.builder()
                .id(id)
                .name(name)
                .productCategory(productCategory)
                .shopCategory(shopCategory)
                .description(description)
                .image(image)
                .date(LocalDate.now())
                .creator(creator)
                .build();
    }

    @SneakyThrows
    protected List<Product> generateProductsFromShop(Shop shop) {
        Product firstProduct = generateRandomProduct(false);
        Product secondProduct = generateRandomProduct(false);
        firstProduct.setShop(shop);
        secondProduct.setShop(shop);
        return new ArrayList<>(List.of(firstProduct, secondProduct));
    }

    protected AccountRequest generateRandomAccountRequest(Boolean withId, Boolean emailConfirmed) {
        return getAccountRequest(withId ? new Date().getTime() : null, UUID.randomUUID() + "@gmail.com", "name",
                "surname", BUYER, emailConfirmed, LocalDate.now());
    }

    protected AccountRequest getAccountRequest(Long id, String email, String name, String surname, UserRole role, Boolean emailConfirmed,
                                               LocalDate date) {
        return AccountRequest.builder()
                .name(name)
                .surname(surname)
                .email(email)
                .status(AccountRequestStatus.PENDING)
                .createdDate(date)
                .emailConfirmed(emailConfirmed)
                .role(role)
                .build();
    }

    protected UrlToken generateUrlToken(AccountRequest accountRequest, User user, int minutes) {
        String tokenValue = UUID.randomUUID().toString();
        UrlToken urlToken = UrlToken.builder()
                .token(tokenValue)
                .user(user)
                .accountRequest(accountRequest)
                .build();
        urlToken.setExpiration(minutes);
        return urlToken;
    }

    protected UrlTokenDto generateTokenDto(UrlToken urlToken) {
        return UrlTokenDto.builder()
                .token(urlToken.getToken())
                .user(urlToken.getUser())
                .accountRequest(urlToken.getAccountRequest())
                .expiration(urlToken.getExpiration()).build();

    }

    protected CreateAccountRequestDto getCreateAccountRequestDto() {
        return CreateAccountRequestDto.builder()
                .name("name")
                .surname("surname")
                .email(UUID.randomUUID() + "@gmail.com")
                .role("BUYER")
                .phoneNumber("070123456")
                .build();
    }

    protected CreateProductDto getCreateProductDto() {
        var tags = new ArrayList<>(List.of("tag1", "tag2"));
        return CreateProductDto.builder()
                .name("name")
                .category(ProductCategory.SHOES)
                .description("description")
                .tags(tags)
                .build();
    }

    protected UpdateProductDto getUpdateProductDto() {
        var tags = new ArrayList<>(List.of("tagNew1", "tagNew2"));
        return UpdateProductDto.builder()
                .name("nameUpdated")
                .category(ProductCategory.OTHER)
                .description("descrUpdated")
                .tags(tags)
                .build();
    }

    protected CreatePostDto getCreatePostDto() {
        var tags = new ArrayList<>(List.of("tag1", "tag2"));
        return CreatePostDto.builder()
                .name("name")
                .productCategory("OTHER")
                .shopCategory("ONLINE")
                .description("description")
                .priceRangeMin(500)
                .priceRangeMax(1000)
                .tags(tags)
                .build();
    }

    protected UpdatePostDto getUpdatePostDto() {
        var tags = new ArrayList<>(List.of("tagNew1", "tagNew2"));
        return UpdatePostDto.builder()
                .name("newName")
                .productCategory("CRAFTS")
                .shopCategory("STORE")
                .description("newDescription")
                .priceRangeMin(200)
                .priceRangeMax(500)
                .tags(tags)
                .build();
    }

    protected CreateUserDto getCreateUserDto() {
        return CreateUserDto.builder()
                .name("firstName")
                .surname("surname")
                .phoneNumber("071292523")
                .email("username@gmail.com")
                .role("BUYER")
                .build();
    }

    protected UpdateUserDto getUpdateUserDto(String email) {
        return UpdateUserDto.builder()
                .name("updateName")
                .surname("updateSurname")
                .phoneNumber("070123456")
                .email(email).build();
    }
    protected PasswordDto getResetPasswordDto() {
        return PasswordDto.builder()
                .password("newPassword")
                .build();
    }

    protected ChangeUserPasswordDto getChangePasswordDto() {
        return ChangeUserPasswordDto.builder()
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();
    }

    protected String getResetPasswordUrl(String token) {
        return BASE_URL + RESET_PASSWORD_PATH + token;
    }

    protected static Stream<Arguments> invalidPasswords() {
        return Stream.of(
                Arguments.of("p@ssword"),
                Arguments.of("123456789"),
                Arguments.of("zxcvbnm,./"),
                Arguments.of("test"),
                Arguments.of("badpassword"),
                Arguments.of(" ")
        );
    }

    protected static Stream<Arguments> passwordStrength() {
        return Stream.of(
                Arguments.of("qwER43@!", 2),
                Arguments.of("Tr0ub4dour&3", 2),
                Arguments.of("correcthorsebatterystaple", 4),
                Arguments.of("coRrecth0rseba++ery9.23.2007staple$k", 4),
                Arguments.of("a b c d e f g h i j k l m n o p q r s t u v w x y z 0 1 2 3 4 5 6 7 8 9", 4),
                Arguments.of("correct-horse-battery-staple", 4)
        );
    }

    protected static Stream<Arguments> invalidInput() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of(" ", " "),
                Arguments.of("        ", "        "),
                Arguments.of("@  ", "@  "),
                Arguments.of("A#", "A#"),
                Arguments.of("A#", "ada"),
                Arguments.of("Abc1", "Abc")
        );
    }

    public static EmailDto generateEmail(EmailType type) {
        return EmailDto.builder()
                .to("zdraveskaema@gmail.com")
                .subject(type.getSubject())
                .body(type.getBody())
                .toName("Name")
                .toSurname("Surname")
                .url("http://localhost:8080")
                .build();
    }

    protected String getConfirmEmailUrl(String baseUrl, String confirmEmailPath, String token) {
        return baseUrl + confirmEmailPath + token;
    }

    protected User generateRandomUser(Boolean withId) {
        long random = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + new Random().nextLong();
        return getUser(withId ? random : null, random + "@gmail.com", "name", "surname", "071123456", BUYER);
    }

    protected User getUser(Long id, String email, String name, String surname, String phoneNumber, UserRole role) {
        return User.builder()
                .id(id)
                .email(email)
                .name(name)
                .surname(surname)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
    }

    protected LoginRequestDto getLoginRequestDto(String email, String password) {
        return LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();
    }

}