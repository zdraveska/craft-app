package mk.ukim.finki.draftcraft.domain.model.common;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "image")
    byte[] image;

    @Column(name = "image_content_type")
    String imageContentType;

    @Column(name = "image_name")
    String imageName;
}
