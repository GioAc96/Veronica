package rocks.gioac96.veronica.core;

import java.util.Set;
import lombok.Getter;

/**
 * Interface for associating file extensions to MIME types.
 */
public interface MimeType {

    Set<String> getExtensions();

    String getMime();

    /**
     * Enum of common MIME types.
     */
    enum CommonMimeTypes implements MimeType {

        AAC(Set.of(".aac"), "audio/aac"),
        ABIWORD(Set.of(".abw"), "application/x-abiword"),
        ARCHIVE(Set.of(".arc"), "application/x-freearc"),
        AVI(Set.of(".avi"), "video/x-msvideo"),
        AMAZON_KINDLE_EBOOK(Set.of(".azw"), "application/vnd.amazon.ebook"),
        BINARY(Set.of(".bin"), "application/octet-stream"),
        BMP(Set.of(".bmp"), "image/bmp"),
        BZIP(Set.of(".bz"), "application/x-bzip"),
        BZIP2(Set.of(".bz2"), "application/x-bzip2"),
        C_SHELL(Set.of(".csh"), "application/x-csh"),
        CSS(Set.of(".css"), "text/css"),
        CSV(Set.of(".csv"), "text/csv"),
        MICROSOFT_WORD(Set.of(".doc"), "application/msword"),
        MICROSOFT_WORD_OPENXML(
            Set.of(".docx"),
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        ),
        MICROSOFT_EMBEDDED_OPENTYPE(Set.of(".eot"), "application/vnd.ms-fontobject"),
        EPUB(Set.of(".epub"), "application/epub+zip"),
        GZIP(Set.of(".gz"), "application/gzip"),
        GIF(Set.of(".gif"), "image/gif"),
        HTML(Set.of(".htm", ".html"), "text/html"),
        ICON(Set.of(".ico"), "image/vnd.microsoft.icon"),
        ICALENDAR(Set.of(".ics"), "text/calendar"),
        JAVA_ARCHIVE(Set.of(".jar"), "application/java-archive"),
        JPEG(Set.of(".jpeg", ".jpg"), "image/jpeg"),
        JAVASCRIPT(Set.of(".js", ".mjs"), "text/js"),
        JSON(Set.of(".json"), "application/json"),
        JSON_LD(Set.of(".jsonld"), "application/ld+json"),
        MIDI(Set.of(".mid", ".midi"), "audio/midi"),
        MP3(Set.of(".mp3"), "audio/mpeg"),
        MPEG(Set.of(".mpeg"), "video/mpeg"),
        APPLE_INSTALLER_PACKAGE(Set.of(".mpkg"), "application/vnd.apple.installer+xml"),
        OPENDOCUMENT_PRESENTATION(Set.of(".odp"), "application/vnd.oasis.opendocument.presentation"),
        OPENDOCUMENT_SPREADSHEET(Set.of(".ods"), "application/vnd.oasis.opendocument.spreadsheet"),
        OPENDOCUMENT_TEXT(Set.of(".odt"), "application/vnd.oasis.opendocument.text"),
        OGG_AUDIO(Set.of(".oga"), "audio/ogg"),
        OGG_VIDEO(Set.of(".ogv"), "video/ogg"),
        OGG(Set.of(".ogx"), "application/ogg"),
        OPUS(Set.of(".opus"), "audio/opus"),
        OPENTYPE_FONT(Set.of(".otf"), "font/otf"),
        PNG(Set.of(".png"), "image/png"),
        PDF(Set.of(".pdf"), "application/pdf"),
        PHP(Set.of(".php"), "application/x-httpd-php"),
        MICROSOFT_POWERPOINT(Set.of(".ppt"), "application/vnd.ms-powerpoint"),
        MICROSOFT_POWERPOINT_OPENXML(
            Set.of(".pptx"),
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        ),
        RAR(Set.of(".rar"), "application/vnd.rar"),
        RICH_TEXT_FORMAT(Set.of(".rtf"), "application/rtf"),
        BOURNE_SHELL_SCRIPT(Set.of(".sh"), "application/x-sh"),
        SVG(Set.of(".svg"), "image/svg+xml"),
        SWF(Set.of(".swf"), "application/x-shockwave-flash"),
        TAR(Set.of(".tar"), "application/x-tar"),
        TIFF(Set.of(".tif", ".tiff"), "image/tiff"),
        MPEG_TRANSPORT_STREAM(Set.of(".ts"),"video/mp2t"),
        TRUETYPE_FONT(Set.of(".ttf"),"font/ttf"),
        TXT(Set.of(".txt"),"text/plain"),
        MICROSOFT_VISIO(Set.of(".vsd"),"application/vnd.visio"),
        WAV(Set.of(".wav"),"audio/wav"),
        WEBM_AUDIO(Set.of(".weba"),"audio/webm"),
        WEBM_VIDEO(Set.of(".webm"),"video/webm"),
        WEBP_IMAGE(Set.of(".webp"),"image/webp"),
        WOFF(Set.of(".woff"),"font/woff"),
        WOFF2(Set.of(".woff2"),"font/woff2"),
        XHTML(Set.of(".xhtml"),"application/xhtml+xml"),
        MICROSOFT_EXCEL(Set.of(".xls"),"application/vnd.ms-excel"),
        MICROSOFT_EXCEL_OPENXML(Set.of(".xlsx"),"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        XML(Set.of(".xml"),"application/xml"),
        XUL(Set.of(".xul"),"application/vnd.mozilla.xul+xml"),
        ZIP(Set.of(".zip"),"application/zip"),
        _3GP_VIDEO(Set.of(".3gp"),"video/3gpp"),
        _3GP_AUDIO(Set.of(".3gp"),"audio/3gpp"),
        _3G2_VIDEO(Set.of(".3g2"),"video/3gpp2"),
        _3G2_AUDIO(Set.of(".3gp"),"audio/3gpp2"),
        _7ZIP(Set.of(".7z"), "application/x-7z-compressed");

        @Getter
        private final Set<String> extensions;

        @Getter
        private final String mime;

        CommonMimeTypes(Set<String> extensions, String mime) {
            this.extensions = extensions;
            this.mime = mime;
        }

    }

}
