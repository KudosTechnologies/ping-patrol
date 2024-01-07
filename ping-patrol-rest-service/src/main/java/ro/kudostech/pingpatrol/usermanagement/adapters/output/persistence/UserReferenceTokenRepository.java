package ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence.model.UserReferenceTokenDbo;

import java.util.Optional;

@Repository
public interface UserReferenceTokenRepository extends JpaRepository<UserReferenceTokenDbo, Long> {

  Optional<UserReferenceTokenDbo> findByUserIdAndAllowedClientId(
      String userId, String allowedClientId);

  Optional<UserReferenceTokenDbo> findByToken(String referenceToken);

  boolean existsByToken(String token);

  boolean existsByAllowedClientId(String allowedClientId);

  void deleteByToken(String token);

  void deleteAllByAllowedClientId(String allowedClientId);

  void deleteAllByExpirationDateBefore(Example<UserReferenceTokenDbo> example);
}
