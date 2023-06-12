package mk.ukim.finki.draftcraft.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.draftcraft.domain.exceptions.ShopNotFoundException;
import mk.ukim.finki.draftcraft.domain.model.shop.Shop;
import mk.ukim.finki.draftcraft.dto.ShopDto;
import mk.ukim.finki.draftcraft.mapper.ShopMapper;
import mk.ukim.finki.draftcraft.repository.ProductRepository;
import mk.ukim.finki.draftcraft.repository.ShopRepository;
import mk.ukim.finki.draftcraft.service.ShopService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements ShopService {

  private final ShopRepository shopRepository;
  private final ProductRepository productRepository;
  private final ShopMapper shopMapper;

  @Override
  public ShopDto createShop(Shop shop) {
//    Shop shop = new Shop();
    shop = shopRepository.save(shop);
//    List<Product> products = parseProductsFromCsv(shop, shop);
//    products = productRepository.saveAll(products);
//    shop.setProducts(products);
    log.info("Created shop {}", shop.getName());
    return shopMapper.toDto(shop);
  }

  @Override
  public Optional<Shop> getShop(Long id) {
    log.info("Find Shop by id: " + id);
    return shopRepository.findById(id);
  }

  @Override
  public ShopDto findShopDtoById(Long id) {
    Shop shop = getShop(id)
        .orElseThrow(() -> new ShopNotFoundException(id));
    return shopMapper.toDto(shop);
  }

//  @Scheduled(cron = "0 0 8 * * MON")
//  protected void cleanUpShopEntity() {
//    log.info("Deleting unused Shops");
//    ShopRepository.deleteByRestaurant(null);
//  }

}