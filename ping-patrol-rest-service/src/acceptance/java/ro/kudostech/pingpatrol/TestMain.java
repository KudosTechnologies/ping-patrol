package ro.kudostech.pingpatrol;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import java.security.NoSuchAlgorithmException;

public class TestMain {
    public static String secret = "dummy-secret";
  public static void main(String[] args) throws NoSuchAlgorithmException {

    System.out.println(generateToken());
    System.out.println("\n*******");
    System.out.println(generateJwKSet());
  }

  public static String generateToken() {
    Algorithm algorithmHS = Algorithm.HMAC256(secret);

      return JWT.create()
          .withIssuer("yourIssuer")
          .withClaim("name", "John Doe")
          .withClaim("admin", true)
          .sign(algorithmHS);
  }

  public static String generateJwKSet() {
    JWK jwk =
        new OctetSequenceKey.Builder(secret.getBytes())
            .keyUse(KeyUse.SIGNATURE)
            .algorithm(JWSAlgorithm.HS256)
            .keyID("123")
            .build();

    JWKSet jwkSet = new JWKSet(jwk);
    return jwkSet.getKeys().toString();
  }
}
