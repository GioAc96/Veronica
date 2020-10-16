package rocks.gioac96.veronica.common.responses;

import lombok.NonNull;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;

public class Redirect
    extends Response.ResponseBuilder  {

    protected String location;

    protected boolean isPermanent = false;

    public Redirect location(@NonNull String location) {

        this.location = location;

        return this;

    }

    public Redirect permanent(boolean isPermanent) {

        this.isPermanent = isPermanent;

        return this;

    }

    public Redirect permanent() {

        this.isPermanent = true;

        return this;

    }

    public Redirect temporary() {

        this.isPermanent = false;

        return this;

    }

    @Override
    protected boolean isValid() {

        return super.isValid() &&
            location != null;

    }

    @Override
    protected void configure() {

        if (isPermanent) {

            httpStatus(HttpStatus.MOVED_PERMANENTLY);

        } else {

            httpStatus(HttpStatus.TEMPORARY_REDIRECT);

        }

        header("Location", location);

        super.configure();

    }

}
