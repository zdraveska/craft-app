package mk.ukim.finki.draftcraft.dto;

import mk.ukim.finki.draftcraft.domain.model.common.Image;
import mk.ukim.finki.draftcraft.domain.model.shop.Product;

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
