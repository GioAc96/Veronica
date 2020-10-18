package rocks.gioac96.veronica.common;

import java.util.Arrays;
import rocks.gioac96.veronica.MimeType;
import rocks.gioac96.veronica.static_server.MimeResolver;

/**
 * The framework's default common {@link MimeResolver} builder.
 */
public class CommonMimeResolver extends MimeResolver.MimeResolverBuilder {

    @Override
    protected void configure() {

        Arrays.stream(MimeType.CommonMimeType.values()).forEach(this::mime);

        super.configure();

    }

}
