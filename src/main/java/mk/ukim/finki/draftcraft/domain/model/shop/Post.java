package mk.ukim.finki.draftcraft.domain.model.shop;


import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.draftcraft.domain.enumeration.ProductCategory;
import mk.ukim.finki.draftcraft.domain.enumeration.ShopCategory;
import mk.ukim.finki.draftcraft.domain.model.common.Image;
import mk.ukim.finki.draftcraft.domain.model.user.User;
import org.apache.commons.lang3.Range;

import java.time.LocalDate;
import java.util.List;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "creator")
    User creator;

    String name;

    String description;

    LocalDate date;

    Range<Integer> priceRange;

    @Enumerated(value = EnumType.STRING)
    ProductCategory productCategory;

    @Enumerated(value = EnumType.STRING)
    ShopCategory shopCategory;

    @ElementCollection
    List<String> tags;

    @OneToOne
    Image image;

}
