package rocks.gioac96.veronica.common.responses;

public class FileInline extends FileRaw {

    @Override
    protected void configure() {

        header("Content-Disposition", "inline; filename=" + filePath.getFileName());

        super.configure();

    }

}
