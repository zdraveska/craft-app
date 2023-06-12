package mk.ukim.finki.draftcraft.domain.model.common;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String city;

    String address;

    Double latitude;

    Double longitude;

    String mapUrl;
}
