package ro.kudostech.pingpatrol.usermanagement.domain.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence.UserReferenceTokenRepository;
import ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence.model.UserReferenceTokenDbo;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReferenceTokenService {

  @Value("${user-reference-token.lifespan-in-minutes}")
  private long tokenLifespanInMinutes = 60L;

  private final UserReferenceTokenRepository userReferenceTokenRepository;

  @Transactional
  public String getUserReferenceTokenForClient(String userId, String clientId) {
    Optional<UserReferenceTokenDbo> existingToken =
        userReferenceTokenRepository.findByUserIdAndAllowedClientId(userId, clientId);

    if (existingToken.isPresent()) {
      if (isTokenExpired(existingToken.get())) {
        userReferenceTokenRepository.delete(existingToken.get());
        return createNewUserReferenceToken(userId, clientId);
      } else {
        return existingToken.get().getToken();
      }
    }
    return createNewUserReferenceToken(userId, clientId);
  }

  public String getUserIdByReferenceToken(String referenceToken, String clientId) {
    UserReferenceTokenDbo existingToken =
        userReferenceTokenRepository.findByToken(referenceToken)
            .orElseThrow(() -> new IllegalArgumentException("Invalid reference token"));

    if (isTokenExpired(existingToken)) {
      throw new IllegalArgumentException("Reference token expired");
    }

    if (!existingToken.getAllowedClientId().equals(clientId)) {
      throw new IllegalArgumentException("Invalid client id");
    }

    return existingToken.getUserId();
  }

  private String createNewUserReferenceToken(String userId, String clientId) {
    final String token = generateToken();
    final Instant expirationDate = Instant.now().plus(tokenLifespanInMinutes, ChronoUnit.MINUTES);

    if (StringUtils.isBlank(clientId)) {
      throw new IllegalArgumentException("UserReferenceTokenRepository: Client id cannot be empty");
    }

    UserReferenceTokenDbo userReferenceTokenDbo =
        UserReferenceTokenDbo.builder()
            .token(token)
            .userId(userId)
            .allowedClientId(clientId)
            .expirationDate(expirationDate)
            .build();
    userReferenceTokenRepository.save(userReferenceTokenDbo);

    return token;
  }

  private String generateToken() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[24];
    random.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private boolean isTokenExpired(UserReferenceTokenDbo token) {
    return token.getExpirationDate().isBefore(Instant.now());
  }
}
