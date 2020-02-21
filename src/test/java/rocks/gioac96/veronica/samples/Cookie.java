package rocks.gioac96.veronica.samples;

import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.List;
import java.util.Map;
import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;
import rocks.gioac96.veronica.routing.Route;
import rocks.gioac96.veronica.routing.Router;

public class Cookie {

    public static void main(String[] args) throws IOException {

        Application<Request, Response> app = Application.basic(
            80,
            Router.builder()
                .fallbackRoute(Route.builder()
                    .requestHandler(request -> {

                        try {

                            for (Map.Entry<String, String> cookie : request.getCookie().entrySet()) {

                                System.out.println(cookie);

                            }

                            Response response = Response.builder()
                                .body("Check your console")
                                .build();

                            if (request.getCookie().size() == 0) {

                                response.getCookies().add(new HttpCookie("name", "value"));
                                response.getCookies().add(new HttpCookie("name2", "value2"));

                            }

                            return response;

                        } catch (Throwable t) {

                            return Response.builder()
                                .body("error")
                                .build();

                        }

                    })
                    .build())
                .build()
        );

        app.start();

    }

}
