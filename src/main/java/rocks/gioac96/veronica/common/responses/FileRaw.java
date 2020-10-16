package rocks.gioac96.veronica.common.responses;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import rocks.gioac96.veronica.common.CommonMimeResolver;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.providers.CreationException;

public class FileRaw
    extends Response.ResponseBuilder {

    protected Path filePath = null;

    public FileRaw filePath(Path filePath) {

        this.filePath = filePath;

        return this;

    }

    @Override
    protected void configure() {

        String mime = new CommonMimeResolver().provide().resolveMime(filePath);

        if (mime != null) {

            header("Content-Type", mime);

        }

        try {

            body(Files.readAllBytes(filePath));

        } catch (IOException e) {

            throw new CreationException(e);

        }

        super.configure();

    }

}
