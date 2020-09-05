package rocks.gioac96.veronica.common.responses;

public class FileInline extends FileRaw {

    @Override
    protected void configure() {

        super.configure();

        header("Content-Disposition", "inline; filename=" + filePath.getFileName());

    }

}
