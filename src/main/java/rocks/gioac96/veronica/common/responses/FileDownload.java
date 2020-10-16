package rocks.gioac96.veronica.common.responses;

public class FileDownload extends FileRaw {

    @Override
    protected void configure() {

        header("Content-Disposition", "attachment; filename=" + filePath.getFileName());

        super.configure();

    }

}
