package rocks.gioac96.veronica;

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
    private final boolean secure;
    private final boolean httpOnly;
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

        if (secure) {

            stringBuilder.append("; Secure");

        }

        if (httpOnly) {

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

        protected String name;
        protected String value;
        protected ZonedDateTime expires;
        protected Long maxAge;
        protected String domain;
        protected String path;
        protected boolean secure = false;
        protected boolean httpOnly = false;
        protected SameSitePolicy sameSite;

        SetCookieHeaderBuilder() {
        }

        public SetCookieHeaderBuilder name(@NonNull String name) {

            this.name = name;
            return this;

        }

        public SetCookieHeaderBuilder name(@NonNull Provider<String> nameProvider) {

            return name(nameProvider.provide());

        }

        public SetCookieHeaderBuilder value(@NonNull String value) {

            this.value = value;
            return this;

        }

        public SetCookieHeaderBuilder value(@NonNull Provider<String> valueProvider) {

            return value(valueProvider.provide());

        }


        public SetCookieHeaderBuilder expires(ZonedDateTime expires) {

            this.expires = expires;
            return this;

        }

        public SetCookieHeaderBuilder expires(@NonNull Provider<ZonedDateTime> expiresProvider) {

            return expires(expiresProvider.provide());

        }


        public SetCookieHeaderBuilder maxAge(Long maxAge) {

            this.maxAge = maxAge;
            return this;

        }

        public SetCookieHeaderBuilder maxAge(@NonNull Provider<Long> maxAgeProvider) {

            return maxAge(maxAgeProvider.provide());

        }


        public SetCookieHeaderBuilder domain(String domain) {

            this.domain = domain;
            return this;

        }

        public SetCookieHeaderBuilder domain(@NonNull Provider<String> domainProvider) {

            return domain(domainProvider.provide());

        }

        public SetCookieHeaderBuilder path(String path) {

            this.path = path;
            return this;

        }

        public SetCookieHeaderBuilder path(Provider<String> pathProvider) {

            return path(pathProvider.provide());

        }

        public SetCookieHeaderBuilder secure(boolean secure) {

            this.secure = secure;
            return this;

        }

        public SetCookieHeaderBuilder secure() {

            this.secure = true;
            return this;

        }

        public SetCookieHeaderBuilder secure(@NonNull Provider<Boolean> secureProvider) {

            return secure(secureProvider.provide());

        }

        public SetCookieHeaderBuilder httpOnly(Boolean httpOnly) {

            this.httpOnly = httpOnly;
            return this;

        }

        public SetCookieHeaderBuilder httpOnly() {

            this.httpOnly = true;
            return this;

        }

        public SetCookieHeaderBuilder httpOnly(@NonNull Provider<Boolean> httpOnlyProvider) {

            return httpOnly(httpOnlyProvider.provide());

        }


        public SetCookieHeaderBuilder sameSite(SameSitePolicy sameSite) {

            this.sameSite = sameSite;
            return this;

        }

        public SetCookieHeaderBuilder sameSite(@NonNull Provider<SameSitePolicy> sameSiteProvider) {

            return sameSite(sameSiteProvider.provide());

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
