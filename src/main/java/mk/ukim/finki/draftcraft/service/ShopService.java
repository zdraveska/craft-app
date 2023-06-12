package mk.ukim.finki.draftcraft.service;

import mk.ukim.finki.draftcraft.domain.model.shop.Shop;
import mk.ukim.finki.draftcraft.dto.ShopDto;

import java.util.Optional;

public interface ShopService {

  ShopDto createShop(Shop shop);

  Optional<Shop> getShop(Long id);

  ShopDto findShopDtoById(Long id);

}