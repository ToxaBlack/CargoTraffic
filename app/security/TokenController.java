package security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import models.User;
import org.apache.commons.lang3.StringUtils;
import play.Logger;
import play.Play;
import play.mvc.Http;
import service.UserService;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Anton Chernov on 1/4/2016.
 */
public class TokenController {
    private static final Logger.ALogger LOGGER = Logger.of(TokenController.class);
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;
    private static final String KEY = Play.application().configuration().getString("play.crypto.secret");
    private static final int tokenLive = 3600;
    private static final String COOKIE_NAME = "access_token";


    public static void setToken(User user, String host, Http.Response response) {
        LOGGER.debug("Set cookie token for user = {} id = {}", user.username, user.id);
        response.setCookie(
                COOKIE_NAME,
                createToken(user, host),
                tokenLive
        );
    }

    public static void removeToken(Http.Response response) {
        LOGGER.debug("Remove token");
        response.discardCookie(COOKIE_NAME);
    }

    private static String createToken(User user, String host) {
        LOGGER.debug("Create token for user = {} id = {}", user.username, user.id);
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, ALGORITHM.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(user.id.toString())
                .setIssuedAt(now)
                .setSubject(user.username)
                .setIssuer(host)
                .signWith(ALGORITHM, signingKey);


        long expMillis = nowMillis + tokenLive * 100000;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }

    public static boolean validateToken(Http.Context context) throws Throwable {
        Http.Cookie cookie = context.request().cookie(COOKIE_NAME);

        if (Objects.isNull(cookie)) {
            LOGGER.debug("Token cookie is null");
            return false;
        }

        String token = cookie.value();
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(KEY))
                .parseClaimsJws(token).getBody();

        User user = (new UserService()).find(Long.parseLong(claims.getId()));

        if (!Objects.isNull(user)) context.args.put("user", user);
        else return false;
        LOGGER.debug("Validate token for user = {} id = {} roles = {}", user.username, user.id, StringUtils.join(user.getRoles()));

        Date exp = claims.getExpiration();
        long nowMillis = System.currentTimeMillis();
        Long expMillis = exp.getTime();
        return ((expMillis - nowMillis > 0) &&
                StringUtils.equals(user.username, claims.getSubject()) &&
                StringUtils.equals(user.id.toString(), claims.getId()));
    }
}
