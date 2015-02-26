package exceptions;

/**
 * Created by ivar on 23.02.2015.
 */
public class DBConnectionException extends DBException {
    public DBConnectionException(Exception e) {
        super(e);
    }
    public DBConnectionException(String msg) {
        super(msg);
    }
}
