package mk.ukim.finki.draftcraft.domain.shop;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.draftcraft.domain.common.Image;

import java.util.List;

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
    List<String> tags;
    @ManyToOne
    Shop shop;
    @OneToOne
    Image image;
    Integer price;
    Integer quantity;

}
