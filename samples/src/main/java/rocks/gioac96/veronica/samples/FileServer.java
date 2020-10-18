package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.common.CommonRequestHandlers;
import rocks.gioac96.veronica.file_server.FilePermissionsManager;
import rocks.gioac96.veronica.file_server.StaticRouteBuilder;
import rocks.gioac96.veronica.routing.Router;

public class FileServer {

    public static void main(String[] args) {

        Application.builder()
            .requestHandler(Router.builder()
                .route(new StaticRouteBuilder<Boolean>()
                    .permissionManager(FilePermissionsManager.<Boolean>builder()
                        .permissions("D:\\projects\\veronica\\src", true)
                        .permissions("D:\\projects\\veronica\\src\\test", false)
                        .provide())
                    .permissionDecider((request, filePermissions) -> filePermissions)
                    .baseDir("D:\\projects\\veronica")
                    .basePath("/static")
                    .provide())
                .defaultRequestHandler(CommonRequestHandlers.notFound())
                .provide())
            .port(80)
            .provide()
            .start();


    }

}
