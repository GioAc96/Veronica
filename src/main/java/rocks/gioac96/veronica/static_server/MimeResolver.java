package rocks.gioac96.veronica.static_server;

import java.nio.file.Path;
import java.util.HashMap;
import lombok.NonNull;
import rocks.gioac96.veronica.core.MimeType;
import rocks.gioac96.veronica.providers.Builder;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;

/**
 * Class used to resolve file extensions to MIME types.
 */
public class MimeResolver {

    private final HashMap<String, String> extensionMimeMap;

    protected MimeResolver(MimeResolverBuilder b) {

        this.extensionMimeMap = b.extensionMimeMap;

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static MimeResolverBuilder builder() {

        class MimeResolverBuilderImpl extends MimeResolverBuilder implements BuildsMultipleInstances {

        }

        return new MimeResolverBuilderImpl();

    }

    /**
     * Resolves the MIME type of a file given its file name.
     *
     * @param fileName the name of the file to resolve the MIME type of
     * @return the MIME type
     */
    public String resolveMime(String fileName) {

        int lastDotPosition = fileName.lastIndexOf('.');

        if (lastDotPosition < 0) {

            return null;

        }

        String extension = fileName.substring(lastDotPosition);

        return extensionMimeMap.get(extension);

    }

    /**
     * Resolves the MIME type of a file given its file name.
     *
     * @param filePath the path of the file to resolve the MIME type of
     * @return the MIME type
     */
    public String resolveMime(Path filePath) {

        return resolveMime(filePath.getFileName().toString());

    }


    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public abstract static class MimeResolverBuilder extends Builder<MimeResolver> {

        private final HashMap<String, String> extensionMimeMap = new HashMap<>();

        public MimeResolverBuilder mime(@NonNull MimeType mimeType) {


            for (String extension : mimeType.getExtensions()) {

                extensionMimeMap.put(extension, mimeType.getMime());

            }

            return this;

        }

        public MimeResolverBuilder removeMime(@NonNull MimeType mimeType) {

            for (String extension : mimeType.getExtensions()) {

                extensionMimeMap.remove(extension);

            }

            return this;

        }


        @Override
        protected MimeResolver instantiate() {

            return new MimeResolver(this);

        }

    }

}
