package rocks.gioac96.veronica.common;

import java.util.Arrays;
import rocks.gioac96.veronica.core.MimeType;

public class CommonMimeResolver extends rocks.gioac96.veronica.static_server.MimeResolver.MimeResolverBuilder {

    @Override
    protected void configure() {

        Arrays.stream(MimeType.CommonMimeTypes.values()).forEach(this::mime);

    }

}
