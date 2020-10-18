package rocks.gioac96.veronica.samples;

import rocks.gioac96.veronica.core.Application;
import rocks.gioac96.veronica.core.common.CommonResponses;

public class Benchmark {

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .requestHandler(request -> CommonResponses.ok())
            .provide()
            .start();

    }

}
