package mk.ukim.finki.draftcraft.domain.model.user;

import jakarta.persistence.*;
import lombok.*;
import mk.ukim.finki.draftcraft.domain.model.common.Image;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "userss")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    Name name;

    @Column(unique = true, name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "phoneNumber")
    String phoneNumber;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    UserRole role;

    @PrimaryKeyJoinColumn
    @OneToOne
    Image image;

    private final boolean isAccountNonExpired = true;
    private final boolean isAccountNonLocked = true;
    private final boolean isCredentialsNonExpired = true;
    private final boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
