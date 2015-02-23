package exceptions;

/**
 * Created by ivar on 23.02.2015.
 */
public class DBException extends Exception {
    public DBException(Exception e) {
        super(e);
    }
}
