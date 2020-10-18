package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.common.CommonRequestHandlers;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.Route;
import rocks.gioac96.veronica.Router;
import rocks.gioac96.veronica.static_server.FilePermissionsManager;

public class StaticServer {

    public static void main(String[] args) {

        Application.builder()
            .router(Router.builder()
                .route(Route.<Boolean>staticRouteBuilder()
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
