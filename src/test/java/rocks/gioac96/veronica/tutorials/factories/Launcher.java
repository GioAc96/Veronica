package rocks.gioac96.veronica.tutorials.factories;

import rocks.gioac96.veronica.Application;
import rocks.gioac96.veronica.http.Request;
import rocks.gioac96.veronica.http.Response;

public class Launcher {

    public static void main(String[] args) {

        Application app = new MyApplication().build();

        app.start();

    }

}
