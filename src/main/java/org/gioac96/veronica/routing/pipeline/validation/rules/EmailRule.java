package org.gioac96.veronica.routing.pipeline.validation.rules;

import lombok.NonNull;

public class EmailRule extends RegexRule {

    public EmailRule() {

        super("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");

    }

}
