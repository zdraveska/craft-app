package mk.ukim.finki.draftcraft.dto;

import mk.ukim.finki.draftcraft.domain.common.Image;
import mk.ukim.finki.draftcraft.domain.shop.Product;

import java.util.List;

public class BuyerDto {

    Long id;

    String name;

    String surname;

    String email;

    Image image;

    List<Product> boughtProducts;

    List<Product> favouritesProducts;
}
