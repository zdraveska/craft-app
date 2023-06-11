package mk.ukim.finki.draftcraft.domain.shop;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.draftcraft.domain.common.Address;
import mk.ukim.finki.draftcraft.domain.common.Image;
import mk.ukim.finki.draftcraft.domain.users.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String description;

    @Enumerated(value = EnumType.STRING)
    ShopCategory shopCategory;
    @OneToMany
    List<Address> addresses;
    //https://stackoverflow.com/a/57241586
    @ElementCollection
    @Column(name = "phoneNumbers")
    List<String> phoneNumbers;
    @ManyToMany
    @Column(name = "employees")
    List<User> employees;
    @OneToMany
    List<Product> products;
    @OneToOne
    Image image;

}
