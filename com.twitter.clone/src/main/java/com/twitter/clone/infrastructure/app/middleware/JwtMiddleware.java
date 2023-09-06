package com.twitter.clone.infrastructure.app.middleware;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.twitter.clone.infrastructure.commen.exceptions.UnauthorizedException;
import com.twitter.clone.infrastructure.model.ConfigurationRecords;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import javalinjwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor(onConstructor = @__({@Inject}))
public class JwtMiddleware implements Handler {

    private final ConfigurationRecords.CookieConfig cookieConfig;
    private final JWTProvider jwtProvider;

    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/twitter-clone/authentication/",
            "/img/"
    );

    @Override
    public void handle(@NotNull Context context) throws Exception {
        if (EXCLUDED_PATHS.stream().anyMatch(context.path()::contains)) {
            return;
        }
        String jwtToken = context.cookie(cookieConfig.CookieName());

        if (jwtToken == null || !isValid(context, jwtToken)) {
            throw new UnauthorizedException();
        }
    }

    private boolean isValid(Context context, String jwtToken) {
        Optional<DecodedJWT> decodedJWT = jwtProvider.validateToken(jwtToken);
        if (!decodedJWT.isPresent() || decodedJWT.isEmpty()) {
            return false;
        }
        setCookieClaimsToContextAttribute(context, decodedJWT);
        return true;
    }

    private static void setCookieClaimsToContextAttribute(Context context, Optional<DecodedJWT> decodedJWT) {
        Map<String, Claim> claims = decodedJWT.get().getClaims();
        for (Map.Entry<String, Claim> entry : claims.entrySet()) {
            String claimName = entry.getKey();
            Claim claim = entry.getValue();
            String claimValue = claim.asString();
            context.attribute(claimName, claimValue);
        }
    }
}
