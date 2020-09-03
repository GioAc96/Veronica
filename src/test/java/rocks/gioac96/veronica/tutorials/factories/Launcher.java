package rocks.gioac96.veronica.tutorials.factories;

import rocks.gioac96.veronica.Application;

public class Launcher {

    public static void main(String[] args) {

        Application app = new MyApplication().build();

        app.start();

    }

}
