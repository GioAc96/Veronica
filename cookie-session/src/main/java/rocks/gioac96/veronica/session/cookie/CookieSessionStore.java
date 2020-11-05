package rocks.gioac96.veronica.session.cookie;

import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.core.Request;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.core.SetCookieHeader;
import rocks.gioac96.veronica.core.providers.ConfigurableProvider;
import rocks.gioac96.veronica.core.providers.Provider;
import rocks.gioac96.veronica.session.SessionStore;

public abstract class CookieSessionStore<D> implements SessionStore<D> {

    @Getter
    protected final String cookieName;

    protected CookieSessionStore(CookieSessionStoreBuilder<D, ?, ?> b) {

        this.cookieName = b.cookieName;

    }

    protected UUID getSessionKey(Request request) {

        String sessionId = request.getCookie().get(cookieName);

        if (sessionId == null) {
            return null;
        }

        return UUID.fromString(sessionId);

    }

    protected void storeSessionCookie(Response.ResponseBuilder responseBuilder, UUID sessionKey) {

        responseBuilder.cookie(generateSessionKeyCookie(sessionKey));

    }

    private SetCookieHeader generateSessionKeyCookie(UUID sessionKey) {

        return SetCookieHeader.builder()
            .httpOnly()
            .secure()
            .name(cookieName)
            .value(sessionKey.toString())
            .provide();

    }

    protected static abstract class CookieSessionStoreBuilder<
        D,
        C extends CookieSessionStore<D>,
        B extends CookieSessionStoreBuilder<D, C, B>
        > extends ConfigurableProvider<C> {

        protected String cookieName = "_vs";

        protected abstract B self();

        @Override
        protected boolean isValid() {

            return SetCookieHeader.isNameValid(cookieName);

        }

        public B cookieName(@NonNull String cookieName) {

            this.cookieName = cookieName;
            return self();

        }

        public B cookieName(@NonNull Provider<String> cookieNameProvider) {

            return cookieName(cookieNameProvider.provide());

        }

    }
}
