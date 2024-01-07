package ro.kudostech.pingpatrol.common.auth;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@UtilityClass
public class TokenBasedAccessUtils {

    private static final String AZP_CLAIM = "azp";

    boolean isTechnicalClient() {
        return true;
    }

    public String getClientId(Authentication authentication) {
        return ((JwtAuthenticationToken) authentication)
                .getToken()
                .getClaims()
                .get(AZP_CLAIM)
                .toString();
    }

}
