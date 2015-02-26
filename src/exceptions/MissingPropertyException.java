package exceptions;

/**
 * Created by ivar on 26.02.2015.
 */
public class MissingPropertyException extends Exception {
    public MissingPropertyException(Exception e) {
        super(e);
    }
    public MissingPropertyException(String msg) {
        super(msg);
    }
}
