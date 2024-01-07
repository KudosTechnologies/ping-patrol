package ro.kudostech.pingpatrol.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.lang.JoseException;
import ro.kudostech.pingpatrol.configuration.WireMockConfigurations;

public final class TokenUtils {
  public static final String KEYCLOAK_REALM = "pingpatrol";
  public static final String JWT_RESOURCE_ID = "pingpatrol-webapp";
  public static final String ROLE_USER = "user";
  public static final String ROLE_ADMIN = "admin";

  private static final RsaJsonWebKey rsaJsonWebKey;

  static {
    try {
      rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
    } catch (JoseException e) {
      throw new RuntimeException(e);
    }
  }

  private TokenUtils() {}

  /** Generates a JSON Web Key (JWK) that can be used for signing and verification of the JWT. */
  public static RsaJsonWebKey generateRsaJsonWebKey() {
    rsaJsonWebKey.setKeyId("k1");
    rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
    rsaJsonWebKey.setUse("sig");
    return rsaJsonWebKey;
  }

  public static String generateUserJWT() {
    return generateJWT(createClaims(List.of(ROLE_USER)));
  }

  public static String generateAdminJWT() {
    return generateJWT(createClaims(List.of(ROLE_USER, ROLE_ADMIN)));
  }

  private static JwtClaims createClaims(List<String> roles) {
    // Create the Claims, which will be the content of the JWT
    JwtClaims claims = new JwtClaims();
    claims.setJwtId(UUID.randomUUID().toString()); // a unique identifier for the token
    claims.setExpirationTimeMinutesInTheFuture(
        10); // time when the token will expire (10 minutes from now)
    claims.setNotBeforeMinutesInThePast(
        0); // time before which the token is not yet valid (2 minutes ago)
    claims.setIssuedAtToNow(); // when the token was issued/created (now)
    claims.setAudience("account"); // to whom this token is intended to be sent
    claims.setIssuer(
        String.format(
            "%s/realms/%s",
            WireMockConfigurations.keycloakBaseUrl,
            KEYCLOAK_REALM)); // who creates the token and signs it
    claims.setSubject(
        UUID.randomUUID().toString()); // the subject/principal is whom the token is about
    claims.setClaim("typ", "Bearer"); // set type of token
    claims.setClaim(
        "azp", "example-client-id"); // Authorized party  (the party to which this token was issued)
    claims.setClaim(
        "auth_time",
        NumericDate.fromMilliseconds(Instant.now().minus(11, ChronoUnit.SECONDS).toEpochMilli())
            .getValue()); // time when authentication occured
    claims.setClaim("session_state", UUID.randomUUID().toString()); // keycloak specific ???
    claims.setClaim("acr", "0"); // Authentication context class
    claims.setClaim(
        "realm_access",
        Map.of("roles", List.of("offline_access", "uma_authorization", "user"))); // keycloak roles
    claims.setClaim(
        "resource_access", Map.of(JWT_RESOURCE_ID, Map.of("roles", roles))); // keycloak roles
    claims.setClaim("scope", "profile email");
    claims.setClaim(
        "name", "John Doe"); // additional claims/attributes about the subject can be added
    claims.setClaim("email_verified", true);
    claims.setClaim("preferred_username", "doe.john");
    claims.setClaim("given_name", "John");
    claims.setClaim("family_name", "Doe");
    return claims;
  }

  /** Generate JWT Token. */
  private static String generateJWT(JwtClaims claims) {

    // A JWT is a JWS and/or a JWE with JSON claims as the payload.
    // In this example it is a JWS so we create a JsonWebSignature object.
    JsonWebSignature jws = new JsonWebSignature();

    // The payload of the JWS is JSON content of the JWT Claims
    jws.setPayload(claims.toJson());

    // The JWT is signed using the private key
    jws.setKey(rsaJsonWebKey.getPrivateKey());

    // Set the Key ID (kid) header because it's just the polite thing to do.
    // We only have one key in this example but a using a Key ID helps
    // facilitate a smooth key rollover process
    jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

    // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

    // set the type header
    jws.setHeader("typ", "JWT");

    // Sign the JWS and produce the compact serialization or the complete JWT/JWS
    // representation, which is a string consisting of three dot ('.') separated
    // base64url-encoded parts in the form Header.Payload.Signature
    try {
      return jws.getCompactSerialization();
    } catch (JoseException e) {
      throw new RuntimeException(e);
    }
  }
}
