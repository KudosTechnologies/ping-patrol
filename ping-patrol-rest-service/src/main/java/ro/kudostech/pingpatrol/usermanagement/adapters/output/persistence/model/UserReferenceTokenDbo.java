package ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
    name = "user_reference_token",
    indexes = {
      @Index(name = "index_token", columnList = "token", unique = true),
      @Index(name = "index_user_id", columnList = "user_id"),
      @Index(name = "index_allowed_client_id", columnList = "allowed_client_id")
    })
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReferenceTokenDbo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @NotNull
  @Column(unique = true)
  private String token;

  @NotNull
  @Column(name = "user_id")
  private String userId;

  @NotNull
  @Column(name = "allowed_client_id")
  private String allowedClientId;

  private Instant expirationDate;
}
