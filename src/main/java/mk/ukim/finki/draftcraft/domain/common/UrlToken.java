package mk.ukim.finki.draftcraft.domain.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.draftcraft.domain.users.AccountRequest;
import mk.ukim.finki.draftcraft.domain.users.User;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "url_token")
public class UrlToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "token")
  private String token;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false, name = "expiration")
  private LocalDateTime expiration;

  @OneToOne
  @JoinColumn(name = "account_request_id")
  private AccountRequest accountRequest;

  public void setExpiration(int minutes) {
    this.expiration = LocalDateTime.now().plusMinutes(minutes);
  }

  public boolean isValid() {
    return expiration.isAfter(LocalDateTime.now());
  }

}