package mk.ukim.finki.draftcraft.web.rest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.draftcraft.dto.ProductDto;
import mk.ukim.finki.draftcraft.dto.input.shop.CreateProductDto;
import mk.ukim.finki.draftcraft.repository.ShopRepository;
import mk.ukim.finki.draftcraft.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ShopRepository shopRepository;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDto> createProduct(HttpServletResponse response, @RequestBody @Valid CreateProductDto createProductDto) {
        return ResponseEntity.ok(productService.createProduct(createProductDto, shopRepository.findFirstByName("shop1").getId()));
    }
}
