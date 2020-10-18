package rocks.gioac96.veronica.core.common;

import java.util.Arrays;
import rocks.gioac96.veronica.core.MimeResolver;
import rocks.gioac96.veronica.core.MimeType;

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
