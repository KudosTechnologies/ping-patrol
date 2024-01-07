package ro.kudostech.pingpatrol.step;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Map;

//@Data
public class TokenSession {
    private final String userToken;
    private final JWTCreator.Builder jwtBuilder;
    private final Algorithm signingSecret;
    private String token;

    public TokenSession() {

        String secret = "my-secret-key";
        jwtBuilder = JWT.create();
        signingSecret = Algorithm.HMAC256(secret);

        userToken =
                convertClaimMapToToken(technicalTokenClaims, defaultValuesGeneric);
    }

    private final Map<String, Object> defaultValuesGeneric = Map.of(
            "azp", "monolith",
            "company_id", "18",
            "iss", "https://example.com/user-service/auth/realms/generic"
    );

    private final Map<String, Object> technicalTokenClaims = Map.of(
            "iat", 1626446812,
            "exp", 1941806812
    );

    private String convertClaimMapToToken(Map<String, Object> claims, Map<String, Object> defaultClaims) {
        return "Bearer " + jwtBuilder
                .withPayload(defaultClaims)
                .withPayload(claims)
                .sign(signingSecret);
    }

    public String getUserToken() {
        return userToken;
    }

}
