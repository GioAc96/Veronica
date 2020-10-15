package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.common.CommonResponses;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.Router;

public class File {

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .router(Router.builder()
                .defaultRequestHandler(request -> CommonResponses.fileInline("D:\\test.pdf"))
                .build())
            .build()
            .start();

    }

}
