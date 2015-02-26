package exceptions;

/**
 * Created by ivar on 23.02.2015.
 */
public class DBDriverException extends DBException {
    public DBDriverException(Exception e) {
        super(e);
    }
    public DBDriverException(String msg) {
        super(msg);
    }
}
