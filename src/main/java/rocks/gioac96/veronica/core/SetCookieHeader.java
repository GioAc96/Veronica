package rocks.gioac96.veronica.core;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Used to generate Set-Cookie http headers. Supports all features from RFC 6265, section 4.1: Set-Cookie.
 * All features are explained here https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie.
 */
@Getter
public final class SetCookieHeader {

    private static final String INVALID_NAME_SPECIAL_CHARS = "()<>@,;:\\\"/[]?={}";

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss");
    private static final Charset COOKIE_VALUE_CHARSET = StandardCharsets.UTF_8;
    private final String name;
    private final String value;
    private final ZonedDateTime expires;
    private final Long maxAge;
    private final String domain;
    private final String path;
    private final Boolean secure;
    private final Boolean httpOnly;
    private final SameSitePolicy sameSite;

    protected SetCookieHeader(
        SetCookieHeaderBuilder b
    ) {

        this.name = b.name;
        this.value = b.value;
        this.maxAge = b.maxAge;
        this.expires = b.expires;
        this.domain = b.domain;
        this.path = b.path;
        this.secure = b.secure;
        this.httpOnly = b.httpOnly;
        this.sameSite = b.sameSite;

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod"})
    public static SetCookieHeaderBuilder builder() {

        return new SetCookieHeaderBuilder();

    }

    private static boolean isNameValid(String name) {

        if (name == null) {

            return false;

        }

        int len = name.length();

        if (name.length() == 0 || name.charAt(0) == '$') {

            return false;

        }

        for (int i = 0; i < len; i++) {

            char c = name.charAt(i);

            if (c <= 0x20 || c >= 0x7f || INVALID_NAME_SPECIAL_CHARS.indexOf(c) != -1) {

                return false;

            }

        }

        return true;

    }

    private static String getExpiresString(ZonedDateTime expires) {

        return expires.withZoneSameInstant(ZoneOffset.UTC).format(timeFormatter) + " GMT";

    }

    /**
     * Generates a Set-Cookie header string.
     *
     * @return the Set-Cookie header string
     */
    public String toHeaderString() {

        StringBuilder stringBuilder = new StringBuilder();

        String encodedValue = URLEncoder.encode(value, COOKIE_VALUE_CHARSET);

        stringBuilder.append(name);
        stringBuilder.append("=");
        stringBuilder.append(encodedValue);

        if (expires != null) {

            stringBuilder.append("; Expires=");
            stringBuilder.append(getExpiresString(expires));

        }

        if (maxAge != null) {

            stringBuilder.append("; Max-Age=");
            stringBuilder.append(maxAge);

        }

        if (domain != null) {

            stringBuilder.append("; Domain=");
            stringBuilder.append(domain);

        }

        if (path != null) {

            stringBuilder.append("; Path=");
            stringBuilder.append(path);

        }

        if (secure != null && secure) {

            stringBuilder.append("; Secure");

        }

        if (httpOnly != null && httpOnly) {

            stringBuilder.append("; HttpOnly");

        }

        if (sameSite != null) {

            stringBuilder.append("; SameSite=");
            stringBuilder.append(sameSite.getValue());

        }

        return stringBuilder.toString();

    }

    /**
     * Cookie same site policies.
     */
    public enum SameSitePolicy {

        STRICT("Strict"),
        LAX("Lax"),
        NONE("None");

        @Getter
        private final String value;

        SameSitePolicy(String value) {

            this.value = value;

        }

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType", "unused"})
    public static class SetCookieHeaderBuilder extends ConfigurableProvider<SetCookieHeader> {

        private String name;
        private String value;
        private ZonedDateTime expires = null;
        private Long maxAge = null;
        private String domain = null;
        private String path = null;
        private Boolean secure = null;
        private Boolean httpOnly = null;
        private SameSitePolicy sameSite = null;

        SetCookieHeaderBuilder() {
        }

        public SetCookieHeaderBuilder name(@NonNull String name) {

            this.name = name;
            return this;

        }

        public SetCookieHeaderBuilder name(@NonNull Provider<String> name) {

            return name(name.provide());

        }

        public SetCookieHeaderBuilder value(@NonNull String value) {

            this.value = value;
            return this;

        }

        public SetCookieHeaderBuilder value(@NonNull Provider<String> value) {

            return value(value.provide());

        }


        public SetCookieHeaderBuilder expires(ZonedDateTime expires) {

            this.expires = expires;
            return this;

        }

        public SetCookieHeaderBuilder expires(@NonNull Provider<ZonedDateTime> expires) {

            return expires(expires.provide());

        }


        public SetCookieHeaderBuilder maxAge(Long maxAge) {

            this.maxAge = maxAge;
            return this;

        }

        public SetCookieHeaderBuilder maxAge(@NonNull Provider<Long> maxAge) {

            return maxAge(maxAge.provide());

        }


        public SetCookieHeaderBuilder domain(String domain) {

            this.domain = domain;
            return this;

        }

        public SetCookieHeaderBuilder domain(@NonNull Provider<String> domain) {

            return domain(domain.provide());

        }


        public SetCookieHeaderBuilder path(String path) {

            this.path = path;
            return this;

        }

        public SetCookieHeaderBuilder secure(Boolean secure) {

            this.secure = secure;
            return this;

        }

        public SetCookieHeaderBuilder secure(@NonNull Provider<Boolean> secure) {

            return secure(secure.provide());

        }


        public SetCookieHeaderBuilder secure() {

            this.secure = true;
            return this;

        }

        public SetCookieHeaderBuilder httpOnly(Boolean httpOnly) {

            this.httpOnly = httpOnly;
            return this;

        }

        public SetCookieHeaderBuilder httpOnly(@NonNull Provider<Boolean> httpOnly) {

            return httpOnly(httpOnly.provide());

        }


        public SetCookieHeaderBuilder httpOnly() {

            this.httpOnly = true;
            return this;

        }

        public SetCookieHeaderBuilder sameSite(SameSitePolicy sameSite) {

            this.sameSite = sameSite;
            return this;

        }

        public SetCookieHeaderBuilder sameSite(@NonNull Provider<SameSitePolicy> sameSite) {

            return sameSite(sameSite.provide());

        }


        @Override
        protected boolean isValid() {

            return isNameValid(name)
                && value != null;

        }

        @Override
        protected SetCookieHeader instantiate() {

            return new SetCookieHeader(
                this
            );

        }

    }

}
