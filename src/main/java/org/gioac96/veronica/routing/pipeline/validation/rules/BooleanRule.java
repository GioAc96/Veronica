package org.gioac96.veronica.routing.pipeline.validation.rules;

public class BooleanRule extends InArrayRule {

    public BooleanRule() {

        super(new String[]{
            "true",
            "false",
            "on",
            "off",
            "yes",
            "no",
            "ok",
            "1",
            "0",
            ""
        });

    }

}
