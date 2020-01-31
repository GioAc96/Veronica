package org.gioac96.veronica.samples;

import java.io.IOException;
import org.gioac96.veronica.Application;

public class Echo {

    public static void main(String[] args) throws IOException {

        Application application = new Application(80);

        application.start();

    }

}
