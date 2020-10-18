package rocks.gioac96.veronica.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import rocks.gioac96.veronica.providers.ConfigurableProvider;
import rocks.gioac96.veronica.providers.Provider;

/**
 * Authentication credentials.
 */
@Getter
public class Credentials {

    private final String username;
    private final String password;

    protected Credentials(CredentialsBuilder b) {

        this.username = b.username;
        this.password = b.password;

    }

    public static CredentialsBuilder builder() {

        return new CredentialsBuilder();

    }

    public static class CredentialsBuilder extends ConfigurableProvider<Credentials> {

        protected String username;
        protected String password;

        @Override
        protected boolean isValid() {

            return username != null
                && password != null;

        }

        @Override
        protected Credentials instantiate() {

            return new Credentials(this);

        }

        public CredentialsBuilder username(@NonNull String username) {
            
            this.username = username;
            return this;
            
        }
        
        public CredentialsBuilder username(@NonNull Provider<String> usernameProvider) {
            
            return username(usernameProvider.provide());
            
        }

        public CredentialsBuilder password(@NonNull String password) {
            
            this.password = password;
            return this;
            
        }
        
        public CredentialsBuilder password(@NonNull Provider<String> passwordProvider) {
            
            return password(passwordProvider.provide());
            
        }
        
    }


}
