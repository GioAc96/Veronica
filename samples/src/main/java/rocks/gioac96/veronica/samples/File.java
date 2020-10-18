package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.core.common.CommonResponses;
import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.routing.Router;

public class File {

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .requestHandler(Router.builder()
                .defaultRequestHandler(request -> CommonResponses.fileInline("D:\\test.pdf"))
                .provide())
            .provide()
            .start();

    }

}
