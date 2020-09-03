package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.routing.CommonRoutes;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;
import rocks.gioac96.veronica.statics.FilePermissionsManager;

public class StaticServer {

    public static void main(String[] args) {

        Application.builder()
            .router(Router.builder()
                .route(Route.<Boolean>staticRouteBuilder()
                    .permissionManager(FilePermissionsManager.<Boolean>builder()
                        .setPermissions("D:\\projects\\veronica\\src",true)
                        .setPermissions("D:\\projects\\veronica\\src\\test",false)
                        .build())
                    .permissionDecider((request, filePermissions) -> filePermissions)
                    .baseDir("D:\\projects\\veronica")
                    .basePath("/static")
                    .build())
                .fallbackRoute(CommonRoutes.notFound())
                .build())
            .port(80)
            .build()
            .start();


    }

}
