package billing.billing_app.exceptions;

public class WrongPasswordException extends Exception {

    public WrongPasswordException(String msg) {
        super(msg);
    }

}
