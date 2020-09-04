package rocks.gioac96.veronica.static_server;

import java.util.Arrays;
import java.util.HashMap;
import rocks.gioac96.veronica.core.MimeType;
import rocks.gioac96.veronica.providers.Builder;

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

        return new MimeResolverBuilder();

    }

    /**
     * Instantiates a MimeResolver builder with already all common MIME types configured.
     * @return the instantiated MimeResolver builder
     */
    public static MimeResolverBuilder basic() {

        MimeResolverBuilder builder = builder();

        Arrays.stream(MimeType.CommonMimeTypes.values()).forEach(builder::mime);

        return builder;

    }

    /**
     * Resolves the MIME type of a file given its file name.
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


    @SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:MissingJavadocType"})
    public static class MimeResolverBuilder extends Builder<MimeResolver> {

        private final HashMap<String, String> extensionMimeMap = new HashMap<>();

        public MimeResolverBuilder mime(MimeType mimeType) {


            for (String extension : mimeType.getExtensions()) {

                extensionMimeMap.put(extension, mimeType.getMime());

            }

            return this;

        }

        public MimeResolverBuilder removeMime(MimeType mimeType) {

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
