package rocks.gioac96.veronica.session.application;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.session.cookie.CookieSessionStore;

public class ApplicationSessionStore<D> extends CookieSessionStore<D> {

    private final Map<UUID, SessionEntry<D>> entries = new HashMap<>();

    @Getter
    private final long expirationTime;

    protected ApplicationSessionStore(ApplicationSessionStorageBuilder<D> b) {

        super(b);

        this.expirationTime = b.expirationTime;

    }

    public static <D> ApplicationSessionStorageBuilder<D> builder() {

        return new ApplicationSessionStorageBuilder<>();

    }

    synchronized public boolean clearExpiredSessions() {

        int initialSize = entries.size();

        entries.entrySet().removeIf(entry -> entry.getValue().hasExpired());

        return entries.size() < initialSize;

    }

    @Override
    synchronized public boolean clearAllSessions() {

        if (entries.isEmpty()) {

            return false;

        } else {

            entries.clear();

            return true;

        }

    }

    synchronized private SessionEntry<D> getSessionEntryCheckingExpiration(UUID sessionKey) {

        SessionEntry<D> sessionEntry = entries.get(sessionKey);

        if (sessionEntry == null) {

            return null;

        }

        if (sessionEntry.hasExpired()) {

            entries.remove(sessionKey);
            return null;

        }

        return sessionEntry;

    }

    private SessionEntry<D> getSessionEntryCheckingExpiration(Request request) {

        UUID sessionKey = getSessionKey(request);

        if (sessionKey == null) {

            return null;

        } else {

            return getSessionEntryCheckingExpiration(sessionKey);

        }


    }

    @Override
    public D getSessionData(@NonNull Request request) {

        SessionEntry<D> sessionEntry = getSessionEntryCheckingExpiration(request);

        if (sessionEntry == null) {

            return null;

        } else {

            return sessionEntry.sessionData;

        }

    }

    private UUID generateNewSessionKey() {

        UUID sessionKey;

        do {

            sessionKey = UUID.randomUUID();

        } while (entries.containsKey(sessionKey));

        return sessionKey;

    }

    private SessionEntry<D> generateNewSessionEntry(
        D data
    ) {

        return new SessionEntry<>(
            LocalDateTime.now().plusSeconds(expirationTime),
            data
        );

    }

    synchronized private void storeNewSession(Response.ResponseBuilder responseBuilder, D sessionData) {

        UUID sessionKey = generateNewSessionKey();
        SessionEntry<D> sessionEntry = generateNewSessionEntry(sessionData);

        storeSessionCookie(responseBuilder, sessionKey);
        entries.put(sessionKey, sessionEntry);

    }

    synchronized private void updateSessionEntry(SessionEntry<D> sessionEntry, D sessionData) {

        sessionEntry.sessionData = sessionData;
        sessionEntry.expiresAt = LocalDateTime.now().plusSeconds(expirationTime);

    }

    @Override
    public void setSessionData(
        Request request,
        Response.ResponseBuilder responseBuilder,
        D sessionData
    ) {

        UUID sessionKey = getSessionKey(request);
        SessionEntry<D> sessionEntry;

        if (sessionKey == null) {

            // Request does not have a session key. Generating key and entry
            storeNewSession(responseBuilder, sessionData);

        } else {

            // Request has a session key.
            sessionEntry = getSessionEntryCheckingExpiration(sessionKey);

            if (sessionEntry == null) {

                // Session key is invalid or expired
                storeNewSession(responseBuilder, sessionData);

            } else {

                // Session key is valid, updating
                updateSessionEntry(sessionEntry, sessionData);

            }

        }

    }

    @Override
    synchronized public boolean clearSessionData(Request request) {

        UUID sessionKey = getSessionKey(request);

        if (sessionKey == null) {

            return false;

        }

        return entries.remove(sessionKey) != null;

    }

    @AllArgsConstructor
    private static class SessionEntry<D> {

        private LocalDateTime expiresAt;

        private D sessionData;

        public boolean hasExpired() {

            return expiresAt == null || expiresAt.isBefore(LocalDateTime.now());

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
