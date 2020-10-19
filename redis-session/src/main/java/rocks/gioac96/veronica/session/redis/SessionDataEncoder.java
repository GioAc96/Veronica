package rocks.gioac96.veronica.session.redis;

public interface SessionDataEncoder<D> {

    String encode(D sessionData);

}
