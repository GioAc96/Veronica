package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.common.CommonRoutes;
import rocks.gioac96.veronica.core.Route;
import rocks.gioac96.veronica.core.Router;
import rocks.gioac96.veronica.static_server.FilePermissionsManager;

public class StaticServer {

    public static void main(String[] args) {

        Application.builder()
            .router(Router.builder()
                .route(Route.<Boolean>staticRouteBuilder()
                    .permissionManager(FilePermissionsManager.<Boolean>builder()
                        .permissions("D:\\projects\\veronica\\src",true)
                        .permissions("D:\\projects\\veronica\\src\\test",false)
                        .build())
                    .permissionDecider((request, filePermissions) -> filePermissions)
                    .baseDir("D:\\projects\\veronica")
                    .basePath("/static")
                    .build())
                .defaultRoute(CommonRoutes.notFound())
                .build())
            .port(80)
            .build()
            .start();


    }

}
