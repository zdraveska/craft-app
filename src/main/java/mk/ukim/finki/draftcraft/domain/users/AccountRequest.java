package mk.ukim.finki.draftcraft.domain.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account_request")
public class AccountRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "name")
  String name;

  @Column(name = "surname")
  String surname;

  @Column(name = "username")
  String username;

  @Column(name = "email")
  String email;

  @Column(name = "role")
  UserRole role;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  AccountRequestStatus status;

  @Column(name = "email_confirmed")
  Boolean emailConfirmed;

  @Column(name = "created_date")
  LocalDate createdDate;
}