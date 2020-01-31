package org.gioac96.veronica.samples;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.gioac96.veronica.Application;

public class Echo {

    public static void main(String[] args) throws IOException {

        Application application = new Application(80);

        application.start();

    }

}
