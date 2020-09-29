package rocks.gioac96.veronica.common.responses;

import lombok.NonNull;
import rocks.gioac96.veronica.core.HttpStatus;
import rocks.gioac96.veronica.core.Response;
import rocks.gioac96.veronica.providers.BuildsMultipleInstances;

public class Redirect extends Response.ResponseBuilder implements BuildsMultipleInstances {

    protected String location;

    protected boolean permanent = false;

    public Redirect location(@NonNull String location) {

        this.location = location;

        return this;

    }

    public Redirect permanent(boolean permanent) {

        this.permanent = permanent;

        return this;

    }

    public Redirect permanent() {

        this.permanent = true;

        return this;

    }

    public Redirect temporary() {

        this.permanent = false;

        return this;

    }

    @Override
    protected boolean isValid() {

        return super.isValid() &&
            location != null;

    }

    @Override
    protected void configure() {

        if (permanent) {

            httpStatus(HttpStatus.MOVED_PERMANENTLY);

        } else {

            httpStatus(HttpStatus.TEMPORARY_REDIRECT);

        }

        header("Location", location);
        emptyBody();

    }

}
