package rocks.gioac96.veronica.session.redis;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.session.cookie.CookieSessionStore;

public class RedisSessionStore<D> extends CookieSessionStore<D> {

    private final Jedis connection;
    private final String redisSessionKeyPrefix;
    private final SessionDataEncoder<D> encoder;
    private final SessionDataParser<D> parser;
    private final int expirationTime;

    public RedisSessionStore(RedisSessionStoreBuilder<D> b) {

        super(b);

        connection = b.connection;
        redisSessionKeyPrefix = b.redisSessionKeyPrefix;
        encoder = b.encoder;
        parser = b.parser;
        expirationTime = b.expirationTime;

    }

    private String getRedisSessionKey(UUID sessionKey) {

        return redisSessionKeyPrefix + sessionKey.toString();


    }

    private String getRedisSessionKey(Request request) {

        UUID sessionKey = getSessionKey(request);

        if (sessionKey == null) {

            return null;

        } else {

            return getRedisSessionKey(sessionKey);

        }

    }

    @Override
    public D getSessionData(Request request) {

        String redisSessionKey = getRedisSessionKey(request);

        if (redisSessionKey == null) {

            return null;

        }

        String encodedSessionData = connection.get(redisSessionKey);

        if (encodedSessionData == null) {

            return null;

        } else {

            connection.expire(redisSessionKey, expirationTime);

            return parser.parse(encodedSessionData);

        }

    }

    private UUID generateNewSessionKey() {

        UUID sessionKey;

        do {

            sessionKey = UUID.randomUUID();

        } while (connection.exists(getRedisSessionKey(sessionKey)));

        return sessionKey;

    }

    @Override
    public void setSessionData(Request request, Response.ResponseBuilder responseBuilder, D sessionData) {

        String encodedSessionData = encoder.encode(sessionData);

        String redisSessionKey = getRedisSessionKey(request);

        if (connection.ttl(redisSessionKey) <= 0) {

            // Session key has either expired or is invalid. Generating a new one.
            UUID sessionKey = generateNewSessionKey();
            storeSessionCookie(responseBuilder, sessionKey);

            redisSessionKey = getRedisSessionKey(sessionKey);

        }

        connection.set(redisSessionKey, encodedSessionData);
        connection.expire(redisSessionKey, expirationTime);

    }

    @Override
    public void clearSessionData(Request request) {

        String redisSessionKey = getRedisSessionKey(request);

        connection.del(redisSessionKey);

    }

    @Override
    public void clearAllSessions() {

        List<String> matchingKeys = new LinkedList<>();
        ScanParams params = new ScanParams();
        params.match(redisSessionKeyPrefix + "*");

        String nextCursor = "0";

        do {
            ScanResult<String> scanResult = connection.scan(nextCursor, params);
            List<String> keys = scanResult.getResult();
            nextCursor = scanResult.getCursor();

            matchingKeys.addAll(keys);

        } while (!nextCursor.equals("0"));

        if (matchingKeys.size() == 0) {
            return;
        }

        connection.del(matchingKeys.toArray(new String[0]));

    }

    public static class RedisSessionStoreBuilder<D> extends CookieSessionStoreBuilder<
        D,
        RedisSessionStore<D>,
        RedisSessionStoreBuilder<D>
        > {

        protected Jedis connection;
        protected String redisSessionKeyPrefix = "_vrs-";
        protected SessionDataEncoder<D> encoder;
        protected SessionDataParser<D> parser;
        protected int expirationTime = 3600;

        @Override
        protected boolean isValid() {

            return super.isValid()
                && connection != null
                && connection.isConnected()
                && redisSessionKeyPrefix != null
                && encoder != null
                && parser != null
                && expirationTime > 0;

        }

        @Override
        protected RedisSessionStoreBuilder<D> self() {
            return this;
        }

        @Override
        protected RedisSessionStore<D> instantiate() {
            return new RedisSessionStore<>(this);
        }

        public RedisSessionStoreBuilder<D> connection(@NonNull Jedis connection) {

            this.connection = connection;
            return this;

        }

        public RedisSessionStoreBuilder<D> connection(@NonNull Provider<Jedis> connectionProvider) {

            return connection(connectionProvider.provide());

        }

        public RedisSessionStoreBuilder<D> redisSessionKeyPrefix(@NonNull String redisSessionKeyPrefix) {

            this.redisSessionKeyPrefix = redisSessionKeyPrefix;
            return this;

        }

        public RedisSessionStoreBuilder<D> redisSessionKeyPrefix(
            @NonNull Provider<String> redisSessionKeyPrefixProvider
        ) {

            return redisSessionKeyPrefix(redisSessionKeyPrefixProvider.provide());

        }

        public RedisSessionStoreBuilder<D> encoder(@NonNull SessionDataEncoder<D> encoder) {

            this.encoder = encoder;
            return this;

        }

        public RedisSessionStoreBuilder<D> encoder(
            @NonNull Provider<SessionDataEncoder<D>> encoderProvider
        ) {

            return encoder(encoderProvider.provide());

        }

        public RedisSessionStoreBuilder<D> parser(@NonNull SessionDataParser<D> parser) {

            this.parser = parser;
            return this;

        }

        public RedisSessionStoreBuilder<D> parser(
            @NonNull Provider<SessionDataParser<D>> parserProvider
        ) {

            return parser(parserProvider.provide());

        }

        public RedisSessionStoreBuilder<D> expirationTime(int expirationTime) {

            this.expirationTime = expirationTime;
            return this;

        }

        public RedisSessionStoreBuilder<D> expirationTime(@NonNull Provider<Integer> expirationTimeProvider) {

            return expirationTime(expirationTimeProvider.provide());

        }

    }

}
