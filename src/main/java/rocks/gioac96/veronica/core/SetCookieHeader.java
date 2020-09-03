package rocks.gioac96.veronica.core;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Used to generate Set-Cookie http headers. Supports all features from RFC 6265, section 4.1: Set-Cookie.
 * All features are explained here https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie.
 */
public final class SetCookieHeader {

    protected static final boolean DEFAULT_SECURE = false;
    protected static final boolean DEFAULT_HTTPONLY = false;

    private static final String INVALID_NAME_SPECIAL_CHARS = "()<>@,;:\\\"/[]?={}";
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss");

    @Getter
    @Setter
    private static Charset COOKIE_VALUE_CHARSET = StandardCharsets.UTF_8;

    @Getter
    @NonNull
    private String name;

    @Getter
    @Setter
    @NonNull
    private String value;

    @Getter
    @Setter
    private ZonedDateTime expires;

    @Getter
    @Setter
    private Long maxAge;

    @Getter
    @Setter
    private String domain;

    @Getter
    @Setter
    private String path;

    @Getter
    @Setter
    private Boolean secure;

    @Getter
    @Setter
    private Boolean httpOnly;

    @Getter
    @Setter
    private SameSitePolicy sameSite;

    protected SetCookieHeader(
        @NonNull String name,
        @NonNull String value,
        ZonedDateTime expires,
        Long maxAge,
        String domain,
        String path,
        Boolean secure,
        Boolean httpOnly,
        SameSitePolicy sameSite
    ) {

        validateName(name);
        this.name = name;

        this.value = value;
        this.maxAge = maxAge;
        this.expires = expires;
        this.domain = domain;
        this.path = path;
        this.secure = secure;
        this.httpOnly = httpOnly;
        this.sameSite = sameSite;

    }

    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "unused"})
    public static SetCookieHeaderBuilder builder() {

        return new SetCookieHeaderBuilder();

    }

    private static void validateName(String name) throws IllegalArgumentException {

        int len = name.length();

        if (name.length() == 0 || name.charAt(0) == '$') {

            throw new IllegalArgumentException("Illegal cookie name");

        }

        for (int i = 0; i < len; i++) {

            char c = name.charAt(i);

            if (c <= 0x20 || c >= 0x7f || INVALID_NAME_SPECIAL_CHARS.indexOf(c) != -1) {

                throw new IllegalArgumentException("Illegal cookie name");

            }

        }

    }

    private static String getExpiresString(ZonedDateTime expires) {

        return expires.withZoneSameInstant(ZoneOffset.UTC).format(timeFormatter) + " GMT";

    }

    /**
     * Validates and sets the cookie name.
     *
     * @param name name to validate and assign to cookie
     * @throws IllegalArgumentException on invalid cookie name
     */
    public void setName(String name) throws IllegalArgumentException {

        validateName(name);
        this.name = name;

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
    public static class SetCookieHeaderBuilder {

        private @NonNull String name;
        private @NonNull String value;
        private ZonedDateTime expires;
        private Long maxAge;
        private String domain;
        private String path;
        private Boolean secure;
        private Boolean httpOnly;
        private SameSitePolicy sameSite;

        SetCookieHeaderBuilder() {
        }

        public SetCookieHeaderBuilder name(@NonNull String name) {

            validateName(name);
            this.name = name;
            return this;

        }

        public SetCookieHeaderBuilder value(@NonNull String value) {

            this.value = value;
            return this;

        }

        public SetCookieHeaderBuilder expires(ZonedDateTime expires) {

            this.expires = expires;
            return this;

        }

        public SetCookieHeaderBuilder maxAge(Long maxAge) {

            this.maxAge = maxAge;
            return this;

        }

        public SetCookieHeaderBuilder domain(String domain) {

            this.domain = domain;
            return this;

        }

        public SetCookieHeaderBuilder path(String path) {

            this.path = path;
            return this;

        }

        public SetCookieHeaderBuilder secure(Boolean secure) {

            this.secure = secure;
            return this;

        }

        public SetCookieHeaderBuilder secure() {

            this.secure = true;
            return this;

        }

        public SetCookieHeaderBuilder httpOnly(Boolean httpOnly) {

            this.httpOnly = httpOnly;
            return this;

        }

        public SetCookieHeaderBuilder httpOnly() {

            this.httpOnly = true;
            return this;

        }

        public SetCookieHeaderBuilder sameSite(SameSitePolicy sameSite) {

            this.sameSite = sameSite;
            return this;

        }

        public SetCookieHeader build() {

            return new SetCookieHeader(name, value, expires, maxAge, domain, path, secure, httpOnly, sameSite);

        }

    }

}
