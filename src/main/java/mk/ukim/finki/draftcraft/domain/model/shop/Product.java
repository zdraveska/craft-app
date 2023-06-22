package mk.ukim.finki.draftcraft.domain.model.shop;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.draftcraft.domain.enumeration.ProductCategory;
import mk.ukim.finki.draftcraft.domain.model.common.Image;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    @Enumerated(value = EnumType.STRING)
    ProductCategory category;
    @ElementCollection // https://stackoverflow.com/a/57241586
    ArrayList<String> tags;
    @ManyToOne
    Shop shop;
    @OneToOne
    @JoinColumn(name = "image_id")
    Image image;
    Integer price;
    Integer quantity;

}
