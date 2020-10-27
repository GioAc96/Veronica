package rocks.gioac96.veronica.session.application;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.core.util.Tuple;
import rocks.gioac96.veronica.session.cookie.CookieSessionStore;

public class ApplicationSessionStore<D> extends CookieSessionStore<D> {

    private final SessionEntries<D> sessionEntries;

    protected ApplicationSessionStore(ApplicationSessionStorageBuilder<D> b) {

        super(b);

        this.sessionEntries = new SessionEntries<>(b.expirationTime);

        Executors.newSingleThreadScheduledExecutor()
            .scheduleAtFixedRate(
                sessionEntries::clearExpiredEntries,
                b.expirationTime / 2 + 1,
                b.expirationTime,
                TimeUnit.SECONDS
            );


    }


    public static <D> ApplicationSessionStorageBuilder<D> builder() {

        return new ApplicationSessionStorageBuilder<>();

    }

    public long getExpirationTime() {

        return sessionEntries.expirationTime;

    }

    @Override
    public void clearAllSessions() {

        sessionEntries.clearAll();

    }

    @Override
    public D getSessionData(@NonNull Request request) {

        UUID sessionKey = getSessionKey(request);

        if (sessionKey == null) {

            return null;

        } else {

            return sessionEntries.getData(sessionKey);

        }

    }

    @Override
    public void setSessionData(
        Request request,
        Response.ResponseBuilder responseBuilder,
        D sessionData
    ) {

        UUID sessionKey = getSessionKey(request);

        if (sessionKey == null) {

            // Request does not have a session key. Generating key and entry
            sessionKey = sessionEntries.generateSessionKey();
            storeSessionCookie(responseBuilder, sessionKey);

        } else if (sessionEntries.isValidSession(sessionKey)) {

            // Session key is invalid or expired, updating
            sessionKey = sessionEntries.generateSessionKey();

            // Storing new session key
            storeSessionCookie(responseBuilder, sessionKey);

        }

        sessionEntries.setData(sessionKey, sessionData);

    }

    @Override
    public void clearSessionData(Request request) {

        UUID sessionKey = getSessionKey(request);

        sessionEntries.clearSessionData(sessionKey);

    }

    public int getStoredSessionsCount() {

        return sessionEntries.entries.size();

    }

    private final static class SessionEntries<D> {

        private final LinkedHashMap<UUID, Tuple<LocalDateTime, D>> entries = new LinkedHashMap<>();
        private final long expirationTime;

        public SessionEntries(long expirationTime) {

            this.expirationTime = expirationTime;

        }

        synchronized void clearAll() {

            entries.clear();

        }

        synchronized void clearExpiredEntries() {

            Iterator<Map.Entry<UUID, Tuple<LocalDateTime, D>>> iterator = entries.entrySet().iterator();

            while (iterator.hasNext()) {

                Map.Entry<UUID, Tuple<LocalDateTime, D>> next = iterator.next();

                if (next.getValue().getFirst().isBefore(LocalDateTime.now())) {

                    iterator.remove();

                } else {

                    break;

                }

            }

        }

        synchronized D getData(UUID id) {

            Tuple<LocalDateTime, D> entry = entries.get(id);

            if (entry == null) {

                return null;

            }

            if (entry.getFirst().isBefore(LocalDateTime.now())) {

                return null;

            }

            return entry.getSecond();

        }

        synchronized void setData(UUID id, D data) {

            entries.remove(id);

            entries.put(id, new Tuple<>(LocalDateTime.now().plusSeconds(expirationTime), data));

        }

        UUID generateSessionKey() {

            UUID sessionKey;

            do {

                sessionKey = UUID.randomUUID();

            } while (entries.containsKey(sessionKey));

            return sessionKey;

        }

        boolean isValidSession(UUID sessionKey) {

            Tuple<LocalDateTime, D> entry = entries.get(sessionKey);

            if (entry == null) {

                return false;

            }

            return !entry.getFirst().isBefore(LocalDateTime.now());

        }

        void clearSessionData(UUID sessionKey) {

            entries.remove(sessionKey);

        }

    }

    public static class ApplicationSessionStorageBuilder<D>
        extends CookieSessionStoreBuilder<D, ApplicationSessionStore<D>, ApplicationSessionStorageBuilder<D>> {

        protected long expirationTime = 3600;

        @Override
        protected ApplicationSessionStorageBuilder<D> self() {

            return this;

        }

        @Override
        protected boolean isValid() {

            return super.isValid()
                && expirationTime > 0;

        }

        @Override
        protected ApplicationSessionStore<D> instantiate() {

            return new ApplicationSessionStore<>(this);

        }

        public ApplicationSessionStorageBuilder<D> expirationTime(long expirationTime) {

            this.expirationTime = expirationTime;
            return this;

        }

        public ApplicationSessionStorageBuilder<D> expirationTime(@NonNull Provider<Long> expirationTimeProvider) {

            return expirationTime(expirationTimeProvider.provide());

        }

    }

}
