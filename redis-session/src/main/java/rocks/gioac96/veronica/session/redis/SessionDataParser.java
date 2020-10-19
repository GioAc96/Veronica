package rocks.gioac96.veronica.session.redis;

public interface SessionDataParser<D> {

    D parse(String encodedSessionData);

}
