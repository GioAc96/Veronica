package rocks.gioac96.veronica.tutorials.factories;

import rocks.gioac96.veronica.core.Application;

public class Launcher {

    public static void main(String[] args) {

        Application app = new MyApplication().build();

        app.start();

    }

}
