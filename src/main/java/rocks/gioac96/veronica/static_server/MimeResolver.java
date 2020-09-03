package rocks.gioac96.veronica.static_server;

import java.util.HashMap;
import rocks.gioac96.veronica.core.MimeType;

/**
 * Class used to resolve file extensions to MIME types.
 */
public class MimeResolver {

    private final HashMap<String, String> extensionMimeMap;

    protected MimeResolver(MimeResolverBuilder<?, ?> b) {

        this.extensionMimeMap = b.extensionMimeMap;

    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public static MimeResolverBuilder<?, ?> builder() {

        return new MimeResolverBuilderImpl();

    }

    /**
     * Instantiates a MimeResolver builder with already all common MIME types configured.
     * @return the instantiated MimeResolver builder
     */
    public static MimeResolverBuilder<?, ?> basic() {

        MimeResolverBuilder builder = builder();

        for (MimeType mimeType : MimeType.CommonMimeTypes.values()) {

            builder.mime(mimeType);

        }

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
    public abstract static class MimeResolverBuilder<C extends MimeResolver, B extends MimeResolverBuilder<C, B>> {

        private final HashMap<String, String> extensionMimeMap = new HashMap<>();

        public B mime(MimeType... mimeType) {

            for (MimeType mt : mimeType) {

                for (String extension : mt.getExtensions()) {

                    extensionMimeMap.put(extension, mt.getMime());

                }

            }

            return self();

        }

        public B removeMime(MimeType mimeType) {

            for (String extension : mimeType.getExtensions()) {

                extensionMimeMap.remove(extension);

            }

            return self();

        }

        protected abstract B self();

        public abstract C build();

    }

    private static final class MimeResolverBuilderImpl
        extends MimeResolverBuilder<MimeResolver, MimeResolverBuilderImpl> {

        private MimeResolverBuilderImpl() {

        }

        protected MimeResolver.MimeResolverBuilderImpl self() {

            return this;

        }

        public MimeResolver build() {

            return new MimeResolver(this);

        }

    }

}
