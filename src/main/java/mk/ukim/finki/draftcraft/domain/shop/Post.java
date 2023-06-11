package mk.ukim.finki.draftcraft.domain.shop;


import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.draftcraft.domain.common.Image;
import mk.ukim.finki.draftcraft.domain.users.User;
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
