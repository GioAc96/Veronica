package rocks.gioac96.veronica.samples;

import static rocks.gioac96.veronica.common.CommonResponses.ok;

import rocks.gioac96.veronica.core.Application;

public class Benchmark {

    public static void main(String[] args) {

        Application.builder()
            .port(80)
            .requestHandler(request -> ok())
            .build()
            .start();

    }

}
