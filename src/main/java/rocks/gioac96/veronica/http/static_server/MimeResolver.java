package rocks.gioac96.veronica.http.static_server;

import java.util.HashMap;


public class MimeResolver {

    private final HashMap<String, String> extensionMimeMap;

    protected MimeResolver(MimeResolverBuilder<?, ?> b) {

        this.extensionMimeMap = b.extensionMimeMap;

    }

    public static MimeResolverBuilder<?, ?> builder() {

        return new MimeResolverBuilderImpl();

    }

    public static MimeResolverBuilder<?, ?> basic() {

        MimeResolverBuilder builder = builder();

        for (MimeType mimeType : MimeType.CommonMimeTypes.values()) {

            builder.mime(mimeType);

        }

        return builder;

    }

    public String resolveMime(String fileName) {

        int lastDotPosition = fileName.lastIndexOf('.');

        if (lastDotPosition < 0) {

            return null;

        }

        String extension = fileName.substring(lastDotPosition + 1);

        return resolveMime(extension);

    }


    public static abstract class MimeResolverBuilder<C extends MimeResolver, B extends MimeResolverBuilder<C, B>> {

        private HashMap<String, String> extensionMimeMap = new HashMap<>();

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

    private static final class MimeResolverBuilderImpl extends MimeResolverBuilder<MimeResolver, MimeResolverBuilderImpl> {
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
