package rocks.gioac96.veronica.common;

import java.util.Arrays;
import rocks.gioac96.veronica.core.MimeType;
import rocks.gioac96.veronica.providers.BuildsSingleInstance;
import rocks.gioac96.veronica.static_server.MimeResolver;

public class CommonMimeResolver extends MimeResolver.MimeResolverBuilder implements BuildsSingleInstance {

    @Override
    protected void configure() {

        Arrays.stream(MimeType.CommonMimeTypes.values()).forEach(this::mime);

    }
    
}
