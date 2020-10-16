package rocks.gioac96.veronica.common;

import java.util.Arrays;
import rocks.gioac96.veronica.core.MimeType;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.static_server.MimeResolver;

/**
 * The framework's default common {@link MimeResolver} builder.
 */
public class CommonMimeResolver extends MimeResolver.MimeResolverBuilder implements BuildsSingleInstance {

    @Override
    protected void configure() {

        Arrays.stream(MimeType.CommonMimeType.values()).forEach(this::mime);

        super.configure();

    }

}
